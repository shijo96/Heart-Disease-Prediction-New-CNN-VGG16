from flask import *
from database import *
import uuid
from werkzeug.utils import secure_filename
import base64
from PIL import Image
from io import BytesIO
import os


api = Blueprint('api',__name__)


# Define the path to save images
SAVE_DIR = os.path.join('static', 'ecg_images')
os.makedirs(SAVE_DIR, exist_ok=True)  # Create directory if it doesn't exist



# POST endpoint for login
@api.route('/login', methods=['POST'])
def login():
	# Get JSON data from request
	data = request.get_json()

	# Extract username and password
	username = data.get('username')
	password = data.get('password')

	q ="SELECT * FROM login INNER JOIN users using(login_id) where username='%s' and password='%s'" % (username,password)
	user = select(q)
	print(user)
	print(user[0]['login_id'])
	lid=user[0]['login_id']
	uid=user[0]['user_id']

	if user:
		response = {'status': 'success', 'message': 'Login successful','logid':lid,'uid':uid}
	else:
		response = {'status': 'error', 'message': 'Invalid username or password'}

	# Return the response in JSON format
	return jsonify(response)


# POST endpoint for login
@api.route('/register', methods=['POST'])
def register():
	# Get JSON data from request
	data = request.get_json()

	# Extract username and password
	fname = data.get('fname')
	lname = data.get('lname')
	hname = data.get('hname')
	place = data.get('place')
	phone = data.get('phone')
	email = data.get('email')
	username = data.get('username')
	password = data.get('password')

	q ="SELECT * FROM login where username='%s'" % (username)
	res = select(q)
	if res:
		response = {'status': 'exist', 'message': 'username or password already exist'}
	else:
		q="INSERT INTO `login` VALUES (null,'%s','%s','user')"%(username,password)
		id=insert(q)
		qq="INSERT INTO `users` VALUES (null,'%s','%s','%s','%s','%s','%s','%s')"%(id,fname,lname,hname,place,phone,email)
		user=insert(qq)
		if user:
			response = {'status': 'success', 'message': 'Registration Successful'}
		else:
			response = {'status': 'error', 'message': 'Sorry.. Registration Failed'}

	# Return the response in JSON format
	return jsonify(response)




# @api.route('/register',methods=['get','post'])
# def register():
# 	data={}
# 	fname = request.args['fname']
# 	lname = request.args['lname']
# 	place = request.args['place']
# 	phone = request.args['phone']
# 	email = request.args['email']
# 	passw=request.args['passw']

# 	q1="INSERT INTO `login`(`username`,`password`,`usertype`)VALUES('%s','%s','user')"%(email,passw)
# 	id=insert(q1)
# 	q="INSERT INTO `user`(`login_id`,`fname`,`lname`,`place`,`phone`,`email`)VALUES('%s','%s','%s','%s','%s','%s')" % (id,fname,lname,place,phone,email)
# 	c=insert(q)
# 	if(c>0):
# 		data['status']  = 'success'
# 	else:
# 		data['status']	= 'failed'
# 	data['method']  ='register'
# 	return str(data)



@api.route('/User_profile',methods=['get','post'])
def User_profile():	
	data={}
	logid=request.args['login_id']
	print(logid)
	q="SELECT * FROM `login` INNER JOIN users USING(`login_id`) where login.login_id='%s'"%(logid)
	rr=select(q)
	if rr:
		data['status']  = 'success'
		data['data'] = rr
	else:
		data['status']	= 'failed'
	return  str(data)



@api.route('/User_view_doctors',methods=['get','post'])
def User_view_doctors():	
	data={}

	qq="SELECT * FROM `doctors` where status='Approved'"
	res=select(qq)
	print(res)
	if res:
		data['status']  = 'success'
		
		data['data'] = res
	else:
		data['status']	= 'failed'
	data['method']  = 'User_view_doctors'
	return  str(data)




@api.route('/User_filter_doctors',methods=['get','post'])
def User_filter_doctors():	
	data={}
	filter=request.args['filter']
	user_place=request.args['location']
	print("location : ",user_place)

	if filter == "Top Rated":

		qq="""
	SELECT *,AVG(ratings.rate) AS avg_rate
	FROM `ratings`
	INNER JOIN doctors USING(doctor_id) where status='Approved'
	GROUP BY doctors.doctor_id
	ORDER BY avg_rate DESC;
	"""
		res=select(qq)
		print(res)
	if filter == "Near by Place":
		# user_place = "Kodungallur"
		from location import calculate_distance
		out = calculate_distance(user_place)
		print("out : ",out)

		# Convert the tuples into the desired dictionary format
		res = [
			{
				'doctor_id': doctor[1],
				'login_id': doctor[0],
				'first_name': doctor[2],
				'last_name': doctor[3],
				'gender': doctor[4],
				'house_name': doctor[5],
				'place': doctor[6],
				'landmark': doctor[7],
				'qualification': doctor[8],
				'phone': doctor[9],
				'email': doctor[10],
				'status': doctor[11]+"\nDistance : "+str(doctor[12])+"Km"
			}
			for doctor in out
		]

		# Print the formatted data
		print("formatted_data : ",res)

	
	if res:
		data['status']  = 'success'
		data['data'] = res
	else:
		data['status']	= 'failed'
	data['method']  = 'User_view_doctors'
	return  str(data)


