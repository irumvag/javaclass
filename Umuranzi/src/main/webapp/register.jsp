<%@include file="header.jsp" %>
<%@include file="navbar.jsp" %>

<div class="container mt-5">
    <%-- Error Message --%>
    <% if (request.getAttribute("errorMessage") != null) {%>
    <div class="alert alert-yellow alert-dismissible fade show" role="alert">
        <%= request.getAttribute("errorMessage")%>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% }%>
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-lg">
                <div class="card-header bg-yellow text-black">
                    <h3 class="text-center mb-0">Create Account</h3>
                </div>
                <div class="card-body">
                    <form action="RegisterServlet" method="POST">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="fullName" class="form-label">Full Name</label>
                                    <input type="text" class="form-control" id="fullName" name="fullName" required>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="email" class="form-label">Email address</label>
                                    <input type="email" class="form-control" id="email" name="email" required>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="password" class="form-control" id="password" name="password" required>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="confirmPassword" class="form-label">Confirm Password</label>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                                </div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-warning w-100">Register</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="footer.jsp" %>