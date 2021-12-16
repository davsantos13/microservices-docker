package com.davsantos.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.davsantos.bookservice.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
