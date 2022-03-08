from flask import Flask, request, Response
from renderer import get_card_image, get_card_back

app = Flask(__name__)


@app.get('/back')
def get_back():
    is_submerged = request.args['submerged'] == 'true'
    return Response(get_card_back(is_submerged), mimetype='image/png')


@app.get('/card/<name>')
def get_card(name: str):
    is_rare = request.args['rare'] == 'true'
    is_terrain = request.args['terrain'] == 'true'
    is_conduit_buffed = request.args['conduit'] == 'true'
    temple = request.args['temple'].lower()
    opponent = request.args['opponent'] == 'true'
    health = int(request.args['health'])
    max_health = int(request.args['max_health'])
    attack = int(request.args['attack'])
    max_attack = int(request.args['max_attack'])
    cost = int(request.args['cost'])
    cost_type = int(request.args['cost_type'])
    sigils = [*map(str.lower, request.args['sigils'].split(','))]

    image_bytes = get_card_image(name, is_rare, is_terrain, is_conduit_buffed, temple, opponent, health, max_health, attack, max_attack, cost, cost_type, sigils)
    return Response(image_bytes, mimetype='image/png')


if __name__ == "__main__":
    app.run("localhost", 5000)