@api.route('/User_view_doctor_schedule', methods=['get', 'post'])
def User_view_doctor_schedule():
	data = {}
	doctor_ids = request.args['doctor_ids']

	qq = "SELECT * FROM `consulting_times` WHERE `doctor_id`='%s'" % (doctor_ids)
	res = select(qq)

	if res:
		data['status'] = 'success'
		data['data'] = res
	else:
		data['status'] = 'failed'

	data['method'] = 'User_view_doctor_schedule'
	return str(data)




@api.route('/User_book_doctor',methods=['get','post'])
def User_book_doctor():
	data={}
	login_id=request.args['login_id']
	consulting_ids=request.args['consulting_ids']
	date=request.args['date']

	qq="INSERT INTO `bookings` VALUES (null,(select user_id from users where login_id='%s'),'%s','%s',CURRENT_TIMESTAMP(),'Booked')"%(login_id,consulting_ids,date)
	bid=insert(qq)
	qa="INSERT INTO `payments` VALUES (null,'%s',CURRENT_TIMESTAMP(),'Paid')"%(bid)
	insert(qa)
	data['method']  = 'User_book_doctor'
	data['status']  = 'success'
	return  str(data)



@api.route('/User_view_bookings',methods=['get','post'])
def User_view_bookings():
	data={}
	user_id=request.args['user_id']
	
	q="SELECT *,consulting_times.date_time as cdate, consulting_times.day as cday, consulting_times.start_time as cstart, consulting_times.end_time as cend, bookings.status as bstatus, payments.status as pstatus FROM bookings INNER JOIN consulting_times USING(consulting_id) INNER JOIN doctors USING(doctor_id) INNER JOIN payments USING(booking_id) WHERE user_id='%s'"%(user_id)
	c=select(q)
	print(c)

	if c:
		data['status']  = 'success'
		data['data'] = c
	else:
		data['status']	= 'failed'
	data['method']  ='User_view_bookings'
	return str(data)





@api.route('/User_add_review', methods=['POST'])
def User_add_review():
    try:
        # Get JSON data from request
        data = request.get_json()

        # Extract data from JSON
        rting = data.get('rting')
        review = data.get('review')
        doc_id = data.get('doc_id')
        uid = data.get('uid')

        if not all([rting, review, doc_id, uid]):
            return jsonify({"status": "error", "message": "Missing required fields"}), 400

        # Check if the review already exists
        q = "SELECT * FROM `ratings` WHERE `doctor_id`='%s' AND `user_id`='%s'"%((doc_id, uid))
        res = select(q)

        if res:
            # Update the existing review
            qq = """
                UPDATE `ratings` 
                SET `rate`='%s', `review`='%s', `date_time`=CURRENT_TIMESTAMP()
                WHERE `doctor_id`='%s' AND `user_id`='%s'
            """%(rting, review, doc_id, uid)
            update(qq)
            return jsonify({"status": "success", "message": "Review updated successfully"})
        else:
            # Insert a new review
            qq = """
                INSERT INTO `ratings` (`doctor_id`, `user_id`, `rate`, `review`, `date_time`) 
                VALUES ('%s',' %s', '%s', '%s', CURRENT_TIMESTAMP())
            """%(doc_id, uid, rting, review)
            insert(qq)
            return jsonify({"status": "success", "message": "Review submitted successfully"})

    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500
	


@api.route('/User_view_review',methods=['get','post'])
def User_view_review():
	data={}
	uid=request.args['uid']
	doc_id=request.args['doc_id']

	q="SELECT * FROM `ratings` WHERE `doctor_id`='%s' and `user_id`='%s'"%(doc_id,uid)
	
	res=select(q)
	if res:
		data['status']  = 'success'
		data['data'] = res
	else:
		data['status']	= 'failed'
	data['method']  = 'User_view_review'
	return  str(data)



# @api.route('/User_send_chat',methods=['get','post'])
# def User_send_chat():
# 	data={}

# 	ulogid = request.args['ulogid']
# 	dlogid = request.args['dlogid']
# 	input_chat = request.args['input_chat']
# 	q="INSERT INTO `chat` VALUES (null,'%s','User','%s','Doctor','%s',CURRENT_TIMESTAMP())"%(ulogid,dlogid,input_chat)
# 	insert(q)

# 	data['method']  = 'User_send_chat'
# 	return  str(data)

# @api.route('/User_view_chat',methods=['get','post'])
# def User_view_chat():
# 	data={}

