import pytest
import unittest
import mock



def example(x):
    y = 3
    return x + y

def test_example():
    x= example()
    # test that it was actually removed
    assert example(2)==5, "Example unit test."


if __name__ == '__main__':
    test = mock.Mock()
    print(test)


