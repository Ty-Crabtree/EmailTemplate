from fuzzywuzzy import fuzz
from fuzzywuzzy import process
from simple_colors import *

class String_Ratio:

    def __init__(self):
        pass

    def get_color(self, ratio):
        ratio = int(ratio)
        if ratio > 90:
            print(green(f"Similartiy ratio: {ratio}"))
        elif ratio > 60:
            print(yellow(f"Similartiy ratio: {ratio}"))
        else:
            print(red(f"Similartiy ratio: {ratio}"))

    def string_similarty(self, string_a, string_b):
        ratio = fuzz.ratio(string_a, string_b)
        self.get_color(ratio)
        return ratio


    def sort_and_compare_string_similarty(self, string_a, string_b):
        ratio = fuzz.token_sort_ratio(string_a, string_b)
        self.get_color(ratio)
        return ratio


if __name__ == '__main__':
    string_similarity_obj = String_Ratio()
    a = input("Enter input A:")
    b = input("Enter input B:")
    string_similarity = string_similarity_obj.string_similarty(a,b)
    sorted_string_similarity = string_similarity_obj.sort_and_compare_string_similarty(a,b)
