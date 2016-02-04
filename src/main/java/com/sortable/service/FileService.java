package com.sortable.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.sortable.domain.Result;

public class FileService {

	public <T> List<T> readFile(String file, Class<T> classOfT) {
		return readFile(file, classOfT, new Gson());
	}

	public <T> List<T> readFile(String file, Class<T> classOfT,
			JsonDeserializer<T> jsonDeserializer) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(classOfT, jsonDeserializer);
		return readFile(file, classOfT, gsonBuilder.create());
	}

	private <T> List<T> readFile(String file, Class<T> classOfT, Gson gson) {

		List<T> objects = new ArrayList<T>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));

			String line = null;

			while ((line = in.readLine()) != null) {

				T obj = gson.fromJson(line, classOfT);

				objects.add(obj);
			}

			in.close();

		} catch (IOException e) {
			System.err.println("I/O excpetion reading file: " + file);
			throw new RuntimeException(e);
		}
		
		return objects;

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
