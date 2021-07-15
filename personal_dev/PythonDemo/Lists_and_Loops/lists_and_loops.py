"""
Basic lists and loops
"""
simple_list = []  # creating an empty list
for i in range(10):  # looping 10 times
    simple_list.append(i)  # adding the loop index (i) to the list

for i in simple_list:  # looping over the list
    print(i)  # printing what is in the list

"""
Checking whats in a list
"""
names = ['max', 'tyler', 'collen']
if 'max' in names:  # check if something is in the list
    print('Found Max')
else:
    print('No Max in the list')

empty_list = []
if empty_list:  # a list is True if there is something in it
    print("List is NOT empty")
else:
    print("List IS empty")

"""
Advanced lists and loops 
"""
one_line_list = [i for i in range(10)]  # creating a list and filling it in the same line

two_dementinal_list = [['Hello'], ['World']]  # a list of lists

for outside_list in two_dementinal_list:
    for inside_list in outside_list:
        print(inside_list)
