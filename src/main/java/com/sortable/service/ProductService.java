package com.sortable.service;

import java.util.List;

import com.sortable.domain.Product;

public class ProductService {

	private FileService fileService;

	public ProductService(FileService fileService) {
		this.fileService = fileService;
	}

	public Product[] getProducts() {
		List<Product> products = fileService.readFile("./products.txt",
				Product.class);
		return products.toArray(new Product[] {});
	}

}
