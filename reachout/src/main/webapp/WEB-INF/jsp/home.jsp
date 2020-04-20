<!DOCTYPE html>
<html lang="en-GB">
<head>
<title>ReachOut</title>
<%@ include file="/components/stylesheets.jsp"%>
<meta charset="UTF-8">
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>
		<div class="jumbotron">
			<div class="row">
				<div class="col-md-6 col-md-offset-3">
			<img src="images/reachout.png" alt="reachout logo"
									class="img-circle img-responsive">
				</div>
			</div>
			<p>Sign up now to volunteer and help vulnerable people and those affected by COVID-19. 
      There are so many ways you can help so choose what's right for you. Once signed up and verified
      you will be able to see requests local to you and start helping immediately. Please ensure that you
      follow social distancing measures to keep yourself and others safe. We offer many different ways to help
      so even if you are vulnerable you may still be able to get involved remotely. </p>
      <div class="row"></div>
      <div class="col-md-6 col-md-offset-3">
      <video width="500" height="400" controls>
 	 <source src="images/reachoutpitch.mp4" type="video/mp4">
		Your browser does not support the video tag.
		</video>
		</div>
		</div>
			<p>
				<a class="btn btn-primary btn-large" href="about">Learn more</a>
				<link type="text/css" rel="stylesheet" href="css/cc.css" />
			</p>
		</div>
		<div class="row">
			<div class="col-md-4">
				<img src="images/guidelines.jfif" alt="gov guidelines" class="homeimage">
				<h2>Are you in self-isolation or vulnerable?</h2>
				<p>If you are in need of services please sign up and be sure to provide us with 
        information about your current condition. You can make requests for any essential 
        items such as food or medication. 
        .</p>
				<p>
					<a class="btn" href="signup">Click here to register»</a>
				</p>
			</div>
			<div class="col-md-4">
				<img src="images/food.jfif" alt="food" class="homeimage">
				<h2>Services</h2>
				<p>We are happy to assist with food shopping, dog walking, 
        plant watering for those who are unable to leave their houses. </p>
				<p>
					<a class="btn" href="about">View details »</a>
				</p>
			</div>
			<div class="col-md-4">
				<img src="images/phonecall.jfif" alt="phonecall" class="homeimage">
				<h2>Conversation</h2>
				<p>Being in isolation can lead to loneliness for many people who live alone, we 
        want to prevent this by allowing users to connect over the phone for conversations.</p>
				<p>
					<a class="btn" href="#">View details »</a>
				</p>
			</div>
		</div>
	</div>

</body>
</html>