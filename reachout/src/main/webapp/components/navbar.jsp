<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<sec:authentication var="princ" property="principal" />
<c:set var="pagina"
	value="${requestScope['javax.servlet.forward.request_uri']}" />

<div class="row">
	<div class="col-md-12">
		<nav class="navbar navbar-expand-lg navbar-dark bg-primary static-top">

			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			  </button>

			  <div class="collapse navbar-collapse" id="navbarSupportedContent">

				<a class="navbar-brand" href="/profile"><img
					alt="ReachOut Logo" class="brand-img"
					src="images/reachout-logo-dark.png" /></a>

				<sec:authentication var="princ" property="principal" />
				<c:choose>
					<c:when test="${princ=='anonymousUser'}">
						<!-- NOT Logged In -->
						<div class="ml-auto button-group">
							<ul class="navbar-nav">
								<li
									class="nav-item ${pagina.endsWith('/login') ? 'active' : ''}"><a
									class="nav-link" href="login">Login${pagina.endsWith('/login') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
								<li
									class="nav-item ${pagina.endsWith('/signup') ? 'active' : ''}"><a
									class="nav-link" href="signup">Sign
										Up${pagina.endsWith('/signup') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
							</ul>
						</div>
					</c:when>
					<c:otherwise>
						<!-- Logged In -->
						<div class="button-group">
							<ul class="navbar-nav">
								<li
									class="nav-item ${pagina.endsWith('/profile') ? 'active' : ''}"><a
									class="nav-link" href="profile">Profile${pagina.endsWith('/profile') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
								<li
									class="nav-item ${pagina.endsWith('/viewMyMessages') ? 'active' : ''}"><a
									class="nav-link" href="viewMyMessages">My Messages${pagina.endsWith('/viewMyMessages') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
								<li
									class="nav-item ${pagina.endsWith('/viewRequests') ? 'active' : ''}"><a
									class="nav-link" href="viewRequests">Requests${pagina.endsWith('/viewRequests') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
								<li
									class="nav-item ${pagina.endsWith('/viewServices') ? 'active' : ''}"><a
									class="nav-link" href="viewServices">Services${pagina.endsWith('/viewServices') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
								<li
									class="nav-item ${pagina.endsWith('/viewGroups') ? 'active' : ''}"><a
									class="nav-link" href="viewGroups">Groups${pagina.endsWith('/viewGroups') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
							</ul>

						</div>
						<div class="ml-auto button-group">
							<ul class="navbar-nav">
								<li
									class="nav-item ${pagina.endsWith('/help') ? 'active' : ''}"><a
									class="nav-link" href="help">Help${pagina.endsWith('/help') ? '<span class="sr-only">(current)</span>' : ''}</a></li>
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
