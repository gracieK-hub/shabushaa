use std::error::Error;
use mongodb::{Client, Database};
use mongodb::options::{ClientOptions, Credential};
use crate::conf;

pub async fn create_client() -> Result<Database, Box<dyn Error>> {
    let database_map = conf::configuration::config_source_map("database");

    // Handle the case where configuration is not found
    let item = database_map.ok_or("database config not found")?;

    let url = item.get("url").ok_or("missing database url")?;
    let username = item.get("username").ok_or("missing database username")?;
    let password = item.get("password").ok_or("missing database password")?;
    let schema = item.get("schema").ok_or("missing database schema")?;

    // Parse the URL for the client options
    let mut client_options = ClientOptions::parse(url.as_str()).await?;

    let credential = Credential::builder()
        .username(username.to_string())
        .password(password.to_string())
        .build();

    client_options.credential = Some(credential);

    // Create the MongoDB client
    let client = Client::with_options(client_options)?;

    // Get the database
    let db = client.database(schema.as_str());
    Ok(db)
}