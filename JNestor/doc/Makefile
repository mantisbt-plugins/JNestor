#
# Makefile
#

my_book = jnestor-guide

# options of the different programs
xsltproc_options = --nonet --xinclude
fop = fop


.PHONY: book
book: html html_single text

.PHONY: html_single
html_single:
	# make html_single directory if it don't exist
	if [ -d html_single ] ; then true; else mkdir html_single; fi; 
	# copy the content of the gfx and images directory
	# but not the .cvs or .svn directories
	rsync -rlptg --copy-dirlinks --exclude=.* gfx/ html_single/gfx
	rsync -rlptg --copy-dirlinks --exclude=.* images/ html_single/images
	cp docbook.css html_single
	xsltproc $(xsltproc_options) --output "html_single/$(my_book).html" xsl/html_single.xsl "$(my_book).xml"
 
.PHONY: html
html:
	# make html directory if it don't exist
	if [ -d html ] ; then true; else mkdir html; fi; 
	# copy the content of the gfx and images directory
	rsync -rlptg --copy-dirlinks --exclude=.* gfx/ html/gfx
	rsync -rlptg --copy-dirlinks --exclude=.* images/ html/images
	cp docbook.css html
	xsltproc $(xsltproc_options) --output "html/$(my_book).html" xsl/html.xsl "$(my_book).xml"
 
.PHONY: pdf
pdf:
	# make pdf directory if it don't exist
	if [ -d pdf ] ; then true; else mkdir pdf; fi; 
	xsltproc $(xsltproc_options) --output "pdf/$(my_book).fo" xsl/fo.xsl "$(my_book).xml"
	$(fop) "pdf/$(my_book).fo" -pdf "pdf/$(my_book).pdf"

.PHONY: text
text:
	# make html directory if it don't exist
	if [ -d text ] ; then true; else mkdir text; fi; 
	lynx -dump html_single/$(my_book).html > text/$(my_book).txt 
						
.PHONY: publish
publish:
	rsync -rlptg --copy-dirlinks --exclude=.* html_single/ ../webContent/doc/html_single/
	rsync -rlptg --copy-dirlinks --exclude=.* html/ ../webContent/doc/html/
	-zip -r ../webContent/doc/html_single.zip html_single/
	-zip -r ../webContent/doc/html.zip html/

clean:
	# to ignore errors, start the command with a hyphen
	-rm -f html/*
	-rm -f html_single/*
	-rm -f text/*
	-rm -f pdf/*

