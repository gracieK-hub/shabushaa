
inputName = input("请输入文件名")
index = inputName.rfind(".")
copyName = inputName[:index] + '复制' + inputName[index:]

inputFile = open(inputName, 'r')
copyFile = open(copyName, 'w')

line = inputFile.readline()
while line:
    copyFile.write(line)
    line = inputFile.readline()

inputFile.close()
copyFile.close()