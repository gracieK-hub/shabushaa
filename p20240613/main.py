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
    class_mode='categorical',  # 标签模式
    subset='training'  # 设置为训练集
)

# 创建验证数据生成器
validation_generator = train_datagen.flow_from_directory(
    r'D:\code\dataset',  # 数据集路径
    target_size=(img_height, img_width),  # 调整图像大小
    batch_size=batch_size,  # 批量大小
    class_mode='categorical',  # 标签模式
    subset='validation'  # 设置为验证集
)
