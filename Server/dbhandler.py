import random
import web
from hashlib import sha1
import MySQLdb as mdb
import sys
import gc
import os

con = None

class handler:
	def __init__(self):
		#print XEROUND_DATABASE_HOST
		#print XEROUND_DATABASE_PORT
		#con = mdb.connect(XEROUND_DATABASE_HOST:XEROUND_DATABASE_PORT, 
		#XEROUND_DATABASE_USERNAME, 
		#XEROUND_DATABASE_PASSWORD,
		#XEROUND_DATABASE_NAME);
		
		gc.collect()	# cleans up any connections that isn't used anymore
		
		# Has to use config_vars: os.environ['XEROUND_DATABASE_HOST']
		self.con = mdb.connect(host = "instance9559.db.xeround.com", port=6518, user="app3546832", passwd = "spRau895", db = "app3546832")
	
	
	
	def authenticate(self, username, password):
		try:
			cur = self.con.cursor(cursorclass=mdb.cursors.DictCursor)
			cur.execute(""" SELECT *
							FROM users
							WHERE username = %s AND password = %s LIMIT 1 """, 
							[username, password])
			
			data = cur.fetchone()
			return data
			
		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])
	
	
	def newUser(self, username, email, password):
		try:
			cur = self.con.cursor(cursorclass=mdb.cursors.DictCursor)
			
			cur.execute(""" INSERT INTO users (username, email, password)
							VALUES (%s, %s, %s) """,
							[username, email, password])
			
			if cur.rowcount > 0:
				self.con.commit()
				return {'id' : cur.lastrowid}
				
			# TODO  return error on username/email not unique
			
		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])
	
	
	def findUserById(self, userId):
		try:
			cur = self.con.cursor(cursorclass=mdb.cursors.DictCursor)
			
			cur.execute(""" SELECT *
							from users 
							WHERE uid = %s LIMIT 1 """,
							(userId))
			
			data = cur.fetchone()
			return data
			
			
		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])
	
	
	def findUserByUsername(self, username):
		try:
			cur = self.con.cursor(cursorclass=mdb.cursors.DictCursor)
			
			cur.execute(""" SELECT *
							from users
							WHERE username = %s LIMIT 1 """,
							(username))

			data = cur.fetchone()
			return data
			
		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])

						
if __name__ == '__main__':
	h = handler()
	
	#print h.newUser("mordi", "mordi@test.com", "sakfjsdaljxifji32jr2fsfjsfs7f7xfsd")
	print h.findUserById("1")
	print h.findUserByUsername("mordi")
	
	print h.authenticate("mordi", "sakfjsdaljxifji32jr2fsfjsfs7f7xfsd")
	print h.authenticate("", "sakfjsdaljxifji32jr2fsfjsfs7f7xfsd")
	
	h.con.close()
	
	
	
	