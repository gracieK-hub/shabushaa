import requests
import config
from openai import OpenAI

# 将上述代码用sonar严格规则扫描之后,
#                 优化代码sonar优先级：[BLOCKER, CRITICAL, MAJOR] 三个级别,
#                 添加详细的中文注释提高注释率, 删除无效代码和无效引用. 注意你返回的结果要包含全部代码,
#                 不要省略掉任何代码, 原来的代码不能被删除或者省略, 不能写: ... (省略其他方法以节省空间), 要全部返回给我, 不能因为你优化之后我无法使用了, 不要添加多余的描述###之类的, 只返回给我代码, 不要你额外的解释, 只返回给我代码.


# def get_openai_response(prompt):
#     client = OpenAI(api_key=config.OPENAI['token'], organization=config.OPENAI['organization'])
#
#     completion = client.chat.completions.create(
#         model="gpt-4o-mini",
#         messages=[
#             # {"role": "system", "content": "You are a helpful assistant."},
#             {
#                 "role": "user",
#                 "content": prompt
#             }
#         ]
#     )
#     print(completion.choices[0].message)

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
        response_data = response.json()["choices"][0]["message"]['content']
        return response_data
    else:
        return "Error: " + response.text


if __name__ == "__main__":
    target_file = r"D:\ideaCode\company\gitCode\buckup\cnv-front-schedule-java-service\src\main\java\com\unitechs\common\util\CirFluxRedisUtils.java"
    with open(target_file, 'r', encoding='utf-8') as file:
        content = file.read()
    print(get_ai_response(content))