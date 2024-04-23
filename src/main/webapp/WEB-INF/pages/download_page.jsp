<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Group</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
</head>
<body style="background-color:#bdb66b;" >
<h2><a href="https://iportal.com.ua/wp-content/uploads/Contacts.xml">Student group</a></h2>
<h2><a href="https://senior-pomidor.com.ua/Contacts25.xml">Work group</a></h2>
<div class="container" >
    <form role="form" class="form-horizontal" action="/download" method="post">
        <div class="form-group"><input type="text" class="form-control" name="url" placeholder="You url"></div>
        <div class="form-group"><input type="submit"  class="btn btn-danger"  value="Add students to contacts"></div>
        <div class="form-group"><input type="submit" class="btn btn-danger"  value="Add workers to contacts"></div>
    </form>
</div>
</body>
</html>