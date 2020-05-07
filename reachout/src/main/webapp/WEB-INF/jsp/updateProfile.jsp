<!DOCTYPE html>
<html lang="en-GB">

<head>
	<title>ReachOut | Update Profile</title>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ include file="/components/stylesheets.jsp"%>
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>
		
		<div class="row profile-row">
			<div class="col-sm-4"></div>
			<div class="col-md-4">
				<div class="card card-bkg">

					<!-- Display Users Name -->
					<h2><strong>${firstName} ${lastName}</strong></h2>

					<!-- Edit Profile Form -->
					<div class="profile-form">
						<form action="" method="POST">
							<sec:csrfInput />
							<fieldset>

								<!-- Profile Picture -->
								<figure>
									<img src="images/no-profile-pic.png" alt="" class="rounded-circle">
								</figure>

								<!-- Edit Bio -->
								<div class="form-group">
									<label for="userBio">Bio</label>
									<textarea class="form-control" name="userBio" path="bio" id="userBio" 
										placeholder="Please enter a short sentence or two about yourself."
										maxLength="160" rows="4" cols="40">${bio}</textarea>
								</div>

								<!-- Remaining Characters -->
								<div class="row">
									<div class="col-sm-3"></div>
									<div class="col-sm-6"><br></div>
									<div class="col-sm-3">
										<span id='remainingC'></span>
									</div>
								</div>
								
								<!-- Edit Health Status -->
								<div class="form-group">
									<label for="healthStatus">Status</label>
									<select id="healthStatus" name="healthStatus" class="form-control">
										<c:forEach var="hs" items="${healthList}">
											<option ${hs == healthStatus ? 'selected' : ''}><c:set var="h" value="${hs}" />
												<c:out value="${h}" /></option>
										</c:forEach>
									</select>
								</div>

								<!-- Save Button -->
								<button class="btn btn-primary btn-large btn-block save-profile-btn" href="updateProfile">Save</button>
							</fieldset>
						</form>
					</div>
				</div>

				<c:when test="${not empty errors}"> Errors : ${errors} </c:when>

			</div>
		</div>
	</div>
</body>

<script>
	$(document).ready(function() {
	var len = 0;
	var maxchar = 160;

	$( '#userBio' ).keyup(function(){
		len = this.value.length
		if(len > maxchar){
			return false;
		}
		else if (len > 0) {
			$( "#remainingC" ).html(maxchar - len);
		}
		else {
			$( "#remainingC" ).html(maxchar);
		}
	})
	});
</script>

</html>