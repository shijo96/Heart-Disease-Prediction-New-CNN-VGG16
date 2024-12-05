from flask import *
from database import*
import os
from training import vgg_16_training
from pred import predict_image
from tensorflow.keras.models import load_model


admin=Blueprint('admin',__name__)

# Load the trained model
model = load_model('ecg_model_vgg16_new_100.h5')


@admin.route("admin_home")
def admin_home():
	data={}
	if not session.get("lid") is None:
		
		return render_template("admin_home.html",data=data)
	else:
		return redirect(url_for("public.login"))

@admin.route('/admin_view_doctors',methods=['get','post'])
def admin_view_doctors():
	data={}
	q="SELECT * FROM `doctors` inner join login using(login_id)"
	res=select(q)
	data['view_doc']=res

	if "action" in request.args:
		action=request.args['action']
		id=request.args['id']
	else:
		action = None
	if action == "accept":
		q="UPDATE `doctors` SET `status`='Approved' WHERE `doctor_id`='%s'"%(id)
		update(q)
		flash(('Success..!', 'success'))  # Flash a success message
		return redirect(url_for('admin.admin_view_doctors')) 
	
	if action == "reject":
		q="UPDATE `doctors` SET `status`='Rejected' WHERE `doctor_id`='%s'"%(id)
		update(q)
		flash(('Rejected..!', 'danger'))  # Flash a danger message
		return redirect(url_for('admin.admin_view_doctors')) 
	
	
	return render_template("admin_view_doctors.html",data=data)


import os
import shutil  # Import shutil to handle directory deletion
from flask import jsonify, render_template, request

@admin.route('/admin_train_dataset', methods=['GET', 'POST'])
def admin_train_dataset():
	result = None  # Variable to hold the result for rendering in the template
	if request.method == 'POST':
		if "upload_data" in request.form:
			epo = request.form['epo']
			# Check if the folder was uploaded
			if 'datasetFolder' not in request.files:
				return render_template("admin_train_dataset.html", result={"error": "No file part"})

			files = request.files.getlist('datasetFolder')
			if not files:
				return render_template("admin_train_dataset.html", result={"error": "No files uploaded"})

			# Create a base directory to store uploaded files
			upload_folder = 'Dataset/'

			# Delete the existing Dataset folder if it exists
			if os.path.exists(upload_folder):
				shutil.rmtree(upload_folder)  # Remove the existing folder and its contents

			os.makedirs(upload_folder, exist_ok=True)  # Create a new upload folder

			# Initialize counts and structure
			total_files = 0
			subfolder_counts = {}

			for file in files:
				# Get the original path of the file
				file_path = file.filename  # This includes any subdirectory structure

				# Create the full path to save the file
				full_path = os.path.join(upload_folder, file_path)

				# Ensure that the directory exists before saving the file
				os.makedirs(os.path.dirname(full_path), exist_ok=True)

				# Save the file
				file.save(full_path)

				# Count total files
				total_files += 1

				# Handle subfolder counts
				subfolder_name = os.path.dirname(file_path)  # Get the subfolder name
				if subfolder_name:
					if subfolder_name not in subfolder_counts:
						subfolder_counts[subfolder_name] = 0
					subfolder_counts[subfolder_name] += 1

			# Call the training function
			tr = vgg_16_training(upload_folder,epo)
			print("^" * 100)
			print("Accuracy : ",tr)

			# Prepare the final result
			result = {
				"message": "Training completed successfully!",
				"model_accuracy": tr,
				"training_time": "0 hours 0 minutes",
				"folder_name": upload_folder,
				"total_uploaded_files": total_files,
				"subfolder_counts": subfolder_counts,
			
			}

	# Render the template for both GET and POST requests, passing the result
	return render_template("admin_train_dataset.html", result=result)



# import os
# from flask import jsonify, render_template

# @admin.route('/admin_train_dataset', methods=['GET', 'POST'])
# def admin_train_dataset():
# 	result = None  # Variable to hold the result for rendering in the template
# 	if request.method == 'POST':
# 		if "upload_data" in request.form:
# 			# Check if the folder was uploaded
# 			if 'datasetFolder' not in request.files:
# 				return render_template("admin_train_dataset.html", result={"error": "No file part"})

# 			files = request.files.getlist('datasetFolder')
# 			if not files:
# 				return render_template("admin_train_dataset.html", result={"error": "No files uploaded"})

# 			# Create a base directory to store uploaded files
# 			upload_folder = 'Dataset/'
# 			os.makedirs(upload_folder, exist_ok=True)

# 			# Initialize counts and structure
# 			total_files = 0
# 			subfolder_counts = {}

# 			for file in files:
# 				# Get the original path of the file
# 				file_path = file.filename  # This includes any subdirectory structure

# 				# Create the full path to save the file
# 				full_path = os.path.join(upload_folder, file_path)

# 				# Ensure that the directory exists before saving the file
# 				os.makedirs(os.path.dirname(full_path), exist_ok=True)

