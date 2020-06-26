<!DOCTYPE html>
<html lang="en-GB">
<head>
<title>ReachOut | Pending Approvals</title>
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
						<h1>Group Pending Requests</h1>
					</div>

					<div class="col-lg-12"></div>
				</div>

				<c:choose>
					<c:when test="${empty pendingRequests}">
						<!-- Display success message -->
						<div class="alert alert-success alert-spacing" role="alert">
							<p>There are no pending requests at the moment, please check
								back later.</p>
						</div>
					</c:when>
					<c:otherwise>
						<div class="row pt-3">
							<div class="col-md-4">
								<h4 class="card-title">Group</h4>
							</div>
							<div class="col-md-4">
								<h4 class="card-title">User</h4>
							</div>
							<div class="col-md-4">
								<h4 class="card-title">Accept/Reject</h4>
							</div>
							<hr>
						</div>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${not empty error}">
						<div class="alert alert-warning alert-spacing" role="alert">
							<p>${error}</p>
						</div>
					</c:when>
				</c:choose>

				<c:forEach items="${pendingRequests}" var="request">
					<div class="card card-request">
						<div class="row">
							<div class="col-md-4">
								<h4 class="card-title">${request.getName()}</h4>
							</div>
							<div class="col-md-4">
								<h4 class="card-title">${request.getUsername()}</h4>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<form class="form-inline" action="" method="POST">
										<sec:csrfInput />
										<input type="hidden" id="groupMemberID" name="groupMemberID" value="${request.getID()}" />
										<button class="btn btn-success btn-block" name="submit" type="submit"  value="accept">Accept Request</button>
										<button class="btn btn-danger btn-block" name="submit" type="submit" value="reject">Reject Request</button>
									</form>
								</div>
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
