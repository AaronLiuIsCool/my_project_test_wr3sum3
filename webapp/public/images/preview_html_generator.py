#this script allows us to genrate a preview.html webpage to easily preview all svg images

import cgi
import os


def is_svg_file(filename):
  return filename.endswith(".svg") #update this!

def createHTML():
  videoDirectory = os.listdir("./")
  with open("preview.html", "w") as f:
    f.write("<html><body><ul>\n")
    for filename in videoDirectory:
      if is_svg_file(filename):
        f.write('<div><img src="%s" /> <p>%s</p></div>' %
                (cgi.escape(filename, True), cgi.escape(filename)))
    f.write("</body></html>\n")


createHTML()
