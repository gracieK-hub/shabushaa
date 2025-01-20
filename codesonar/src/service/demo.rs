use std::path::Path;
use mongodb::{bson, Client};
use mongodb::bson::{Bson, doc, to_bson};
use mongodb::options::{ClientOptions, CreateCollectionOptions, Credential};
use crate::conf;
use crate::entity::user_information::UserInformation;
use crate::entity::user_information_description::UserInformationDescription;

async fn mains() -> Result<(), Box<dyn std::error::Error>> {
    let database_url = conf::configuration::config_source_str("database", "url");
    let database_username = conf::configuration::config_source_str("database", "username");
    let database_password = conf::configuration::config_source_str("database", "password");
    let database_schema = conf::configuration::config_source_str("database", "schema");

    // 解析数据库连接
    let mut client_options = ClientOptions::parse(database_url.unwrap().as_str()).await?;
    let credential = Credential::builder()
        .username(database_username)
        .password(database_password)
        .build();
    client_options.credential = Some(credential);

    let db_client = Client::with_options(client_options)?;
    let mut database_client = db_client.database(database_schema.unwrap().as_str());

    // 获取项目路径和名称
    let mut project_path = Path::new(r"D:\ideaCode\company\gitCode\sonarCode\cnv-front-schedule-java-service");
    let project_name = project_path.file_name().unwrap().to_str().unwrap();
    let project_language = "java8";
    let framework = "junit";
    let user_id = "wangc";

    // 创建用户信息
    let user_information = UserInformation {
        user_id: Some(user_id.to_string()),
        project_description: Some(UserInformationDescription {
            project_name: Some(project_name.to_string()),
            language: Some(project_language.to_string()),
            framework: Some(framework.to_string()),
        }),
    };

    // 使用 bson::ser::to_bson 序列化 UserInformation
    let params = to_bson(&user_information)?;
    let select_collection = format!("{}_{}", user_id, project_name);

    // 获取集合列表，检查是否存在
    database_client.collection::<UserInformation>("user_information");
    let collection_list_result = database_client.list_collection_names(None).await?;
    let mut collection: mongodb::Collection<bson::Document> = if collection_list_result.contains(&select_collection) {
        // 然后删除 对应的集合
        let collection = database_client.collection(&select_collection);
        let result = collection.delete_many(doc! {}, None).await?;
        println!("Collection {} already exists, delete result {}", select_collection, result.deleted_count);
        collection // 返回 collection 以便后续使用
    } else {
        // 如果集合不存在，创建新集合
        let create_options = CreateCollectionOptions::builder()
            .capped(false) // 可选：指定集合是否为限制大小的集合
            .build();
        database_client.create_collection(&select_collection, create_options).await?;
        let collection = database_client.collection(&select_collection);
        println!("Collection '{}' created.", select_collection);
        collection // 返回 collection 以便后续使用
    };

    // 将文档插入到集合中
    if let Bson::Document(doc) = params {
        collection = database_client.collection("user_information");
        collection.delete_one(doc.clone(), None).await?;
        collection.insert_one(doc, None).await?;
        println!("Document inserted into collection '{}'", select_collection);
    }

    Ok(())
}