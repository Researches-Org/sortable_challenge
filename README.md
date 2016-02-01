# Sortable Challenge

Solution for the [Sortable challenge](http://sortable.com/challenge/).

## Challenge description

A file products.txt has a list of products and each product has a unique identifier (name),
a manufacturer, a model, an announced date and optionally a family.

A file listings.txt has listing objects and each listing has a title,
a manufacturer, a currency and a price.

The challenge is to match the listing objects with products, where each listing
is matched with at most one product.

## Required Software

The project uses maven 3.1.1, java version 1.8, gson-2.5.jar.

## Build

Execute the following command to generate the sortable_challenge.jar in the target folder.

<pre>
mvn clean package
</pre> 

## Execution

Execute the program using the sh file located in the project root directory (the files listings.txt, products.txt and sortable_challenge.jar must be in this directory):

<pre>
./sortable_challenge.sh
</pre>

The solution writes the file results.txt also in the current directory.

## Solution
The solution creates a matching relevance based on where the matchings occurs. 

There must be a matching between the listing manufacturer and the product manufacturer, 
if there is not this matching, so there is not a matching between the listing and product.

It also considers that if a product unique identifier (product name) is presented 
in a listing title, then it is a matching with higher precision and it will be 
presented in the result.

if there is a matching between the listing manufacturer and the product manufacturer and the 
product name is not presented in the listing title, so the model and family are considered. 

The matching relevance gives more relevance to the following matchings (from the most to the less relevant):

* Model and Family: The model and family are presented in the listing title.
* Model: The model is presented in the listing title and the product does not have family.
* Model and not Family: The model is presented in the listing title, the product has family but it is not presented in the listing title.
* Family and not Model: The family is presented in the listing title and the model is not presented in the listing title.

The algorithm chooses the top one product for each listing, if there is one, based on the rules above. It considers
that only the following matchings have high precision trying to avoid false positives: 

* Model and Family
* Model
* Model and not Family

If The Top One Product for a listing is found, the algorithm updates the result map, that is, the listing object is added to the list of listing objects associated with the result object that represents the product found. 

At the end, the file results.txt is generated with all the results objects. Only products that have been matched to some listing are presented in the results.txt file.

## Time Complexty
Considering a fixed list of products the algorithms executes in O(n) where n is the number of listing objects presented in the listings.txt file. If the number of products can increase as much as the number of listing objects, so the time complexity is O(nm), where n is the number of listing objects and m is the number of products.

## Observation
The algorithms uses the structure of the elements been searched, in this case, products, to give relevance to search terms based on their location in the
searched elements (products), for example, the highest relevance is given to the product name, since it is a unique identifier for product. The second highest
relevance is given to the product model and family. Other techniques not applied here could also be used, such as, probability and bayesian filters.   