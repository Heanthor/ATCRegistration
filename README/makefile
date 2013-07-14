.PHONY : all clean

VPATH = 
SHELL = /bin/bash
TEXS = $(shell find . -name '*.tex')
ROOT=readme
ROOT_TEX=$(addsuffix .tex, $(ROOT))
PDF=$(addsuffix .pdf, $(ROOT))

all: one

one: $(TEXS)
	pdflatex $(ROOT_TEX)

three: $(TEXS)
	pdflatex $(ROOT_TEX)
	@if [ $$? -eq 0 ]; 	then pdflatex $(ROOT_TEX); \
													 pdflatex $(ROOT_TEX); fi # compile 2nd and 3rd time
	# note that the shell commands must all be on the same line
	# if you do not include the @ symbol, make prints the statment

clean: 
	rm -f $(ROOT).{aux,log,out,toc}

realclean:
	rm -f $(ROOT).{pdf,aux,log,out,toc}
