<!DOCTYPE html>
<html lang="en-GB">
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>${user}'sProfile</title>
<%@ include file="/components/stylesheets.jsp"%>
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>
		<div class="row">
			<div class="col-md-offset-2 col-md-8 col-lg-offset-3 col-lg-6">
				<div class="well profile">
					<div class="col-sm-12">
						<div class="col-xs-12 col-sm-8">
							<h2>First Last</h2>
							<p>
								<strong>User Bio: </strong> Some details about the user
							</p>
						</div>
						<div class="col-xs-12 col-sm-4 text-center">
							<figure>
								<img src="images/no-profile-pic.png" alt=""
									class="img-circle img-responsive">
							</figure>
						</div>
					</div>
					<div class="col-xs-12 divider text-center">
						<div class="col-xs-12 col-sm-4 emphasis">
							<h2>
								<strong>1</strong>
							</h2>
							<p>
								<small>Requests</small>
							</p>
							<button class="btn btn-success btn-block">
								<span class="fa fa-plus-circle"></span> Create New Request
							</button>
						</div>
						<div class="col-xs-12 col-sm-4 emphasis">
							<h2>
								<strong>2</strong>
							</h2>
							<p>
								<small>Services Offered</small>
							</p>
							<button class="btn btn-info btn-block">
								<span class="fa fa-user"></span> Offer New Service
							</button>
						</div>
						<div class="col-xs-12 col-sm-4 emphasis">
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