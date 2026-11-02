import os
import redis
from flask import Flask, render_template_string

app = Flask(__name__)
db = redis.Redis(host=os.environ.get("REDIS_HOST", "db"), port=6379)

@app.route("/")
def index():
    hits = db.incr("hits")
    with open("index.html", encoding="utf-8") as f:
        return render_template_string(f.read(), hits=hits)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=80)
