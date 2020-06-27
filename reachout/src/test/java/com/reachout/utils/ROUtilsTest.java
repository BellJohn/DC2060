package com.reachout.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class ROUtilsTest {

	@Test
	void testIsNumeric() {
		assertFalse(ROUtils.isNumericInt("abc"));
		assertFalse(ROUtils.isNumericInt(""));
		assertFalse(ROUtils.isNumericInt(null));
		assertFalse(ROUtils.isNumericInt("abc123"));
		assertFalse(ROUtils.isNumericInt("123abc"));
		assertFalse(ROUtils.isNumericInt("123.456"));
		assertTrue(ROUtils.isNumericInt("1"));
		assertTrue(ROUtils.isNumericInt("123"));
	}

	@Test
	void testGetPictureExtension() {
		MultipartFile file = new MockMultipartFile("data", "filename.png", "text/plain", "some xml".getBytes());
		assertEquals(".png", ROUtils.getPictureExtension(file));
	}

	@Test
	void testIsValidPic() {
		MultipartFile file = new MockMultipartFile("data", "filename.png", "text/plain", "some xml".getBytes());
		// Small generic starting file
		assertTrue(ROUtils.validPic(file));
		Random rnd = new Random();
		byte[] tenMb = new byte[10000 * 1024];
		rnd.nextBytes(tenMb);

		// More than 10mb
		file = new MockMultipartFile("data", "filename.png", "text/plain", tenMb);
		assertFalse(ROUtils.validPic(file));

		// Less than 1-mb
		tenMb = new byte[5000 * 1024];
		rnd.nextBytes(tenMb);
		file = new MockMultipartFile("data", "filename.png", "text/plain", tenMb);
		assertTrue(ROUtils.validPic(file));

		// Bad extension
		file = new MockMultipartFile("data", "filename.txt", "text/plain", tenMb);
		assertFalse(ROUtils.validPic(file));

	}
}
