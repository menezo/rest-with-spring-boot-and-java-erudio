package br.com.erudio.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import br.com.erudio.exceptions.UnsupportedMathOperationException;

public class Calculator {

	private BigDecimal numberOne;
	private BigDecimal numberTwo;

	public Calculator(String numberOne) {
		numberOne = numberOne.replaceAll(",", ".");
		this.numberOne = new BigDecimal(numberOne);
	}

	public Calculator(String numberOne, String numberTwo) {

		numberOne = numberOne.replaceAll(",", ".");
		numberTwo = numberTwo.replaceAll(",", ".");

		if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		} else {
			this.numberOne = new BigDecimal(numberOne);
			this.numberTwo = new BigDecimal(numberTwo);
		}
	}

	public BigDecimal sum() {
		return numberOne.add(numberTwo);
	}

	public BigDecimal subtract() {
		return numberOne.subtract(numberTwo);
	}

	public BigDecimal multiply() {
		return numberOne.multiply(numberTwo);
	}

	public BigDecimal divide() {
		return numberOne.divide(numberTwo, 5, RoundingMode.HALF_DOWN);
	}

	public BigDecimal average() {
		return sum().divide(new BigDecimal("2.0"));
	}

	public BigDecimal sqrt() {
		MathContext mc = new MathContext(5);
		return numberOne.sqrt(mc);
	}

	private boolean isNumeric(String number) {

		if (number == null) {
			return false;
		} else {
			return number.matches("[-+]?[0-9]*\\.?[0-9]+");
		}
	}
}
