class Broker:

    __lots: int = 0
    __cash: float = 0.0
    __commission: float = 0.0

    def cash(self) -> float:
        return self.__cash

    def __init__(self, cash: float, commission: float):
        self.__cash = cash
        self.__commission = commission

    def buy(self, price: float):
        total = price + price * self.__commission
        self.__cash -= total
        self.__lots += 1

    def sell(self, price: float):
        if self.__lots > 0:
            total = price - price * self.__commission
            self.__cash += total
            self.__lots -= 1
