package com.reachout.models;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * Represents a Password object that can be stored in the database. </br>
 * The database table is PASSWORDS. </br>
 * The Hibernate Mapping is
 * src/main/resources/com/reachout/models/Password.hbm.xml </br>
 * Any changes to fields here must be reflected in that file and the table
 * structure </br>
 * <em>Note, any changes with setters done here are "IN MEMORY ONLY" and not
 * reflected in the database. The object must be saved or updated through a DAO
 * to store that change</em>
 * 
 * @author John
 *
 */
public class Password {
	// The higher the number of iterations the more
	// expensive computing the hash is for us and
	// also for an attacker.
	private static final int ITERATIONS = 20 * 1000;
	private static final int SALT_LEN = 32;
	private static final int DESIRED_KEY_LEN = 256;

	private int pwdId;

	private int userId;

	private long createdDate;

	private String passwordString;

	/**
	 * Returns the current password ID as held in memory
	 * 
	 * @return
	 */
	public int getPwdId() {
		return pwdId;
	}

	/**
	 * Returns the current User ID as held in memory
	 * 
	 * @return
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Returns the current createdDate (epoch) as held in memory
	 * 
	 * @return
	 */
	public long getCreatedDate() {
		return createdDate;
	}

	/**
	 * Returns the current password string as held in memory
	 * 
	 * @return
	 */
	public String getPasswordString() {
		return passwordString;
	}

	/**
	 * Sets the current password ID in memory
	 * 
	 * @param pwdId
	 */
	public void setPwdId(int pwdId) {
		this.pwdId = pwdId;
	}

	/**
	 * sets the current user ID in memory
	 * 
	 * @param userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * sets the current created date in memory
	 * 
	 * @param createdDate
	 */
	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Sets the current password string in memory to the value provided
	 * 
	 * @param passwordString
	 * @throws GeneralSecurityException
	 */
	public void setPasswordString(String passwordString) {
		this.passwordString = passwordString;
	}

	/**
	 * Sets a plaintext password string in memory after hashing with a generated
	 * salt
	 * 
	 * @param passwordString
	 * @throws GeneralSecurityException
	 */
	public void setHashedPasswordString(String passwordString) throws GeneralSecurityException {
		this.passwordString = getSaltedHash(passwordString);
	}

	private static byte[] generateSalt() throws NoSuchAlgorithmException {
		return SecureRandom.getInstance("SHA1PRNG").generateSeed(SALT_LEN);
	}

	/**
	 * Computes a salted PBKDF2 hash of given plaintext password suitable for
	 * storing in a database. Empty passwords are not supported.
	 */
	public static String getSaltedHash(String password) throws GeneralSecurityException {
		byte[] salt = generateSalt();
		// store the salt with the password
		return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
	}

	/**
	 * Checks whether given plaintext password matches a stored salted hash
	 * of the password.
	 * 
	 * @throws GeneralSecurityException - Something goes wrong with the hashing function
	 * @throws IllegalStateException - Something is wrong with the stored value and it doesn't match "*$*"
	 */
	public boolean comparePassword(String passwordToCheck) throws GeneralSecurityException {
		// Get stored hash and split into salt|password
		String[] saltAndHash = passwordString.split("\\$");
		// Make sure we have found both parts
		if (saltAndHash.length != 2) {
			throw new IllegalStateException("The stored password must have the form 'salt$hash'");
		}
		// Hash the provided password with the salt we found.
		String hashOfInput = hash(passwordToCheck, Base64.decodeBase64(saltAndHash[0]));
		// Check to see if our stored hash and new hash are then same
		return hashOfInput.equals(saltAndHash[1]);
	}

	/**
	 * Hashes the provided password with the provided salt and returns a base64
	 * representation
	 * 
	 * @param password
	 * @param salt
	 * @return base64 encoded hash of password using salt
	 * @throws GeneralSecurityException
	 */
	private static String hash(String password, byte[] salt) throws GeneralSecurityException {
		if (password == null || password.length() == 0)
			throw new IllegalArgumentException("Empty passwords are not supported.");
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, DESIRED_KEY_LEN));
		return Base64.encodeBase64String(key.getEncoded());
	}

}
