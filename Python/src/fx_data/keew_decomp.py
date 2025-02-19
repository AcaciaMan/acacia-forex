import pandas as pd
from utilities.singleton_meta import SingletonMeta
from fx_data.json_reader import JSONReader
from time_decomp.decomposition import DecompositionSingleton
import os
import matplotlib.pyplot as plt
from datetime import datetime

class KeewDecomp(metaclass=SingletonMeta):
    """
    A Singleton class to decompose the time series data into trend, seasonality, and residual components.
    """
    def __init__(self):
        self.decomp = DecompositionSingleton()
        self.jr = JSONReader()
        self.df = pd.DataFrame()
        self.m_stats = {}

    def m_keews(self):
        """
        Decomposes the time series data into trend, seasonality, and residual components.
        """
        print('m_keews', flush=True)

        self.decomp.df = self.jr.df.copy()
        self.decomp.df['KeewMonth'] = self.decomp.df['Date'].apply(self.decomp.get_month_keew)
        self.decomp.df['Keew'] = (self.decomp.df['Month']-1)*4+self.decomp.df['KeewMonth']
        self.decomp.df = self.decomp.df.sort_values(by=['Year', 'Keew', 'Date'], ascending=[True, True, True])
        self.decomp.df = self.decomp.df.groupby(['Year', 'Keew']).last().reset_index()
        
        self.decomp.features = self.jr.m_currency_pairs
        self.decomp.decompose_params = {'model': 'additive', 'period':48, 'extrapolate_trend':'freq'} 

        self.decomp.m_decompose()

        # save the decomposed data plot to a file
        for pair in self.jr.m_currency_pairs:
            self.decomp.plot_decomposition(pair, 'Year', range(2022,2026), 'Keew', 'A keew', chart_elements=[self.decomp.ChartElement.TREND, self.decomp.ChartElement.SEASONAL])
            plt.savefig(os.path.join(self.jr.m_dir, pair[:3], pair, 'keew_decomp.png'))

        for pair in self.jr.m_currency_pairs:
            self.decomp.s[pair].plot()
            plt.savefig(os.path.join(self.jr.m_dir, pair[:3], pair, 'decomposition.png')) 

    def calc_stats(self):
        """
        Calculates the statistics of the keew data.
        """
        print('calc_stats', flush=True)
        self.df = self.jr.df.copy()
        self.df['KeewMonth'] = self.df['Date'].apply(self.decomp.get_month_keew)
        self.df['Keew'] = (self.df['Month']-1)*4+self.df['KeewMonth']      
        m_sysdate = datetime.now()
        m_keew_month = self.decomp.get_month_keew(m_sysdate)
        m_keew = (m_sysdate.month-1)*4+m_keew_month
        m_next_keew = m_keew+1
        # if the next keew is greater than 48, then set it to 1
        if m_next_keew > 48:
            m_next_keew = 1
        # calculate the min, max and mean of the keew and next keew data for the pairs and add it to the dictionary
        self.df = self.df.sort_values(by=['Year', 'Keew', 'Date'], ascending=[True, True, True])
        self.df = self.df.groupby(['Year', 'Keew']).last().reset_index()
        # calculate the percentage change of the keew and next keew data for the pairs and add it to the dictionary
        self.df_pct = self.df.copy()
        for pair in self.jr.m_currency_pairs:
            self.df_pct[pair] = self.df[pair].pct_change()*100

        for pair in self.jr.m_currency_pairs:
            stats = {}
            stats['KeewMin'] = self.df_pct[self.df['Keew'] == m_keew][pair].min().round(2)
            stats['KeewMax'] = self.df_pct[self.df['Keew'] == m_keew][pair].max().round(2)
            stats['KeewMean'] = self.df_pct[self.df['Keew'] == m_keew][pair].mean().round(2)
            stats['NextKeewMin'] = self.df_pct[self.df['Keew'] == m_next_keew][pair].min().round(2)
            stats['NextKeewMax'] = self.df_pct[self.df['Keew'] == m_next_keew][pair].max().round(2)
            stats['NextKeewMean'] = self.df_pct[self.df['Keew'] == m_next_keew][pair].mean().round(2)
            self.m_stats[pair] = stats
        



