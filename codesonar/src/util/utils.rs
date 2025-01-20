use std::env;

pub fn get_newline() -> String {
    if env::consts::OS == "windows" {
        "\r\n".to_string()
    } else {
        "\n".to_string()
    }
}