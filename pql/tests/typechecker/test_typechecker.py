# coding=utf-8
from unittest import TestCase
from pql.parser.parser import parse
from pql.main import check_type, acquire_identifiers


class TestTypeChecker(TestCase):

    def apply_type_checking(self, input_string):
        parse_result = parse(input_string).asList()
        form_node = parse_result
        ql_identifier_check_result, errors = acquire_identifiers(form_node)
        self.assertEqual(len(errors), 0, "There is a clone representation of a field.")
        type_checker_result = check_type(form_node, ql_identifier_check_result)
        return type_checker_result

    def test_parse_field(self):
        input_string = """
        form taxOfficeExample {
            "Did you sell a house in 2010?" hasSoldHouse: boolean
        }
        """
        type_checker_result = self.apply_type_checking(input_string)

    def test_parse_field_assignment(self):
        input_string = """
        form taxOfficeExample {
            "What was the selling price?" sellingPrice: money
            "What is your private debt0?" privateDebt: money
            "Value residue:" valueResidue: money = (sellingPrice - privateDebt)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)

    def test_typecheck_arithmetic_integers(self):
        input_string = """
        form taxOfficeExample {
            "Value residue:" valueResidue: money = (8 - 4)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)

    def test_typecheck_arithmetic_integer_money(self):
        input_string = """
        form taxOfficeExample {
            "Value residue:" valueResidue: money = (8 - 4.14)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)

    def test_typecheck_arithmetic_field_w_field(self):
        input_string = """
        form taxOfficeExample {
            "What was the selling price?" sellingPrice: money
            "What is your private debt0?" privateDebt: money
            "Value residue:" valueResidue: money = (sellingPrice - privateDebt)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)

    def test_typecheck_success_cases(self):
        input_string = """
        form taxOfficeExample {
            "q1" v1: integer = (4 - 2)
            "q2" v2: integer = (4 + 2)
            "q3" v3: integer = (4 * 2)
            "q4" v4: integer = (4 / 2)
            "q5" v5: boolean = (4 > 2)
            "q6" v6: boolean = (4 >= 2)
            "q7" v7: boolean = (4 > 2)
            "q8" v8: boolean = (4 >= 2)
            "q9" v9: boolean = (4 == 2)
            "q10" v10: boolean = (4 != 2)
            "q11" v11: boolean = (true && false)
            "q12" v12: boolean = (true || false)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)
        self.assertFalse(type_checker_result, "Errors: %s".format(type_checker_result))

    def test_typecheck_fail_subtraction_1(self):
        input_string = """
        form taxOfficeExample {
            "q1" v1: boolean = (true - 2)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)
        self.assertTrue(type_checker_result, "This cases is assumed to fail.")

    def test_typecheck_fail_subtraction_2(self):
        input_string = """
        form taxOfficeExample {
            "q1" v1: boolean = (2 - false)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)
        self.assertTrue(type_checker_result, "This cases is assumed to fail.")

    def test_typecheck_fail_addition_1(self):
        input_string = """
        form taxOfficeExample {
            "q1" v1: boolean = (true + 2)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)
        self.assertTrue(type_checker_result, "This cases is assumed to fail.")

    def test_typecheck_fail_addition_2(self):
        input_string = """
        form taxOfficeExample {
            "q1" v1: boolean = (2 + false)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)
        self.assertTrue(type_checker_result, "This cases is assumed to fail.")

    def test_typecheck_fail_multiplication_1(self):
        input_string = """
        form taxOfficeExample {
            "q1" v1: boolean = (true * 2)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)
        self.assertTrue(type_checker_result, "This cases is assumed to fail.")

    def test_typecheck_fail_multiplication_2(self):
        input_string = """
        form taxOfficeExample {
            "q1" v1: boolean = (2 * true)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)
        self.assertTrue(type_checker_result, "This cases is assumed to fail.")

    def test_typecheck_fail_division_1(self):
        input_string = """
        form taxOfficeExample {
            "q1" v1: boolean = (true / 2)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)
        self.assertTrue(type_checker_result, "This cases is assumed to fail.")

    def test_typecheck_fail_division_2(self):
        input_string = """
        form taxOfficeExample {
            "q1" v1: boolean = (2 / false)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)
        self.assertTrue(type_checker_result, "This cases is assumed to fail.")

    def test_typecheck_fail_g2_1(self):
        input_string = """
        form taxOfficeExample {
            "q1" v1: boolean = (true * 2)
        }
        """
        type_checker_result = self.apply_type_checking(input_string)
        self.assertTrue(type_checker_result, "This cases is assumed to fail.")