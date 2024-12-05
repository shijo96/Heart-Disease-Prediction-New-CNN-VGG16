# dataset path - dataset


# import tensorflow as tf
# from tensorflow.keras.applications import VGG16
# from tensorflow.keras.models import Model
# from tensorflow.keras.layers import Dense, Flatten, Dropout
# from tensorflow.keras.optimizers import Adam
# from tensorflow.keras.preprocessing.image import ImageDataGenerator

# # Define constants
# IMG_HEIGHT, IMG_WIDTH = 224, 224
# NUM_CLASSES = 3
# BATCH_SIZE = 32
# EPOCHS = 1

# def vgg_16_training(dataset_path):


#     # Load the pre-trained VGG16 model without the top (classification) layers
#     base_model = VGG16(weights='imagenet', include_top=False, input_shape=(IMG_HEIGHT, IMG_WIDTH, 3))

#     # Freeze the pre-trained layers
#     for layer in base_model.layers:
#         layer.trainable = False

#     # Add custom classification layers on top of the pre-trained model
#     x = Flatten()(base_model.output)
#     x = Dense(512, activation='relu')(x)
#     x = Dropout(0.5)(x)
#     output = Dense(NUM_CLASSES, activation='softmax')(x)

#     # Create the model
#     model = Model(inputs=base_model.input, outputs=output)

#     # Compile the model
#     model.compile(optimizer=Adam(),
#                 loss='categorical_crossentropy',
#                 metrics=['accuracy'])

#     # Data augmentation and preprocessing
#     train_datagen = ImageDataGenerator(
#         rescale=1./255,
#         rotation_range=20,
#         width_shift_range=0.2,
#         height_shift_range=0.2,
#         shear_range=0.2,
#         zoom_range=0.2,
#         horizontal_flip=True,
#         validation_split=0.2
#     )

#     train_generator = train_datagen.flow_from_directory(
#         dataset_path,
#         target_size=(IMG_HEIGHT, IMG_WIDTH),
#         batch_size=BATCH_SIZE,
#         class_mode='categorical',
#         subset='training'
#     )

#     validation_generator = train_datagen.flow_from_directory(
#         dataset_path,
#         target_size=(IMG_HEIGHT, IMG_WIDTH),
#         batch_size=BATCH_SIZE,
#         class_mode='categorical',
#         subset='validation'
#     )

#     # Train the model
#     history = model.fit(
#         train_generator,
#         steps_per_epoch=train_generator.samples // BATCH_SIZE,
#         epochs=EPOCHS,
#         validation_data=validation_generator,
#         validation_steps=validation_generator.samples // BATCH_SIZE
#     )

#     # Evaluate the model on the test set
#     test_datagen = ImageDataGenerator(rescale=1./255)
#     test_generator = test_datagen.flow_from_directory(
#         dataset_path,
#         target_size=(IMG_HEIGHT, IMG_WIDTH),
#         batch_size=BATCH_SIZE,
#         class_mode='categorical'
#     )

#     test_loss, test_acc = model.evaluate(test_generator, steps=test_generator.samples // BATCH_SIZE)
#     print(f'Test accuracy: {test_acc}')

#     # Save the model
#     model.save('ecg_model_vgg16_1.h5')

#     return "success"



# dataset path - Dataset/dataset


import tensorflow as tf
from tensorflow.keras.applications import VGG16
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Dense, Flatten, Dropout
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import os



def vgg_16_training(dataset_path,epo):

    # Define constants
    IMG_HEIGHT, IMG_WIDTH = 224, 224
    NUM_CLASSES = 3
    BATCH_SIZE = 32
    EPOCHS = int(epo)

    # Construct the correct path to the inner dataset directory
    full_dataset_path = os.path.join(dataset_path, 'dataset')
    
    # Verify the dataset directory exists and has the expected structure
    if not os.path.exists(full_dataset_path):
        raise ValueError(f"Dataset directory not found at {full_dataset_path}")
    
    class_dirs = [d for d in os.listdir(full_dataset_path) 
                 if os.path.isdir(os.path.join(full_dataset_path, d))]
    
    if len(class_dirs) != NUM_CLASSES:
        raise ValueError(f"Expected {NUM_CLASSES} classes but found {len(class_dirs)} classes")
    
    print(f"Found {len(class_dirs)} classes: {', '.join(class_dirs)}")
   
    # Load the pre-trained VGG16 model
    base_model = VGG16(weights='imagenet', 
                      include_top=False, 
                      input_shape=(IMG_HEIGHT, IMG_WIDTH, 3))

    # Freeze the pre-trained layers
    for layer in base_model.layers:
        layer.trainable = False

    # Add custom classification layers
    x = Flatten()(base_model.output)
    x = Dense(512, activation='relu')(x)
    x = Dropout(0.5)(x)
    output = Dense(NUM_CLASSES, activation='softmax')(x)

    # Create the model
    model = Model(inputs=base_model.input, outputs=output)

    # Compile the model
    model.compile(optimizer=Adam(learning_rate=0.001),
                 loss='categorical_crossentropy',
                 metrics=['accuracy'])

    # Data augmentation and preprocessing
    train_datagen = ImageDataGenerator(
        rescale=1./255,
        rotation_range=20,
        width_shift_range=0.2,
        height_shift_range=0.2,
        shear_range=0.2,
        zoom_range=0.2,
        horizontal_flip=True,
        validation_split=0.2
    )

    # Create data generators
    try:
        train_generator = train_datagen.flow_from_directory(
            full_dataset_path,
            target_size=(IMG_HEIGHT, IMG_WIDTH),
            batch_size=BATCH_SIZE,
            class_mode='categorical',
            subset='training',
            shuffle=True
        )

        validation_generator = train_datagen.flow_from_directory(
            full_dataset_path,
            target_size=(IMG_HEIGHT, IMG_WIDTH),
            batch_size=BATCH_SIZE,
            class_mode='categorical',
            subset='validation',
            shuffle=True
        )

        print("Class mapping:", train_generator.class_indices)

        # Train the model
        history = model.fit(
            train_generator,
            steps_per_epoch=train_generator.samples // BATCH_SIZE,
            epochs=EPOCHS,
            validation_data=validation_generator,
            validation_steps=validation_generator.samples // BATCH_SIZE
        )

        # Evaluate the model
        test_datagen = ImageDataGenerator(rescale=1./255)
        test_generator = test_datagen.flow_from_directory(
            full_dataset_path,
            target_size=(IMG_HEIGHT, IMG_WIDTH),
            batch_size=BATCH_SIZE,
            class_mode='categorical',
            shuffle=False
        )

        test_loss, test_acc = model.evaluate(test_generator, 
                                           steps=test_generator.samples // BATCH_SIZE)
        print(f'Test accuracy: {test_acc}')



        # Save the model
        # model_save_path = os.path.join(dataset_path, 'ecg_model_vgg16_1.h5')
        model_save_path = 'ecg_model_vgg16_'+epo+'.h5'
        model.save(model_save_path)
        print(f"Model saved to {model_save_path}")
        
        return test_acc

    except Exception as e:
        print(f"Error during training: {str(e)}")
        return f"error: {str(e)}"