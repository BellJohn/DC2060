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
				<c:choose>
					<c:when test="${empty postSent}">
						<div class="profile-form">
							<form action="" method="POST">
								<sec:csrfInput />
								<fieldset>
									<div id="legend">
										<legend class="">
											<h2>Sign Up</h2>
										</legend>
									</div>
									<p>Update your profile information</p>
									<hr>
									<div class="control-group">
										<!-- Username -->
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1">
													<i class="fa fa-user"></i></span>
											</div>
											<input type="text" class="form-control" name="firstname" id="firstname"
												placeholder="First Name" required="required">
										</div>
									</div>
									<div class="control-group">
										<!-- E-mail -->
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1">
													<i class="fa fa-paper-plane"></i></span>
											</div>
											<input type="text" class="form-control" name="surname" id="surname"
												placeholder="Surname" required="required">
										</div>
									</div>
									<div class="control-group">
										<!-- Password-->
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1"><i
														class="fa fa-lock"></i></span>
											</div>
											<input type="file" class="form-control" name="profilepic" id="profilepic"
												placeholder="Upload a profile picture" required="required">
										</div>
									</div>
									<div class="control-group">
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1">
													<i class="fa fa-lock"></i>
												</span>
											</div>
											<input type="test" class="form-control" name="userbio"
												id="userbio" placeholder="User Bio" required="required">
										</div>
									</div>
									<div class="control-group">
										<!-- Button -->
										<button class="btn btn-primary btn-lg">Update</button>
									</div>
								</fieldset>
							</form>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
					
							</c:otherwise>

						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="col-sm-4"></div>
		</div>
	</div>

</body>

</html>