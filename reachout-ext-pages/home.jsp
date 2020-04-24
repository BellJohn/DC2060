<!DOCTYPE html>
<html lang="en-GB">

<head>
	<title>ReachOut | Home</title>
	<%@ include file="/components/stylesheets.jsp"%>
	<meta charset="UTF-8">
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>

		<div class="jumbotron">
			<div class="row">
				<img src="images/reachout.png" alt="reachout logo" class="img-circle img-responsive center">
			</div>
			<div class="container flex-center">
				<div class="row pt-5 mt-3">
					<div class="col-lg-6 mb-5 text-center text-lg-left">
						<h1 class="h1-responsive font-weight-bold">Vulnerable, Self-Isolating or ready to provide help?</h1>
						<hr class="hr-light">
						<p>Sign up now to either request support, or start helping those local to you that are
							vulnerable or affected by COVID-19. Help can be requested for many things, so you can get
							the support you need or provide help to those that need it. Once verified, you can make
							requests or start helping others immediately. Please ensure that you follow social
							distancing measures to keep yourself and others safe. We offer many different ways to help
							so even if you are vulnerable you may still be able to get involved remotely.
						</p>
						<br>
					</div>

					<div class="col-lg-6">
						<div class="embed-responsive embed-responsive-16by9">
							<video width="320" height="240" controls>
								<source src="images/reachoutpitch.mp4" type="video/mp4">
							</video>
						</div>
					</div>
				</div>
			</div>

			<div class="row" class="midpage">
				<div class="col-md-4">
					<img src="images/guidelines.jfif" alt="gov guidelines" class="homeimage">
					<h2>In Self-Isolation or Vulnerable?</h2>
					<p>If you are in need of services then you will be able to sign up and provide us with information about your current condition. You can then make requests for any essential items such as collection and drop-off for food or medication.</p>
				</div>
				<div class="col-md-4">
					<img src="images/food.jfif" alt="food" class="homeimage">
					<h2>In Need Of Help?</h2>
					<p>We are happy to create the connection between those in isolation, and those willing to assist with things like food shopping, dog walking, plant watering, so you can get what you need, when you need it.</p>
				</div>
				<div class="col-md-4">
					<img src="images/phonecall.jfif" alt="phonecall" class="homeimage">
					<h2>Just Want a Conversation?</h2>
					<p>Being in self-isolation can lead to loneliness for many people. We aim to prevent this by allowing users to connect over the phone to speak when they may have nobody else to listen.</p>
				</div>
			</div>
		</div>
	</div>
</body>

</html>