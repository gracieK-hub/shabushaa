import os

import requests

import ai_response
import config

SONAR_API_BASE_URL = f"http://{config.LOCAL_SERVER['ip']}:{config.LOCAL_SERVER['port']}"

def get_scan_results():
    url = f"{SONAR_API_BASE_URL}/api/issues/search"
    page = 1
    ps = 500
    all_issues = []

    while True:
        params = {
            'componentKeys': config.PROJECT_INFO['key'],
            's': 'FILE_LINE',
            'resolved': 'false',
            # 'severities': 'BLOCKER,CRITICAL,MAJOR,MINOR',
            'ps': ps,
            'p': page,
            # 'facets': 'owaspTop10,sansTop25,severities,sonarsourceSecurity,types',
            'facets': 'owaspTop10,sansTop25,severities,sonarsourceSecurity,types,scopes,resolutions',
            'additionalFields': '_all',
            'timeZone': 'Asia/Shanghai'
        }
        response = requests.get(url, params=params, auth=(config.SONAR_SERVER['username'], config.SONAR_SERVER['password']))

        if response.status_code == 200:
            issues = response.json().get('issues', [])
            all_issues.extend(issues)
            if len(issues) < ps:
                break
            page += 1
        else:
            print(f"出错: {response.status_code}, {response.text}")
            break

    return all_issues

def scan_local_repo():
    path = config.PROJECT_INFO['local_path']
    java_files = []
    for root, dirs, files in os.walk(path):
        for file in files:
            if file.endswith(".java"):
                file_path = os.path.join(root, file)
                java_files.append(os.path.abspath(file_path))
    return java_files

def get_issues():
    issues = get_scan_results()
    if not issues:
        print("空结果")
        return
    print(f"未处理重复行, 总共有: {len(issues)}")
    dist_issue = set()
    for issue in issues:
        file_path = issue.get('component', '').split(':')[1]
        if file_path not in dist_issue:
            dist_issue.add(file_path)
    print(f"去重后, 总共有: {len(dist_issue)}")
    return dist_issue

def ai_modify():
    # 项目路径
    prefix_path = config.PROJECT_INFO['local_path_prefix']
    for get_issue in get_issues():
    # get_issue = list(sonar_serve.get_issues())[0]
        edit_path = get_issue.replace("/", "\\")
        edit_path = prefix_path + edit_path
        print("修改文件: " + edit_path)
        with open(edit_path, 'r', encoding='utf-8') as file:
            content = file.read()

        if len(content) < 4000:
            write_java_str = ai_response.get_ai_response(content)
            filtered_lines = [line for line in write_java_str.splitlines() if '```java' not in line and '```' not in line]

            with open(edit_path, 'w', encoding='utf-8') as file:
                file.write('\n'.join(filtered_lines))

def ai_modify_only_comment_line():
    local_file = scan_local_repo()
    for ele in local_file:
        with open(ele, 'r', encoding='utf-8') as file:
            content = file.read()

        if len(content) < 4000:
            print("修改文件: " + ele)
            write_java_str = ai_response.get_ai_response(content)
            filtered_lines = [line for line in write_java_str.splitlines() if
                              '```java' not in line and '```' not in line]

            with open(ele, 'w', encoding='utf-8') as file:
                file.write('\n'.join(filtered_lines))

if __name__ == '__main__':
    for ele in scan_local_repo():
        print(ele)