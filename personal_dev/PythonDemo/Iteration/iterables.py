"""
In python all collections are iterable and if set objects are too

A iterable item is something that can be looped over
"""

a_list = [1, 2, 3, 4, 5]
for i in a_list:
    print(i)  # will loop (iterate) every item in the list

"""
Generators 

A generator is just a iterable item that you don't/can't know the length or value type of 

A generator will ALWAYS throw a StopIteration Exception when it finishes 

You can only get the value from a generator using a loop or the function next()
"""

inline_generator = (i for i in range(4))
print(inline_generator)
print(next(inline_generator))
print(next(inline_generator))
print(next(inline_generator))
print(next(inline_generator))
print(next(inline_generator))  # this will throw an error


def simple_gen(num):
    for it in range(num):
        yield it


def _simple_gen():
    for item in simple_gen(10):
        print(item)
