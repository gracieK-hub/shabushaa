import tensorflow as tf
from tensorflow.keras.preprocessing.image import ImageDataGenerator

# 定义图像大小和批量大小
img_height, img_width = 224, 224
batch_size = 32

# 创建数据增强生成器
train_datagen = ImageDataGenerator(
    rescale=1./255,  # 将像素值归一化到[0,1]
    rotation_range=20,  # 随机旋转
    width_shift_range=0.2,  # 随机水平平移
    height_shift_range=0.2,  # 随机垂直平移
    shear_range=0.2,  # 随机错切变换
    zoom_range=0.2,  # 随机缩放
    horizontal_flip=True,  # 随机水平翻转
    validation_split=0.2  # 将数据集的20%用作验证集
)

# 创建训练数据生成器
train_generator = train_datagen.flow_from_directory(
    r'D:\code\dataset',  # 数据集路径
    target_size=(img_height, img_width),  # 调整图像大小
    batch_size=batch_size,  # 批量大小
    class_mode='binary',  # 二分类
    subset='training'  # 设置为训练集
)

# 创建验证数据生成器
validation_generator = train_datagen.flow_from_directory(
    r'D:\code\dataset',  # 数据集路径
    target_size=(img_height, img_width),  # 调整图像大小
    batch_size=batch_size,  # 批量大小
    class_mode='binary',  # 二分类
    subset='validation'  # 设置为验证集
)


from tensorflow.keras.applications import EfficientNetB0
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, GlobalAveragePooling2D

# 加载 EfficientNetB0 模型，不包括顶层分类部分
base_model = EfficientNetB0(weights='imagenet', include_top=False, input_shape=(img_height, img_width, 3))

# 构建新的模型
model = Sequential([
    base_model,
    GlobalAveragePooling2D(),
    Dense(1024, activation='relu'),
    Dense(1, activation='sigmoid')  # 改为sigmoid激活函数，适应二分类问题
])

# 冻结 base_model 的层
base_model.trainable = False

# 编译模型
model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

# 训练模型
model.fit(
    train_generator,
    epochs=10,
    validation_data=validation_generator
)

# 解冻预训练模型的部分层
base_model.trainable = True

# 只训练后面的几个层
for layer in base_model.layers[:-10]:
    layer.trainable = False

# 重新编译模型
model.compile(optimizer=tf.keras.optimizers.Adam(1e-5),  # 使用较低的学习率
              loss='binary_crossentropy',
              metrics=['accuracy'])

# 继续训练模型
model.fit(
    train_generator,
    epochs=10,
    validation_data=validation_generator
)

# 保存模型
model.save('efficientnetb0_model_binary_finetuned.keras')



import numpy as np
from tensorflow.keras.preprocessing import image
import matplotlib.pyplot as plt

def load_and_preprocess_image(img_path, target_size=(224, 224)):
    img = image.load_img(img_path, target_size=target_size)
    img_array = image.img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0)  # 增加批量维度
    img_array = img_array / 255.0  # 归一化
    return img_array

# 加载训练好的 Keras 模型
model = tf.keras.models.load_model('efficientnetb0_model_binary_finetuned.keras')

# 测试图像路径
img_path = r'D:\code\dataset\dnficon\Snipaste_2024-06-15_10-28-29.jpg'  # 替换为要测试的图像路径

# 加载和预处理图像
img_array = load_and_preprocess_image(img_path)

# 打印预处理后的图像
plt.imshow(img_array[0])
plt.show()

# 进行预测
predictions = model.predict(img_array)
predicted_class = (predictions[0] > 0.5).astype("int32")  # 二分类阈值为0.5

# 输出预测结果
print(f'Predicted class: {predicted_class}')
