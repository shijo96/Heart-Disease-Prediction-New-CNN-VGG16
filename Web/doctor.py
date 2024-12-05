from flask import *
from database import*
import os
from training import vgg_16_training
from pred import predict_image


doctor=Blueprint('doctor',__name__)

@doctor.route("doctor_home",methods=['get','post'])
def doctor_home():
    data={}
    q="SELECT *,COUNT(`booking_id`) as tp FROM `bookings` INNER JOIN consulting_times USING(`consulting_id`) WHERE doctor_id='%s' GROUP BY `user_id`"%(session['did'])
    print(q)
    rr=select(q)
    if rr:
        data['tp']=rr[0]['tp']
    else:
        data['tp']="0"
    
    q1="SELECT *,COUNT(`booking_id`) as op FROM `bookings` INNER JOIN consulting_times USING(`consulting_id`) WHERE doctor_id='%s' and bookings.date_time like CURRENT_DATE()"%(session['did'])
    rr1=select(q1)
    if rr1:
        data['op']=rr1[0]['op']
    else:
        data['op']="0"

    q3="SELECT *,COUNT(`booking_id`) as tot_bk FROM `bookings` INNER JOIN consulting_times USING(`consulting_id`) WHERE doctor_id='%s'"%(session['did'])
    rr3=select(q3)
    print(q3)
    if rr3:
        data['tot_bk']=rr3[0]['tot_bk']
        print("qqqqq : ",data['tot_bk'])
    else:
        data['tot_bk']="0"

    q4="SELECT *,avg(`rate`) as avg_rt FROM `ratings` WHERE `doctor_id`='%s'"%(session['did'])
    rr4=select(q4)
    print(q4)
    if rr4:
        data['avg_rt']=rr4[0]['avg_rt']
        print("qqqqq : ",data['avg_rt'])
    else:
        data['avg_rt']="0"

    q5="""SELECT *, 
    bookings.date_time AS bk_date, 
    users.login_id AS ulog_id 
FROM 
    bookings 
INNER JOIN 
    users USING(user_id) 
INNER JOIN 
    consulting_times USING(consulting_id) 
WHERE 
    consulting_times.doctor_id = '%s' 
    AND bookings.status = 'Booked' 
    AND DATE(bookings.date_time) = CURDATE() 
ORDER BY 
    bookings.booking_id DESC;
"""%(session['did'])
    print(q5)
    rr5=select(q5)
    if rr5:

        data['bk_p']=rr5
    else:
        data['bk_p']="No Data"


    if "action" in request.args:
        action=request.args['action']
        bid=request.args['bid']
    else:
        action=None
    if action=="accept":
        q="UPDATE `bookings` SET `status`='Confirmed' WHERE `booking_id`='%s'"%(bid)
        update(q)
        return redirect(url_for("doctor.doctor_home"))
    if action=="reject":
        q="UPDATE `bookings` SET `status`='Rejected' WHERE `booking_id`='%s'"%(bid)
        update(q)
        return redirect(url_for("doctor.doctor_home"))
    
    return render_template("doctor_home.html",data=data)



@doctor.route("doctor_manage_fees",methods=['get','post'])
def doctor_manage_fees():
    data={}
    if "manage_fee" in request.form:
        amount= request.form['amount']
        print("*"*100)
        qq="INSERT INTO `fee`VALUES (null,'%s','%s',curdate())"%(session['did'],amount)
        insert(qq)
        return redirect(url_for("doctor.doctor_manage_fees"))
    
    q="SELECT * FROM `fee` WHERE `doctor_id`='%s'"%(session['did'])
    res=select(q)
    data['fee']=res

    return render_template("doctor_manage_fees.html",data=data)




@doctor.route("doctor_manage_consulting_time", methods=['get', 'post'])
def doctor_manage_consulting_time():
    data = {}
    
    if "manage_consulting_time" in request.form:
        day = request.form['day']
        start_time = request.form['start_time']
        end_time = request.form['end_time']
        
        # Inserting the consulting time details into the database
        query = "INSERT INTO `consulting_times` VALUES (null, '%s', '%s', '%s', '%s',curdate())" % (session['did'], day, start_time, end_time)
        insert(query)
        
        # Redirect to refresh the page and display updated consulting times
        return redirect(url_for("doctor.doctor_manage_consulting_time"))
    
    # Fetch existing consulting times for the doctor
    query = "SELECT * FROM `consulting_times` WHERE `doctor_id`='%s'" % (session['did'])
    res = select(query)
    data['consulting_times'] = res

    return render_template("doctor_manage_consulting_time.html", data=data)



