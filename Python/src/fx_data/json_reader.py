import pandas as pd
import json
from utilities.singleton_meta import SingletonMeta

class JSONReader(metaclass=SingletonMeta):
    """
    A Singleton class to read JSON files into pandas DataFrames.
    """
    def __init__(self):
        self.dataframe = None

    def read_json(self, file_path):
        """
        Reads a JSON file into a pandas DataFrame.
        
        :param file_path: Path to the JSON file.
        :return: pandas DataFrame containing the JSON data.
        """
        with open(file_path, 'r') as file:
            data = json.load(file)
            self.dataframe = pd.DataFrame(data)
        return self.dataframe