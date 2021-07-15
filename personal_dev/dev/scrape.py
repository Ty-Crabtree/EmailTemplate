import art
import colorama
import wikipedia
import webbrowser
import requests
import bs4
import torrequest
import urllib3
from googlesearch import search
from awesome_progress_bar import ProgressBar
from vaporwavely import vaporize
from PIL import Image
from picharsso import new_drawer
import sphinx
import torch
from torchvision import models


class Scrape:
    """

    """

    def __init__(self):
        """

        """
        colorama.init()
        self.search = None
        self.html = None
        self.progress_bar = None
        self.url_list = []
        self.image_list = []
        self.scrape_folder = "scraped/"

    def header(self):
        """

        Returns
        -------

        """
        art.tprint("Scraper", font="small")

    def error_msg(self, error):
        """

        Parameters
        ----------
        error

        Returns
        -------

        """
        print(colorama.Fore.RED + str(error) + colorama.Style.RESET_ALL)

    def warning_msg(self, error):
        """

        Parameters
        ----------
        error

        Returns
        -------

        """
        print(colorama.Fore.YELLOW + str(error) + colorama.Style.RESET_ALL)

    def purple_print(self, word):
        """

        Parameters
        ----------
        word

        Returns
        -------

        """
        print(colorama.Fore.MAGENTA + str(word) + colorama.Style.RESET_ALL)

    def green_print(self, word):
        """

        Parameters
        ----------
        word

        Returns
        -------

        """
        print(colorama.Fore.GREEN + str(word) + colorama.Style.RESET_ALL)

    def web_browser(self):
        """

        Returns
        -------

        """
        try:
            print(webbrowser.open(self.search))
        except Exception as e:
            self.error_msg(e)

    def wiki_search(self):
        """

        Returns
        -------

        """
        self.purple_print("Searching Wiki's API:")
        try:
            for page in wikipedia.search(self.search):
                try:
                    search = wikipedia.page(page)
                    print("Title :" + search.title)
                    print("Summary :" + search.summary)
                    print("Image: " + str(search.images))
                    self.image_list.append(search.images)
                    print("Categories: " + str(search.categories))
                except Exception as e:
                    self.error_msg(e)
        except Exception as e:
            self.error_msg(e)

    def tor_search(self):
        """

        Returns
        -------

        """
        with torrequest.TorRequest() as tr:
            response = tr.get('https://smarsh.com')

            print(response.text)  # not your IP address
            print(response.content)  # not your IP address

    def get_html_from_url(self, url=None):
        """

        Parameters
        ----------
        url

        Returns
        -------

        """
        urllib3.disable_warnings()
        self.purple_print("Grabbing html...")
        try:
            for url in self.url_list:
                try:
                    re = requests.get(url, verify=False)
                    self.html = re.content
                    self.save_html()
                except Exception as e:
                    self.warning_msg(e)
        except Exception as e:
            self.error_msg(e)

    def save_html(self):
        """

        Returns
        -------

        """
        try:
            soup = bs4.BeautifulSoup(self.html, 'html.parser')
            self.green_print(f'Saving {soup.title.string}')
            with open(f'{self.scrape_folder}{soup.title.string}.html', 'w') as f:
                f.write(f'{soup}')
        except Exception as e:
            self.error_msg("Couldn't parse html.")
            self.error_msg(e)

    def save_images(self):
        """

        Returns
        -------

        """
        try:
            self.green_print('Saving images.')
            with open(f'{self.scrape_folder}{self.search}_images.txt', 'w') as f:
                f.write(f'{self.image_list}')
        except Exception as e:
            self.error_msg("Couldn't save images.")
            self.error_msg(e)

    def google_parse(self):
        """

        Returns
        -------

        """
        self.purple_print("Parsing web...")
        self.url_list = search(self.search)

    def neural(self):
        """

        Returns
        -------

        """
        self.progress_bar = ProgressBar(10, prefix='Loading', suffix='Output: ', bar_length=10)

    def exit(self):
        """

        Returns
        -------

        """
        mystring = "Exiting Scraper"
        drawer = new_drawer("braille", height=10, colorize=True)
        print(vaporize(mystring))
        image = Image.open("pic.png")
        print('\n' + drawer(image))
        ProgressBar.stop(self.progress_bar)
        exit(0)

    def controller(self):
        """

        Returns
        -------

        """
        self.header()
        self.search = input("Search: ")
        self.neural()
        # self.tor_search()
        self.google_parse()
        self.wiki_search()
        self.get_html_from_url()
        self.save_images()
        self.exit()


if __name__ == '__main__':
    """
    """
    scraper = Scrape()
    scraper.controller()
