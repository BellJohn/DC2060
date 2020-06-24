/**
 * 
 */
package com.reachout.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.reachout.processors.SystemPropertiesService;

/**
 * @author John
 *
 */
public class ROUtils {
	private static final long TEN_MB_AS_BYTES = 10000000L;
	private static final Logger logger = LogManager.getLogger(ROUtils.class);

	/**
	 * Private constructor as all methods are statics
	 */
	private ROUtils() {
	}

	/**
	 * Test to see if the string passed is actually an integer representation
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNumericInt(String val) {
		try {
			Integer.parseInt(val);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String getPictureExtension(MultipartFile profilePic) {
		return profilePic.getOriginalFilename().substring(profilePic.getOriginalFilename().lastIndexOf('.'))
				.toLowerCase();
	}

	public static boolean validPic(MultipartFile pic) {
		// determine picture file extension
		boolean validPic = true;
		if (pic.getSize() > TEN_MB_AS_BYTES) {
			logger.info(String.format("Attempted image upload with file size in excess of 10MB: {%s}",
					pic.getSize()));
			validPic = false;
		}

		String extension = getPictureExtension(pic);
		if (!extension.equalsIgnoreCase(".png") && !extension.equalsIgnoreCase(".jpg")
				&& !extension.equalsIgnoreCase(".jfif")) {
			logger.info(String.format("Attempted image upload with unuseable file extension: {%s}", extension));
			validPic = false;
		}

		return validPic;
	}

	/**
	 * Attempts to save the file to disk. On success returns true, failures return
	 * false
	 *
	 * @param profilePic
	 * @param fileName
	 * @return
	 */
	public static boolean saveImageToDisk(MultipartFile profilePic, String fileName) {

		SystemPropertiesService sps = SystemPropertiesService.getInstance();
		String uploadDirectory = sps.getProperty("IMAGE_DIR");
		File uploadDir = new File(sps.getProperty("ROOT_DIR") + File.separator + "tomcat" + File.separator + "webapps"
				+ File.separator + uploadDirectory);
		if (!uploadDir.exists()) {
			logger.info(String.format("Created new directory at {%s}", uploadDir.getPath()));
			uploadDir.mkdir();
		}
		File fileToWrite = new File(uploadDir.getPath() + File.separator + fileName);
		logger.info(String.format("Attempting uploaded of file to path {%s}", fileToWrite.getPath()));

		try {
			if (fileToWrite.exists() || fileToWrite.createNewFile()) {
				Files.write(fileToWrite.toPath(), profilePic.getBytes());
			}
		} catch (IOException e) {
			logger.error("Unable to write file to disk", e);
			return false;
		}
		logger.info(String.format("Successfully uploaded file {%s} with size {%s} bytes", fileToWrite.getPath(),
				fileToWrite.length()));

		return true;

	}

}
