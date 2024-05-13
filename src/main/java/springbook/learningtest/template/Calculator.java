package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

	public Integer calcSum(String filepath) throws IOException {
		LineCallback<Integer> callback = new LineCallback<Integer>() {
			@Override
			public Integer doSomethingWithLine(final String line, final Integer value) {
				return value + Integer.valueOf(line);
			}
		};
		return lineReadTempalte(filepath, callback, 0);
	}

	public Integer calcMultiply(String filepath) throws IOException {
		LineCallback<Integer> callback = new LineCallback<Integer>() {
			@Override
			public Integer doSomethingWithLine(final String line, final Integer value) {
				return value * Integer.valueOf(line);
			}
		};
		return lineReadTempalte(filepath, callback, 1);
	}

	public String concatenate(String filepath) throws IOException {
		LineCallback<String> callback = new LineCallback<String>() {
			@Override
			public String doSomethingWithLine(final String line, final String value) {
				return value + line;
			}
		};
		return lineReadTempalte(filepath, callback, "");
	}

	public <T> T lineReadTempalte(String filepath, LineCallback<T> callback, T initVal) throws IOException {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(filepath));
			T res = initVal;
			String line = null;
			while ((line = br.readLine()) != null) {
				res = callback.doSomethingWithLine(line, res);
			}
			return res;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
}
