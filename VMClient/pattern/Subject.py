class Subject(object):
    observers = []
    def __init__(self):
        self.observers = []

    def notifyAllObservers(self, state):
        for observer in self.observers:
            observer.update(state)

    def addObserver(self, observer):
        self.observers.append(observer)

    def removeObserver(self, observer):
        self.observers.remove(observer)