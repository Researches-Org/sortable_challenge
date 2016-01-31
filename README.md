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

## Execution

Execute the program using the sh file:

<pre>
./sortable_challenge.sh
</pre>

The files listings.txt and products.txt must be in the current directory.
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
that the following matchings have high precision: 

* Model and Family
* Model
* Model and not Family