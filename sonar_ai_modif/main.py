import os

import config
import sonar_serve
import ai_response

def scan_local_repo():
    path = config.PROJECT_INFO['local_path_prefix']
    java_files = []
    for root, dirs, files in os.walk(path):
        for file in files:
            if file.endswith(".java"):
                file_path = os.path.join(root, file)
                java_files.append(os.path.abspath(file_path))
    return java_files

if __name__ == "__main__":
    # sonar_serve.ai_modify()
    sonar_serve.ai_modify_only_comment_line()

