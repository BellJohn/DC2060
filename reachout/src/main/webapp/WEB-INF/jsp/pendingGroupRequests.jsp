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
							<div class="col-md-6"></div>
							<h4 class="card-title col-md-3" style="text-align: right;">GroupName:
								${request.getName()}</h4>

							<h4 class="card-title col-md-3" style="text-align: left;">User:
								${request.getUsername()}</h4>
						</div>
						<hr>


						<div class="col-lg-3">
							<div class="form-group">
								<form action="" method="POST">
									<sec:csrfInput />
									<input type="hidden" id="groupMemberID" name="groupMemberID"
										value="${request.getID()}" />
									<button name="submit" type=submit " value="accept"
										class="btn btn-info btn-block">
										<span class="fa fa-info"></span> Accept Request
									</button>
									<button class="btn btn-info btn-block" name="submit"
										type="submit" value="reject">
										<span class="fa fa-info"></span> Reject Request
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
