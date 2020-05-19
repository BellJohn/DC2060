package com.reachout.models;

import java.io.Serializable;
import java.sql.Blob;

public class Image implements Serializable{


	private int id;
	private String name;
	private String contentType;
	private Blob content;
	private int length;
	private int userId;

	public Image() {

	}



	public Image(String name, String contentType, Blob content, int length, int userId) {
		this.name = name;
		this.contentType = contentType;
		this.content = content;
		this.length = length;
		this.userId = userId;
	}


	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}



	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}



	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}



	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}



	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}



	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}



	/**
	 * @return the content
	 */
	public Blob getContent() {
		return content;
	}



	/**
	 * @param content the content to set
	 */
	public void setContent(Blob content) {
		this.content = content;
	}



	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}



	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}













}
