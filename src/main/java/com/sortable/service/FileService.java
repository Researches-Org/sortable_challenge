package com.sortable.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.sortable.domain.Result;

public class FileService {

	public <T> List<T> readFile(String file, Class<T> classOfT) {

		List<T> objects = new ArrayList<T>();

		Scanner input = null;

		try {
			input = new Scanner(new File(file));

			while (true) {
				String line = input.nextLine();

				Gson gson = new Gson();
				T obj = gson.fromJson(line, classOfT);

				objects.add(obj);
			}

		} catch (NoSuchElementException e) {
			return objects;
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + file);
			throw new RuntimeException(e);
		} finally {
			if (input != null) {
				input.close();
			}
		}

	}

	public void writeFile(Result[] results) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("./results.txt"));

			Gson gson = new Gson();

			for (Result result : results) {
				bw.write(gson.toJson(result));
				bw.write("\n");
			}
			
			bw.close();

		} catch (IOException e) {
			System.err.println("I/O exception on writing file: results.txt");
			throw new RuntimeException(e);
		} 

	}

}
