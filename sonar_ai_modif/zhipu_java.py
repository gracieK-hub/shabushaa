import os

import requests
import yaml


def ai_writing_annotation(class_content, class_path, zhipu_url, headers, data):
    data['messages'].append({
                "role": "user",
                "content": f"""{class_content}
                将上述代码添加丰富的中文注释, 代码注释率要达到90%以上, 不要省略掉任何代码, 不要遗漏任何一行代码, 我需要直接编译, 原来的代码不能被删除或者省略, 
                只返回给我代码, 不要你额外的解释, 只返回给我代码.
                """
            })
    response = requests.post(zhipu_url, headers=headers, json=data)
    response_data = response.json()["choices"][0]["message"]["content"]
    filtered_lines = [line for line in response_data.splitlines() if '```java' not in line and '```' not in line]
    with open(class_path, 'w', encoding='utf-8') as file:
        file.write('\n'.join(filtered_lines))
    data['messages'].clear()


def ai_learn_writing_code_and_write_junit_test():
    config = get_config()
    zhipu_url = config['ZHIPU']['url']
    zhipu_token = config['ZHIPU']['token']
    zhipu_model = config['ZHIPU']['model']
    headers = {
        "Authorization": zhipu_token,
        "Content-Type": "application/json"
    }
    messages = []
    data = {
        "model": zhipu_model,
        "max_tokens": 4096,
        "system_prompt": "java编程语言",
        "messages": messages
    }
    class_list = scan_local_get_java_file_path()
    for class_path in class_list:
        print(f'开始处理文件: {class_path}')
        class_content = get_file_content(class_path)
        if len(class_content) > 4000:
            print(f'文件内容超过4000,跳过: {class_path}')
            continue
        ai_writing_annotation(class_content, class_path, zhipu_url, headers, data)
        need_upload_file_list = check_file_content_get_import_java_path(class_content, class_list, class_path)
        messages.clear()
        if len(need_upload_file_list) > 0:
            for need_upload_file in need_upload_file_list:
                ai_need_class_content = get_file_content(need_upload_file)
                if len(ai_need_class_content) > 4000:
                    print(f'文件内容超过4000,跳过: {need_upload_file}')
                    continue
                print(f'需要上传文件: {need_upload_file}')
                messages.append({
                    "role": "user",
                    "content": ai_need_class_content
                })
                ai_need_class_callback = requests.post(zhipu_url, headers=headers, json=data)
                if ai_need_class_callback.status_code == 200:
                    ai_need_class_callback_data = ai_need_class_callback.json()['choices'][0]['message']
                else:
                    print(f'Error: {ai_need_class_callback.text}')
                    continue
                messages.append(ai_need_class_callback_data)
        if class_path.endswith('Controller.java') or class_path.endswith('Action.java'):
            messages.append({
                "role": "user",
                "content": f"""{class_content}
                            需求:根据上述代码写单元测试类，
                            用 junit4 框架，Controller类编写JUnit 4单元测试，我们需要模拟其依赖的服务层 Service 以及可能用到的其他组件，
                            别忘记写 package，
                            只返回给我代码不要写额外的描述，保证我直接可用。
                            """
            })
        elif class_path.endswith('Service.java') or class_path.endswith('ServiceImpl.java'):
            messages.append({
                "role": "user",
                "content": f"""{class_content}
                            需求:根据上述代码写单元测试类，
                            用 junit4 框架，Service类编写单元测试，我们需要使用 Mockito 模拟其依赖的Mapper或者其他Service。
                            别忘记写 package，
                            只返回给我代码不要写额外的描述，保证我直接可用。
                            """
            })
        elif class_path.endswith('Mapper.java') or class_path.endswith('Dao.java'):
            messages.append({
                "role": "user",
                "content": f"""{class_content}
                            需求:根据上述代码写单元测试类，
                            用 junit4 框架，为了编写Mapper的单元测试，我们需要使用Mockito来模拟MyBatis的Mapper接口。
                            由于Mapper接口本身不包含业务逻辑，单元测试的主要目的是验证接口方法是否被正确调用，以及调用时是否传递了正确的参数。
                            别忘记写 package，
                            只返回给我代码不要写额外的描述，保证我直接可用。
                            """
            })
        else:
            messages.append({
                "role": "user",
                "content": f"""{class_content}
                            需求:根据上述代码写单元测试类，
                            用 junit4 框架，编写单元测试，可能需要使用Mockito来模拟其他相关的接口。
                            别忘记写 package，实体类不需要mock，我们需要验证类的属性是否正确设置和获取，
                            只返回给我代码不要写额外的描述，保证我直接可用。
                            """
            })
        response = requests.post(zhipu_url, headers=headers, json=data)
        if response.status_code == 200:
            response_data = response.json()['choices'][0]['message']['content']
        else:
            print("Error: " + response.text)
            continue
        filter_lines = [line for line in response_data.splitlines() if '```java' not in line and '```' not in line]
        final = class_path.replace('\src\main\java', '\src\\test\java').replace('.java', 'Test.java')
        print(f'生成的测试类: {final}')
        directory = os.path.dirname(final)
        if not os.path.exists(directory):
            os.makedirs(directory)
        with open(final, 'w', encoding='utf-8') as file:
            file.write('\n'.join(filter_lines))

def check_file_content_get_import_java_path(content, class_list, class_path):
    import_java_file_list = check_java_import(content)
    need_upload_file_list = []
    if class_path.endswith('Controller.java'):
        print(f'当前类是Controller类,需要上传基础类')
        current_directory = os.getcwd() + "\\ControllerNeed"
        all_file_paths = [os.path.join(current_directory, f) for f in os.listdir(current_directory) if os.path.isfile(os.path.join(current_directory, f))]
        need_upload_file_list = all_file_paths
    for need_upload in class_list:
        for import_java_file in import_java_file_list:
            need_upload_classname = need_upload.split('\\')[-1].replace('.java', '')
            if import_java_file == need_upload_classname:
                need_upload_file_list.append(need_upload)
    print(f'need_upload_file_list: {need_upload_file_list}')
    return need_upload_file_list

def get_config():
    with open('config.yaml', 'r', encoding='utf-8') as file:
        config = yaml.safe_load(file)
    return config

def scan_local_get_java_file_path():
    config = get_config()
    project_path = config['PROJECT_INFO']['local_path']
    java_files = []
    exclude_keywords = ['Constant', 'Bootstrap', 'Config', 'Client']
    for root, dirs, files in os.walk(project_path):
        for file in files:
            if (file.endswith('.java') and not file.endswith('Test.java') and not any(keyword in file for keyword in exclude_keywords)):
                full_path = os.path.join(root, file)
                java_files.append(full_path)
    for java_file in java_files:
        print(java_file)
    return java_files

def get_file_content(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
    return content

def check_java_import(content):
    lines = content.splitlines()
    imports = [line.strip() for line in lines if line.strip().startswith('import')]
    print(f'imports: {imports}')
    import_classname_list = []
    for ele in imports:
        # 如果包含注释 那么就去除掉 //及后面的注释
        if '//' in ele:
            ele = ele.split('//')[0].strip()
        classname = ele.split('.')[-1].replace(';', '')
        import_classname_list.append(classname)
    print(f'import_classname_list: {import_classname_list}')
    return import_classname_list

if __name__ == '__main__':
    ai_learn_writing_code_and_write_junit_test()
