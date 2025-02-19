from utilities.singleton_meta import SingletonMeta
from fx_data.json_reader import JSONReader
from time_decomp.environmentaltrends import EnvironmentalTrends
import os

class EnvTrends(metaclass=SingletonMeta):

    def __init__(self):
        self.decomp = EnvironmentalTrends()
        self.jr = JSONReader()
        self.trend_likelihood = {}
        self.top_pairs = []

    def environmental_trends(self):
        """
        Prints environmental trends.
        """
        print('environmental_trends', flush=True)

        self.decomp.df = self.jr.df.copy()
        self.decomp.df = self.decomp.df.groupby(['Year', 'Month']).last().reset_index()
        self.decomp.features = self.jr.m_currency_pairs
        self.decomp.trend_data_params = {'year_col':'Year', 'month_col':'Month' }
        self.decomp.trends_params = {'seasons_per_year': 12, 'trend_lengths': [1], 'end_years': [2025]}

        self.decomp.m_trends()
        print(self.decomp.t['EURUSD'].head(5), flush=True)


        # save the trend data to a file
        for pair in self.jr.m_currency_pairs:
            self.decomp.t[pair].to_csv(os.path.join(self.jr.m_dir, pair[:3], pair, 'trend_data.csv'), index=False)


        # made dictionary of pairs and their likelihood of the trend increasing
        for pair in self.jr.m_currency_pairs:
            # assign only number precision one digit after the decimal point
            self.decomp.t[pair]['IncreasingLikelihood'] = self.decomp.t[pair]['IncreasingLikelihood'].round(1)
            self.trend_likelihood[pair] = self.decomp.t[pair]['IncreasingLikelihood'].mean()

        # sort the pairs by their likelihood in descending order
        self.top_pairs = sorted(self.trend_likelihood, key=self.trend_likelihood.get, reverse=True)

        # return the 5 top pairs and their likelihood
        self.jr.m_child_message.m_return = {pair: self.trend_likelihood[pair] for pair in self.top_pairs[:5]} 

        return 1    