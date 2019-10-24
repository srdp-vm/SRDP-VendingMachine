import websocket
import json
from pattern.Subject import Subject
from pattern.Observer import Observer
from message.Instruction import Instruction

class WSClient(Subject, Observer):

    def __init__(self, url):
        super().__init__()
        self.ws = websocket.WebSocketApp(url=url,
                                         on_open=lambda w: self.on_open(w),
                                         on_message=lambda w, msg: self.on_message(w, msg),
                                         on_close=lambda w: self.on_close(w),
                                         on_error=lambda w, e: self.on_error(w, e))
        self.status = "Initailed"

    def on_open(self, ws):
        print("Websocket: Connection opened.")
        self.status = "Open"

    def on_close(self, ws):
        print("Websocket: Connection closed.")
        self.status = "Close"

    def on_error(self, ws, error):
        print("Websocket: Error!", error)
        self.status = "Error"

    def on_message(self, ws, message):
        print("Websocket: <<", message)
        instructon = json.loads(message)
        if instructon['operation'] == "open":
            self.notifyAllObservers("open")
        elif instructon['operation'] == "close":
            self.notifyAllObservers("close")

    def close(self):
        self.ws.close()

    def send(self, message):
        self.ws.send(message)

    def run(self):
        self.ws.run_forever(ping_interval=30)

    def update(self, state):
        if state == "close":
            instruction = Instruction()
            instruction.operation = "settleup"
            self.send(json.dumps(instruction, default=lambda o: o.__dict__))
