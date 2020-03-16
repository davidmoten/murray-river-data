#!/bin/bash
set -e 
TO=../murray-river-data-downloads
mkdir -p $TO 
cat location-download-links.txt|xargs -n 1 wget -P $TO/
