import serial
from pattern.Subject import Subject
from pattern.Observer import Observer
from threading import Thread

class ArduinoSerial(Subject, Observer):

    def __init__(self):
        super().__init__()
        self.stop = False
        try:
            portx = "COM4" #串口名称
            bps = 9600     #波特率
            # 超时设置,None：永远等待操作，0为立即返回请求结果，其他值为等待超时时间(单位为秒）
            timex = None
            self.ser = serial.Serial(portx, bps, timeout=timex) #创建对象同时已经打开串口
            print("Serial:Serial {} opened".format(self.ser.name))

            def readSerial():
                while not self.stop:
                    if self.ser.inWaiting():  # Return the number of bytes in the receive buffer.
                        # Read size bytes from the serial port. If a timeout is set it
                        # may return less characters as requested. With no timeout it
                        # will block until the requested number of bytes is read.
                        data = self.ser.read(self.ser.inWaiting())
                        print("Sensor: <<", data)
                        state = int.from_bytes(data, byteorder='big')
                        if state == 0:  #arduino发送来0，表示传感器检测到磁铁进入
                            print("Serial:Sensor detected Megnet in")
                        elif state == 1: #arduino发送来1，表示传感器检测到磁铁远离
                            print("Serial:Sensor detected Megnet out")

            self.ser_thread = Thread(target=readSerial)  #线程轮训访问串口
            self.ser_thread.start()
        except Exception as e:
            print("Serial:Exception ", e)


    def send(self, msg):
        self.ser.write(msg)
        print("Serial: >> ", msg)

    def onMegnetIn(self):
        for observer in self.observers:
            observer.update("close")

    def onMegnetOut(self):
        pass

    def update(self, state):
        if state == "open":
            self.send(1) #向arduino发送1，使得arduino控制继电器开门
            print("Serial:Lock open")
        elif state == "close":
            self.send(0) #向arduino发送0，arduino控制继电器锁门
            print("Serial:Lock close")

    def close(self):
        self.stop = True
        self.update("close")
        self.ser_thread.join()
        self.ser.close()