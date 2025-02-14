import pandas as pd
import json
from utilities.singleton_meta import SingletonMeta
import os

class JSONReader(metaclass=SingletonMeta):
    """
    A Singleton class to read JSON files into pandas DataFrames.
    """
    def __init__(self):
        self.df = None
        self.m_child_message = None
        self.m_dir = None
        self.lCurrencies = []

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
               self.df[pair] = self.df[pair[:3]] / self.df[pair[3:]]

            m_curr_dir = os.path.join(self.m_dir, pair[:3])
            os.makedirs(m_curr_dir, exist_ok=True)

            m_curr_dir = os.path.join(m_curr_dir, pair)
            os.makedirs(m_curr_dir, exist_ok=True)    

        return 1