package com.sortable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;

public class MainComparator {

	public static void main(String[] args) {
		Set<String> mySet = readFile("./results.txt");
		Set<String> theirSet = readFile("./their_results.txt");

		System.out.println("They have and I not:------------------");

		printDifference(theirSet, mySet);

		System.out.println("I have and they not:------------------");

		printDifference(mySet, theirSet);
	}

	private static void printDifference(Set<String> A, Set<String> B) {
		Set<String> A_Copy = new HashSet<String>(A);

		A_Copy.removeAll(B);

		int i = 1;

		Iterator<String> iterator = A_Copy.iterator();

		while (iterator.hasNext()) {
			System.out.println((i++) + " : " + iterator.next());
		}
	}

	private static Set<String> readFile(String file) {

		Scanner input = null;

		Set<String> set = new HashSet<String>();

		try {
			input = new Scanner(new File(file));

			Gson gson = new Gson();

			while (true) {
				String line = input.nextLine();

				if (!line.contains("[]")) {

					Product obj = gson.fromJson(line, Product.class);

					set.add(obj.product_name);
				}
			}

		} catch (NoSuchElementException e) {
			return set;
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + file);
			throw new RuntimeException(e);
		} finally {
			if (input != null) {
				input.close();
			}
		}

	}

}

class Product {
	String product_name;
}
