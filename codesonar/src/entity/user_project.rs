use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Debug)]
pub struct UserProject {
    pub file_name: Option<String>,
    pub package_name: Option<String>,
    pub copy_reference: Option<String>,
    pub temp_path: Option<String>,
    pub content: Option<String>,
    pub need_reference: Option<Vec<String>>,
}