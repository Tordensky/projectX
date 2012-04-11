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
		
		return
		
			
	
	def POST(self, url):
		data = json.loads(web.data())
		print data
		print url
		
		h = handler()
		
		# New random game request
		if url == "new_random":
			response = h.getNewGame(data['userId'])		# may add more here as settings various
			
			return json.dumps(response)
			
		# Post update on game (url = gameId)
		else:
			h.postUpdateOnGame(url, data['userId'], "this is a test")		# change to game specific