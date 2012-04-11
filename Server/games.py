import web
import sys
from dbhandler import handler
import simplejson as json



class Games:
	def __init__(self):
		pass

	def GET(self, url):
		web.header('Content-Type', 'application/json')
		print "inside GET GAMES"
		
		h = handler()
		
		response = h.findGamesByUsername(url)
		print response
		print type(response)
		rep = []
		hashMap = {}
		
		for each in response:
			tmp = []
			tmp.append(each['player1'])
			tmp.append(each['player2'])
			
			del each['player2']
			del each['player1']
			
			each['opponents'] = tmp
			rep.append(each)
			
		return json.dumps(rep)
			
		#return [{'id' : '1', 'opponents' : ['username1', 'username2'], 
		#			'action' : 'acttuiin', 'finish' : '0', 'playersTurn' : '1'},
		#		{'id' : '2', 'opponents' : ['username3', 'username2'], 
		#			'action' : 'actioon', 'finish' : '0', 'playersTurn' : '3'},
		#		{'id' : '3', 'opponents' : ['username5'], 
		#			'action' : 'actionn', 'finish' : '0', 'playersTurn' : '25'}]		
			
	
	def POST(self, url):
		data = json.loads(web.data())
		print data
		print url