# 	ulogid = request.args['ulogid']
# 	dlogid = request.args['dlogid']

# 	q="SELECT * FROM `chat` where sender_id='%s' and receiver_id='%s'"%(ulogid,dlogid)
# 	res=select(q)
# 	if res:
# 		data['status']  = 'success'
		
# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']  = 'User_view_chat'
# 	return  str(data)



@api.route('/User_send_chat',methods=['get','post'])
def User_send_chat():
	data={}

	ulogid = request.args['ulogid']
	dlogid = request.args['dlogid']
	input_chat = request.args['input_chat']
	q="INSERT INTO `chat` VALUES (null,'%s','User','%s','Doctor','%s',CURRENT_TIMESTAMP())"%(ulogid,dlogid,input_chat)
	insert(q)
	data['status']  = 'success'

	data['method']  = 'User_send_chat'
	return  str(data)

@api.route('/User_view_chat',methods=['get','post'])
def User_view_chat():
	data={}

	ulogid = request.args['ulogid']
	dlogid = request.args['dlogid']

	q="SELECT * FROM `chat` where (sender_id='%s' and receiver_id='%s') or (sender_id='%s' and receiver_id='%s') ORDER BY date_time"%(ulogid,dlogid,dlogid,ulogid)
	res=select(q)
	if res:
		data['status']  = 'success'
		data['data'] = res
	else:
		data['status']	= 'failed'
	data['method']  = 'User_view_chat'
	return  str(data)


@api.route('/User_view_new_chat', methods=['get','post'])
def User_view_new_chat():
    data = {}
    ulogid = request.args['ulogid']
    dlogid = request.args['dlogid']
    last_message_id = request.args.get('last_message_id', 0)

    # Fetch messages after the last known message ID
    q = "SELECT * FROM `chat` WHERE ((sender_id='%s' AND receiver_id='%s') OR (sender_id='%s' AND receiver_id='%s')) AND chat_id > %s ORDER BY date_time" % (
        ulogid, dlogid, dlogid, ulogid, last_message_id
    )
    res = select(q)
    
    if res:
        data['status'] = 'success'
        data['data'] = res
    else:
        data['status'] = 'failed'
    
    data['method'] = 'User_view_new_chat'
    return str(data)


@api.route('/User_send_complaint', methods=['POST', 'GET'])
def user_send_complaint():
	try:
		user_id = request.args.get('user_id')
		complaint = request.args.get('complaint')
		print("complaint : ",type(complaint))

		if not user_id or not complaint:
			return jsonify({
				'status': 'failed',
				'message': 'Missing user_id or complaint',
				'method': 'User_send_complaint'
			}), 400

		query = """
			INSERT INTO `complaints`
			VALUES (null,'%s', '%s', 'NA', CURRENT_TIMESTAMP())
		"""%(user_id, complaint)
		res = insert(query)
		print(res)
		
		if res:
			qr = "SELECT *  FROM `complaints` WHERE `user_id` = '%s' and complaint_id='%s' order by complaint_id desc"%(user_id,res)
			rr=select(qr)
			date_time= rr[0]['date_time']
			print("date_time : ",type(date_time))
			reply= rr[0]['reply']
			print("reply : ",type(reply))

			return jsonify({
				'status': 'success',
				'complaint' : complaint,
				'date_time' : date_time,
				'reply' : reply,
				'method': 'User_send_complaint'
			}), 201
		else:
			return jsonify({
				'status': 'failed',
				'message': 'Failed to insert complaint',
				'method': 'User_send_complaint'
			}), 500
	except Exception as e:
		return jsonify({
			'status': 'failed',
			'message': str(e),
			'method': 'User_send_complaint'
		}), 500

@api.route('/User_view_complaints', methods=['GET','POST'])
def user_view_complaints():
    try:
        user_id = request.args.get('user_id')

        if not user_id:
            return jsonify({
                'status': 'failed',
                'message': 'Missing user_id',
                'method': 'User_view_complaints'
            }), 400

        query = "SELECT *  FROM `complaints` WHERE `user_id` = '%s'"%(user_id)
       
        res = select(query)

        if res:
            return jsonify({
                'status': 'success',
                'data': res,
                'method': 'User_view_complaints'
            }), 200
        else:
            return jsonify({
                'status': 'failed',
                'message': 'No complaints found',
                'method': 'User_view_complaints'
            }), 404
    except Exception as e:
        return jsonify({
            'status': 'failed',
            'message': str(e),
            'method': 'User_view_complaints'
        }), 500
	


