class SimpleClass:
    def __init__(self):
        pass

    def __str__(self):
        pass


class InterfaceClass:
    def print_text(self, text):
        pass


class UsingAInterface(InterfaceClass):

    def print_text(self, text):
        print(text)


class Parent:
    def __init__(self):
        self.h = 'Hello'

    def hello(self):
        print(self.h)


class Child(Parent):

    def __init__(self):
        super().__init__()
        self.w = 'World'

    def world(self):
        print(self.hello())
        print(self.w)
