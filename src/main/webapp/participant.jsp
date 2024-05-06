<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Manage Participant List</title>
</head>
<body>
<div>
	<a class="options" href="./participant">Display all Participants</a> 
</div>

<div class="options">
<!-- Note: action= on the form tells it what path to give to the browser                 -->
<!-- ./gambler is the URL path assigned to ParticipantServlet                            -->
<!-- when the user types in some input and pressed enter - the ParticipantServlet is run -->
<!-- the name of input variable is added to the url path as a query parameter        -->
<!-- the method= tells the server which servlet method to run                        -->
<!--       method="get" indicates doGet() method in servlet should run               -->
<!--       method="post" indicates doPost() method in servlet should run             -->
<!--       method="put" indicates doPut() method in servlet should run               -->
<!--       method="delete" indicates doDelete() method in servlet should run         -->
	<form action="./participant" method="get">
		Display Participant with id: <input type="text" size=5 name="id">
	</form>
</div>
<div class="options">
	<form action="./participant" method="get">
		Display Participant with name: <input type="text" size=25 name="fullName">
	</form>
</div>
<div class="options">
	<form action="./participant?action=delete" method="get">
		Delete Participant with id: <input type="text" size=5 name="id2Delete">
	</form>
</div>
<div class="options">
	<form action="./participant?action=input" method="post">
		Update Participant with id: <input type="text" size=5 name="id2Update">
	</form>
</div>

<div>
	<a class="options" href="./AddParticipant.html">Add New Participant</a> 
</div>


</body>
</html>