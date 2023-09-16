package com.vishal.springmongodb.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vishal.springmongodb.modal.Book;

public interface BookRepository  extends MongoRepository<Book, Integer>{

}
