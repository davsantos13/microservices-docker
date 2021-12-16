package com.davsantos.cambioservice.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davsantos.cambioservice.model.Cambio;
import com.davsantos.cambioservice.repository.CambioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cambio Service API")
@RestController
@RequestMapping(value = "cambio-service")
public class CambioController {

	@Autowired
	private Environment env;

	@Autowired
	private CambioRepository repository;

	@Operation(summary = "Get cambio from currency")
	@GetMapping(value = "/{amount}/{from}/{to}")
	public Cambio getCambio(@PathVariable Double amount, @PathVariable String from, @PathVariable String to) {
		Cambio cambio = repository.findByFromAndTo(from, to);

		if (cambio == null) {
			throw new RuntimeException("Currency Unsupported");
		}

		String port = env.getProperty("local.server.port");
		cambio.setEnvironment(port);
		Double conversionFactor = cambio.getConversionFactor();
		Double convertedValue = conversionFactor * amount;
		cambio.setConvertedValue(BigDecimal.valueOf(convertedValue).setScale(2, RoundingMode.CEILING).doubleValue());

		return cambio;
	}
}