@api.route('/upload_file', methods=['POST'])
def upload_file():
	try:
		# Get the image data from the POST request
		data = request.get_json()
		if 'image' not in data:
			return jsonify({"status": "error", "message": "No image data received"})

		# Decode the base64 image
		uid = data['uid']
		print("uid : ",uid)
		image_data = data['image']
		image_bytes = base64.b64decode(image_data)
		image = Image.open(BytesIO(image_bytes))

		from datetime import datetime

		# Get the current date and time
		current_datetime = datetime.now()

		# Print the current date and time
		print("Current date and time:", current_datetime)
		formatted_datetime = current_datetime.strftime("%Y-%m-%d %H:%M:%S")
		print("Formatted date and time:", formatted_datetime)


		# Generate a unique filename for the image
		filename = str(uid)+"_date_"+str(formatted_datetime)+"_uploaded_image.png"  # You can modify this to create unique filenames if needed
		image_path = os.path.join(SAVE_DIR, filename)
		print("image_path : ",image_path)

		# Save the image to the specified directory
		image.save(image_path)

		# Call the prediction function
		from pred import predict_image
		
		import numpy as np
		from tensorflow.keras.preprocessing import image
		from tensorflow.keras.models import load_model
		# # Load the trained model
		model = load_model('ecg_model_vgg16_new_100.h5')

		result = predict_image(model,image_path)
		print("result : ",result)
		# Split the result into label and confidence
		disease_label = result[0]
		confidence_score = result[1]

		predict_out = "Based on the analysis, the disease predicted is: "+disease_label+"."

		qq="INSERT INTO `history` VALUES (null,'%s','%s','%s',CURRENT_TIMESTAMP())"%(uid,image_path,predict_out)
		insert(qq)

		return jsonify({"status": "success", "result": predict_out})

	except Exception as e:
		return jsonify({"status": "error", "message": str(e)})
	



@api.route('/User_view_history',methods=['get','post'])
def User_view_history():
	data={}
	uid=request.args['uid']

	q="SELECT * FROM `history` WHERE `user_id`='%s' ORDER BY `history_id` DESC"%(uid)
	print(q)
	res=select(q)
	if res:
		data['status']  = 'success'
		
		data['data'] = res
	else:
		data['status']	= 'failed'
	data['method']  = 'User_view_history'
	return  str(data)

# @api.route('/viewadvocatedetails',methods=['get','post'])
# def viewadvocatedetails():
# 	data={}

# 	aid=request.args['adv_id']

# 	q="SELECT *,CONCAT(`first_name`,' ',`last_name`)AS advocate_name FROM `advocates` WHERE `adv_id`='%s'"%(aid)
# 	res=select(q)
# 	if res:
# 		data['status']  = 'success'
		
# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']  = 'viewadvocatedetails'
# 	return  str(data)

# @api.route('/viewcasenotes',methods=['get','post'])
# def viewcasenotes():
# 	data={}

# 	cid=request.args['case_id']

# 	q="SELECT * FROM `case_notes` INNER JOIN `cases` USING(`case_id`) WHERE `case_id`='%s'"%(cid)
# 	res=select(q)
# 	if res:
# 		data['status']  = 'success'
		
# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']  = 'viewcasenotes'
# 	return  str(data)


# # @api.route('/viewcasefiles',methods=['get','post'])
# # def viewcasefiles():
# # 	data={}

# # 	# cid=request.args['case_id']

# # 	# q="SELECT * FROM `case_files` INNER JOIN `cases` USING(`case_id`) WHERE `case_id`='%s'"%(cid)
# # 	# res=select(q)
# # 	# filename=res[0]['file_name']
# # 	key=request.args['key']
# # 	a="select * from case_files w"
# # 	password="#34^%%$w5454"
# # 	pth1=r"C:\RISS PROJECTS D\FISAT\Legal Advisor\static\encrypted\\"+filename
# # 	pth2=r"C:\RISS PROJECTS D\FISAT\Legal Advisor\static\downloads\\"+filename
# # 	print(pth1)
# # 	print(pth2)
# # 	with open(pth1, "rb") as imageFile:
# # 		stri = base64.b64encode(imageFile.read()).decode('utf-8')
# # 		dec2 = decrypt(stri, key).decode('utf-8')
# # 		fh1 = open(pth2, "wb")
# # 		fh1.write(base64.b64decode(dec2))
# # 		fh1.close()
# # 	if res:
# # 		data['status']  = 'success'
		
# # 		data['data'] = res
# # 	else:
# # 		data['status']	= 'failed'
# # 	data['method']  = 'viewcasefiles'
# # 	return  str(data)

# # @api.route('/viewcasefiles',methods=['get','post'])
# # def viewcasefiles():
# # 	data={}

# # 	cid=requet.args['case_id']

# # 	q="SELECT * FROM `case_files` WHERE `case_id`='%s'"%(cid)
# # 	res=select(q)
# # 	if res:
# # 		data['status']  = 'success'
		
# # 		data['data'] = res
# # 	else:
# # 		data['status']	= 'failed'
# # 	data['method']  = 'viewcasefiles'
# # 	return  str(data)


