<!DOCTYPE html>
<html lang="en-GB">
<head>
<title>ReachOut | Services</title>
<%@ include file="/components/stylesheets.jsp"%>
<meta charset="UTF-8">

<style>
#map {
	width: 100%;
	height: 400px;
	background-color: grey;
}
</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maps.googleapis.com/maps/api/js?key=<API_KEY>&libraries=places"
	async defer></script>
<script>
	// This example requires the Places library. Include the libraries=places
	// parameter when you first load the API. For example:
	// <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places">

	var map;
	var service;
	var infowindow;

	function triggerMapLoad() {
		var e = document.getElementById("locations");
		var loc = e.options[e.selectedIndex].value;
		console.log(loc);
		initMap(loc);
	}

	function initMap(loc) {
		var sydney = new google.maps.LatLng(-33.867, 151.195);
		//var loc = "Worcester";
		infowindow = new google.maps.InfoWindow();

		map = new google.maps.Map(document.getElementById('map'), {
			center : sydney,
			zoom : 15
		});

		var request = {
			query : loc + ', UK',
			fields : [ 'name', 'geometry' ],
		};

		service = new google.maps.places.PlacesService(map);

		service.findPlaceFromQuery(request, function(results, status) {
			if (status === google.maps.places.PlacesServiceStatus.OK) {
				for (var i = 0; i < results.length; i++) {
					createMarker(results[i]);
				}

				map.setCenter(results[0].geometry.location);
			}
		});
	}

	function createMarker(place) {
		var marker = new google.maps.Marker({
			map : map,
			position : place.geometry.location,
			description : 'This is a test Title'
		});

		var contentString = '<div id="content">' + '<div id="siteNotice">'
				+ '</div>'
				+ '<h1 id="firstHeading" class="firstHeading">Uluru</h1>'
				+ '<div id="bodyContent">' + '<p>someContent</p>' + '</div>'
				+ '</div>';

		google.maps.event.addListener(marker, 'click', function() {
			infowindow.setContent(contentString);
			infowindow.open(map, this);
		});
	}

	function orchestrate() {
		var geocoder = geocoder = new google.maps.Geocoder();
		var reqLoc = document.getElementById("inputAddress");
		var radInput = document.getElementById("inputRadius");
		var reqRad = radInput.options[radInput.selectedIndex].value;
		var zoom = 8;
		switch(reqRad) {
		  case "1":
			  zoom = 14;
		    break;
		  case "2":
				zoom = 14;
		    break;
		  case "5":
			  zoom = 11;
		    break;
		  case "10":
			    zoom = 11;
			    break;
		  case "100":
			  zoom = 7;
			    break;
		  case "999":
			    zoom = 6;
			    break;
		  default:
		    text = "I have never heard of that fruit...";
		}
		var loc = reqLoc.value + ",UK";
		console.log(loc);
		var address = loc;

		geocoder.geocode({
			'address' : address
		}, function(results, status) {
			if (status == 'OK') {
				console.log(results);
				var latlng = results[0].geometry.location;
				console.log(latlng);
				console.log("In method: " + latlng.lat());
				console.log("In method: " + latlng.lng());
				var mapOptions = {
					zoom : 8,
					center : latlng
				}
				map = new google.maps.Map(document.getElementById('map'),
						mapOptions);

				map.setCenter(results[0].geometry.location);
				var marker = new google.maps.Marker({
					map : map,
					position : results[0].geometry.location
				});
				fetchForLatLng(latlng, reqRad);
			} else {
				alert('Geocode was not successful for the following reason: '
						+ status);
			}
		});
	}

	function fetchForLatLng(latlng, reqRad) {
		$.ajax({
			type : 'GET',
			url : 'ListingFetchController?type=service&lat=' + latlng.lat()
					+ '&lng=' + latlng.lng() + '&radius=' + reqRad,
			complete : function(resp) {
			}
		});
	}
</script>
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
						<h1>All Services</h1>
					</div>
					<div class="col-lg-3">
						<form action="createService">
							<button class="btn btn-info text-right ">
								<span class="fa fa-plus-circle"></span> Offer New Service
							</button>
						</form>
					</div>
					<div class="col-lg-12 text-justify">
						<p>Here you can view all open service offers that have been
							created by other users. In order to see your own offered
							services, please visit your profile. For more information on
							Services, please see the help page.</p>
					</div>
				</div>

				<div class="col-lg-12 card card-request">
					<div class="form-group row">
						<label for="inputAddress" class="col-form-label"
							style="margin-left: 5em;">Address</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="inputAddress"
								placeholder="Where are you searching?">
						</div>
						<label for="inputRadius" class="col-form-label">Distance</label>
						<div class="col-sm-3">
							<select class="form-control" id="inputRadius"><option
									value="1">1 mile</option>
								<option value="2">2 miles</option>
								<option value="5">5 miles</option>
								<option value="10">10 miles</option>
								<option value="100">100 miles</option>
								<option value="999">National</option></select>
						</div>
						<div class="col-sm-2">
							<button type="button" onclick="orchestrate()"
								class="btn btn-primary" style="text-align: right; float: right;">Search</button>
						</div>
					</div>
				</div>
				<div id="map"></div>
				<c:forEach items="${liveServices}" var="service">

					<div class="card card-request">
						<h4 class="card-title">${service.getTitle()}</h4>
						<h6 class="card-subtitle mb-2 text-muted">${service.getCity()},
							${service.getCounty()}</h6>
						<p class="card-text">${service.getFormattedDescription()}</p>

						<hr>

						<div class="row">
							<div class="col-lg-9">
								<p class="text-muted">Created by ${service.getUsername()} on
									${service.getCreatedDate()} at ${service.getCreatedTime()}.</p>
							</div>

							<div class="col-lg-3">
								<form action="viewListing" method="POST">
									<sec:csrfInput />
									<input type="hidden" id="listingType" name="listingType"
										value="${service.getListingType()}" /> <input type="hidden"
										id="listingID" name="listingID"
										value="${service.getListingID()}" />
									<button class="btn btn-info btn-block">
										<span class="fa fa-info"></span> View Details
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
