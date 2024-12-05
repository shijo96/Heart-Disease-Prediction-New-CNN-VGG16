from flask import Blueprint,render_template,request,redirect,url_for,session,flash
from database import*
import uuid

public=Blueprint("public",__name__)

@public.route("/", methods=['get','post'])
def home():
	session.clear()
	
	return render_template("index.html")
	# return render_template("index_new.html")

@public.route('/login',methods=['get','post'])
def login():
    session.clear() 
    # if "reg" in request.form:
    #     fname=request.form['fname']
    #     lname=request.form['lname']
    #     gender=request.form['gender']
    #     hname=request.form['hname']
    #     place=request.form['place']
    #     land=request.form['land']
    #     qual=request.form['qual']
    #     phone=request.form['phone']
    #     email=request.form['email']
    #     passw=request.form['pass']

    #     q="select * from login where username='%s'" %(email)
    #     res=select(q)
    #     if res:
    #         return ("<script>alert('Username Already Exist');window.location='/login'</script>")
    #     else:
    #         q1="INSERT INTO `login` VALUES (null,'%s','%s','Doctor')"%(email,passw)
    #         id=insert(q1)
    #         qq="INSERT INTO `doctors` VALUES (null,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','Pending')"%(id,fname,lname,gender,hname,place,land,qual,phone,email)
    #         insert(qq)
    #         return ("<script>alert('Success');window.location='/login'</script>")
    if 'submit' in request.form:
        uname=request.form['uname']
        passs=request.form['passs']
        q="select * from login where username='%s' and password='%s'" %(uname,passs)
        res=select(q)
        if res:
            session['lid']=res[0]['login_id']
            if res[0]['user_type']=="admin":
                # flash("Logging in")	
                return """
    <!DOCTYPE html>
    <html lang="en">
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Error</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: rgba(0, 0, 0, 0.5); /* Dim background */
            overflow: hidden;
        }
        .overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5); /* Semi-transparent background */
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .card {
            background-color: #ffffff;
            padding: 20px 40px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            text-align: center;
            width: 300px;
            position: relative;
        }
        .card h1 {
            font-size: 20px;
            color: #277D05;
            margin-bottom: 15px;
        }
        .card img {
            width: 100px;
            height: auto;
            margin-bottom: 15px;
        }
    </style>
    </head>
    <body>
    <div class="overlay">
        <div class="card">
            <img src="/static/succ.gif" alt="Loading GIF">
            <h1>Login Success</h1>
        </div>
    </div>

    <script>
        setTimeout(function() {
            window.location = 'admin/admin_home';
        }, 3500); // 2-second delay
    </script>
    </body>
    </html>
    """		
                # return redirect(url_for("admin.admin_home"))
            if res[0]['user_type']=="Doctor":
                qq="SELECT * FROM `doctors` WHERE login_id='%s' "%(session['lid'])
                rr=select(qq)
                if rr:
                    if rr[0]['status']=='Pending':
                        flash(('We are in the process of verifying your account. Thank you for your patience as we work to complete this verification.', 'danger')) 
                
                    elif rr[0]['status']=="Approved":
                        session['did']=rr[0]['doctor_id']
                        return """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login Error</title>
        <style>
            body {
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
                font-family: Arial, sans-serif;
                background-color: rgba(0, 0, 0, 0.5); /* Dim background */
                overflow: hidden;
            }
            .overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5); /* Semi-transparent background */
                display: flex;
                justify-content: center;
                align-items: center;
            }
            .card {
                background-color: #ffffff;
                padding: 20px 40px;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                text-align: center;
                width: 300px;
                position: relative;
            }
            .card h1 {
                font-size: 20px;
                color: #277D05;
                margin-bottom: 15px;
            }
            .card img {
                width: 100px;
                height: auto;
                margin-bottom: 15px;
            }
        </style>
    </head>
    <body>
        <div class="overlay">
            <div class="card">
                <img src="/static/succ.gif" alt="Loading GIF">
                <h1>Login Success</h1>
            </div>
        </div>

        <script>
            setTimeout(function() {
                window.location = 'doctor/doctor_home';
            }, 3000); // 2-second delay
        </script>
    </body>
    </html>
    """
                    # flash("Logging in")			
                    # return redirect(url_for("doctor.doctor_home"))
        else:
            # return ("<script>alert('Check Username or Password');window.location='/login'</script>")
            return """
    <!DOCTYPE html>
    <html lang="en">
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Error</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: rgba(0, 0, 0, 0.5); /* Dim background */
            overflow: hidden;
        }
        .overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5); /* Semi-transparent background */
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .card {
            background-color: #ffffff;
            padding: 20px 40px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            text-align: center;
            width: 300px;
            position: relative;
        }
        .card h1 {
            font-size: 20px;
            color: #ff4d4d;
            margin-bottom: 15px;
        }
        .card img {
            width: 100px;
            height: auto;
            margin-bottom: 15px;
        }
    </style>
    </head>
    <body>
    <div class="overlay">
        <div class="card">
            <img src="/static/fld.gif" alt="Loading GIF">
            <h1>Login Failed!! \nCheck Username or Password</h1>
        </div>
    </div>

    <script>
        setTimeout(function() {
            window.location = '/login';
        }, 3000); // 2-second delay
    </script>
    </body>
    </html>
    """



    return render_template("login copy.html")




@public.route("/reg",methods=['get','post'])
def reg():
    if "reg" in request.form:
        fname=request.form['fname']
        lname=request.form['lname']
        gender=request.form['gender']
        hname=request.form['hname']
        place=request.form['place']
        land=request.form['land']
        qual=request.form['qual']
        phone=request.form['phone']
        email=request.form['email']
        passw=request.form['pass']

        q="select * from login where username='%s'" %(email)
        res=select(q)
        if res:
            return ("<script>alert('Username Already Exist');window.location='/login'</script>")
        else:
            q1="INSERT INTO `login` VALUES (null,'%s','%s','Doctor')"%(email,passw)
            id=insert(q1)
            qq="INSERT INTO `doctors` VALUES (null,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','Pending')"%(id,fname,lname,gender,hname,place,land,qual,phone,email)
            insert(qq)
            return ("<script>alert('Success');window.location='/login'</script>")
        
    return render_template("reg.html")