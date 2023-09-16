package com.vishal.springmongodb.modal;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class BookCountByAuthorName {

	@Field("_id")
	private String authorName;
	private int count;

}
