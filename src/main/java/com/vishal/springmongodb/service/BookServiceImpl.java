package com.vishal.springmongodb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.vishal.springmongodb.controller.BookController;
import com.vishal.springmongodb.modal.Book;
import com.vishal.springmongodb.modal.BookCountByAuthorName;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BookServiceImpl {

	@Autowired
    private MongoTemplate mongoTemplate;

	 public List<Book> searchBooks(String keyword, String author) {
//	        Query query = new Query();
//	        query.addCriteria(Criteria.where("bookName").regex(keyword, "i"))
//	        		.addCriteria(Criteria.where("authorName").is(author));
	        
	        AggregationOperation match = Aggregation.match(
	                new Criteria().orOperator(
	                    new Criteria().andOperator(
	                        Criteria.where("bookName").is("Two States"),
	                        Criteria.where("authorName").is("chetha")
	                        // Add more AND conditions here as needed
	                    ),
	                    new Criteria().andOperator(
	                    		Criteria.where("bookName").is("Love"),
		                        Criteria.where("authorName").is("vishal")
	                        // Add more AND conditions here as needed
	                    ),
	                    new Criteria().andOperator(
	                    		Criteria.where("bookName").is("gayathri"),
		                        Criteria.where("authorName").is("vishal")
	                        // Add more AND conditions here as needed
	                    )
	                )
	            );
	        
	        Aggregation aggregation = Aggregation.newAggregation(match);
	        List<Book> books = mongoTemplate.aggregate(aggregation, "Book", Book.class).getMappedResults();
	        log.info("books: {} ", books);
	        return books;
	        
//	        return mongoTemplate.find(query, Book.class);
	    }
	 
	 
	 public List<Book> aggregatebooks(List<Book> bookCriteriaList) {
		 
	     List<Criteria> orOperations = new ArrayList<>();
	        for (Book bookCriteria : bookCriteriaList) {
	            Criteria criteria = new Criteria().andOperator(
	            		Criteria.where("bookName").is(bookCriteria.getBookName()),
                        Criteria.where("authorName").is(bookCriteria.getAuthorName())
	                // Add more AND conditions here as needed based on your Book class
	            );

	            orOperations.add(criteria);
	        }
	        Criteria[] criteriaArray = orOperations.toArray(new Criteria[0]);

	        Aggregation aggregation = Aggregation.newAggregation(
	            Aggregation.match(new Criteria().orOperator(criteriaArray))
	        );
	        List<Book> books = mongoTemplate.aggregate(aggregation, "Book", Book.class).getMappedResults();

	        log.info("books: {} ", books);
	        return books;
	        
	    }

	    // Example of using getDocumentBasedQuery for custom queries
	    public List<Book> customQuery(String customField, String customValue) {
	        Query query = new Query(Criteria.where(customField).is(customValue));
	        return mongoTemplate.find(query, Book.class);
	    }
	    
	    // Example of using aggregation to get task count by title
	    public List<BookCountByAuthorName> bookByAuthorName() {
	    	GroupOperation groupOperation = Aggregation.group("authorName").count().as("count");
	        Aggregation aggregation = Aggregation.newAggregation(groupOperation);
	        AggregationResults<BookCountByAuthorName> result = mongoTemplate.aggregate(aggregation, "Book", BookCountByAuthorName.class);
	        return result.getMappedResults();
	    }
}