@doctor.route("doctor_view_booking",methods=['get','post'])
def doctor_view_booking():
    data={}
  
    q="SELECT *,bookings.date_time as bk_date,users.login_id as ulog_id FROM `bookings` INNER JOIN users USING(`user_id`) INNER JOIN consulting_times USING(`consulting_id`) WHERE consulting_times.doctor_id='%s' ORDER BY `booking_id` DESC"%(session['did'])
    res=select(q)
    data['booking']=res

    if "action" in request.args:
        action=request.args['action']
        bid=request.args['bid']
    else:
        action=None
    if action=="accept":
        q="UPDATE `bookings` SET `status`='Confirmed' WHERE `booking_id`='%s'"%(bid)
        update(q)
        return redirect(url_for("doctor.doctor_view_booking"))
    if action=="reject":
        q="UPDATE `bookings` SET `status`='Rejected' WHERE `booking_id`='%s'"%(bid)
        update(q)
        return redirect(url_for("doctor.doctor_view_booking"))

    return render_template("doctor_view_booking.html",data=data)


@doctor.route("doctor_view_review",methods=['get','post'])
def doctor_view_review():
    data={}
  
    q="SELECT * FROM `ratings` INNER JOIN users USING(`user_id`) WHERE `doctor_id`='%s' ORDER BY `rating_id` DESC"%(session['did'])
    res=select(q)
    data['ratings']=res

    return render_template("doctor_view_review.html",data=data)


@doctor.route("doctor_view_patient_details",methods=['get','post'])
def doctor_view_patient_details():
    data={}
    pid=request.args['pid']
  
    q="SELECT * FROM `users` WHERE `user_id`='%s'"%(pid)
    res=select(q)
    data['view_patinet']=res

    return render_template("doctor_view_patient_details.html",data=data)




@doctor.route("doctor_view_medical_history",methods=['get','post'])
def doctor_view_medical_history():
    data={}
    pid=request.args['pid']
  
    q="SELECT * FROM `history` WHERE `user_id`='%s' ORDER BY `history_id` DESC"%(pid)
    res=select(q)
    data['view_history']=res

    return render_template("doctor_view_medical_history.html",data=data)


@doctor.route("doctor_view_payment",methods=['get','post'])
def doctor_view_payment():
    data={}
    bid=request.args['bid']
  
    q="SELECT * FROM `payments` WHERE `booking_id`='%s'"%(bid)
    res=select(q)
    data['view_payments']=res

    return render_template("doctor_view_payment.html",data=data)



@doctor.route("doctor_chat_patient", methods=['get', 'post'])
def doctor_chat_patient():
    data = {}
    ulog_id = request.args['ulog_id']
    
    # Handle message sending
    if "send_chat" in request.form:
        message = request.form['message']
        q = "INSERT INTO `chat` VALUES (null, '%s', 'Doctor', '%s', 'User', '%s', NOW())" % (session['lid'], ulog_id, message)
        insert(q)
        return redirect(url_for("doctor.doctor_chat_patient", ulog_id=ulog_id))
    
    # Fetch chat messages between doctor and patient
    qq = "SELECT * FROM `chat` WHERE (`sender_id`='%s' AND `receiver_id`='%s') OR (`sender_id`='%s' AND `receiver_id`='%s') ORDER BY `date_time`" % (session['lid'], ulog_id, ulog_id, session['lid'])
    res = select(qq)
    data['chat'] = res

    return render_template("doctor_chat_patient.html", data=data)

@doctor.route("doctor_patient_chat_list", methods=['get'])
def doctor_patient_chat_list():
    data = {}
    # Get distinct user IDs of patients (Users) who have chatted with the doctor
    q = """
        SELECT DISTINCT u.login_id, u.first_name, u.last_name 
        FROM chat AS c
        JOIN users AS u ON (u.login_id = c.receiver_id AND c.receiver_type = 'User') 
                        OR (u.login_id = c.sender_id AND c.sender_type = 'User')
        WHERE c.sender_id = '%s' OR c.receiver_id = '%s'
    """ % (session['lid'], session['lid'])
    
    res = select(q)
    data['patients'] = res  # Pass the patient list to the template

    return render_template("doctor_patient_chat_list.html", data=data)
