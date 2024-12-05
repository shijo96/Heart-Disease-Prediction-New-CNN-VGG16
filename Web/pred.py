
import numpy as np
from tensorflow.keras.preprocessing import image
from tensorflow.keras.models import load_model

# # Load the trained model
model = load_model('ecg_model_vgg16_new_100.h5')

# Function to preprocess an image
def preprocess_image(img_path):
    img = image.load_img(img_path, target_size=(224, 224))
    img_array = image.img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0)
    return img_array / 255.

# Function to make predictions
def predict_image(model, img_path):
    print("*"*100)
    print("img_pathssss : ",img_path)
    preprocessed_img = preprocess_image(img_path)
    prediction = model.predict(preprocessed_img)
    predicted_class = np.argmax(prediction)
    class_map = {
        0: 'Myocardial Infarction',
        1: 'Abnormal Heartbeat',
        2: 'Normal'
    }
    predicted_label = class_map[predicted_class]
    confidence = prediction[0][predicted_class]
    return predicted_label, confidence

# # Example usage
# img_path = r'dataset\ECG Images of Myocardial Infarction Patients (240x12=2880)\MI(240).jpg'  # Replace with the path to your new ECG image
# predicted_label, confidence = predict_image(model, img_path)
# print(f'Predicted Label: {predicted_label}')
# print(f'Confidence: {confidence}')