# @api.route('/accept_proposal',methods=['get','post'])
# def accept_proposal():
# 	data={}
# 	pid=request.args['pids']
# 	cid=request.args['cid']
# 	lid=request.args['login_id']
# 	adid=request.args['advid']
# 	fee=request.args['fee']
# 	q="UPDATE `proposals` SET `status`='accepted' WHERE `proposal_id`='%s'"%(pid)
# 	res=update(q)
# 	e="insert into client_assigns values(null,(select user_id from users where login_id='%s'),'%s','%s',now(),'assigned')"%(lid,adid,cid)
# 	res=insert(e)
# 	r="insert into payment values(null,(select user_id from users where login_id='%s'),'%s','%s',curdate(),'%s')"%(lid,cid,fee,adid)
# 	res=insert(r)
# 	if res:
# 		data['status']  = 'success'
		
# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']  = 'accept_proposal'
# 	return  str(data)


# @api.route('/reject_proposal',methods=['get','post'])
# def reject_proposal():
# 	data={}

# 	pid=request.args['pids']

# 	q="UPDATE `proposals` SET `status`='rejected' WHERE `proposal_id`='%s'"%(pid)
# 	res=update(q)
# 	if res:
# 		data['status']  = 'success'

# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']  = 'reject_proposal'
# 	return  str(data)


# @api.route('/assigncase_to_adv',methods=['get','post'])
# def assigncase_to_adv():
# 	data={}

# 	aid=request.args['adv_id']
# 	cid=request.args['case_id']
# 	lid=request.args['login_id']

# 	q="INSERT INTO `client_assigns`(`client_id`,`adv_id`,`case_id`,`date_time`,`status`) VALUES((select client_id from client where login_id='%s'),'%s','%s',NOW(),'assigned')"%(lid,aid,cid)
# 	insert(q)
# 	q="UPDATE `cases` SET `status`='assigned' WHERE `case_id`='%s'"%(cid)
# 	res=update(q)


# 	if res:
# 		data['status']  = 'success'
		
# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']  = 'assigncase_to_adv'
# 	return  str(data)


# @api.route('/chat',methods=['get','post'])
# def chat():
# 	data={}

# 	sid=request.args['sender_id']
# 	rid=request.args['receiver_id']
# 	msg=request.args['details']

# 	q="INSERT INTO `chat`(`sender_id`,`sender_type`,`receiver_id`,`receiver_type`,`message`,`date_time`)VALUES('%s','client','%s','advocate','%s',NOW())"%(sid,rid,msg)
# 	res=insert(q)
# 	if res:
# 		data['status']  = 'success'
		
# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']  = 'chat'
# 	return  str(data)

	
# @api.route('/chatdetail',methods=['get','post'])
# def chatdetail():
# 	data={}

# 	sid=request.args['sender_id']
# 	rid=request.args['receiver_id']


# 	q="SELECT * FROM `chat` WHERE (`receiver_id`='%s' AND `sender_id`='%s') or (`receiver_id`='%s' AND `sender_id`='%s')"%(sid,rid,rid,sid)
# 	res=select(q)
# 	if res:
# 		data['status']  = 'success'
		
# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']  = 'chatdetail'
# 	return  str(data)

	

# @api.route('/chattedadv',methods=['get','post'])
# def chattedadv():
# 	data={}

# 	lid=request.args['login_id']

# 	# q="SELECT *,CONCAT(`first_name`,' ',`last_name`)AS advocate_name FROM `chat` INNER JOIN `advocates` ON `advocates`.`logid`=`chat`.`receiver_id` WHERE `sender_id`='%s' GROUP BY receiver_id"%(lid)
# 	q="SELECT *,CONCAT(`first_name`,' ',`last_name`)AS advocate_name FROM `chat` INNER JOIN `advocates` ON `advocates`.`login_id`=`chat`.`sender_id` WHERE `receiver_id`='%s' GROUP BY receiver_id"%(lid)
# 	res=select(q)
# 	if res:
# 		data['status']  = 'success'
		
# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']  = 'chattedadv'
# 	return  str(data)


# @api.route('/rate_advocate',methods=['get','post'])
# def rate_advocate():

# 	data={}

# 	login_id=request.args['login_id']
# 	adv_id=request.args['adv_id']
# 	rating=request.args['rating']
# 	review=request.args['review']

# 	q="SELECT * FROM `ratings` WHERE `adv_id`='%s' AND `client_id`=(select user_id from users where login_id='%s')"%(adv_id,login_id)
# 	res=select(q)
# 	if res:

# 		q="UPDATE `ratings` SET `rate`='%s',`review`='%s',`date_time`=NOW() WHERE `adv_id`='%s'"%(rating,review,adv_id)
# 		update(q)
# 		data['status'] = 'success'
# 	else:
# 		q="INSERT INTO `ratings`(`client_id`,`adv_id`,`rate`,`review`,`date_time`)VALUES((select user_id from users where login_id='%s'),'%s','%s','%s',NOW())"%(login_id,adv_id,rating,review)
# 		id=insert(q)
# 		if id>0:
# 			data['status'] = 'success'
			
