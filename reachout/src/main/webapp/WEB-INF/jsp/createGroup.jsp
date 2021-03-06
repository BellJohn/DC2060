<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/components/stylesheets.jsp"%>
<title>ReachOut | Create Group</title>
</head>
<body>
	<div class="container-fluid">
		<%@ include file="/components/navbar.jsp"%>
		<div class="row profile-row">
			<div class="col-sm-4"></div>
			<div class="col-md-4">
				<c:choose>
					<c:when test="${empty postSent}">
						<div class="card card-bkg">
							<h2>
								<strong>Create New Group</strong>
							</h2>

							<p>Groups are a way for you to add users into a private space where requests and services can be private within the group. You may use this for a neighbourhood, family, community group or however you please.</p>

							<!-- New Request Form -->
							<form action="createGroup" method="POST" id="createGroup" enctype="multipart/form-data">
								<sec:csrfInput />
								<fieldset>

									<!-- Group Name -->
									<div class="form-group">
										<label for="groupName">Group Name</label> <input
											id="groupName" name="groupName"
											placeholder="Please give your group a name" type="text"
											required="required" class="form-control" maxlength="128"
											minlength="10">
									</div>

									<!-- Group Description -->
									<div class="form-group remainingCounterText">
										<label for="groupDesc">Description</label>
										<textarea id="groupDesc" name="groupDesc" cols="40" rows="5"
											placeholder="Please give a small description to let users know why you have created this group. Please note, everyone on the site will be able to view this information."
											class="form-control" maxlength="2000" minlength="10"
											required="required"></textarea>
									</div>

									<!-- Remaining Characters -->
									<div class="row remainingCounter">
										<div class="col-sm-3"></div>
										<div class="col-sm-6">
											<br>
										</div>
										<div class="col-sm-3">
											<span id='remainingC'></span>
										</div>
									</div>

									<!-- Group Picture -->
									<div class="form-group">
										<label for="groupPicture">Group Picture (optional)</label><input
											type="file" id="groupPicture" name="groupPicture" class="form-control">
									</div>

									<!-- Group Town -->
									<div class="form-group">
										<label for="groupCity">City/Town of Group (e.g.
											Chelsea)</label> <input id="groupCity" name="groupCity"
											required="required" placeholder="Town/City" type="text"
											class="form-control" maxlength="60">
									</div>

									<!-- Group County -->
									<div class="form-group">
										<label for="groupCounty">County of Group (e.g.
											Cambridgeshire)</label> <input id="groupCounty" name="groupCounty"
											placeholder="County" type="text" class="form-control"
											required="required" maxlength="30">
									</div>
									
									<!-- Remaining Characters -->
									<div class="row remainingCounter">
										<div class="col-sm-3"></div>
										<div class="col-sm-6">
											<br>
										</div>
										<div class="col-sm-3">
											<span id='remainingC'></span>
										</div>
									</div>

									<!-- Create Button -->
									<button name="submit" type="submit"
										class="btn btn-primary btn-large btn-block" id="submit">Create
										Group</button>



								</fieldset>
							</form>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${createSuccess}">
								<!-- Display success message -->
								<div class="alert alert-success alert-spacing" role="alert">
									<p>
										Success! Your group is now live. Please visit the <a
											href="viewGroups">Groups Page</a> to see your groups.
									</p>
								</div>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${not empty error}">
						<div class="alert alert-warning alert-spacing" role="alert">
							<p>${error}</p>
						</div>
					</c:when>
				</c:choose>
			</div>
		</div>
	</div>
	<script>
		//Disable the submit button if there were no validation errors on the form
		$(document).ready(function() {
			var len = 0;
			var maxchar = 2000;

			$('#groupDesc').keyup(function() {
				len = this.value.length
				if (len > maxchar) {
					return false;
				} else if (len > 0) {
					$("#remainingC").html(maxchar - len);
				} else {
					$("#remainingC").html(maxchar);
				}
			})

			$("#createGroup").submit(function() {
				$(this).find(':submit').attr('disabled', 'disabled');
			});

			$("#createGroup").bind("invalid-form.validate", function() {
				$(this).find(':submit').prop('disabled', false);
			});
		});
	</script>
</body>

</html>
