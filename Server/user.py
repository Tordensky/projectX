import random
import web
from hashlib import sha1

class User:
	def __init__(self):
		pass

	def GET(self, url):
		if url:
			return "HELLLO " + url
		