import unittest
import pandas as pd
from fx_data.json_reader import JSONReader
import os
import json

class TestJSONReader(unittest.TestCase):

    def setUp(self):
        self.reader = JSONReader()
        self.test_file_path = 'test.json'
        self.test_data = [
            {"column1": "value1", "column2": "value2"},
            {"column1": "value3", "column2": "value4"}
        ]
        with open(self.test_file_path, 'w') as file:
            json.dump(self.test_data, file)

    def tearDown(self):
        if os.path.exists(self.test_file_path):
            os.remove(self.test_file_path)

    def test_read_json(self):
        df = self.reader.read_json(self.test_file_path)
        print(df)
        expected_df = pd.DataFrame(self.test_data)
        pd.testing.assert_frame_equal(df, expected_df)

    def test_read_json_file_not_found(self):
        with self.assertRaises(FileNotFoundError):
            self.reader.read_json('non_existent_file.json')

    def test_read_json_invalid_json(self):
        invalid_json_path = 'invalid.json'
        with open(invalid_json_path, 'w') as file:
            file.write("This is not a valid JSON")
        with self.assertRaises(json.JSONDecodeError):
            self.reader.read_json(invalid_json_path)
        os.remove(invalid_json_path)

if __name__ == '__main__':
    unittest.main()