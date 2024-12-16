import os
import requests
import yaml

import config

from datetime import datetime

def get_ai_response(prompt):
    url = config.ZHIPU["url"]

    headers = {
        "Authorization": config.ZHIPU["token"],
        "Content-Type": "application/json"
    }
    data = {
        # "assistant_id": "65a393b3619c6f13586246cd",
        # "conversation_id": None,
        "model": config.ZHIPU["model"],
        "messages": [
            {
                "role": "user",
                "content": f"""{prompt}
                将上述代码添加丰富的中文注释, 代码注释率要达到90%以上, 不要省略掉任何代码, 不要遗漏任何一行代码, 我需要直接编译, 原来的代码不能被删除或者省略, 
                只返回给我代码, 不要你额外的解释, 只返回给我代码.
                """
            }
        ]
    }

    response = requests.post(url, headers=headers, json=data)
    if response.status_code == 200:
        response_data = response.json()["choices"][0]["message"]["content"]
        return response_data
    else:
        return "Error: " + response.text

def ai_write_junit_test(prompt):
    url = config.ZHIPU["url"]
    current_timestamp = datetime.now().timestamp()

    headers = {
        "Authorization": config.ZHIPU["token"],
        "Content-Type": "application/json"
    }
    messages = [
        {
            "role": "user",
            "content": f"""{prompt}
                            需求:根据上述代码写单元测试类,测试类要保证通过率为100%,
                            用'junit4'框架,
                            用'@RunWith(SpringRunner.class)'注解,用'org.junit.Assert'判断参数,别忘记写'package',不要忽略任何的'import',比如'import org.junit.Test',
                            不使用'@SpringBootTest'注解,不使用'@InjectMocks'注解,不使用'(expected = Exception.class)'注解参数,
                            利用反射使用所有的函数和类属性,使用'throws Exception'抛出所有的异常,使用'method.setAccessible(true);'设置方法访问控制,然后强转反射函数的返回值.
                            只返回给我代码不要写额外的描述,保证我直接可用.
                            """
        }
    ]
    data = {
        "model": config.ZHIPU["model"],
        "max_tokens": 4096,
        # "system_prompt": "书写测试单元类 用 @RunWith(SpringRunner.class) 注解",
        "customerId": f"${current_timestamp}",
        "messages": messages
    }

    response = requests.post(url, headers=headers, json=data)
    if response.status_code == 200:
        response_data = response.json()["choices"][0]["message"]["content"]
        print(response_data)
        return response_data
    else:
        return "Error: " + response.text

def get_config():
    with open('config.yaml', 'r', encoding='utf-8') as file:
        config = yaml.safe_load(file)
    return config

def ai_learn_writing_code_and_write_junit_test(file_path):
    url = config.ZHIPU["url"]
    current_timestamp = datetime.now().timestamp()
    # 获取当前工作目录
    current_directory = os.getcwd() + "\\template"
    token = config.ZHIPU["token"]
    model = config.ZHIPU["model"]

    headers = {
        "Authorization": token,
        "Content-Type": "application/json"
    }
    messages = [
        {
            "role": "user",
            "content": f"""
                    需求: 你将要学习一些java的代码, 代码的内容是junit的测试模板代码, 学会注解使用, 学会模拟数据, 学习测试代码的书写风格
                    """
        }
    ]
    data = {
        "model": model,
        "max_tokens": 4096,
        "customerId": f"${current_timestamp}",
        "messages": messages
    }

    # 获取当前目录下所有文件的完整路径
    all_file_paths = [os.path.join(current_directory, f) for f in os.listdir(current_directory) if
                      os.path.isfile(os.path.join(current_directory, f))]
    for all_file_path in all_file_paths:
        response = requests.post(url, headers=headers, json=data)
        if response.status_code == 200:
            response_data = response.json()["choices"][0]["message"]
            print(response_data["content"])
        else:
            return "Error: " + response.text

        with open(all_file_path, "r", encoding="utf-8") as file:
            content = file.read()
        user_callback_mes = {
            "role": "user",
            "content": content
        }
        messages.append(response_data)
        messages.append(user_callback_mes)

    # 学习结束 开始写一个测试类
    with open(file_path, "r", encoding="utf-8") as file:
        content = file.read()
    user_callback_mes = {
        "role": "user",
        "content": f"""${content}
             需求: 上述代码的所有方法, 用junit4框架, 用mock模拟操作数据库的操作, mock所有可能连接外部资源获取数据的数据, 写测试类, 只返回给我代码不要写额外的描述, 保证我直接可用
            """
    }
    messages.append(user_callback_mes)
    response = requests.post(url, headers=headers, json=data)
    if response.status_code == 200:
        response_data = response.json()["choices"][0]["message"]["content"]
        print(response_data)
        return response_data
    else:
        return "Error: " + response.text

if __name__ == "__main__":
    target_file = r"D:\ideaCode\company\gitCode\sonarCode\cnv-front-schedule-java-service\src\main\java\com\unitechs\common\util\CirFluxRedisUtils.java"
    with open(target_file, "r", encoding="utf-8") as file:
        content = file.read()
    # 代码语言类型为java，需要用到的测试框架为junit4
    print(len(content))