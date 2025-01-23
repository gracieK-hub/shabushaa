import re


def split_java_code_by_methods(code, max_length):
    """
    Split Java code by methods, ensuring each part is within the max_length limit.

    :param code: The Java code as a string.
    :param max_length: The maximum length of each part.
    :return: A list of code parts.
    """
    parts = []
    current_part = ""
    method_pattern = re.compile(r'(?s)(public\s+[\w<>\s]+[\w<>\s]+\s+[\w]+\(.*?\)\s*{.*?})')
    matches = method_pattern.finditer(code)

    for match in matches:
        method_code = match.group(0)
        if len(current_part) + len(method_code) > max_length:
            # Current part is full, add it to the list and start a new part
            parts.append(current_part)
            current_part = ""
        current_part += method_code

    if current_part:
        parts.append(current_part)

    return parts

target_file = r"D:\ideaCode\company\gitCode\sonarCode\cnv-front-schedule-java-service\src\main\java\com\unitechs\common\util\CirFluxRedisUtils.java"
with open(target_file, "r", encoding="utf-8") as file:
    content = file.read()
java_code = content

max_length = 1000
code_parts = split_java_code_by_methods(java_code, max_length)
for i, part in enumerate(code_parts):
    print(f"Part {i + 1}:\n{part}\n")
