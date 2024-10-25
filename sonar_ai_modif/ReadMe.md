# 准备阶段 拥有 py3.11 注册一个bigmodel开放平台账号 创建一个 api token
    cmd 执行 
    pip install paramiko sshtunnel
    pip install openai
    pip uninstall protobuf 
    pip install protobuf==4.23.4 
# 首先 备份一份要优化的项目
# 其次 配置 config.py
ZHIPU = {
    'url':'https://open.bigmodel.cn/api/paas/v4/chat/completions',
    'token':'ea0ab6b243bfec4f56d9f8d880780a44.nttX3gepmAC3ZTSn',
    'model': 'glm-4-air'
}
在智谱网页中找到控制台 获取免费的api token 修改 token 
LOCAL_SERVER = {
    'ip':'127.0.0.1',
    'port':9000
}
检查端口9000是否被占用，如果被占用, 当掉当前的9000让这个程序用
PROJECT_INFO = {
    'key':'cnv-front-schedule-java-service-develop',
    'token':'0d4784d7430dae414d7b9365149285f29150d9ca',
    'local_path':'D:\ideaCode\company\gitCode\\buckup\cnv-front-schedule-java-service\\src\main\java\\',
    'local_path_prefix': 'D:\ideaCode\company\gitCode\\buckup\cnv-front-schedule-java-service\\'
}
将 'key' 和 'token' 替换为 云sonar的项目key 和 token
将 'local_path_prefix' 替换为 本地项目路径只写到项目名即可
local_path 替换到项目路径的java目录
# 然后 执行 ssh_tunnel.py 运行代理脚本
# 最后 启动main.py 开始添加注释