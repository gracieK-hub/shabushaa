import tensorflow as tf
import tensorflow_model_optimization as tfmot


def convert_model_with_quantization():
    # 加载预训练的 MobileNetV2 模型
    model = tf.keras.applications.MobileNetV2(weights='imagenet')
    model.summary()

    # 应用量化感知训练
    quantize_model = tfmot.quantization.keras.quantize_model
    q_aware_model = quantize_model(model)

    # 编译量化模型
    q_aware_model.compile(optimizer='adam',
                          loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
                          metrics=['accuracy'])

    # 这里你可以使用已经训练好的权重进行量化感知训练，或者直接转换
    # q_aware_model.fit(train_dataset, validation_data=val_dataset, epochs=1)

    # 转换为 TensorFlow Lite 模型
    converter = tf.lite.TFLiteConverter.from_keras_model(q_aware_model)
    converter.optimizations = [tf.lite.Optimize.DEFAULT]
    tflite_model = converter.convert()

    # 保存转换后的模型
    with open('mobilenet_v2_quant.tflite', 'wb') as f:
        f.write(tflite_model)
    print("Model converted with quantization successfully!")


convert_model_with_quantization()
