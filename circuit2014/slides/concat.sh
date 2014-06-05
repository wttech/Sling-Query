#!/bin/bash
for x in `cut -d'"' -f2 list.json | grep '.md'`; do cat $x; echo; done | multimarkdown > slides.html
