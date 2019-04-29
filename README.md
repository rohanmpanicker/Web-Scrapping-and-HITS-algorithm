# Web-Scrapping-and-HITS-algorithm
In these project I've web scrapped the top 30 results of any google search and then implement the HITS algorithm on those links.


Here I've basically computed the Hub and Authority scores for all the top 30 web pages that we get from google search queries and the pages
they link to.

The 30 web pages fetched from google are called the seed set
The links that we fetch from the pages are called the base set

I've created the neighborhood graph for these pages and on that I've used the HITS algorithm. 

There are three input that the code takes : 
query: the search query that you want to search
k: this tells the program how many base set pages to include per seed set pages
N: this gives you the ability to fetch the names of the pages with the top hub and authority values. It fetches the top N hub and authority
pages


Output:
description.txt : contains the links of all seed pages and their titles
expanded.txt : contains all the links extracted from the seed pages 
graph.txt : contains the neighborhood graph
mapping.txt : contains the parent-child mapping between seed and base set pages
maxValues.txt : contains the top N seed and authority pages
output.txt : contains the hub and authority scores for all the pages
seed.txt: contains all the links of the seed set pages

I've placed some output files to be viewed.
They are generated for the following inputs
query: machine learning
k: 30
N: 10
