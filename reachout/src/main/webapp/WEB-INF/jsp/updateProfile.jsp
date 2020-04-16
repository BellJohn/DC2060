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
							<form action="" method="POST">
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
											<input type="file" class="form-control" name="profilepic" id="profilePic"
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
											<select id = "healthStatus" name="healthStatus" placeholder="Health Status"
											class="form-control">
               								<option value = "1">Self-Isolating - due to suspected COVID-19</option>
               								<option value = "2">Quarantine due to exposure</option>
               								<option value = "3">Healthy</option>
               								<option value = "4">Recovered from COVID-19</option>
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
											<input type="test" class="form-control" name="userbio"
												id="userBio" placeholder="User Bio">
										</div>
									</div>
									<div class="control-group">
										<!--Update Button -->
										<button class="btn btn-primary btn-lg">Update</button>
									</div>
								</fieldset>
							</form>
						</div>
			</div>
			<div class="col-sm-4"></div>
		</div>
	</div>

</body>

</html>