# 		else:
# 			data['status'] = 'failed'
# 	data['method'] = 'rate_advocate'
# 	return str(data)


# @api.route('/view_rating',methods=['get','post'])
# def view_rating():
# 	data = {}

# 	login_id=request.args['login_id']
# 	adv_id=request.args['adv_id']
	
# 	q=" SELECT * FROM `ratings` WHERE `adv_id`='%s' AND `client_id`=(select client_id from client where login_id='%s')"%(adv_id,login_id)
# 	print(q)
# 	result=select(q)
# 	if result:
# 		data['status'] = 'success'
# 		data['data'] = result[0]['rate']
# 		data['data1'] = result[0]['review']
		
# 	else:
# 		data['status'] = 'failed'
# 	data['method'] = 'view_rating'
# 	return str(data)






# @api.route('/meeting',methods=['get','post'])
# def meeting():
# 	data={}
# 	data['method']  = 'managecase'
# 	login_id=request.args['lid']
# 	time=request.args['time']
# 	date=request.args['date']
# 	advid=request.args['advid']
# 	q="insert into meeting values(null,'%s','%s','%s','pending',(select client_id from client where login_id='%s'))"%(advid,time,date,login_id)
# 	r=insert(q)
# 	if r:
# 		data['status'] = 'success'
# 	else:
# 		data['status'] = 'failed'
# 	return str(data)



# # @api.route('/key',methods=['get','post'])
# # def key():
# # 	data={}
# # 	data['method']='managecase'
# # 	key=request.args['title']
# # 	logid=request.args['login_id']
# # 	qa="select * from case_files where `key`='%s'"%(key)
# # 	res=select(qa)
# # 	img=res[0]['file_name']
# # 	pth1="static/encrypted/"+img
# # 	pth2=r"static\\downloads\\"+img
# # 	with open(pth1, "rb") as imageFile:
# # 		print(imageFile,'1111111111111111111111111111111111111111111111111111')
# # 		stri = base64.b64encode(imageFile.read()).decode('utf-8')
# # 		dec2 = decrypt(stri, key).decode('utf-8')
# # 		fh1 = open(pth2, "wb")
# # 		vak=fh1.write(base64.b64decode(dec2))
# # 		data['path']='static\downloads\\'+img

# # 		print('static\downloads\\'+img,',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,')
# # 		data['status']='success'
# # 		return str(data)



# @api.route('/viewpolice_station')
# def viewpolice_station():
#     data={}
#     qry="select * from stations"
#     res=select(qry)
#     if res:
#         data['status']="success"
#         data['data']=res
#     else:
#         data['status']="failed"
#     data['method']="viewstation"
#     return str(data)



# @api.route('/viewcrimetypes')
# def viewcrimetypes():
#     data={}
#     qry="select * from crime_types"
#     res=select(qry)
#     if res:
#         data['status']="success"
#         data['data']=res
#     else:
#         data['status']="failed"
#     data['method']="viewcrimetypes"
#     return str(data)




# @api.route('/viewcriminals')
# def viewcriminals():
#     data={}
#     id=request.args['stationid']
#     qry="select * from criminals"
#     res=select(qry)
    
#     if res:
#         data['status']="success"
#         data['data']=res
#     else:
#         data['status']="failed"
#     data['method']="viewcriminals"
#     return str(data)


# @api.route('/viewcrimes')
# def viewcrimes():
#     data={}
#     id=request.args['stationid']
#     qry="select * from crimes inner join crime_types using(crime_type_id) where station_id='%s'"%(id)
#     res=select(qry)
#     if res:
#         data['status']="success"
#         data['data']=res
#     else:
#         data['status']="failed"
#     data['method']="viewcrimes"
#     return str(data)


# @api.route('/newcom')
# def newcom():
#     data={}
#     userid=request.args['userid']
#     # stationid=request.args['stationid']
#     complaint=request.args['complaint']
    
    
#     qry="insert into complaints values(null,'%s','%s',curdate(),'pending')"%(userid,complaint)
#     res=insert(qry)
#     if res:
#         data['status']="success"
#     else:
#         data['status']="failed"
#     data['method']="send"
#     return str(data)


# @api.route('/newcomview')
# def newcomview():
#     data={}
#     userid=request.args['userid']
    
#     qry="select * from complaints where user_id='%s'"%(userid)
#     res=select(qry)

#     if res:
#         data['status']="success"
#         data['data']=res
#     else:
#         data['status']="failed"
#     data['method']="viewcom"
#     return str(data)
    
    
    

# @api.route('/uploadevidence',methods=['get','post'])
# def uploadevidence():
#     data={}
#     print("PPPPPPPPPPPPPPPPPP")
#     ppost=request.form['cy_post']
#     print("ppkkkkkk")
#     path=request.files['image']
#     img="static/images/"+str(uuid.uuid4())+path.filename
#     path.save(img)
#     id=request.form['id']
    
