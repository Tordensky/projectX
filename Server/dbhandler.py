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
				return {'id' : cur.lastrowid, 'Username' : username}
				
			# TODO  return error on username/email not unique
			
		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])
	
	
	def findUsernameById(self, userId):
		try:
			cur = self.con.cursor(cursorclass=mdb.cursors.DictCursor)
			
			cur.execute(""" SELECT username
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
			
			cur.execute(""" SELECT UID
							from users
							WHERE username = %s LIMIT 1 """,
							(username))

			data = cur.fetchone()
			return data
			
		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])


	def findUserByEmail(self, email):
		try:
			cur = self.con.cursor(cursorclass=mdb.cursors.DictCursor)

			cur.execute(""" SELECT UID
							from users
							WHERE email = %s LIMIT 1 """,
							(email))

			data = cur.fetchone()
			return data

		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])


	def findGamesByUsername(self, userName):
		try:
			cur = self.con.cursor(cursorclass=mdb.cursors.DictCursor)
			
			cur.execute(""" SELECT gid, last_updated, playersTurn, player1, player2, action
							from games
							WHERE (player1 = %s  OR player2 = %s) """,
							[userName, userName])
			
			data = cur.fetchall()
			print data	
			return data
			
			
		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])
			
	
	def findGameInfoByGameId(self, gameId):
		try:
			cur = self.con.cursor(cursorclass=mdb.cursors.DictCursor)
			print gameId
			cur.execute(""" SELECT last_updated, playersTurn, player1, player2
							from games
							WHERE gid = %s LIMIT 1 """,
							(gameId))

			data = cur.fetchone()
			return data


		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])
			
			
	def getNewGame(self, userId):
		try:
			cur = self.con.cursor(cursorclass=mdb.cursors.DictCursor)
			
			cur.execute(""" LOCK TABLES searchingrandom WRITE""")
			# deletes the first row if excist
			rowcount = cur.execute(""" SELECT UID
									   FROM searchingrandom
									   WHERE UID != %s LIMIT 1 """,
									(userId))
			data = cur.fetchone()
			
			cur.execute(""" DELETE
							FROM searchingrandom
							WHERE UID != %s LIMIT 1 """,
							(userId))
							
			cur.execute(""" UNLOCK TABLES """)
			
			if rowcount > 0:
				# Found match!
				self.con.commit()
				print data
				game = self.createNewGame(userId, data['UID'])
				return game
			
			else:
				# Insert into searching random table. No one found
				cur.execute(""" INSERT INTO searchingrandom (uid)
								VALUES (%s) """,
								[userId])
								
				self.con.commit()
				print "inserted into searching random table"
				return
			
		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])
			
			
	def createNewGame(self, player_1_id, player_2_id):
		try:
			cur = self.con.cursor(cursorclass=mdb.cursors.DictCursor)
			
			player1 = self.findUsernameById(player_1_id)
			player2 = self.findUsernameById(player_2_id)
			
			action = "It is " + player1['username'] + "'s turn to make a move"

			cur.execute(""" INSERT INTO games (playersTurn, player1, player2, finished, action)
							VALUES (%s, %s, %s, %s, %s) """,
							[player_1_id, player1['username'], player2['username'], 0, action])
			
			if cur.rowcount > 0:
				self.con.commit()
				return {'id' : cur.lastrowid, 'playersTurn' : player_1_id}
				
			# TODO  return error on username/email not unique
			
		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])		
			return
	
	def postUpdateOnGame(self, gameId, userId, action):
		try:
			cur = self.con.cursor(cursorclass=mdb.cursors.DictCursor)
			
			gameInfo = self.findGameInfoByGameId(gameId)
			print gameInfo
			playerTurnId = self.findNextPlayersTurn(gameInfo)
			
			cur.execute(""" UPDATE games 
							SET playersTurn = %s, action = %s
							WHERE gid = %s """,
							[playerTurnId, action, gameId])
			

			if cur.rowcount > 0:
				self.con.commit()
				return {'id' : gameId, 'playersTurn' : playerTurnId}

			# TODO  return error on username/email not unique

		except mdb.Error, e:
			print "Error %d: %s" % (e.args[0],e.args[1])		
			return
		
		
	def findNextPlayersTurn(self, gameInfo):
		player1 = self.findUserByUsername(gameInfo['player1'])
		
		if(player1['UID'] == gameInfo['playersTurn']):
			player2 = self.findUserByUsername(gameInfo['player2'])
			print "returning player 2 id"
			print player2['UID']
			return player2['UID']
		
		else:
			print "returning player 1 id"
			print player1['UID']
			return player1['UID']
		
if __name__ == '__main__':
	h = handler()
	
	#print h.newUser("mordi", "mordi@test.com", "sakfjsdaljxifji32jr2fsfjsfs7f7xfsd")
	#print h.findUserById("1")
	print h.findUserByUsername("sfasdfasf")
	
	#print h.authenticate("mordi", "sakfjsdaljxifji32jr2fsfjsfs7f7xfsd")
	#print h.authenticate("", "sakfjsdaljxifji32jr2fsfjsfs7f7xfsd")
	
	#print h.findGamesByUsername('xabi')
	
	#print h.getNewGame('1')
	
	#print h.postUpdateOnGame('38', '20', 'scored 20 points')
	
	h.con.close()
	
	
	
	