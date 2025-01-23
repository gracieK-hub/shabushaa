import re
import os
from pathlib import Path
def add_suppress_warnings_to_java_files(directory):
    class_pattern = re.compile(r'^s*public class+')
    suppress_warning_pattern = re.compile(r'^s*@s*SuppressWarnings(.*).*')
    encodings = ['utf-8', 'iso-8859-1']

    for foldername, _, filenames in os.walk(directory):
        for filename in filenames:
            if filename.endswith('.java'):
                file_path = Path(foldername) / filename
                original_encoding = None

                for encoding in encodings:
                    try:
                        with file_path.open('r', encoding=encoding) as file:
                            java_code = file.readlines()
                        original_encoding = encoding
                        break
                    except UnicodeDecodeError:
                        continue

                if original_encoding is None:
                    continue

                annotation_added = False
                annotation_exists = any(suppress_warning_pattern.match(line) for line in java_code)
                modified_code = []
                for line in java_code:
                    if class_pattern.match(line) and not annotation_added and not annotation_exists:
                        modified_code.append('@SuppressWarnings("all")\n')
                        annotation_added = True
                    modified_code.append(line)

                if annotation_added:
                    with file_path.open('w', encoding=original_encoding) as file:
                        file.writelines(modified_code)

add_suppress_warnings_to_java_files(r'D:\ideaCode\company\gitCode\sonarCode\cnv-front-schedule-java-service\src\test\java')
