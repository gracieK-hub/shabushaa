use std::{fs, io};
use std::fs::File;
use std::io::BufRead;
use std::path::{Path, PathBuf};
use mongodb::Database;
use crate::entity::user_information::UserInformation;
use crate::entity::user_information_description::UserInformationDescription;
use crate::{repository, service};
use crate::entity::user_project::UserProject;
use crate::repository::mongodb_client::create_client;


pub async fn scan_project(project_path: &Path) -> Option<Vec<UserProject>> {
    let mut file_list: Vec<PathBuf> = Vec::new();
    scan_dir_for_files(project_path, &mut file_list);
    let mut insert_list: Vec<UserProject> = Vec::new();
    for file_item in file_list {
        // 直接使用 file_item，无需解构
        let file = File::open(&file_item).unwrap();
        let reader = io::BufReader::new(file);
        let mut need_reference_list: Vec<String> = Vec::new();
        let mut package_name = String::new();
        let file_name = file_item.file_name()
            .and_then(|name| name.to_str())
            .map(|name| name.rsplitn(2, '.').nth(1).unwrap_or(name))
            .unwrap().to_string();
        let mut copy_reference = String::new();
        let mut file_content = String::new();
        let temp_file_path = file_item.display().to_string();

        for line in reader.lines() {
            match line {
                Ok(content) => {
                    file_content.push_str(&format!("{}\n", content));
                    //首先检查 开头是不是 import
                    let linestr = content.split("//").collect::<Vec<&str>>()[0].trim();
                    if linestr.starts_with("import") {
                        let reference = linestr.split("import")
                            .collect::<Vec<&str>>()[1].replace(" ", "").replace(";", "");
                        need_reference_list.push(reference);
                    }
                    if linestr.starts_with("package") {
                        package_name = linestr.split("package").collect::<Vec<&str>>()[1].replace(" ", "").replace(";", "");
                        copy_reference = format!("{}.{}", package_name, file_name);
                    }
                }
                Err(e) => eprintln!("读取出错: {}", e),
            }
        }
        let user_project = UserProject {
            file_name: Option::from(file_name),
            package_name: Option::from(package_name),
            copy_reference: Option::from(copy_reference),
            temp_path: Option::from(temp_file_path),
            content: Option::from(file_content),
            need_reference: Option::from(need_reference_list),
        };
        insert_list.push(user_project);
    }
    Some(insert_list)
}

pub fn scan_dir_for_files(dir: &Path, file_list: &mut Vec<PathBuf>) -> std::io::Result<()> {
    if dir.is_dir() {
        // 读取目录中的所有条目
        for entry in fs::read_dir(dir)? {
            let entry = entry?;
            let path = entry.path();

            // 如果是目录，则递归调用
            if path.is_dir() {
                scan_dir_for_files(&path, file_list)?;
            } else if path.extension().map_or(false, |ext| ext == "java") {
                file_list.push(path)
            }
        }
    }
    Ok(())
}

pub async fn reload_project(user_id: &str, project_name: &str, project_language: &str, framework: &str, project_path: &Path) -> Result<(), Box<dyn std::error::Error>> {
    // 创建用户信息
    let user_information = UserInformation {
        user_id: Some(user_id.to_string()),
        project_description: Some(UserInformationDescription {
            project_name: Some(project_name.to_string()),
            language: Some(project_language.to_string()),
            framework: Some(framework.to_string()),
        }),
    };
    println!("{:?}", &user_information);
    match create_client().await {
        Ok(db) => {
            let reload_res = user_reload_project_info(&user_information, &db).await;
            println!("{}", reload_res.unwrap_or("reload project info error".to_string()));
            let project_info_list = scan_project(project_path).await.unwrap_or_else(|| Vec::new());
            let user_name = user_information.user_id.unwrap();
            let project_name = user_information.project_description.unwrap().project_name.unwrap();
            let collection_name = format!("{}_{}", user_name, project_name);
            let insert_count = repository::mapper::insert_user_project(&project_info_list, &collection_name, &db).await.unwrap_or_else(|| 0);
            println!("insert count: {}", insert_count);
            Ok(())
        }
        Err(e) => {
            println!("{}", e.to_string());
            Ok(())
        }
    }
}

pub async fn user_reload_project_info(user: &UserInformation, db: &Database) -> Option<String> {
    let res = repository::mapper::get_user_information(user, db).await?;
    if res.user_id.is_some() {
        let drop = repository::mapper::delete_collection(user, db).await;
        if drop != Some(1) {
            println!("drop collection error")
        }
        let create = repository::mapper::create_collection(user, db).await;
        if create != Some(1) {
            return Some("create collection error".to_string());
        }
    } else {
        let create = repository::mapper::insert_user_information(user, db).await;
        if create != Some(1) {
            return Some("insert user information error".to_string());
        }
        let create = repository::mapper::create_collection(user, db).await;
        if create != Some(1) {
            return Some("create collection error".to_string());
        }
    }
    return Some("success".to_string());
}