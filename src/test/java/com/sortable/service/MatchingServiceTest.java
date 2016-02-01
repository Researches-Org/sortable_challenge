package com.sortable.service;

import java.util.Arrays;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MatchingServiceTest {

	private ProductService productService;
	
	private ListingService listingService;
	
	private FileService fileService;
	
	private MatchingService matchingService;
	
	@BeforeClass
	public void setUp() {
		fileService = new FileService();
		productService = new ProductService(fileService);
		listingService = new ListingService(fileService);
		matchingService = new MatchingService(productService, listingService);
	}
	
	@Test(enabled = false)
	public void match() {
		fileService.writeFile(matchingService.match());
	}

	@Test
	public void matchUsingIndex() {
		fileService.writeFile(matchingService.matchUsingIndex());
	}
	
	@Test(enabled = false)
	public void testRegex() {//"\\s|\\\\|/|\\(|\\)|\\[|\\]|\\{|\\}|\\,|\\;|\\:"
		System.out.println(Arrays.toString("a b c d e".split("\\s")));
		System.out.println(Arrays.toString("a\\b\\c\\d\\e".split("\\\\")));
		System.out.println(Arrays.toString("a/b/c/d/e".split("/")));
		System.out.println(Arrays.toString("a(b(c(d(e".split("\\(")));
		System.out.println(Arrays.toString("a)b)c)d)e".split("\\)")));
		System.out.println(Arrays.toString("a[b[c[d[e".split("\\[")));
		System.out.println(Arrays.toString("a]b]c]d]e".split("\\]")));
		System.out.println(Arrays.toString("a{b{c{d{e".split("\\{")));
		System.out.println(Arrays.toString("a}b}c}d}e".split("\\}")));
		System.out.println(Arrays.toString("a,b,c,d,e".split("\\,")));
		System.out.println(Arrays.toString("a;b;c;d;e".split("\\;")));
		System.out.println(Arrays.toString("a:b:c:d:e".split("\\:")));
		System.out.println(Arrays.toString("a-b-c-d-e".split("\\-")));
		System.out.println(Arrays.toString("a_b_c_d_e".split("\\_")));
		System.out.println(Arrays.toString("a.b.c.d.e".split("\\.")));
		System.out.println(Arrays.toString("a b c d e a\\b\\c\\d\\e\\a/b/c/d/e/a(b(c(d(e(a)b)c)d)e)a[b[c[d[e[a]b]c]d]e]a{b{c{d{e{a}b}c}d}e}a,b,c,d,e,a;b;c;d;e;a:b:c:d:e:a-b-c-d-e-a_b_c_d_e".split("\\s|\\\\|/|\\(|\\)|\\[|\\]|\\{|\\}|\\,|\\;|\\:|\\-|\\_")));
		
	}
}     