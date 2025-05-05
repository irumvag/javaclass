<%@include file="Sections/header.jsp" %>
<%@include file="Sections/navbar.jsp" %>

<div class="container mt-5">
    <%-- Error Message --%>
    <% if (request.getAttribute("errorMessage") != null) {%>
    <div class="alert alert-yellow alert-dismissible fade show" role="alert">
        <%= request.getAttribute("errorMessage")%>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% }%>
    <div class="row justify-content-center vh-100">
        <div class="col-md-8">
            <div class="card shadow-lg">
                <div class="card-header bg-yellow text-black">
                    <h3 class="text-center mb-0">Create Account</h3>
                </div>
                <div class="card-body">
                    <form action="RegisterServlet" method="POST">
                         <input type="hidden" name="csrfToken" value="${csrfToken}">
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
                            <%-- Add after password input --%>
                            <div class="password-strength mt-2 mb-4">
                                <div class="progress mb-3" style="height: 3px; width:50%">
                                    <div class="progress-bar" role="progressbar"></div>
                                </div>
                                <small class="text-muted">Password must contain at least 8 characters, 1 number and 1 special character</small>
                            </div>

                            <script>
                                document.getElementById('password').addEventListener('input', function (e) {
                                    const strength = {
                                        0: {color: '#dc3545', width: '25%'},
                                        1: {color: '#ffc107', width: '50%'},
                                        2: {color: '#28a745', width: '100%'}
                                    };

                                    const progress = document.querySelector('.progress-bar');
                                    const value = e.target.value;

                                    let score = 0;
                                    if (value.length >= 8)
                                        score++;
                                    if (/\d/.test(value))
                                        score++;
                                    if (/[!@#$%^&*]/.test(value))
                                        score++;

                                    progress.style.width = strength[score].width;
                                    progress.style.backgroundColor = strength[score].color;
                                });
                            </script>
                        </div>
                        <button type="submit" class="btn btn-warning w-100">Register</button>
                        <p class="text-center mt-3">
                            Already have an account? <a href="login.jsp" style="text-decoration: none; color: green;">Login here</a>
                        </p>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="Sections/footer.jsp" %>