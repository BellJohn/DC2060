<!DOCTYPE html>
<html lang="en-GB">
<head>
	<title>ReachOut | Profile</title>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ include file="/components/stylesheets.jsp"%>
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/navbar.jsp"%>
		<div class="row">
			<div class="col-md-offset-4 col-md-8 col-lg-offset-3 col-lg-6">
				<div class="well profile">
					<div class="col-sm-12">
						<div class="col-xs-12 col-sm-8" style="width: 981px; ">
						
							<h2>${firstName} ${lastName}</h2>
							<p>
								<strong>User Bio:
								 </strong> 
								 <c:choose>
  									  <c:when test="${empty bio}">
        							Tell us more about yourself
    								</c:when>
   									 <c:otherwise>
        							${bio}
    								</c:otherwise>
								</c:choose>
							</p>
						</div>
						<div class="col-xs-12 col-sm-6 text-center">
							<figure>
								<img src="images/no-profile-pic.png" alt=""
									class="img-circle img-responsive">
							</figure>
						</div>
						<div class="col-xs-12 col-sm-6 text-center">
						<c:choose>
  									  <c:when test="${empty healthStatus}">
        							Health Status : Unknown
    								</c:when>
   									 <c:otherwise>
   									 Health Status :  ${healthStatus}
    								</c:otherwise>
								</c:choose>
					</div>
					<div class="col-xs-12 col-sm-6 emphasis">
					<div class="btn-group dropup btn-block">
								<a class="btn btn-primary btn-large" href="updateProfile">Update Profile</a>
							</div>
					</div>
					<div class="col-xs-12 divider text-center">
						<div class="col-xs-12 col-sm-6 emphasis">
							<h2>
								<strong>1</strong>
							</h2>
							<p>
								<small>Requests</small>
							</p>
							<form action="createRequest">
								<button class="btn btn-success btn-block">
									<span class="fa fa-plus-circle"></span> Create New Request
								</button>
							</form>

						</div>
						<div class="col-xs-12 col-sm-6 emphasis">
							<h2>
								<strong>2</strong>
							</h2>
							<p>
								<small>Services Offered</small>
							</p>
							<form action="createService">
								<button class="btn btn-info btn-block">
									<span class="fa fa-user"></span> Offer New Service
								</button>
							</form>
						</div>
						<div class="col-xs-12 col-sm-6 emphasis">
							<div class="btn-group dropup btn-block">
								<button type="button" class="btn btn-primary">Options</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>