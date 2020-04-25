package com.reachout.models;

	/**
	 * Represents a UserProfile object as stored in the database. </br>
	 * The database table is USERS. </br>
	 * The Hibernate Mapping is src/main/resources/com/reachout/models/User.hbm.xml
	 * </br>
	 * Any changes to fields here must be reflected in that file and the table
	 * structure </br>
	 * <em>Note, any changes with setters done here are "IN MEMORY ONLY" and not
	 * reflected in the database. The object must be saved or updated through a DAO
	 * to store that change</em>
	 * 
	 * @author Jessica
	 *
	 */
	public class UserProfile {

		private int userId;
		private String profilePic;
		private String bio;
		private String healthStatus;

		/**
		 * 
		 */
		public UserProfile() {
		}
		
		/**
		 * Represents a UserProfile object that can be stored in the database through hibernate
		 * mappings
		 * 
		 * @param profilePic
		 * @param bio
		 * @param healthStatus
		 */
		public UserProfile(String profilePic, String bio, String healthStatus, int userId) {
			this.profilePic = profilePic;
			this.bio = bio;
			this.healthStatus = healthStatus;
			this.userId = userId;
		}
		
		/**
		 * Gets the profilePic as it exists in current memory
		 * 
		 * @return profilePic
		 */

		public String getProfilePic() {
			return profilePic;
		}
		
		/**
		 * sets the profilePic in memory
		 * 
		 * @param profilePic
		 */
		public void setProfilePic(String profilePic) {
			this.profilePic = profilePic;
		}

		/**
		 * Gets the bio as it exists in current memory
		 * 
		 * @return bio
		 */
		public String getBio() {
			return bio;
		}
		
		/**
		 * sets the bio ID in memory
		 * 
		 * @param bio
		 */
		public void setBio(String bio) {
			this.bio = bio;
		}
		
		/**
		 * Gets the userId as it exists in current memory
		 * 
		 * @return userId
		 */
		public int getUserId() {
			return userId;
		}

		
		/**
		 * Sets the userID
		 * @param int
		 */
		public void setUserId(int userId) {
			this.userId = userId;
		}
		/**
		 * Gets the healthStatus as it exists in current memory
		 * 
		 * @return healthStatus
		 */
		public String getHealthStatus() {
			return healthStatus;
		}

		/**
		 * sets the current healthStatus in memory
		 * 
		 * @param healthStatus
		 */
		public void setHealthStatus(String healthStatus) {
			this.healthStatus = healthStatus;
		}


}

