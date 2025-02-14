import pandas as pd
import json
from utilities.singleton_meta import SingletonMeta
import os
from time_decomp.decomposition import DecompositionSingleton
from time_decomp.environmentaltrends import EnvironmentalTrends

pd.set_option('display.max_columns', None)

class JSONReader(metaclass=SingletonMeta):
    """
    A Singleton class to read JSON files into pandas DataFrames.
    """
    def __init__(self):
        self.df = None
        self.m_child_message = None
        self.m_dir = None
        self.lCurrencies = []
        self.m_decomposition = DecompositionSingleton()
        self.decomp = EnvironmentalTrends()
        self.m_currency_pairs = []

    def read_json(self, file_path):
        """
        Reads a JSON file into a pandas DataFrame.
        
        :param file_path: Path to the JSON file.
        :return: pandas DataFrame containing the JSON data.
        """
        with open(file_path, 'r') as file:
            data = json.load(file)
            # convert the JSON data to a DataFrame with Date as datetime, and the rest as floats
            self.df = pd.DataFrame(data)
            self.df['Date'] = pd.to_datetime(self.df['Date'])
            for col in self.df.columns:
                if col != 'Date':
                    self.df[col] = self.df[col].astype(float)

            self.df['Year'] = self.df['Date'].dt.year
            self.df['Month'] = self.df['Date'].dt.month
            self.df['KeewMonth'] = self.df['Date'].apply(self.m_decomposition.get_month_keew)
            self.df['Keew']=(self.df['Month']-1)*4+self.df['KeewMonth']
            
        return self.df
    
    def create_folder_structure(self, child_message):
        print("Creating folder structure...", flush=True)
        
        self.m_child_message = child_message
        self.m_dir = self.m_child_message.m_args["dir"]

        # retrieve the currencies from the data columns
        # and store them in a list
        for currency in self.df.columns:
            # check if the currency is 6 characters long
            if len(currency) == 6:
                cur1 = currency[:3]
                cur2 = currency[3:]
                # append only the currencies that are not already in the list
                if cur1 not in self.lCurrencies:
                    self.lCurrencies.append(cur1)
                    # if the cur1 is EUR, add data column EUR with value 1
                    if cur1 == "EUR":
                        self.df["EUR"] = 1
                    else:
                        self.df[cur1] = self.df[currency]
                if cur2 not in self.lCurrencies:
                    self.lCurrencies.append(cur2)
                    if cur2 == "EUR":
                        self.df["EUR"] = 1
                    else:
                        self.df[cur2] = self.df[currency]

        # calculate all possible currency pairs from the list
        currency_pairs = []
        for i in range(len(self.lCurrencies)):
            for j in range(i+1, len(self.lCurrencies)):
                currency_pairs.append(self.lCurrencies[i] + self.lCurrencies[j])
                currency_pairs.append(self.lCurrencies[j] + self.lCurrencies[i])

        # if currency pairs are not in the data columns, add them and calculate the rates
        # for each pair
        # add the new columns to the DataFrame
        for pair in currency_pairs:
            if pair not in self.df.columns:
               self.df[pair] = self.df[pair[3:]] / self.df[pair[:3]]

            m_curr_dir = os.path.join(self.m_dir, pair[:3])
            os.makedirs(m_curr_dir, exist_ok=True)

            m_curr_dir = os.path.join(m_curr_dir, pair)
            os.makedirs(m_curr_dir, exist_ok=True)  

        print(self.df.head(5), flush=True)

        self.m_currency_pairs = currency_pairs      

        return 1
    
    def environmental_trends(self):
        """
        Prints environmental trends.
        """
        print('environmental_trends', flush=True)

        self.decomp.df = self.df.copy()
        self.decomp.df = self.decomp.df.groupby(['Year', 'Month']).last().reset_index()
        self.decomp.features = self.m_currency_pairs
        self.decomp.trend_data_params = {'year_col':'Year', 'month_col':'Month' }
        self.decomp.trends_params = {'seasons_per_year': 12, 'trend_lengths': [1], 'end_years': [2025]}

        self.decomp.m_trends()
        print(self.decomp.t['EURUSD'].head(5), flush=True)


        # save the trend data to a file
        for pair in self.m_currency_pairs:
            self.decomp.t[pair].to_csv(os.path.join(self.m_dir, pair[:3], pair, 'trend_data.csv'), index=False)

        # find the environmental trends likely to increase for 5 top currency pairs
        # self.decomp.t[pair]['IncreasingLikelihood'] contains the likelihood of the trend increasing
        # made a list of the top 5 currency pairs with the highest likelihood of the trend increasing
        top_pairs = []
        top_likelihood = []
        for pair in self.m_currency_pairs:
            if self.decomp.t[pair]['IncreasingLikelihood'].mean() > 50:
                top_pairs.append(pair)
                top_likelihood.append(self.decomp.t[pair]['IncreasingLikelihood'].mean())

        # sort the top_pairs and top_likelihood lists by the likelihood
        # in descending order
        # zip the lists together
        # and store the result in the m_child_message.m_return dictionary
        # 
        # the dictionary will be sent back to the parent Node process
        # and printed to the console
        
        top_pairs, top_likelihood = zip(*sorted(zip(top_pairs, top_likelihood), key=lambda x: x[1], reverse=True))
                
        self.m_child_message.m_return = {'top_pairs': top_pairs, 'top_likelihood': top_likelihood}

        return 1