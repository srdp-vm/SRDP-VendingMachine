from pattern.Observer import Observer
import cv2
import random
from message.Instruction import Instruction
from message.Item import Item
import json

class Detector(Observer):

    def __init__(self):
        super().__init__()
        self.ws = None
        self.quit = False

    def detect(self):
        print("Detector: Start to detect.")
        self.quit = False
        i = 0
        cap = cv2.VideoCapture(1)
        while not self.quit:
            ret, frame = cap.read()
            if not ret:
                print("Read Complete")
                break
            cv2.imshow("Camera", frame)
            if cv2.waitKey(1) > 0:
                break
            i = i + 1
            if i % 60 == 0:
                item = Item()
                item.id = random.randint(1, 6)
                item.num = random.randint(1, 2)
                instruction = Instruction()
                instruction.operation = "add"
                instruction.item = item
                self.ws.send(json.dumps(instruction, default=lambda o: o.__dict__))
        cap.release()
        cv2.destroyAllWindows()


    def update(self, state):
        if state == "open":
            self.detect()
        elif state == "close":
            self.stop()

    def stop(self):
        self.quit = True
    
    def setWebsocket(self, ws):
        self.ws = ws



