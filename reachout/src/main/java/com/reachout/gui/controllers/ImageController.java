package com.reachout.gui.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.LobCreator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.reachout.dao.HibernateImageDAOImpl;
import com.reachout.models.*;


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
	private final int DEFAULT_BUFFER_SIZE = 10240;
	private SessionFactory sessionFactory;

	/**
	 * create an image entity from a multipart file
	 * 
	 * @param request
	 * @return
	 * @throws IOException 
	 */

	public boolean saveImage(MultipartFile file, int userId) throws IOException {

		Blob blob = null;
		boolean saveImageSuccess = false;

		try {
			//failing here, not sure if I am getting the session properly?
			Session session = sessionFactory.getCurrentSession();
			logger.info("session returned");
			blob = Hibernate.getLobCreator(session).createBlob(file.getInputStream(), file.getSize());
		} catch (Exception e) {
			logger.error("File could not be read");
		}

		Image image = new Image();
		image.setName(file.getOriginalFilename());
		image.setContentType(file.getContentType());
		image.setLength((int) file.getSize());
		image.setContent(blob);

		try (HibernateImageDAOImpl imageDAO = new HibernateImageDAOImpl()) {
			image.setId(userId);
			saveImageSuccess = imageDAO.saveImage(image);
			if(!saveImageSuccess) {
				logger.error("Unable to save image");
			}
		}
		return saveImageSuccess;
	}


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
