<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Manage Session</title>
</head>
<body>
<div>
	<a class="options" href="./batch">Display all Batches</a> 
</div>
<div class="options">
	<form action="./batch" method="get">
		Display Batch with id: <input type="text" size=25 name="batchId">
	</form>
</div>
<div class="options">
	<form action="./batch?action=delete" method="get">
		Delete Batch with id: <input type="text" size=5 name="id2Delete">
	</form>
</div>
<div>
	<a class="options" href="./AddBatch.html">Add New Batch</a> 
</div>

<div>
    <br>
	<a class="options" href="./enrollment">Display Session Enrollment</a> 
</div>
<div class="options">
	<form action="./enrollment" method="get">
		Display Session with id: <input type="text" size=25 name="enrollmentId">
	</form>
</div>
<div class="options">
	<form action="./enrollment?action=delete" method="get">
		Delete Session with id: <input type="text" size=5 name="id2Delete">
	</form>
</div>
<div>
	<a class="options" href="./AddEnrollment.html">Add Session Batch</a> 
</div>

</body>
</html>