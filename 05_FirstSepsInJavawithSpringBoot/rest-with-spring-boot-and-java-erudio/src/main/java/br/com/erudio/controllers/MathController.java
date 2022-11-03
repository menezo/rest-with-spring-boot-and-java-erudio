package br.com.erudio.controllers;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.services.Calculator;

@RestController
public class MathController {

	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/sum/{numberOne}/{numberTwo}", method = RequestMethod.GET)
	public BigDecimal sum(@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception {
		return new Calculator(numberOne, numberTwo).sum();
	}

	@RequestMapping(value = "/subtract/{numberOne}/{numberTwo}", method = RequestMethod.GET)
	public BigDecimal subtract(@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception {
		return new Calculator(numberOne, numberTwo).subtract();
	}

	@RequestMapping(value = "/multiply/{numberOne}/{numberTwo}", method = RequestMethod.GET)
	public BigDecimal multiply(@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception {
		return new Calculator(numberOne, numberTwo).multiply();
	}

	@RequestMapping(value = "/divide/{numberOne}/{numberTwo}", method = RequestMethod.GET)
	public BigDecimal divide(@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception {
		return new Calculator(numberOne, numberTwo).divide();
	}

	@RequestMapping(value = "/average/{numberOne}/{numberTwo}", method = RequestMethod.GET)
	public BigDecimal average(@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception {
		return new Calculator(numberOne, numberTwo).average();
	}

	@RequestMapping(value = "/sqrt/{number}", method = RequestMethod.GET)
	public BigDecimal sqrt(@PathVariable(value = "number") String number) throws Exception {
		return new Calculator(number).sqrt();
	}
}