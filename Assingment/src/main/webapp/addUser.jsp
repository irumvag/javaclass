<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add User|UMS</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <%
            if (session.getAttribute("user") == null) {
                response.sendRedirect("index.jsp");
                return;
            }
        %>
        <!-- Header / Logo -->
        <div class="container-fluid d-flex justify-content-center w-100 bg-warning" style="min-height: 50px;">
            <img src="images/ums-logo.png" alt="UMS logo" class="p-1 m-auto img-fluid" style="height: 40px;">
        </div> 

        <!-- Heading -->
        <div class="container-fluid bg-light justify-content-center align-items-center pt-3 d-flex flex-column">
            <div class="display-6">Add user</div>
        </div>

        <!-- Sign-Up Form -->
        <div class="container-fluid bg-light vh-100">
            <div class="d-flex justify-content-center align-items-center py-2">
                <div class="m-auto d-flex flex-column align-items-middle p-4 bg-white shadow rounded" style="width: 100%; max-width: 500px;">
                    <form action="adduser" method="post" class="mt-3">
                        <% if (request.getAttribute("error") != null) {%>
                        <p style="color: red; text-align:center;"><%= request.getAttribute("error")%></p>
                        <% }%>

                        <div class="mb-3">
                            <label for="username" class="form-label">Username:</label>
                            <input type="text" name="username" id="username" class="form-control border-0 bg-light rounded" required>
                        </div>

                        <div class="mb-3">
                            <label for="phone" class="form-label">Phone Number:</label>
                            <input type="text" name="phone" id="phone" class="form-control border-0 bg-light rounded" required>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">Email Address:</label>
                            <input type="email" name="email" id="email" class="form-control border-0 bg-light rounded" required>
                        </div>

                        <div class="mb-3">
                            <label for="country" class="form-label">Country:</label>
                            <input type="text" name="country" id="country" class="form-control border-0 bg-light rounded" required>
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label">Password:</label>
                            <input type="password" name="password" id="password" class="form-control border-0 bg-light rounded" required>
                        </div>

                        <button type="submit" class="btn btn-warning p-2 w-100">Add User</button>
                        <a href="dashboard" class="btn btn-secondary mt-2 w-100">Cancel</a>

                    </form>
                </div>
            </div>
        </div>

        <footer style="background-color: #D9D9D9;">
            <p class="text-center m-auto">&copy; 2025 UMS</p>
        </footer>   

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>
