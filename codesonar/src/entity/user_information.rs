use serde::{Deserialize, Serialize};
use crate::entity::user_information_description::UserInformationDescription;

#[derive(Serialize, Deserialize, Debug)]
pub struct UserInformation {
    pub user_id: Option<String>,
    pub project_description: Option<UserInformationDescription>,
}