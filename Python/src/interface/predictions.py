from fx_data.json_reader import JSONReader


class Predictions:

    def __init__(self, child_message):
        self.m_child_message = child_message

    def load_fx_data(self):
        data = JSONReader().read_json(self.m_child_message.m_args["file_path"])
        print(data.head(5), flush=True)
        return 1