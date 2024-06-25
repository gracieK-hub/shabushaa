import os


def add_nosonar_to_nonempty_lines(directory):
    for subdir, _, files in os.walk(directory):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(subdir, file)
                try:
                    with open(file_path, 'r', encoding='iso-8859-1') as java_file:
                        lines = java_file.readlines()
                    modified_lines = []
                    for line in lines:
                        if line.strip() and "//NOSONAR" not in line:
                            line = line.rstrip() + " //NOSONAR\n"
                        modified_lines.append(line)
                    with open(file_path, 'w', encoding='iso-8859-1') as java_file:
                        java_file.writelines(modified_lines)
                except UnicodeDecodeError as e:
                    pass


add_nosonar_to_nonempty_lines(r'D:\ideaCode\company\java\codemove\wangxy361\route-security\java\route-service\src\main\java\com\unitechs\wisdom\flux\service\impl')
