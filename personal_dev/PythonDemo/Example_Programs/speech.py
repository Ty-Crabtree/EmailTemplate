# Example program that uses a class

'''

#  Three types of imports
# Type 1: Internal (comes from Python)
# Type 2: External (generally comes from pip)
# Type 3: Local Files (usually created by yourself/fellow developer)

'''

# Type 1: Internal libary (Python comes with a math library!)
import math

# Type 2: Pip installs
from emoji import emojize  # External pip library, run 'pip install emoji'. Link: https://pypi.org/project/emoji/
from macos_speech import Synthesizer  # Run 'pip install macos-speech'. Link: https://pypi.org/project/macos-speech/
from simple_colors import *  # Run 'pip install macos-speech'. Link: https://pypi.org/project/macos-speech/

# Type 3: Local file with imported  function (since string_match.py is in the same directory we can import it!)
from string_match import string_similarty


class Speech:
    """
    Example class, named "Speech":

    A class groups similar functions.
    In this case, every function utilizes a speech module.

    """

    def __init__(self):  # Functions in classes always take a parameter called 'self'
        """
        '__init__' functions automatically run when a class is created.
        These functions create variables that any other function in this class can use
        This is helpful if there is a variable every function uses, we can define that here!
        """
        self.speaker = Synthesizer(voice='Alex')  # Example class variable (every class variable can access this)

    def speak_with_color_and_ratio(self,ratio):  # Use self since this is a class function, then we pass in other vars
        """
        This function takes the parameter ratio (the parameter 'self' simply indicates this is a class function)

        Parameters
        ----------
        ratio: A numerical value based on string similarity. Neat!
        -------
        """
        ratio = math.floor(ratio)  # example use of internal library
        ratio = int(ratio)  # This is shows something called 'casting'.
        '''
        Python is dynamically typed (Python essentially guesses a variable's type such as int, string, etc) 
        Python's internal guesses isn't always sufficient, or sometimes a developer wants to change a type explicitly  
        That is when we can use casting! 
        
        The parameter ratio was originally a data type called float -> 0.0
        A float is basically a number with a decimal
        The line 'int(ratio)' changes the type from a float (0.0) to an integer (number without decimal: 0)
        
        This simplifies the next section
        '''
        if ratio > 90:  # Simple mathematical check
            print(green(f"Similartiy ratio: {ratio}"))  # applies color to output and prints ratio
        elif ratio > 60:  # Simple mathematical check
            print(yellow(f"Similartiy ratio: {ratio}"))  # applies color to output and prints ratio
        else:  # Simple mathematical check (implied)
            print(red(f"Similartiy ratio: {ratio}"))  # applies color to output and prints ratio

        # The line below uses 'self.speaker' this is a class variable (defined in the '__init__' function)
        self.speaker.say(f"Similartiy ratio: {ratio}")  # Uses speech module, neat!

    def speak(self, words):
        """
        Parameters
        ----------
        words: a string that is used in the speech module
        -------
        """
        self.speaker.say(words)  # Uses speech module, neat!


if __name__ == '__main__':
    print(magenta("Starting speech program. " + emojize(":thumbs_up:")))  # prints using two different modules
    """
    The line below creates an object! This is when the '__init__' function is ran
    The object then can use any function defined inside the class
    """
    speaker = Speech()  # Creates an object officially!
    user_input_1 = input("Enter a word :")
    user_input_2 = input("Enter a second word :")
    speaker.speak(f"User entered: {user_input_1}")  # Repeats user's first input
    speaker.speak(f"User also entered:  {user_input_2}")  # Repeats user's second input
    ratio = string_similarty(user_input_1, user_input_2)  # Get a numerical
    speaker.speak_with_color_and_ratio(ratio)
