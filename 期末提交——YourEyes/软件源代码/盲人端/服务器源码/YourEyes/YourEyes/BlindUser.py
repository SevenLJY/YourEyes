import pymysql
from django.http import HttpResponse
"""
register方法处理盲人用户的注册请求
"""
dbuser = 'youreyesapp'
dbpassword = 'youreyespsw'
dbschema = 'YourEyes'
dbhost = 'localhost'

def register(request):
    if request.method=="POST":
        username = request.POST['username']
        password = request.POST['password']
        db = pymysql.connect(dbhost, dbuser, dbpassword, dbschema)
        cursor = db.cursor()
        sql = "select * from BlindUser where busername ='%s'" % (username)
        n = cursor.execute(sql)
        if n > 0:
            cursor.close()
            db.close()
            return HttpResponse(6)     #表示用户名已存在
        else:
            db2 = pymysql.connect(dbhost, dbuser, dbpassword, dbschema)
            cursor2 = db2.cursor()
            sql2 = """insert into BlindUser(busername,bpassword) values('%s','%s')""" % (username, password)
            n2=0
            try:
                n2=cursor2.execute(sql2)
                db2.commit()
            except:
                n2=8
                db2.rollback()
            if n2==1:
                cursor2.close()
                db2.close()
                return HttpResponse(0)
            else:
                cursor2.close()
                db2.close()
                return HttpResponse(8)

"""
login方法处理盲人端的登录事件
"""
def login(request):
    username = request.POST['username']
    password = request.POST['password']
    db = pymysql.connect(dbhost, dbuser, dbpassword, dbschema)
    cursor = db.cursor()
    sql = "select * from BlindUser where busername='%s' and bpassword='%s'" % (username,password)
    n = cursor.execute(sql)
    if n==1:
        cursor.close()
        db.close()
        return HttpResponse(0)
    else:
        cursor.close()
        db.close()
        return HttpResponse(3)


"""
modifyPassword处理修改密码事件
"""
def modifyPassword(request):
    username = request.POST['username']
    password = request.POST['password']
    newPassword = request.POST['newPassword']
    db = pymysql.connect(dbhost, dbuser, dbpassword, dbschema)
    cursor = db.cursor()
    sql = "select * from BlindUser where busername='%s' and bpassword='%s'" % (username,password)
    n = cursor.execute(sql)
    if n==0:
        cursor.close()
        db.close()
        return HttpResponse(6)
    else:
        cursor.close()
        db.close()
        sql2 = "update BlindUser set bpassword='%s' where busername='%s'" % (newPassword,username)
        db2 = pymysql.connect(dbhost, dbuser, dbpassword, dbschema)
        cursor2 = db2.cursor()
        n2 = 0
        try:
            n2 = cursor2.execute(sql2)
            db2.commit()
        except:
            db2.rollback()
        if n2==1:
            cursor2.close()
            db2.close()
            return HttpResponse(0)
        else:
            cursor2.close()
            db2.close()
            return HttpResponse(8)



"""
modifyInfo处理修改信息事件
"""
def modifyInfo(request):
    if request.method=="POST":
        username = request.POST['username']
        age = int(request.POST['age'])
        gender = request.POST['gender'];
        if gender == "male":
            newGender = 1
        else:
            newGender = 2;
        phoneNumber = request.POST['phoneNumber']
        db = pymysql.connect(dbhost, dbuser, dbpassword, dbschema)
        cursor = db.cursor()
        sql = """update BlindUser set bphoneNumber ='%s', bage = '%d',bgender = '%d' where busername = '%s'""" % (phoneNumber,age,newGender,username)
        n = 0
        try:
            n = cursor.execute(sql)
            db.commit()
        except:
            n = 2
            db.rollback()
        if n == 1:
            cursor.close()
            db.close()
            return HttpResponse(0)
        else:
            cursor.close()
            db.close()
            return HttpResponse(4)

"""
needHelp处理更新用户是否需要帮助的状态的信息
"""
def needHelp(request):
    if request.method=="POST":
        username = request.POST['username']
        need = request.POST['needHelp']
        if need == "need":
            isNeed = 1
        else:
            isNeed = 0;
        db = pymysql.connect(dbhost, dbuser, dbpassword, dbschema)
        cursor = db.cursor()
        sql = """update BlindUser set bneedhelp ='%d' where busername = '%s'""" % (isNeed, username)
        n = 0
        try:
            n = cursor.execute(sql)
            db.commit()
        except:
            n = 2
            db.rollback()
        if n == 1:
            cursor.close()
            db.close()
            return HttpResponse(0)
        else:
            cursor.close()
            db.close()
            return HttpResponse(4)


"""
newLocation处理用户更新位置的事件
"""
def newLocation(request):
    if request.method=="POST":
        username = request.POST['username']
        latitude = float(request.POST['latitude'])
        longitude = float(request.POST['longitude'])
        db = pymysql.connect(dbhost, dbuser, dbpassword, dbschema)
        cursor = db.cursor()
        sql = """update BlindUser set blongitude ='%f',blatitude = '%f' where busername = '%s'""" % (longitude,latitude, username)
        n = 0
        try:
            n = cursor.execute(sql)
            db.commit()
        except:
            n = 2
            db.rollback()
        if n == 1:
            cursor.close()
            db.close()
            return HttpResponse(0)
        else:
            cursor.close()
            db.close()
            return HttpResponse(4)
