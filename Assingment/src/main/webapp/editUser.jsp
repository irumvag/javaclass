<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Edit User | UMS</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
    </head>
    <body>
        <%
            if (session.getAttribute("user") == null) {
                response.sendRedirect("index.jsp");
                return;
            }
        %>
        <a href="dashboard">
            <div class="container-fluid d-flex justify-content-center w-100 bg-warning" style="min-height: 50px;">
                <img src="" alt="UMS logo" class="p-1 m-auto img-fluid" />
            </div>
        </a>

        <div class="container-fluid bg-light justify-content-center align-items-center pt-3 d-flex flex-column">
            <div class="display-6">Edit User Details</div>
        </div>

        <div class="container-fluid bg-light vh-100">
            <div class="d-flex justify-content-center align-items-center py-2">
                <div class="m-auto d-flex flex-column align-items-middle p-4 bg-white shadow rounded w-50">
                    <form action="updateUser" method="post" class="mt-3">
                        <input type="hidden" name="id" value="${user.id}" />

                        <div class="mb-3 w-100">
                            <label for="username" class="form-label">Username:</label>
                            <input type="text" name="username" id="username" class="form-control border-0 bg-light rounded" value="${user.username}" required />
                        </div>

                        <div class="mb-3 w-100">
                            <label for="password" class="form-label">New Password:</label>
                            <input type="password" name="password" id="password" class="form-control border-0 bg-light rounded" required />
                        </div>

                        <div class="mb-3 w-100">
                            <label for="phone" class="form-label">Phone:</label>
                            <input type="text" name="phone" id="phone" class="form-control border-0 bg-light rounded" value="0${user.phone}" required />
                        </div>

                        <div class="mb-3 w-100">
                            <label for="email" class="form-label">Email:</label>
                            <input type="email" name="email" id="email" class="form-control border-0 bg-light rounded" value="${user.email}" required />
                        </div>

                        <div class="mb-3 w-100">
                            <label for="country" class="form-label">Country:</label>
                            <input type="text" name="country" id="country" class="form-control border-0 bg-light rounded" value="${user.country}" required />
                        </div>

                        <button type="submit" class="btn btn-success w-100 p-2">Update User</button>
                        <a href="dashboard" class="btn btn-secondary mt-2 w-100">Cancel</a>
                    </form>
                </div>
            </div>
        </div>

        <footer style="background-color: #D9D9D9;">
            <p class="text-center m-auto">&copy; 2025 UMS</p>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
