#!/bin/bash
set -e 
set -x
mkdir -p ../murray-river-data-downloads
cat location-download-links.txt|xargs -n 1 wget -P ../murray-river-data-downloads/