# 				# Save the file
# 				file.save(full_path)

# 				# Count total files
# 				total_files += 1

# 				# Handle subfolder counts
# 				subfolder_name = os.path.dirname(file_path)  # Get the subfolder name
# 				if subfolder_name:
# 					if subfolder_name not in subfolder_counts:
# 						subfolder_counts[subfolder_name] = 0
# 					subfolder_counts[subfolder_name] += 1
			
			
			
# 			tr=vgg_16_training(upload_folder)
# 			print("^"*100)
# 			# print(tr)


# 			# Prepare the final result
# 			result = {
# 				"message": "Training completed successfully!",
# 				"model_accuracy": "95%",
# 				"training_time": "2 hours 30 minutes",
# 				"folder_name": upload_folder,
# 				"total_uploaded_files": total_files,
# 				"subfolder_counts": subfolder_counts
# 			}


# 	# Render the template for both GET and POST requests, passing the result
# 	return render_template("admin_train_dataset.html", result=result)




import os
from werkzeug.utils import secure_filename

@admin.route('/admin_test_dataset', methods=['GET', 'POST'])
def admin_test_dataset():
	data = {}
	if request.method == 'POST' and "submit" in request.form:

		import os
		import shutil

		# Specify the path to the directory
		dir_path = 'static/temp'

		# Check if the directory exists
		if os.path.isdir(dir_path):
			# Iterate over all files and subdirectories in the directory
			for filename in os.listdir(dir_path):
				file_path = os.path.join(dir_path, filename)  # Get full file path
				try:
					if os.path.isfile(file_path):
						os.remove(file_path)  # Remove file
						print(f"File {file_path} has been removed.")
					elif os.path.isdir(file_path):
						shutil.rmtree(file_path)  # Remove directory and all its contents
						print(f"Directory {file_path} has been removed.")
				except Exception as e:
					print(f"Error removing {file_path}: {e}")
		else:
			print(f"Directory {dir_path} does not exist.")


		imageFile = request.files['imageFile']

		# Save the file temporarily
		filename = secure_filename(imageFile.filename)
		temp_file_path = os.path.join('static/temp', filename)  # Create a 'temp' directory if it doesn't exist
		imageFile.save(temp_file_path)
		print("temp_file_path : ",temp_file_path)

		# Perform prediction logic here
		predicted_result = predict_image(model, temp_file_path)
		print("predicted_result : ",predicted_result)
		# Unpacking the tuple
		label, confidence = predicted_result

		# Print the results
		print("Label: ", label)
		print("Confidence: ", confidence)

		# Convert to percentage
		confidence_percentage = confidence * 100

		# Print the result
		print(f"Confidence: {confidence_percentage:.2f}%")
		conf = f"{confidence_percentage:.2f}%"
		print("conf : ",conf)



		# Store the prediction result in the data dictionary
		data['predicted_result'] = label +" - "+ conf
		data['image_url'] = filename  # Store the filename to display the image

		# Optionally: remove the temp file after use
		# os.remove(temp_file_path)

	return render_template("admin_test_dataset.html", data=data)


# @admin.route('/admin_test_dataset', methods=['GET', 'POST'])
# def admin_test_dataset():
#     data = {}
#     if request.method == 'POST' and "submit" in request.form:
#         imageFile = request.files['imageFile']
        
#         # Perform prediction logic here
#         # Assuming you have a function `predict_image(image)` that returns a prediction
#         predicted_result = predict_image(model,imageFile)  # Implement this function as per your model

#         # Store the prediction result in the data dictionary
#         data['predicted_result'] = predicted_result
#         data['image_url'] = imageFile.filename  # Or save the image and return the path

#     return render_template("admin_test_dataset.html", data=data)



@admin.route('/admin_view_patient',methods=['get','post'])
def admin_view_patient():
	data={}

	q="SELECT * FROM `users`"
	res=select(q)
	data['view_user']=res
	
	return render_template("admin_view_patient.html",data=data)


@admin.route('/admin_view_complaints',methods=['get','post'])
def admin_view_complaints():
	data={}
	q="SELECT * FROM `complaints` INNER JOIN users using(`user_id`) ORDER BY complaint_id DESC"
	res=select(q)
	data['comp']=res
	
	return render_template("admin_view_complaints.html",data=data)


@admin.route('/admin_send_reply', methods=['POST','GET'])
def admin_send_reply():
	data = request.get_json()
	complaint_id = data['complaint_id']
	reply = data['reply']
	
	# Update the database
	q = "UPDATE complaints SET reply = '%s' WHERE complaint_id = '%s'"%(reply, complaint_id)
	update(q)  # Assuming you have an update function defined to execute the SQL query

	return jsonify(success=True, message="Reply sent successfully.")

@admin.route('/admin_view_top_rated_doctors',methods=['get','post'])
def admin_view_top_rated_doctors():
	data={}
	
	return render_template("admin_view_top_rated_doctors.html",data=data)
