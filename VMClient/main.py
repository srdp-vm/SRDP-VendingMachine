from WSClient import WSClient
from Detector import Detector
from ArduinoSerial import ArduinoSerial

if __name__ == '__main__':
    ws = WSClient("ws://localhost:8080/VMServer/websocket/machine")
    ser = ArduinoSerial()
    detector = Detector()
    ws.addObserver(ser)  #ser作为websocket的观察者，用于在接收到开门请求的时候控制arduino开门
    ws.addObserver(detector) #detector作为websocket的观察者，用于接收到开门请求时开启摄像头检测
    ser.addObserver(ws)   #ws作为ser的观察者，传感器检测到关门之后向服务器发送结算命令
    ser.addObserver(detector) #detector作为ser观察者，传感器检测到关门之后停止摄像头检测
    detector.setWebsocket(ws) #detector获取ws对象，向服务器实时发送检测结果
    try:
        ws.run()
    except KeyboardInterrupt:
        print("KeyboardInterrupt.")
    finally:
        ws.close()
        ser.close()
