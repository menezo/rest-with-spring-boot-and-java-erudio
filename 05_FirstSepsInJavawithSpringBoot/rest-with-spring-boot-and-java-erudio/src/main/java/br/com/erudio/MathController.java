package br.com.erudio;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.exceptions.UnsupportedMathOperationException;

@RestController
public class MathController {

	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/sum/{numberOne}/{numberTwo}",
			method = RequestMethod.GET)
	public BigDecimal sum(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo)
		throws Exception
	{
		numberOne = numberOne.replaceAll(",", ".");
		numberTwo = numberTwo.replaceAll(",", ".");
		
		if(!isNumeric(numberOne) || !isNumeric(numberTwo))  
		{
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}
		else {
			return new BigDecimal(numberOne).add(new BigDecimal(numberTwo));
		}
	}
	
	@RequestMapping(value = "/subtract/{numberOne}/{numberTwo}",
			method = RequestMethod.GET)
	public BigDecimal subtract(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) {
		
		numberOne = numberOne.replaceAll(",", ".");
		numberTwo = numberTwo.replaceAll(",", ".");
		
		if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}
		else {
			return new BigDecimal(numberOne).subtract(new BigDecimal(numberTwo));
		}
	}
	
	@RequestMapping(value = "/multiply/{numberOne}/{numberTwo}",
			method = RequestMethod.GET)
	public BigDecimal multiply(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) {
		
		numberOne = numberOne.replaceAll(",", ".");
		numberTwo = numberTwo.replaceAll(",", ".");
		
		if(!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}
		else {
			return new BigDecimal(numberOne).multiply(new BigDecimal(numberTwo));
		}
	}
	
	@RequestMapping(value = "/divide/{numberOne}/{numberTwo}",
			method = RequestMethod.GET)
	public BigDecimal divide (
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) {
		
		numberOne = numberOne.replaceAll(",", ".");
		numberTwo = numberTwo.replaceAll(",", ".");
		
		if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}
		else {
			return new BigDecimal(numberOne).divide(new BigDecimal(numberTwo), 5, RoundingMode.HALF_DOWN);
		}
	}
	
	@RequestMapping(value = "/avarage/{numberOne}/{numberTwo}",
			method = RequestMethod.GET)
	public BigDecimal avarage(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) {
		
		numberOne = numberOne.replaceAll(",", ".");
		numberTwo = numberTwo.replaceAll(",", ".");
		
		if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}
		else {
			return new BigDecimal(numberOne).add(new BigDecimal(numberTwo)).divide(new BigDecimal("2.0"));
		}
		
	}
	
	@RequestMapping(value = "/sqrt/{number}",
			method = RequestMethod.GET)
	public BigDecimal sqrt(
			@PathVariable(value = "number") String number) {
		
		number = number.replaceAll(",", ".");

		if(!isNumeric(number)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}
		else {
			MathContext mc = new MathContext(5);
			return new BigDecimal(number).sqrt(mc);
		}
	}

	private boolean isNumeric(String number) {

		if(number == null) {
			return false;
		}
		else {
			return number.matches("[-+]?[0-9]*\\.?[0-9]+");
		}
	}
}