import os

import requests
import yaml

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
        need_upload_file_list = check_file_content_get_import_java_path(class_content, class_list)
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
        messages.append({
            "role": "user",
            "content": f"""{class_content}
            需求:根据上述代码写单元测试类，
            用 junit4 框架，通过模拟外部服务层的调用来测试。通过确保 HTTP 请求和响应的状态、错误消息和数据返回的正确性，确保测试覆盖率和通过率为 100%。
            别忘记写 package，实体类不需要mock简单测试覆盖
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

def check_file_content_get_import_java_path(content, class_list):
    import_java_file_list = check_java_import(content)
    need_upload_file_list = []
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
