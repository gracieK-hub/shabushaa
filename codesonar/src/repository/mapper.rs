
use mongodb::{bson, Database};
use mongodb::bson::{Bson, bson, doc, to_bson};
use mongodb::results::InsertManyResult;
use crate::entity::user_information::UserInformation;
use crate::entity::user_information_description::UserInformationDescription;
use crate::entity::user_project::UserProject;

pub async fn get_user_information(user: &UserInformation, db: &Database) -> Option<UserInformation> {
    let table_user = "user_information";
    let mut collection = db.collection::<UserInformation>(table_user);
    let param = bson::to_document(&user).ok();
    let result = collection.find_one(param, None).await;

    // 根据查询结果判断返回 Option<UserInformation>
    match result {
        Ok(Some(user_info)) => Some(user_info), // 找到用户信息
        _ => Some(UserInformation {
            user_id: None,
            project_description: None,
        }),
    }
}

pub async fn insert_user_information(user: &UserInformation, db: &Database) -> Option<i16> {
    // 获取 "user_information" 集合
    let table_user = "user_information";
    let collection = db.collection::<UserInformation>(table_user);
    // 将 UserInformation 转换为 BSON 文档
    collection.insert_one(user, None).await.ok()?;

    // 如果插入成功，返回 Some(1)，否则返回 None
    Some(1)
}

pub async fn create_collection(user: &UserInformation, db: &Database) -> Option<i16> {
    // 安全地处理 Option 中的值，避免 panic
    let collection_name = match (&user.user_id, &user.project_description) {
        (Some(user_id), Some(project_desc)) => {
            match &project_desc.project_name {
                Some(project_name) => format!("{}_{}", user_id.to_string(), project_name),
                None => return None,  // 如果 project_name 为 None，返回 None
            }
        }
        _ => return None,  // 如果 user_id 或 project_description 为 None，返回 None
    };

    println!("Collection Name: {}", collection_name);
    db.create_collection(collection_name, None).await.expect("Failed to create collection");
    Some(1)
}

pub async fn delete_collection(user: &UserInformation, db: &Database) -> Option<i16> {
    // 安全地处理 Option 中的值，避免 panic
    let collection_name = match (&user.user_id, &user.project_description) {
        (Some(user_id), Some(project_desc)) => {
            match &project_desc.project_name {
                Some(project_name) => format!("{}_{}", user_id.to_string(), project_name),
                None => return None,  // 如果 project_name 为 None，返回 None
            }
        }
        _ => return None,
    };
    db.collection::<UserInformationDescription>(&collection_name).delete_many(doc! {}, None).await.expect("Failed to drop collection");
    Some(1)
}

pub async fn insert_user_project(user_project_info: &Vec<UserProject>, collection_name: &String, db: &Database) -> Option<i16> {
    // 尝试执行插入操作
    let result = db.collection::<UserProject>(collection_name)
        .insert_many(user_project_info, None).await;
    println!("{:?}", result);
    Some(1)
}