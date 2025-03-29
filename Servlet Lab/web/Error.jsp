<%-- 
    Document   : Error
    Created on : 27 Mar 2025, 17:23:53
    Author     : Chairman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
    </head>
    <body>
        <center>
		<h1>Error</h1>
		<h2><%=exception.getMessage() %><br/> </h2>
	</center>	
    </body>
</html>
