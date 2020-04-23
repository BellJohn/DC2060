<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pagina" value="${requestScope['javax.servlet.forward.request_uri']}" />

<div class="row">
	<div class="col-md-12">
		<nav
			class="navbar navbar-expand-lg navbar-dark bg-primary static-top">

			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<a class="navbar-brand" href="#"><img class="brand-img"
					src="images/clipart-home-heart-1.png" /></a>
				<h2 class="brand-name">|ReachOut|</h2>
				<ul class="navbar-nav">
					<li class="nav-item ${pagina.endsWith('/home') ? 'active' : ''}"><a class="nav-link" href="home">Home${pagina.endsWith('/home') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
					<li class="nav-item ${pagina.endsWith('/blog') ? 'active' : ''}"><a class="nav-link" href="blog">Blog${pagina.endsWith('/blog') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
					<li class="nav-item ${pagina.endsWith('/requests') ? 'active' : ''}"><a class="nav-link" href="requests">Requests${pagina.endsWith('/requests') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
					

				</ul>
				<sec:authentication var="princ" property="principal" />
				<c:choose>
					<c:when test="${princ=='anonymousUser'}">
						<div class="ml-auto button-group">
							<ul class="navbar-nav">
								<li class="nav-item ${pagina.endsWith('/login') ? 'active' : ''}"><a class="nav-link" href="login">Login${pagina.endsWith('/login') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
								<li class="nav-item ${pagina.endsWith('/signup') ? 'active' : ''}"><a class="nav-link" href="signup">Sign	Up${pagina.endsWith('/signup') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
							</ul>
						</div>
					</c:when>
					<c:otherwise>
						<!-- Logged In -->
						<div class="ml-auto button-group">
							<ul class="navbar-nav">
								<li class="nav-item ${pagina.endsWith('/profile') ? 'active' : ''}"><a class="nav-link" href="profile">Profile${pagina.endsWith('/profile') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
								<li class="nav-item"><a class="nav-link" href="logout">Logout</a></li>
							</ul>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</nav>

	</div>
</div>