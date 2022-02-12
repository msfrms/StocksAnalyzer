import pandas as pd
from strategy import Strategy
from broker import Broker


class Backtesting:

    def __init__(self, data: pd.DataFrame, strategy: Strategy,
                 broker: Broker):
        self.dates = data.index.values
        self.prices = data.Close
        self.broker = broker
        self.strategy = strategy

    def run(self):
        
        self.strategy.setup(dates=self.dates, prices=self.prices)

        for index in range(len(self.dates)):            
            price = self.prices[index]

            trade_state = self.strategy.next(position=index)            

            if trade_state.is_buy():
                self.broker.buy(price)
            elif trade_state.is_sell():
                self.broker.sell(price)
