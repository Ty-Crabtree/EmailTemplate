'''
This demo file shows:

1. How to get user input.
2. Compares similarities of strings (words) and assings a numeric value
3. Usage of external (pip) installs

'''

from fuzzywuzzy import fuzz  # Pip install: pip install fuzzywuzzy
from simple_colors import *  # Pip install: pip install simple-colors


def get_color( ratio):
    ratio = int(ratio)
    if ratio > 90:
        print(green(f"Similartiy ratio: {ratio}"))
    elif ratio > 60:
        print(yellow(f"Similartiy ratio: {ratio}"))
    else:
        print(red(f"Similartiy ratio: {ratio}"))


def string_similarty(string_a, string_b):
    ratio = fuzz.ratio(string_a, string_b)
    get_color(ratio)
    return ratio


def sort_and_compare_string_similarty(string_a, string_b):
    ratio = fuzz.token_sort_ratio(string_a, string_b)
    get_color(ratio)
    return ratio


if __name__ == '__main__':
    a = input("Enter input A:")
    b = input("Enter input B:")
    string_similarity = string_similarty(a, b)
    sorted_string_similarity = sort_and_compare_string_similarty(a, b)
