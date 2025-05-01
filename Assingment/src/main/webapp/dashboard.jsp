<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>User Dashboard | UMS</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <%
            if (session.getAttribute("user") == null) {
                response.sendRedirect("index.jsp");
                return;
            }
        %>
        <div class="container-fluid d-flex justify-content-center w-100 bg-warning" style="min-height: 50px;">
            <img
                src=""
                alt="UMS logo"
                class="p-1 m-auto img-fluid"
                />
        </div> 
        <div class="container mt-5">
            <h2 class="mb-4">Welcome, '<% out.print(session.getAttribute("user")); %>' in User Management System.</h2>
            <a href="addUser.jsp" class="btn btn-success mb-3">Add New User</a>
            <form class="d-flex justify-content-end p-3" action="dashboard" method="post">
                <input class="form-control me-2" type="search" name="search" placeholder="Search..." value="${search}">
                <button class="btn btn-outline-success" type="submit" style="width: 50%">Search</button>
            </form>
            <table class="table table-striped table-hover shadow">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Phone</th>
                        <th>Email</th>
                        <th>Country</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${userList}">
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>0${user.phone}</td>
                            <td>${user.email}</td>
                            <td>${user.country}</td>
                            <td>
                                <a href="viewUser?id=${user.id}" class="btn btn-primary btn-sm">View</a>
                                <a href="editUser?id=${user.id}" class="btn btn-warning btn-sm">Edit</a>
                                <a href="deleteUser?id=${user.id}" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this user?');">
                                    Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="d-flex justify-content-end p-3 bg-warning mt-5">
            <form action="logout" method="get">
                <button type="submit" class="btn btn-danger">Logout</button>
            </form>
        </div>

    </body>
</html>
