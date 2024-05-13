package springbook.learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

	Calculator calculator;
	String numFilepath;

	@BeforeEach
	void setUp() {
		this.calculator = new Calculator();
		this.numFilepath = getClass().getResource("/numbers.txt").getPath();
	}

	@Test
	void sumOfNumbers() throws IOException {
		assertEquals(calculator.calcSum(this.numFilepath), 10);
	}

	@Test
	void multiplyOfNumbers() throws IOException {
		assertEquals(calculator.calcMultiply(this.numFilepath), 24);
	}

	@Test
	void concatenate() throws IOException {
		assertEquals(calculator.concatenate(this.numFilepath), "1234");
	}
}