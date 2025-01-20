use std::fmt::Debug;
use std::io::BufRead;
use std::path::{Path};
use crate::entity::user_information::UserInformation;
use crate::entity::user_information_description::UserInformationDescription;
use crate::repository::mongodb_client::create_client;
use crate::service::service::{scan_project, user_reload_project_info};


mod conf;
mod entity;
mod model;
mod service;
mod repository;
mod util;

#[tokio::main]
async fn main() {
    // 获取项目路径和名称
    let mut project_path = Path::new(r"D:\ideaCode\company\gitCode\sonarCode\cnv-front-schedule-java-service");
    let project_name = project_path.file_name().unwrap().to_str().unwrap();
    let project_language = "java";
    let framework = "junit";
    let user_id = "wangc";
    let reload_result = service::service::reload_project(user_id, project_name, project_language, framework, project_path).await;

}


