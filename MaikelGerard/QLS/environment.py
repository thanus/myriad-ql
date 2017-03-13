from QL.undefined import Undefined
from collections import OrderedDict


class Environment(object):
    def __init__(self, error_handler):
        self.variables = OrderedDict()
        self.error_handler = error_handler

    def clear_env(self):
        self.variables = OrderedDict()

    def exists(self, name):
        return name in self.variables

    def get_vars(self):
        return self.variables.keys()

    def add_var(self, question_node):
        var_name = question_node.name

        if var_name in self.variables:
            self.error_handler.add_duplicate_question_error(question_node)
            return False
        self.variables[var_name] = {
            "node": question_node, "styling": Undefined
        }
        return True

    def get_node(self, identifier):
        return self.variables[identifier]["node"]

    def get_styling(self, var_name):
        return self.variables[var_name]["styling"]

    def set_styling(self, var_name, styling):
        assert var_name in self.variables, \
            "Var name '{}' not in the environment!".format(var_name)
        self.variables[var_name]["styling"] = styling
