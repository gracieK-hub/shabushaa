import requests
import config
from datetime import datetime


# 将上述代码用sonar严格规则扫描之后,
#                 优化代码sonar优先级：[BLOCKER, CRITICAL, MAJOR] 三个级别,
#                 添加详细的中文注释提高注释率, 删除无效代码和无效引用. 注意你返回的结果要包含全部代码,
#                 不要省略掉任何代码, 原来的代码不能被删除或者省略, 不能写: ... (省略其他方法以节省空间), 要全部返回给我, 不能因为你优化之后我无法使用了, 不要添加多余的描述###之类的, 只返回给我代码, 不要你额外的解释, 只返回给我代码.

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
        response_data = response.json()["choices"][0]["message"]["ontent"]
        return response_data
    else:
        return "Error: " + response.text


def ai_write_junit_test(prompt):
    url = config.ZHIPU["url"]
    current_timestamp = datetime.now().timestamp()

    messages = [
        {
            "role": "user",
            "content": f"""{prompt}
                    覆盖上述代码的所有方法, 用junit4框架, 用mock模拟操作数据库的操作, mock所有可能连接外部资源获取数据的数据, 写测试类, 只返回给我代码不要写额外的描述, 保证我直接可用
                    """
        }
    ]
    headers = {
        "Authorization": config.ZHIPU["token"],
        "Content-Type": "application/json"
    }
    data = {
        "model": config.ZHIPU["model"],
        "max_tokens": 4096,
        "system_prompt": "你是一个代码助手, 非常善于解决代码为听",
        "customerId": f"${current_timestamp}",
        "messages": messages
    }

    response = requests.post(url, headers=headers, json=data)
    print(response.text)
    if response.status_code == 200:
        response_data = response.json()["choices"][0]["message"]
    else:
        return "Error: " + response.text

    user_callback_mes = {
            "role": "user",
            "content": "重新写一下"
        }
    messages.append(response_data)
    messages.append(user_callback_mes)

    data2 = {
        "model": config.ZHIPU["model"],
        "max_tokens": 4096,
        "system_prompt": "你是一个代码助手, 非常善于解决代码问题",
        "customerId": f"${current_timestamp}",
        "messages": messages
    }
    response2 = requests.post(url, headers=headers, json=data2)
    print(response2.text)


if __name__ == "__main__":
    target_file = r"D:\ideaCode\company\gitCode\sonarCode\cnv-front-cas-java-service\src\main\java\com\unitechs\cas\sysmanager\service\RoleTypeAttackNetService.java"
    with open(target_file, "r", encoding="utf-8") as file:
        content = file.read()
    ai_write_junit_test(content)