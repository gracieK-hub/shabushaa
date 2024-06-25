
import tensorflow as tf

# 加载模型
model_path = r"D:\code\shabushaa\pday20240527\large-075-224-classification\1\saved_model.pb"
model = tf.keras.models.load_model(model_path)

# 保存模型为.h5格式
model.save(r"D:\code\shabushaa\pday20240527\model.h5")
