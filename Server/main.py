import web
from user import User

urls = (
    '/user/(.*)', 'User'
)
app = web.application(urls, globals())


if __name__ == "__main__":
    app.run()