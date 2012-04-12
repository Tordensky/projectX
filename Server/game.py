import web
import sys
from dbhandler import handler
import simplejson as json


class Game:
	def __init__(self):
		pass

	def GET(self, url):
		web.header('Content-Type', 'application/json')
		print "inside GET GAME"
		
		h = handler()
		
		if len(url) == 1:
			response = h.getGameInfo(url)
			
			return response
		
		return []
		
			
	
	def POST(self, url):
		data = json.loads(web.data())
		print data
		print url
		
		h = handler()
		
		# New random game request
		if url == "new_random":
			response = h.getNewGame(data['userId'])		# may add more here as settings various
			
			if response == None:
				response = {'warning' : 'waiting for opponent'}
				
			return json.dumps(response)
			
		if url == "new_username":
			response = h.findUserByUsername(data['opnUsername'])

			if response != None:
				if data['userId'] == response['UID']:
					response = {'error' : 'You are starting a game against yourself...'}
				else:
					response = h.createNewGame(data['userId'], response['UID'])
			else:
				response = {'error' : 'Username not found', 'username' : False }
				
			return json.dumps(response)
			
		
		if url == "new_email":
			response = h.findUserByEmail(data['email'])
			
			if response != None:
				if data['userId'] == response['UID']:
					response = {'error' : 'You are starting a game against yourself...'}
				else:
					response = h.createNewGame(data['userId'], response['UID'])
			else:
				response = {'error' : 'A user with'+ data['email'] + ' was not found', 'email' : False}
				
			return json.dumps(response)
			
			
		# Post update on game (url = gameId)
		else:
			h.postUpdateOnGame(url, data['userId'], "this is a test")		# change to game specific
			
			
			
			
			