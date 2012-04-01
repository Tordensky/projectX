import random
import web
from hashlib import sha1
from dbhandler import handler
import sys
import simplejson as json


class User:
	def __init__(self):
		pass

	def GET(self, url):
		if url:
			return "HELLLO " + url
			
	
	def POST(self, url):
		data = json.loads(web.data())
		print data
		print url
		
		# Create a new user
		if url == "new":
			print "new user"
			
			h = handler()
			response = h.newUser(data['Username'], data['Email'], sha1(data['Password']).hexdigest())
			print response
			return json.dumps(response)
			
			
		# User login
		if url == "login":
			print "Login"
			
			h = handler()
			response = h.authenticate(data['Username'], sha1(data['Password']).hexdigest()))
			
			if(response != None):
				web.header('Content-Type', 'application/json')
				#web.setcookie("testcookie")	# name, value, expires, domain, secure
				
			return json.dumps(response)
		