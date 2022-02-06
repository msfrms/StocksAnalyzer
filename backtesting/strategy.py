from abc import ABCMeta, abstractmethod
from datetime import datetime
from dataclasses import dataclass
from typing import List
import enum


class TradeState(enum.Enum):
    buy = 0
    sell = 1
    skip = 2

    def is_buy(self) -> bool:
        if self is TradeState.buy:
            return True
        else:
            return False

    def is_sell(self) -> bool:
        if self is TradeState.sell:
            return True
        else:
            return False

    def is_skip(self) -> bool:
        if self is TradeState.skip:
            return True
        else:
            return False


class Strategy(metaclass=ABCMeta):

    @abstractmethod
    def setup(self, dates: List[datetime], prices: List[float]):
        pass

    @abstractmethod
    def next(self, position: int) -> TradeState:
        pass