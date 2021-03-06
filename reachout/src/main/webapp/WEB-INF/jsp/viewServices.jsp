<!DOCTYPE html>
<html lang="en-GB">
<head>
<title>ReachOut | Services</title>
<%@ include file="/components/stylesheets.jsp"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />
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
	src="https://maps.googleapis.com/maps/api/js?key=${API_KEY}&libraries=places"
	async defer></script>
<script>						
	var map = null;
	var service;
	var infowindow;
	var allServices;

	class coords {
		constructor(lat, longi){
			this.latitude = lat;
			this.longitude = longi;
		}
	}
	class DummyLocation {
		constructor(lat, longi) {
			this.coords = new coords(lat, longi);
		}
	}


	function createMarkerJSON(data) {
		var marker = new google.maps.Marker({
			map : map,
			position : {lat: data.locationLat, lng: data.locationLong},
			description : data.listing.title
		});

		var token = $("meta[name='_csrf']").attr("content");
		var contentString = '<div class="markerContent"">' + '<div class="siteNotice">'
		+ '</div>'
		+ '<h1 class="firstHeading">' + data.listing.title +'</h1>'
		+ '<div>' + '<p>'+data.listing.description+'</p>' + '</div>'					
		+ '<div>'
		+ '<form action="viewListing" method="POST">'
		+ '<input type="hidden" name="_csrf" value="'+token+'">'
		+ '<input type="hidden" id="listingType" name="listingType"	value="'+ data.listing.listingType +'" />'
		+ '<input type="hidden"	id="listingID" name="listingID"	value="'+ data.listing.id +'" />'
		+ '<button class="btn btn-info btn-block">'
		+ '<span class="fa fa-info"/> View Details</button>'
		+ '</form></div>'
		+ '</div>';
		
		google.maps.event.addListener(marker, 'click', function() {
			infowindow.setContent(contentString);
			infowindow.open(map, this);
		});
	}

	function triggerMapLoad() {
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(initMap, mapLoadError);
		} else {
			// Something went wrong
			console.log("Something went wrong");
		}
	}

	function mapLoadError(){
		// Default to London
		var test = new DummyLocation(51.5074, 0.1278);
		initMap(test);
	}

	function initMap(loc) {
		var userLocation = new google.maps.LatLng(loc.coords.latitude, loc.coords.longitude);
		infowindow = new google.maps.InfoWindow();

		map = new google.maps.Map(document.getElementById('map'), {
			center : userLocation,
			zoom : 7
		});

		var request = {
			query : userLocation + ', UK',
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

		allServices = ${liveServicesJSON};
		if(allServices != null){
			allServices.forEach(createMarkerJSON);
		}
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
		// Delete the data from the first page render from
		// our cache
		allServices = null;
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
			default:
			zoom = 6;
		}
		var loc = reqLoc.value + ",UK";
		var address = loc;

		geocoder.geocode({
			'address' : address
		}, function(results, status) {
			if (status == 'OK') {
				var latlng = results[0].geometry.location;
				var mapOptions = {
					zoom : zoom,
					center : latlng
				}
				map = new google.maps.Map(document.getElementById('map'),
				mapOptions);

				map.setCenter(results[0].geometry.location);
				fetchForLatLng(latlng, reqRad);
			} else {
				alert('Geocode was not successful for the following reason: '
				+ status);
			}
		});
	}

	function createCard(singleListing){
		var token = $("meta[name='_csrf']").attr("content");
		var newCard = '<div class="card card-request">'
		+ '<h4 class="card-title">'+ singleListing.listing.title +'</h4>'
		+ '<h6 class="card-subtitle mb-2 text-muted">' + singleListing.listing.city + ',' + singleListing.listing.county + '</h6>'
		+ '<p class="card-text">'+ singleListing.listing.formattedDescription +'</p>'
		+ '<hr>'

		+ '<div class="row">'
		+ '	<div class="col-lg-9">'
	 	+ '	 <p class="text-muted">Created by ' + singleListing.user.username + ' on ' +singleListing.createdDate +' at ' + singleListing.createdTime +'.</p>'
		+ '</div>'

		+	'<div class="col-lg-3">'
		+ '<form action="viewListing" method="POST">'
		+   '<input type="hidden" name="_csrf" value="'+token+'">'
		+ ' <input type="hidden" id="listingType" name="listingType" value="'+singleListing.listingType+'" />'
		+ ' <input type="hidden" id="listingID" name="listingID" value="'+singleListing.listingID+'"" />'
		+ '	<button class="btn btn-info btn-block"> '
		+ '	<span class="fa fa-info"/> View Details</button>'
		+ '</form></div></div></div>'

		return newCard;
		}

	function fetchForLatLng(latlng, reqRad) {
		$.ajax({
			type : 'GET',
			url : 'ListingFetchController?type=service&lat=' + latlng.lat()
			+ '&lng=' + latlng.lng() + '&radius=' + reqRad,
			complete : function(resp) {
				// Clear out the existing visible listings
				var listingDetailsElement = document.getElementById('fullListingDetails');
				if(resp.status != 200){
					listingDetailsElement.innerHTML = "Something went wrong with the data recovery, refresh the page and try again";
				}
				if (resp.responseText == "[]"){   
					listingDetailsElement.innerHTML = "<p>No data was found for that search. Maybe try a wider search distance or a different area? </p>"
				}
				else{   
					console.log(resp);
					console.log(resp.responseText);
					listingDetailsElement.innerHTML = '';
					 var responseData = JSON.parse(resp.responseText);
					 var replacementText = "";
					 for (var i = 0; i < responseData.length; i++){
						    var singleListing = responseData[i];
						      createMarkerJSON(singleListing);
						      replacementText = replacementText + createCard(singleListing);
						}
					 listingDetailsElement.innerHTML = replacementText;
				}
			}
		});
	}
		</script>
</head>

<body onload="triggerMapLoad()">
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
								class="btn btn-primary" id="search-btn" style="text-align: right; float: right;">Search</button>
						</div>
					</div>
				</div>
				<div id="map"></div>
				<div id="fullListingDetails">
					<c:if test="${empty liveServices}">
						<div class="card card-request">
							<h4 class="card-title">There are no Requests
								available for you to view!</h4>
							<h6 class="card-subtitle mb-2 text-muted">
								<small>Note, your own Requests and Services are not
									visible to you on the map</small>
							</h6>
						</div>
					</c:if>
					<c:forEach items="${liveServices}" var="service">

						<div class="card card-request">
							<h4 class="card-title">${service.getTitle()}</h4>
							<h6 class="card-subtitle mb-2 text-muted">${service.getCity()}, ${service.getCounty()}</h6>
							<p class="card-text">${service.getFormattedDescription()}</p>

							<hr>

							<div class="row">
								<div class="col-lg-9">
									<p class="text-muted">Created by ${service.getUsername()}
										on ${service.getCreatedDate()} at ${service.getCreatedTime()}.</p>
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
			</div>
			<div class="col-lg-2"></div>
		</div>

		<script>
			$(document).ready(function() {
				$('#inputAddress').keypress(function(e) {
					if (e.keyCode == 13)
						$('#search-btn').click();
				});
			});
		</script>
	</div>
</body>
</html>
