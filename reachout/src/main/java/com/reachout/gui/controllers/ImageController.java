package com.reachout.gui.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;


/**
 * Image controller, used to store images in the database
 * and convert them into byte streams
 * 
 * @author Jess
 *
 */

@Controller
public class ImageController {

	private Logger logger = LogManager.getLogger(ImageController.class);
	
	/**
	@RequestMapping("profile.jsp")
	public void displayImage(int userId, HttpServletResponse response) throws IOException {

		HibernateImageDAOImpl imageDAO = new HibernateImageDAOImpl();
		Image image = imageDAO.getImagebyUserID(userId);

		if (image == null ) {
			logger.error("No image to display");
		}

		BufferedInputStream input = null;
		BufferedOutputStream output = null;

		try {
			// Open streams.
			try {
				input = new BufferedInputStream(image.getContent().getBinaryStream(), DEFAULT_BUFFER_SIZE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

			// Write file contents to response.
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
		} finally {
			// Gently close streams.
			output.close();
			input.close();
			imageDAO.close();
		}
	}

	 **/

}
