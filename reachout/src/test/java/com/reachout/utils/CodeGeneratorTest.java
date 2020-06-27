package com.reachout.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class CodeGeneratorTest {

	@Test
	void test() {
		CodeGenerator cg = new CodeGenerator();
		String code = cg.getUniqueCode();

		for (char c : code.toCharArray()) {
			String check = "" + c;
			assertTrue(cg.AB.contains(check));
		}
	}

}
