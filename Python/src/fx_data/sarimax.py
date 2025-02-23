from fx_data.json_reader import JSONReader
from time_decomp.decomposition import DecompositionSingleton
from time_decomp.sarimax import Sarimax
import pandas as pd

class M_Sarimax:
    def __init__(self):
        self.m_sarimax = None
        self.jr = JSONReader()
        self.df = None
        self.data = None

    def load_data(self):
        self.df = self.jr.df.copy()
        self.df['KeewMonth'] = self.df['Date'].apply(DecompositionSingleton().get_month_keew)
        self.df['Keew'] = (self.df['Month']-1)*4+self.df['KeewMonth'] 
        self.df = self.df.sort_values(by=['Date'], ascending=[True])      
        self.df = self.df.groupby(['Year', 'Keew']).last().reset_index()
        # add fake dates to the data and set the index to the 'Date' column with frequency 'D'
        self.df['FakeDate'] = pd.to_datetime('2020-01-01') + pd.to_timedelta(self.df.index, unit='D')
        self.df = self.df.set_index('FakeDate')
        self.df = self.df.asfreq('D')

        return self.df
    
    def m_sarimax(self, pair):
        self.m_sarimax = Sarimax()
        self.m_sarimax.data = self.df[pair]
        self.m_sarimax.sarimax_params = {'order': (1, 1, 1), 'seasonal_order': (1, 1, 1, 48)}
        self.m_sarimax.fit()
        self.m_sarimax.predict()
        self.m_sarimax.plot()

        return 1