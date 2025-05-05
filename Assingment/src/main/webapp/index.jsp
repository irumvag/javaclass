<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page|UMS</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    </head>
    <body>
        <div class="container-fluid d-flex justify-content-center w-100 bg-warning" style="min-height: 50px;">
            <img
                src=""
                alt="UMS logo"
                class="p-1 m-auto img-fluid"
            />
        </div> 
        <div class="container-fluid bg-light justify-content-center align-items-center pt-3 d-flex flex-column ">
            <div class="display-6">
                Sign in into your account
        </div>
        </div>
        <div class="container-fluid bg-light vh-100">
            <div class="d-flex justify-content-center align-items-center py-2">
                <div class="m-auto d-flex flex-column align-items-middle p-4 bg-white shadow rounded">
                    <form action="login" method="post" class="mt-5">
                        <label for="label" class="text-center d-block mb-4">Good User management is good act of knowing them and manage them as you wish. you are most welcome!</label><br>
                            <% if(request.getAttribute("error") != null) { %>
                            <p style="color: red"><%= request.getAttribute("error") %></p>
                            <% } %>
                        <div class="mb-3 w-100">
                            <label for="phone" class="form-label">Username:</label>
                            <input type="text" name="username" id="phone" class="form-control border-0 bg-light rounded" required>
                        </div>
                        <div class="mb-3 w-100">
                            <label for="password" class="form-label">Account Password:</label>
                            <input type="password" name="password" id="password" class="form-control border-0 bg-light rounded" required>
                        </div>
                        <p class="text-center mb-3"><a href="#">Forgot password?</a></p>
                        <button type="submit" class="btn btn-warning p-2 w-100">Login</button>
                        <p class="text-center mt-3">No Account <a href="register.jsp" style="text-decoration: none;color: greenyellow">Sign up</a>,    By clicking login, you accept user terms and policies.</p>
                    </form>
                </div>
            </div>
        </div>
        <footer style="background-color: #D9D9D9;">
            <p class="text-center m-auto">
                &copy; 2025 UMS</p>
        </footer>   
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>
