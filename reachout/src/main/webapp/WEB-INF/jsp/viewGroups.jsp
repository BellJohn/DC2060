<!DOCTYPE html>
<html lang="en-GB">
<head>
<title>ReachOut | Groups</title>
<%@ include file="/components/stylesheets.jsp"%>
<meta charset="UTF-8">
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>

		<div class="row">
			<div class="col-lg-2"></div>
			<div class="col-lg-8">
				<div class="row listing-page">
					<div class="col-lg-9">
						<h1>Groups</h1>
					</div>
					<div class="col-lg-3">
						<form action="createGroup">
							<button class="btn btn-success text-right">
								<span class="fa fa-plus-circle"></span> Create New Group
							</button>
						</form>
					</div>
					<div class="col-lg-12">
						<p>Here you can view all of the user created groups. If you
							would like to join a group, please press request to join and your
							request will be sent to admin for approval. For more information
							on Groups, please see the help page.</p>
					</div>
				</div>
				<h3>Your Groups</h3>

				<c:choose>
					<c:when test="${empty userGroups}">
							You are not in any groups, why not join one of the options below?
						</c:when>
					<c:otherwise>

						<c:forEach items="${userGroups}" var="request">


							<div class="card card-request">
								<div class="row">
									<h4 class="card-title col-md-3">${request.getName()}</h4>
									<div class="col-md-6"></div>
									<h4 class="card-title col-md-3" style="text-align: right;">${request.getDescription()}</h4>
								</div>

								Location
								<h6 class="card-subtitle mb-2 text-muted">${request.getLocationId()}</h6>
								<hr>

								<div class="row">
									<div class="col-lg-3">
										<form action="viewOneGroup" method="POST">
											<sec:csrfInput />
											<input type="hidden"
												id="groupID" name="groupID"
												value="${request.getId()}" />
												<input type="hidden"
												id="username" name="username"
												value="${username}" />
											<button class="btn btn-info btn-block">
												<span class="fa fa-info"></span> View Group
											</button>
										</form>
									</div>

								</div>
							</div>

						</c:forEach>
					</c:otherwise>
				</c:choose>


			<h3>Other groups</h3>
			
			
			<c:forEach items="${otherGroups}" var="otherGroups">


							<div class="card card-request">
								<div class="row">
									<h4 class="card-title col-md-3">${otherGroups.getName()}</h4>
									<div class="col-md-6"></div>
									<h4 class="card-title col-md-3" style="text-align: right;">${otherGroups.getDescription()}</h4>
								</div>

								Location
								<h6 class="card-subtitle mb-2 text-muted">${otherGroups.getLocationId()}</h6>
								<hr>

								<div class="row">
									
							
									<div class="col-lg-3">
										<form action="" method="POST">
											<sec:csrfInput />
											<input type="hidden"
												id="groupID" name="groupID"
												value="${otherGroups.getId()}" />
												<input type="hidden"
												id="username" name="username"
												value="${username}" />
											<button class="btn btn-info btn-block">
												<span class="fa fa-info"></span> Request to join
											</button>
										</form>
									</div>

								</div>
							</div>

						</c:forEach>


			</div>
			<div class="col-lg-2"></div>
		</div>
	</div>
</body>

</html>
