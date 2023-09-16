package com.vishal.springmongodb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.vishal.springmongodb.modal.Book;
import com.vishal.springmongodb.modal.BookCountByAuthorName;

@Service
public class BookServiceImpl {

	@Autowired
    private MongoTemplate mongoTemplate;

	 public List<Book> searchBooks(String keyword, String author) {
	        Query query = new Query();
	        query.addCriteria(Criteria.where("bookName").regex(keyword, "i"))
	        		.addCriteria(Criteria.where("authorName").is(author));
	        return mongoTemplate.find(query, Book.class);
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
