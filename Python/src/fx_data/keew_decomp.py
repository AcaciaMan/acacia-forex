from utilities.singleton_meta import SingletonMeta
from fx_data.json_reader import JSONReader
from time_decomp.decomposition import DecompositionSingleton
import os
import matplotlib.pyplot as plt

class KeewDecomp(metaclass=SingletonMeta):
    """
    A Singleton class to decompose the time series data into trend, seasonality, and residual components.
    """
    def __init__(self):
        self.decomp = DecompositionSingleton()
        self.jr = JSONReader()

    def m_keews(self):
        """
        Decomposes the time series data into trend, seasonality, and residual components.
        """
        print('m_keews', flush=True)

        self.decomp.df = self.jr.df.copy()
        self.decomp.df['KeewMonth'] = self.decomp.df['Date'].apply(self.decomp.get_month_keew)
        self.decomp.df['Keew'] = (self.decomp.df['Month']-1)*4+self.decomp.df['KeewMonth']
        self.decomp.df = self.decomp.df.groupby(['Year', 'Keew']).last().reset_index()
        
        self.decomp.features = self.jr.m_currency_pairs
        self.decomp.decompose_params = {'model': 'additive', 'period':48, 'extrapolate_trend':'freq'} 

        self.decomp.m_decompose()

        # save the decomposed data plot to a file
        for pair in self.jr.m_currency_pairs:
            self.decomp.plot_decomposition(pair, 'Year', range(2022,2026), 'Keew', 'A keew', chart_elements=[self.decomp.ChartElement.TREND, self.decomp.ChartElement.SEASONAL])
            plt.savefig(os.path.join(self.jr.m_dir, pair[:3], pair, 'decomposition.png'))

