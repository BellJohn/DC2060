<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>ReachOut | Edit Group  ${group.getName()}</title>
<%@ include file="/components/stylesheets.jsp"%>
<meta charset="UTF-8">
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/navbar.jsp"%>
		<div class="row profile-row">
			<div class="col-sm-4"></div>
			<div class="col-sm-4">

				<c:choose>
					<c:when test="${empty postSent}">
						<div class="card card-bkg">
							
							<!-- New Edit Form -->
							<form action="" method="POST" id="editGroup">
								<sec:csrfInput />

								<!-- Title -->
								<div class="form-group">
									<label for="Group">Group Name</label>
									<input id="Name" name="Name" placeholder="Name" type="text"
										required="required" class="form-control"
										value="${group.name}" maxlength="128" minlength="10">
								</div>

								<!-- Description -->
								<div class="form-group remainingCounterText">
									<label for="Description">Description</label>
									<textarea id="Description" name="Description" cols="40" rows="5" placeholder="Description" 
									class="form-control" maxlength="2000" minlength="50" 
									required="required">${group.description}</textarea>
								</div>
								
								<!-- Group picture -->
								<!-- Group Picture -->
									<div class="form-group">
										<label for="groupPicture">Group Picture (optional)</label><input type="file" name="groupPicture"
											class="form-control" >
									</div>
								

								<!-- Remaining Characters -->
								<div class="row remainingCounter">
									<div class="col-sm-3"></div>
									<div class="col-sm-6"><br></div>
									<div class="col-sm-3">
										<span id='remainingC'></span>
									</div>
								</div>
								
								<!-- ID -->
								<input type="hidden" id="groupID" name="groupID"
									value="${group.getId()}" />

								<!-- Update/Delete buttons -->
								<div class="form-group">
									<button name="submit" type="submit" value="update" class="btn btn-success" style="float:left;">Update</button>
									<button name="submit" type="submit" value="delete" class="btn btn-danger" style="float:right;">Delete</button>											
								</div>
							</form>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${changeSuccess}">
								<div class="alert alert-success" role="alert">The group has been ${changeType}</div>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<c:choose>
			<c:when test="${not empty error}">
				<div class="alert alert-warning" role="alert">An Error occurred: ${error}</div>
			</c:when>
		</c:choose>
	</div>

	<script>
		//Disable the submit button if there were no validation errors on the form
		$(document).ready(function () {
			var len = 0;
			var maxchar = 2000;

			$( '#Desc' ).keyup(function(){
				len = this.value.length
				if(len > maxchar){
					return false;
				}
				else if (len > 0) {
					$( "#remainingC" ).html(maxchar - len);
				}
				else {
					$( "#remainingC" ).html(maxchar);
				}
			})
			
			$("#editGroup").submit(function () {
				$(this).find(':submit').attr('hidden', 'hidden');
			});
	
			$("#editGroup").bind("invalid-form.validate", function () {
				$(this).find(':submit').prop('disabled', false);
			});
		});
	</script>
</body>

</html>

