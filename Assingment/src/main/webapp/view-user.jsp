<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>View User</title>
        <style>
            .card {
                width: 400px;
                margin: 100px auto;
                padding: 20px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                border-radius: 8px;
                font-family: Arial, sans-serif;
            }
            .card h2 {
                text-align: center;
                margin-bottom: 20px;
            }
            .info {
                margin: 10px 0;
            }
            .back-btn {
                display: block;
                margin-top: 20px;
                text-align: center;
                text-decoration: none;
                color: white;
                background: #007bff;
                padding: 10px;
                border-radius: 4px;
            }
            .back-btn:hover {
                background: #0056b3;
            }
        </style>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container-fluid d-flex justify-content-center w-100 bg-warning" style="min-height: 50px;">
            <img
                src=""
                alt="UMS logo"
                class="p-1 m-auto img-fluid"
                />
        </div> 
        <div class="card">
            <h2>User Details</h2>
            <div class="info"><strong>ID:</strong> ${user.id}</div>
            <div class="info"><strong>Username:</strong> ${user.username}</div>
            <div class="info"><strong>Phone Number:</strong> 0${user.phone}</div>
            <div class="info"><strong>Email:</strong> ${user.email}</div>
            <div class="info"><strong>Country:</strong> ${user.country}</div>
            <a href="dashboard" class="back-btn">Back to Dashboard</a>
        </div>
        <div class="d-flex justify-content-end p-3 bg-warning mt-5">
            <form action="logout" method="get">
                <button type="submit" class="btn btn-danger">Logout</button>
            </form>
        </div>
    </body>
</html>