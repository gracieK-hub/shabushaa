# 首先 
 在 config.yaml 中配置好项目信息 找到 
 PROJECT_INFO:
  key: "cnv-front-schedule-java-service-develop"
  token: "0d4784d7430dae414d7b9365149285f29150d9ca"
  local_path: "D:\\ideaCode\\company\\gitCode\\sonarCode\\cnv-front-cas-java-service"
  只修改 local_path 为自己要扫描的项目本地路径
# 然后
 pip install -r requirements.txt
# 其次
 启动 ssh_tunnel.py
# 下一步
 启动 zhipu_java.py
# 最后
 启动 等待完成 然后修改ai遗漏的错误