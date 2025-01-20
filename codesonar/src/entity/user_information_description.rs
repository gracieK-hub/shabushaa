use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Debug)]
pub struct UserInformationDescription {
    pub project_name: Option<String>,
    pub language: Option<String>,
    pub framework: Option<String>,
}