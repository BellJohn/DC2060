<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<sec:authentication var="princ" property="principal" />
<div class="row">
	<div class="col-md-12">
		<nav class="navbar navbar-expand-lg navbar-dark bg-primary static-top">

			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<a class="navbar-brand" href="#"><img class="brand-img"
					src="images/clipart-home-heart-1.png" /></a>
				<h2 class="brand-name">|ReachOut|</h2>
				<!-- Regular Site Functions Start-->
				<ul class="navbar-nav">
					<li class="nav-item active"><a class="nav-link" href="home">Home
							<span class="sr-only">(current)</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" href="blog">Blog</a></li>
					<c:choose>
						<c:when test="${princ !='anonymousUser'}">
							<!--  Only show the requests & services links to users who are logged in -->
							<li class="nav-item"><a class="nav-link" href="viewRequests">Requests</a></li>
							<!-- Disabled while it is yet not possible to create a service -->
						<!-- 	<li class="nav-item"><a class="nav-link" href="viewServices">Services</a></li>-->

						</c:when>
					</c:choose>

				</ul>
				<!--  Regular Site Functions End -->
				<!--  Profile Functions Start -->

				<c:choose>
					<c:when test="${princ=='anonymousUser'}">
						<!-- When the user is not logged in (principal is anonymousUser) -->
						<div class="ml-auto button-group">
							<ul class="navbar-nav">
								<li class="nav-item"><a class="nav-link" href="login">Login</a></li>
								<li class="nav-item"><a class="nav-link" href="signup">Sign
										Up</a></li>
							</ul>
						</div>
					</c:when>
					<c:otherwise>
						<!-- Logged In -->
						<div class="ml-auto button-group">
							<ul class="navbar-nav">
								<li class="nav-item"><a class="nav-link" href="profile">Profile</a></li>
								<li class="nav-item"><a class="nav-link" href="logout">Logout</a></li>
							</ul>
						</div>
					</c:otherwise>
				</c:choose>
				<!-- Profile Functions End -->
			</div>
		</nav>

	</div>
</div>