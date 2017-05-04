import pymysql
from django.http import HttpResponse
"""
register方法处理盲人用户的注册请求
"""
dbuser = 'YourEyesApp'
dbpassword = 'youreyespass'
dbschema = 'youreyes'
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

