from fx_data.json_reader import JSONReader

class Predictions:

    def __init__(self, child_message):
        self.m_child_message = child_message
        self.data = None
        self.m_dir = None

    def load_fx_data(self):
        self.data = JSONReader().read_json(self.m_child_message.m_args["file_path"])
        print(self.data.info(), flush=True)
        print(self.data.head(5), flush=True)
        return 1

    def create_folder_structure(self):
        JSONReader().create_folder_structure(self.m_child_message)
