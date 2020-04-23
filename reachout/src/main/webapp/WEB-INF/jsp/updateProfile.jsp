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
			<div class="col-sm-4">
				<div class="profile-form" style="height: 424px;">
					<form action="" method="POST" style="height: 399px;">
						<!--  enctype="multipart/form-data"	 -->
						<sec:csrfInput />
						<fieldset>
							<p>Update your profile information</p>
							<hr>
							<!-- Profile Picture -->
							<!-- 
						<div class="control-group">
							<div class="input-group">
								<div class="input-group-prepend">
									<span class="input-group-text" id="basic-addon1"><i
										class="fa fa-lock"></i></span>
								</div>
								<input type="file" class="form-control" name="profilePic"
									id="profilePic" placeholder="Upload a profile picture">
							</div>
						</div>-->
							<!-- Health status -->
							<div class="control-group" style="height: 37px;">
								<div class="input-group">
									<div class="input-group-prepend">
										<span class="input-group-text" id="basic-addon1"> <i
											class="fa fa-user"></i></span>
									</div>

									<select id="healthStatus" name="healthStatus"
										class="form-control" placeholder="Health Status">
										<c:forEach var="hs" items="${healthList}">
											<option><c:set var="h" value="${hs}" />
												<c:out value="${h }" /></option>
										</c:forEach>
									</select>
								</div>
							</div>
							<!-- User Biography -->
							<div class="row">
								<div class="control-group">
									<div class="col-sm-4"></div>
									<!--Update Button -->
									<div class="control-group">
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1"> <i
													class="fa fa-lock"></i>
												</span>
											</div>
											<input type="textarea" class="form-control" name="userBio"
												path="bio" id="userBio" placeholder="User Bio"
												maxLength="750" rows="4" cols="20"
												style="width: 253px; height: 57px">
										</div>
									</div>
								</div>
								<button class="btn btn-primary btn-lg">Update</button>
							</div>
						</fieldset>
					</form>
				</div>
			</div>

		</div>

		<c:choose>
			<c:when test="${empty errors}">
			</c:when>
			<c:otherwise>
   							 Errors :  ${errors}
    						</c:otherwise>
		</c:choose>
	</div>

</body>

</html>