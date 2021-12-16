package com.davsantos.bookservice.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.davsantos.bookservice.model.Book;
import com.davsantos.bookservice.proxy.CambioProxy;
import com.davsantos.bookservice.repository.BookRepository;
import com.davsantos.bookservice.response.Cambio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Book endpoint")
@RestController
@RequestMapping(value = "book-service")
public class BookController {

	@Autowired
	private Environment env;

	@Autowired
	private BookRepository repository;

	@Autowired
	private CambioProxy cambioProxy;

//	@GetMapping(value = "/{id}/{currency}")
//	public Book getBook(@PathVariable Long id, @PathVariable String currency) {
//
//		Book book = repository.getById(id);
//
//		if (book == null) {
//			throw new RuntimeException("Book not found");
//		}
//
//		HashMap<String, String> params = new HashMap<>();
//		params.put("amount", book.getPrice().toString());
//		params.put("from", "USD");
//		params.put("to", currency);
//
//		ResponseEntity<Cambio> response = new RestTemplate().getForEntity("http://localhost:8000/cambio-service/{amount}/{from}/{to}", Cambio.class, params);
//
//		Cambio cambio = response.getBody();
//
//		String port = env.getProperty("local.server.port");
//		book.setEnvironment(port);
//		book.setPrice(cambio.getConvertedValue());
//
//		return book;
//	}

	@Operation(summary = "Find specific book by yoour id")
	@GetMapping(value = "/{id}/{currency}")
	public Book getBook(@PathVariable Long id, @PathVariable String currency) {

		Book book = repository.getById(id);

		if (book == null) {
			throw new RuntimeException("Book not found");
		}

		Cambio cambio = cambioProxy.getCambio(book.getPrice(), "USD", currency);

		String port = env.getProperty("local.server.port");
		book.setEnvironment(port + "FEIGN");
		book.setPrice(cambio.getConvertedValue());

		return book;
	}
}
