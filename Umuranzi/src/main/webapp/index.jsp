<%@include file="header.jsp" %>
<%@include file="navbar.jsp" %>

<!-- Hero Section -->
<div class="hero-section d-flex align-items-center">
    <div class="container text-white text-center">
        <h1 class="display-4 mb-4">Enjoy Meal Freedom!</h1>
        <p class="lead mb-4">Purchase meal packages from your favorite restaurants and redeem anytime!</p>
        <a href="register.jsp" class="btn btn-warning btn-lg">Get Started</a>
    </div>
</div>

<!-- How It Works Section -->
<section class="py-5">
    <div class="container">
        <h2 class="text-center mb-5">How It Works</h2>
        <div class="row g-4">
            <div class="col-md-3 text-center">
                <div class="card meal-card h-100">
                    <div class="card-body">
                        <h3>1. Register</h3>
                        <p>Create your free account</p>
                    </div>
                </div>
            </div>
            <!-- Add similar cards for other steps -->
        </div>
    </div>
</section>

<%@include file="footer.jsp" %>