package com.vishal.springmongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vishal.springmongodb.modal.Book;
import com.vishal.springmongodb.modal.BookCountByAuthorName;
import com.vishal.springmongodb.repo.BookRepository;
import com.vishal.springmongodb.service.BookServiceImpl;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CacheConfig(cacheNames = "book")
public class BookController {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookServiceImpl bookServiceImpl;

	@PostMapping("/addBook")
	public Book saveBook(@RequestBody Book book) {
		return bookRepository.save(book);
	}

	@GetMapping("/allBooks")
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@GetMapping("/byId/{id}")
	@Cacheable(key = "#id")
	public Book getBookById(@PathVariable Integer id) {
		log.info("testing log");
		return bookRepository.findById(id).orElse(null);

	}

	@PutMapping("/byId/{id}")
	@CachePut(key = "#id")
	public Book updateTheBook(@PathVariable Integer id, @RequestBody Book book) {
		Book oldBookDetails = bookRepository.findById(id).get();
		oldBookDetails.setAuthorName(book.getAuthorName());
		oldBookDetails.setBookName(book.getBookName());
		oldBookDetails.setId(book.getId());
		return bookRepository.save(oldBookDetails);

	}

	@DeleteMapping("/delete/{id}")
	@CacheEvict(key = "#id")
	public String deleteById(@PathVariable Integer id) {
		bookRepository.deleteById(id);
		return "Deleted Successfully";
	}

	@GetMapping("/byName/{name}/{author}")
	public List<Book> getBookById(@PathVariable(name = "name") String name,
			@PathVariable(name = "author") String author) {
		return bookServiceImpl.searchBooks(name, author);

	}

	@GetMapping("/byAuthorName")
	public List<BookCountByAuthorName> getBookById() {
		return bookServiceImpl.bookByAuthorName();

	}

	@PostMapping("/aggregate")
	public List<Book> saveBook(@RequestBody List<Book> books) {
		return bookServiceImpl.aggregatebooks(books);
	}
}
