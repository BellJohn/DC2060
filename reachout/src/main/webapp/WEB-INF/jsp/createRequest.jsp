<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ include file="/components/stylesheets.jsp"%>
<title>ReachOut | Create Request</title>
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
								<strong>Create Request</strong>
							</h2>

							<!-- New Request Form -->
							<form:form action="createRequest" method="POST"
								id="createRequest">
								<sec:csrfInput />
								<fieldset>

									<!-- Request Title -->
									<div class="form-group">
										<label for="userBio">Title</label> <input id="reqTitle"
											name="reqTitle"
											placeholder="Please give a quick summary of your request."
											type="text" required="required" class="form-control"
											maxlength="128" minlength="10">
									</div>

									<!-- Request Description -->
									<div class="form-group remainingCounterText">
										<label for="reqDesc">Description</label>
										<textarea id="reqDesc" name="reqDesc" cols="40" rows="5"
											placeholder="Tell us a little about your request. Please note, everyone on the site will be able to view this information."
											class="form-control" maxlength="2000" minlength="50"
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

									<!-- Request County -->
									<div class="form-group">
										<label for="reqCounty">County of Request (e.g.
											Cambridgeshire)</label> <input id="reqCounty" name="reqCounty"
											placeholder="Countyshire" type="text" class="form-control"
											required="required" maxlength="26">
									</div>

									<!-- Request Town -->
									<div class="form-group">
										<label for="reqCity">City/Town of Request (e.g.
											Chelsea)</label> <input id="reqCity" name="reqCity"
											required="required" placeholder="Town city" type="text"
											class="form-control" maxlength="60">
									</div>

									<!-- Request Priority -->
									<div class=form-group">
										<label for="reqPriority">Request Priority</label> <select
											id="reqPriority" name="reqPriority" class="form-control">
											<option value="urgent">Urgent</option>
											<option value="medium">Medium</option>
											<option value="low">Low</option>

										</select>
									</div>

									<c:choose>
										<c:when test="${!empty userGroups}">

											<!-- Public Visibility -->
											<div class="form-group">
											<label for="publicVsisibility">Visibile to Public</label>
												<input type="checkbox" id="publicVisibility" name="publicVisibility"
													value="visible"> <br>
											</div>
											
											<div class="form-group">
												<input type="checkbox" id="groupVisibility" name="groupVisibility"
													value="visible"> <label for="groupVisibility">Only visible in selected group</label><br>
											</div>

											<!-- Group Visibility -->
											<div class="form-group">
												<label for="group">Visible to Group</label><br>
												<select id="group" name="group"
													class="form-control">
														<option disabled selected value> -- select a group -- </option>
													<c:forEach var="hs" items="${userGroups}">
														<option ${hs == userGroups ? 'selected' : ''}><c:set
																var="g" value="${hs}" />
															<c:out value="${g}" /></option>
													</c:forEach>
												</select>
											</div>

										</c:when>
									</c:choose>

									<!-- Create Button -->
									<button name="submit" type="submit"
										class="btn btn-primary btn-large btn-block" id="submit">Create
										Request</button>

								</fieldset>
							</form:form>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${createSuccess}">
								<!-- Display success message -->
								<div class="alert alert-success alert-spacing" role="alert">
									<p>
										Success! Your request is now live. Please visit your <a
											href="profile">Profile</a> to see your open requests.
									</p>
								</div>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
	<script>
		//Disable the submit button if there were no validation errors on the form
		$(document).ready(function() {
			var len = 0;
			var maxchar = 2000;

			$('#reqDesc').keyup(function() {
				len = this.value.length
				if (len > maxchar) {
					return false;
				} else if (len > 0) {
					$("#remainingC").html(maxchar - len);
				} else {
					$("#remainingC").html(maxchar);
				}
			})

			$("#createRequest").submit(function() {
				$(this).find(':submit').attr('disabled', 'disabled');
			});

			$("#createRequest").bind("invalid-form.validate", function() {
				$(this).find(':submit').prop('disabled', false);
			});
		});
	</script>
</body>

</html>
