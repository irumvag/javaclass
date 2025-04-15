<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Happened</title>
    </head>
    <body>
        <div style="background-color: yellow"> User management website</div>
        <div style="
             display: block;
             margin: auto;
             width: 400px;
             height: 500px;
             ">
        <h1><center>
		<h1>Error</h1>
                <h2><%=exception.getMessage() %><br/></h2>
	</center>
        </h1>
        </div>
    </body>
</html>
