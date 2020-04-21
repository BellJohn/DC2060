<!DOCTYPE html>
<html lang="en-GB">

<head>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<title>Sign Up</title>
	<%@ include file="/components/stylesheets.jsp"%>

</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>
		<div class="row">
			<div class="col-sm-4"></div>
			<div class="col-sm-4">
						<div class="profile-form">
							<form action="" method="POST" enctype="multipart/form-data">
							<c:choose>
								<c:when test="${empty postSent}">
								<sec:csrfInput />
								<fieldset>
									<div id="legend">
										<legend class="">
										</legend>
									</div>
									<p>Update your profile information</p>
									<hr>
									<!-- Profile Picture -->
									<div class="control-group">
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1"><i
														class="fa fa-lock"></i></span>
											</div>
											<input type="file" class="form-control" name="profilePic" id="profilePic"
												placeholder="Upload a profile picture">
										</div>
									</div>
									<!-- Health status -->
									<div class="control-group">
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1">
													<i class="fa fa-user"></i></span>
											</div>
										<select id="healthStatus" name="healthList" class="form-control" 
											items="${healthList}" placeholder= "Health Status">
											</select>
									</div>		
									</div>
										<!-- User Biography -->					
									<div class="control-group">
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1">
													<i class="fa fa-lock"></i>
												</span>
											</div>
											<input type="textarea" class="form-control" name="userbio" path="bio"
												id="userBio" placeholder="User Bio" maxLength="750" rows="4" cols="20">
										</div>
									</div>
									<div class="control-group">
										<!--Update Button -->
										<button class="btn btn-primary btn-lg">Update</button>
									</div>
									</fieldset>
								</c:when>
							</c:choose>
								
							</form>
							
							<c:otherwise>
								<h3>Something was wrong with the data you provided!</h3>
								<h3>Could not update profile</h3>
							</c:otherwise>
							
						</div>
			</div>

		</div>
	</div>

</body>

</html>