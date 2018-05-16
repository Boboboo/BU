<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>File Upload to Database</title>
</head>


<body>
    <center>
        <h1>File Upload to Database</h1>
        <form method="post" action="uploadServlet" enctype="multipart/form-data">
        	<!-- 	<input type="file" name="photo" size="50"/> -->
			<input type="submit" value="Save">
        </form>
    </center>
</body>

</html>