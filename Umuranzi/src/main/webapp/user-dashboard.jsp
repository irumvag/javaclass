<%@include file="header.jsp" %>
<%@include file="auth-check.jsp" %>

<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3 bg-yellow vh-100 p-4">
            <div class="text-center mb-4">
                <img src="img/user-avatar.png" class="rounded-circle" width="100" alt="User Avatar">
                <h4 class="text-black mt-2">${user.fullName}</h4>
            </div>
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link text-black d-flex align-items-center" href="#packages">
                        <i class="bi bi-house me-2"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-black d-flex align-items-center" href="#packages">
                        <i class="bi bi-box-seam me-2"></i> My Packages
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-black d-flex align-items-center" href="#tickets">
                        <i class="bi bi-qr-code me-2"></i> Generate Ticket
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-black d-flex align-items-center" href="#history">
                        <i class="bi bi-clock-history me-2"></i> History
                    </a>
                </li>
            </ul>
        </div>
        
        <!-- Main Content -->
        <div class="col-md-9 p-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="text-yellow">Dashboard Overview</h2>
                <div class="balance-card p-3 bg-yellow rounded">
                    <h5 class="mb-0">Available Meals: <strong>15</strong></h5>
                </div>
            </div>

            <!-- Meal Packages Grid -->
            <div class="row row-cols-1 row-cols-md-3 g-4">
                <div class="col">
                    <div class="card meal-card h-100">
                        <img src="meal1.jpg" class="card-img-top" alt="Meal Package">
                        <div class="card-body">
                            <h5 class="card-title">Premium Package</h5>
                            <p class="card-text">10 meals - $150</p>
                            <button class="btn btn-warning w-100">Purchase</button>
                        </div>
                    </div>
                </div>
                <!-- Repeat other packages -->
            </div>
        </div>
    </div>
</div>

<%@include file="footer.jsp" %>