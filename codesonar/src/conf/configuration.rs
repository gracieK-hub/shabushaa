use std::collections::HashMap;
use std::path::Path;
use config::{Config, File, Map};

pub fn config_source_str(h_key: &str, h_value: &str) -> Option<String> {
    let settings = Config::builder()
        .add_source(File::from(Path::new("config/config.toml")))
        .build()
        .expect(&format!("Failed to build config for key: {}", h_key));

    let data = settings.get_string(format!("{}.{}", h_key, h_value).as_str())
        .expect(&format!("Failed to get string from config for key: {}.{}", h_key, h_value)); // 自定义panic消息
    Option::from(data)
}

pub fn config_source_map(h_key: &str) -> Option<HashMap<String, String>> {
    let settings = Config::builder()
       .add_source(File::from(Path::new("config/config.toml")))
       .build()
       .expect(&format!("Failed to build config for key: {}", h_key));
    let data = settings.get_table(h_key)
      .expect(&format!("Failed to get map from config for key: {}", h_key));
    let mut result = HashMap::new();
    for (key, value) in data {
        result.insert(key, value.to_string());
    }
    Option::from(result)
}