#     qry="insert into evidences values(null,'%s','%s','%s',curdate())"%(id,img,ppost)
#     insert(qry)
#     data['status']="success"
    
#     return str(data)






# @api.route('/viewmssg')
# def viewmssg():
#     data={}
#     staion_id=request.args['staion_id']
#     userid=request.args['userid']
    
    
#     qry="select * from message where station_id='%s' and user_id=(select user_id from users where login_id='%s')"%(staion_id,userid)
#     res=select(qry)
#     if res:
#         data['status']="success"
#         data['data']=res
#     else:
#         data['status']="failed"
        
#     data['method']="viewmessage"
#     return str(data)


# @api.route('/sendmssg')
# def sendmssg():
#     data={}
#     mssg_id=request.args['mssg_id']
#     mssg=request.args['mssg']
    
    
#     qry="update message set reply='%s' where message_id='%s'"%(mssg,mssg_id)
#     res=update(qry)
#     if res:
#         data['status']="success"
#     else:
#         data['status']="failed"
#     data['method']="mssg"
#     return str(data)



# # @api.route('/criminalfound',methods=['get','post'])
# # def criminalfound():
# #     data={}
# #     path=request.files['image']
# #     img="static/criminals/"+str(uuid.uuid4())+path.filename
# #     path.save(img)
# #     criminalid=request.form['cid']
# #     userid=request.form['uid']
# #     place=request.form['place']
# #     description=request.form['desc']
# #     qry="insert into foundreport values(null,'%s',(select user_id from users where login_id='%s'),'%s',curdate(),'%s','%s')"%(criminalid,userid,place,description,img)
# #     res=insert(qry)
# #     if res:
# #         data['status']="success"
# #     else:
# #         data['status']="failed"
# #     data['method']="founded"
# #     return str(data)



# @api.route('/criminalfound',methods=['get','post'])
# def criminalfound():
# 	data={}
# 	path=request.files['image']
# 	img="static/criminals/"+str(uuid.uuid4())+path.filename
# 	path.save(img)
# 	criminalid=request.form['cid']
# 	userid=request.form['uid']
# 	place=request.form['place']
# 	description=request.form['desc']
 
# 	qry="insert into foundreport values(null,'%s',(select user_id from users where login_id='%s'),'%s',curdate(),curtime(),'NA','%s','%s')"%(criminalid,userid,place,description,img)
# 	idss=insert(qry)
# 	qry1="select * from criminals where criminal_id='%s'"%(criminalid)
# 	res=select(qry1)
# 	ph1=res[0]['photo']
# 	result=are_faces_same(ph1,img)
# 	print(result,"res"*100)
# 	if result < 0.6:  # Adjust the threshold as needed
# 		result = f"The given face images are the same. Similarity: {100 - result * 100:.2f}%"
# 		qu="update foundreport set similarity ='%s' where found_id='%s'"%(result,idss)
# 		update(qu)
# 	else:
# 		result = "The given face images are different."
# 		qu="update foundreport set similarity ='%s' where found_id='%s'"%(result,idss)
# 		update(qu)

# 	print(result)

# 	if result:
# 		data['status']="success"
# 		data['data']=result
# 		print(data['data'])
		
# 	else:
# 		data['status']="failed"
# 	data['method']="founded"
# 	return str(data)
    
    
      



# @api.route('/Forgot_password')
# def Forgot_password():
# 	data={}
# 	uname=request.args['uname']
# 	email=request.args['email']
# 	npass=request.args['npass']
# 	cpass=request.args['cpass']

# 	q="select * from login inner join users using(login_id) where username='%s' and email='%s'" %(uname,email)
# 	res=select(q)
# 	if res:
# 		session['lid']=res[0]['login_id']
# 		if res[0]['type']=="user":
# 			if npass == cpass:
# 				q="update login set password='%s' where login_id='%s'"%(cpass,res[0]['login_id'])
# 				res1=update(q)
				
# 				data['status']="success"
# 			else:
# 				data['status']="failed"
# 	else:
# 		data['status']="failed"

# 	data['method']="mssg"
# 	return str(data)


 

# @api.route('/add_missing_person',methods=['get','post'])
# def add_missing_person():
# 	data={}
# 	path=request.files['image']
# 	img="static/missing_person/"+str(uuid.uuid4())+path.filename
# 	path.save(img)
# 	fname=request.form['fname']
# 	userid=request.form['uid']
# 	lname=request.form['lname']
# 	address=request.form['address']
# 	age=request.form['age']
# 	mdate=request.form['mdate']
# 	mplace=request.form['mplace']
# 	identi=request.form['identi']
# 	contact=request.form['contact']
# 	gender=request.form['gender']

