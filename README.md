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

## Solution: Matching Relevance and Index Creation
The solution uses a matching relevance based on where the matchings occurs. So it uses the structure of the elements being searched. The matching relevance gives more relevance to the following matchings (from the most to the less relevant):

* Model and Family: The model and family are found in the listing title.
* Model: The model is found in the listing title and the product does not have family.
* Model and not Family: The model is found in the listing title, the product has family but it is not found in the listing title.


There must be a matching between the listing manufacturer and the product manufacturer, if there is not this matching, so, there will not be a matching between the listing and product.

If there is a matching between the listing manufacturer and the product manufacturer, so, the model and family are considered using the matching relevance rules described above. 

The solution also creates an index of terms found in the titles of the listing objects. So it uses an off-line technique because it already has access to the
data sets of listings and products objects.

The index of terms is a map object that maps terms found in the titles to listing objects. Each listing object has a method that returns the title string in lower case with only digits, letters and spaces. It uses regex for this purpose.

<pre>
public String getTitleLower() {
	return title.toLowerCase().replaceAll("[^a-z0-9]", " ");
}
</pre> 

The creation of the index splits the titles of the listings using space as terms separator and creates a map form terms to listings objects that contain each term.

To match the products to listings, the algorithm does the following steps for each product respecting the order of matching relevance described above:

* Try to find listings that match with model and family of the product at the same time when the product has family specified.
* Try to find listings that match with model when the product does not have family specified.
* Try to find listings that match with model but do not match with family when the product has family specified.

During this process, when a listing was already matched to an other product, the tie resolution does the following:

* If the previous matched product was matched with a lower matching relevance than the current product, so the listing is matched to the current product and the matching with the previous one is removed.
* If the previous matched product was matched with a higher matching relevance than the current product, so this matching is preserved and the current product is not matched with this specific listing. 
* If the previous matched product was matched with the same matching relevance as the current product, so the product with longer model length is matched to the specific listing.

Each product object has attributes that store model and family strings in lower case with only digits, letters and spaces, as it was done with the listing title.

<pre>
this.modelLower = this.model.toLowerCase().replaceAll("[^a-z0-9]", " ");
this.familyLower = this.family != null ? this.family.toLowerCase().replaceAll("[^a-z0-9]", " ") : null;
</pre>

When trying to find possible listing objects that match to a specific product, the algorithm does the following steps:

* Find listings that have the exact product term (mode/family).
* Find listings that have the product term (model/family) without spaces.
* Find listings that have all the terms of the product term (model/family), when the product term has spaces in it.

At the end, the file results.txt is generated with all the results objects. Only products that have been matched to some listing are presented in the results.txt file.

## Time Complexity
To create the index, the time complexity is O(n), where n is the number of listing objects.
To match the products to listing objects, the worst case time complexity is O(nm), where n is the number of listing objects and m is the number of products.
So, the worst case time complexity of the algorithm is O(nm). This happens when all products match to all listing objects. However, the probability for the worst case occurs is really small and an average case analysis is necessary.

To note that the probability of the worst case is really small, consider the following reasoning: 

* The greater the number of different terms in one title listing, the greater the likelihood that this listing corresponds to all products.
* Consider t the number of all possible terms to be used in a title listing, off course it is a big number. 
* Consider x the number of terms in a listing title, off course x is much more smaller than t.
* The probability that one of these x terms be a product term (model or family) is x/t, each is a small number.
* The probability that a listing matches all products is the probability that it matches the first product and the second and the third, and so on. So it is x/t to the m power where m is the number of products, which is even smaller than x/t.
* Finally, the probability that all listings match all products is the probability that the first listing matches all products, the second listing matches all products, and so on.  So it is x/t to the n*m power, a really small number, where n is the number of listings and m the number of products.

So in the average case, the time complexity of the algorithm is near to O(n + m).

## Observation
The algorithms uses the structure of the elements been searched, in this case, products, to give relevance to search terms based on their location in the
searched elements (products), for example, The highest relevance is given when the product model and family are found in a listing title. Other more complex techniques not applied here could also be used, such as, probability and bayesian filters.