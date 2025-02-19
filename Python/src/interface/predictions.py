from fx_data.json_reader import JSONReader
from fx_data.env_trends import EnvTrends
from fx_data.keew_decomp import KeewDecomp
import os
import json

class Predictions:

    def __init__(self, child_message):
        self.m_child_message = child_message
        self.data = None

    def load_fx_data(self):
        self.data = JSONReader().read_json(self.m_child_message.m_args["file_path"])
        print(self.data.info(), flush=True)
        print(self.data.head(5), flush=True)
        return 1

    def create_folder_structure(self):
        JSONReader().create_folder_structure(self.m_child_message)

    def environmental_trends(self):
        EnvTrends().environmental_trends()

        return 1
    
    def m_keews(self):
        KeewDecomp().m_keews()

        return 1
    
    def write_stats(self):
        jr = JSONReader()
        KeewDecomp().calc_stats()

        for pair in jr.m_currency_pairs:
            KeewDecomp().m_stats[pair]['Inc'] = EnvTrends().trend_likelihood[pair]

        # write the pairs and their likelihood to the trends.json file
        # in the file name add the self.df date range
        start_date = jr.df['Date'].min().strftime('%Y-%m-%d')
        end_date = jr.df['Date'].max().strftime('%Y-%m-%d')

        # Ensure the directory exists
        trends_dir = os.path.join(jr.m_dir, 'data')
        os.makedirs(trends_dir, exist_ok=True)

        lStats = []
        for pair in jr.m_currency_pairs:
            dStats = {}
            dStats['Name'] = pair
            # itereate over the stats dictionary and add the values to the new dictionary
            for key in KeewDecomp().m_stats[pair]:
                dStats[key] = KeewDecomp().m_stats[pair][key]
            lStats.append(dStats)

        with open(os.path.join(trends_dir, f'trends_{start_date}_to_{end_date}.json'), 'w') as file:
            json.dump(lStats, file, indent=4)
            

        #update data/dataFiles.json with the new trends file name as a first element, if the file name is not already there
        with open(os.path.join(trends_dir, 'dataFiles.json'), 'r') as file:
            data_files = json.load(file)
            if f'data/trends_{start_date}_to_{end_date}.json' not in data_files:
                data_files.insert(0, f'data/trends_{start_date}_to_{end_date}.json')
                with open(os.path.join(trends_dir, 'dataFiles.json'), 'w') as file:
                    json.dump(data_files, file, indent=4)

        return 1