# 	qry="""
# INSERT INTO `missing_person`
# (`missing_person_id`, `post_by_id`, `first_name`, `last_name`, `age`, `gender`, `address`, `missing_date`, `missing_place`, `image`, `identification`, `contact_info`, `date`, `status`) 
# VALUES (null,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',curdate(),'Missing')
# """%(userid,fname,lname,age,gender,address,mdate,mplace,img,identi,contact)
# 	idss=insert(qry)
# 	data['status']="success"
# 	data['method']="add_missing_person"
# 	return str(data)
    









# @api.route('/View_missing_person')
# def View_missing_person():
# 	data={}
# 	q="""select * from missing_person inner join login on login.login_id=missing_person.post_by_id where missing_person.status='Missing' """
# 	res=select(q)
# 	if res:
# 		data['data']=res
				
# 		data['status']="success"
# 	else:
# 		data['status']="failed"

# 	data['method']="View_missing_person"
# 	return str(data)





# @api.route('/Missing_found',methods=['get','post'])
# def Missing_found():
# 	data={}
# 	path=request.files['image']
# 	img="static/missing_person/"+str(uuid.uuid4())+path.filename
# 	path.save(img)
# 	missing_person_id=request.form['cid']
# 	place=request.form['place']
# 	description=request.form['desc']

# 	qry="insert into missing_found values(null,'%s','%s',curdate(),curtime(),'%s','%s','NA',curdate())"%(missing_person_id,place,description,img)
# 	idss=insert(qry)
# 	qry1="select * from missing_person where missing_person_id='%s'"%(missing_person_id)
# 	res=select(qry1)
# 	ph1=res[0]['image']
# 	result=are_faces_same(ph1,img)
# 	print(result,"res"*100)
# 	if result < 0.6:  # Adjust the threshold as needed
# 		result = f"The given face images are the same. Similarity: {100 - result * 100:.2f}%"
# 		qu="update missing_found set similarity ='%s' where missing_found_id='%s'"%(result,idss)
# 		update(qu)
# 	else:
# 		result = "The given face images are different."
# 		qu="update missing_found set similarity ='%s' where missing_found_id='%s'"%(result,idss)
# 		update(qu)

# 	print(result)

# 	if result:
# 		data['status']="success"
# 		data['data']=result
# 		print(data['data'])
		
# 	else:
# 		data['status']="failed"
# 	data['method']="founded"
# 	return str(data)
    




# @api.route('/view_profile',methods=['get','post'])
# def view_profile():
# 	data={}
# 	logid=request.args['logid']
# 	qq="SELECT * FROM `users` WHERE `login_id` ='%s'"%(logid)
# 	result=select(qq)

# 	if result:
# 		data['status']="success"
# 		data['data']=result
# 		print(data['data'])
		
# 	else:
# 		data['status']="failed"
# 	data['method']="view_profile"
# 	return str(data)




# @api.route('/update_profile',methods=['get','post'])
# def update_profile():
# 	data={}
# 	fname = request.args['fname']
# 	lname = request.args['lname']
# 	gender = request.args['gender']
# 	dob = request.args['dob']
# 	phone = request.args['phone']
# 	email = request.args['email']
# 	hname = request.args['hname']
# 	place=request.args['place']
# 	pin = request.args['pin']
# 	logid = request.args['logid']


# 	qq="UPDATE `users` SET `first_name`='%s',`last_name`='%s',`gender`='%s',`dob`='%s',`house_name`='%s',`place`='%s',`pincode`='%s',`phone`='%s',`email`='%s' WHERE `login_id`='%s'"%(fname,lname,gender,dob,hname,place,pin,phone,email,logid)
# 	res=update(qq)
# 	data['status']  = 'success'
	
# 	data['method']  ='update_profile'
# 	return str(data)




# @api.route('/add_found_person_child',methods=['get','post'])
# def add_found_person_child():
# 	data={}
# 	path=request.files['image']
# 	img="static/person_child_found/"+str(uuid.uuid4())+path.filename
# 	path.save(img)
# 	fname=request.form['fname'] +" "+ request.form['lname']
# 	userid=request.form['uid']
# 	address=request.form['address']
# 	age=request.form['age']
# 	mdate=request.form['mdate']
# 	mplace=request.form['mplace']
# 	identi=request.form['identi']
# 	contact=request.form['contact']
# 	gender=request.form['gender']
# 	time=request.form['time']

# 	qry="""
# INSERT INTO `found_person_child`
# VALUES (null,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',curdate(),'Found')
# """%(userid,fname,age,gender,address,mplace,mdate,time,img,identi,contact)
# 	idss=insert(qry)
# 	data['status']="success"
# 	data['method']="add_found_person_child"
# 	return str(data)
    





# @api.route('/View_found_person_child',methods=['get','post'])
# def View_found_person_child():
# 	data={}
# 	qq="select * from found_person_child order by found_person_child_id desc"
# 	res=select(qq)
# 	data['data']=res
# 	data['status']="success"
# 	data['method']="View_found_person_child"
# 	return str(data)
    
