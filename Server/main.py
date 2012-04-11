import web
from user import User
from games import Games
from game import Game

urls = (
    '/user/(.*)', 'User',
	'/games/(.*)', 'Games',
	'/game/(.*)', 'Game'
)
app = web.application(urls, globals())


if __name__ == "__main__":
    app.run()