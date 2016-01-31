package com.sortable.service;

import java.util.List;

import com.sortable.domain.Product;
import com.sortable.domain.ProductJsonDeserializer;

public class ProductService {

	private FileService fileService;

	public ProductService(FileService fileService) {
		this.fileService = fileService;
	}

	public Product[] getProducts() {
		List<Product> products = fileService.readFile("./products.txt",
				Product.class, new ProductJsonDeserializer());
		return products.toArray(new Product[] {});
	}

}
