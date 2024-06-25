import cv2
import numpy as np
from tensorflow.keras.applications.mobilenet_v2 import MobileNetV2, preprocess_input, decode_predictions
from tensorflow.keras.preprocessing import image
# E:\picture\pi.png

# 指定本地模型文件的路径
model_path = r"D:\code\shabushaa\pday20240527\saved_model.pb"
# 加载模型
model = tf.saved_model.load(model_path)
# 加载标签
imagenet_labels_path = tf.keras.utils.get_file('ImageNetLabels.txt','https://storage.googleapis.com/download.tensorflow.org/data/ImageNetLabels.txt')
with open(imagenet_labels_path) as f:
    imagenet_labels = f.read().splitlines()

# 定义图像预处理函数
def preprocess_image(image):
    image = cv2.resize(image, (224, 224))
    image = image / 255.0  # 归一化
    return image.astype(np.float32)

# 定义图像识别函数
def predict_image(image_path):
    # 读取图像
    image = cv2.imread(image_path)
    # 预处理图像
    preprocessed_image = preprocess_image(image)
    # 添加批次维度
    input_image = np.expand_dims(preprocessed_image, axis=0)
    # 图像识别
    predictions = model.predict(input_image)
    # 获取前5个预测结果
    top_k = np.argsort(predictions[0])[-5:][::-1]
    # 打印预测结果
    for i in top_k:
        label = imagenet_labels[i]
        confidence = predictions[0][i]
        print(f"{label}: {confidence:.2f}")

# 测试图像识别函数
image_path = r"E:\picture\pi.png"
predict_image(image_path)
