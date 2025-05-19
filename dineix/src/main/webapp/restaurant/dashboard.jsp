<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Restaurant Owner Dashboard | DineIX</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Manage your restaurant's meal packages and view statistics on DineIX.">
        <meta property="og:title" content="Restaurant Owner Dashboard - DineIX">
        <meta property="og:description" content="Access your DineIX restaurant dashboard to manage meal packages and redeem user meals.">
        <meta property="og:image" content="https://images.pexels.com/photos/3184183/pexels-photo-3184183.jpeg">
        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Bootstrap Icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css" rel="stylesheet">
        <!-- AOS Animations -->
        <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
        <!-- QR Scanner Library -->
        <script src="https://unpkg.com/html5-qrcode@2.3.4/html5-qrcode.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
        <style>
            :root {
                --primary-yellow: #FFD700;
                --secondary-black: #000000;
                --bg-color: #f8f9fa;
                --card-bg: #ffffff;
                --text-color: #212529;
            }

            body {
                background-color: var(--bg-color);
                color: var(--text-color);
                font-family: 'Poppins', sans-serif;
                scroll-behavior: smooth;
            }

            .bg-yellow {
                background-color: var(--primary-yellow) !important;
            }

            .text-black {
                color: var(--secondary-black) !important;
            }

            .navbar-brand img {
                transition: transform 0.3s ease;
            }

            .navbar-brand img:hover {
                transform: scale(1.1);
            }

            .sidebar {
                background: linear-gradient(180deg, var(--primary-yellow), #ffca28);
                min-height: 100vh;
                padding: 1.5rem;
                transition: width 0.3s ease;
            }

            .sidebar.collapsed {
                width: 80px;
            }

            .sidebar .nav-link {
                color: var(--secondary-black);
                font-weight: 500;
                padding: 0.75rem;
                border-radius: 8px;
                transition: background-color 0.3s ease;
            }

            .sidebar .nav-link:hover {
                background-color: rgba(0, 0, 0, 0.1);
            }

            .sidebar .nav-link.active {
                background-color: var(--secondary-black);
                color: var(--primary-yellow);
            }

            .main-content {
                padding: 2rem;
            }

            .card {
                border: none;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                transition: transform 0.3s ease;
            }

            .card:hover {
                transform: translateY(-5px);
            }

            .btn-warning {
                background-color: var(--primary-yellow);
                border: none;
                color: var(--secondary-black);
                font-weight: 600;
                border-radius: 8px;
            }

            .btn-warning:hover {
                background-color: #e6c200;
            }

            .stat-card {
                background: var(--card-bg);
                border-radius: 12px;
                padding: 1.5rem;
                text-align: center;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }

            .footer {
                background-color: var(--secondary-black);
                color: var(--primary-yellow);
                padding: 2rem 0;
            }

            .footer a {
                color: var(--primary-yellow);
                text-decoration: none;
            }

            .footer a:hover {
                color: #fff;
            }

            #loadingOverlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                display: none;
                justify-content: center;
                align-items: center;
                z-index: 9999;
            }

            #backToTop {
                z-index: 1000;
            }

            /* QR Scanner Styles */
            #qr-scanner {
                width: 100%;
                max-width: 500px;
                margin: 0 auto;
                border: 2px solid var(--primary-yellow);
                border-radius: 8px;
                overflow: hidden;
            }

            #qr-reader-results {
                text-align: center;
                margin-top: 1rem;
                font-weight: bold;
            }

            .redeem-method-tabs .nav-link {
                color: var(--secondary-black);
            }

            .redeem-method-tabs .nav-link.active {
                background-color: var(--primary-yellow);
                color: var(--secondary-black);
                font-weight: bold;
            }
            .chart-container {
                background: var(--card-bg);
                border-radius: 12px;
                padding: 1.5rem;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                margin-bottom: 1.5rem;
            }

            .chart-title {
                text-align: center;
                margin-bottom: 1rem;
                color: var(--secondary-black);
            }
        </style>
    </head>
    <body>
        <!-- Loading Overlay -->
        <div id="loadingOverlay">
            <div class="spinner-border text-warning" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>

        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-black sticky-top">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                    <span class="fw-bold">Dine<span class="text-warning">IX</span></span>
               </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navContent" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navContent">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/">Home</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/about">About</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/restaurants">Restaurants</a></li>
                        <li class="nav-item"><a class="nav-link text-warning active" href="${pageContext.request.contextPath}/restaurant/dashboard">Dashboard</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/restaurant/redemption-history"><i class="bi bi-clock-history me-1"></i> Redemption History</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Main Content -->
        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3 sidebar" id="sidebar">
                    <div class="text-center mb-4">
                        <img src="${restaurant.logoUrl != null ? restaurant.logoUrl : '../img/default-restaurant.jpg'}" 
                             class="rounded-circle shadow" width="80" alt="Restaurant Logo" loading="lazy">
                        <h4 class="text-black mt-2">${restaurant.name}</h4>
                        <small class="text-muted">${restaurant.address}</small>
                    </div>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link active" href="#stats" data-bs-toggle="tab" aria-label="Restaurant Statistics">
                                <i class="bi bi-graph-up me-2"></i> Statistics
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#packages" data-bs-toggle="tab" aria-label="Manage Packages">
                                <i class="bi bi-menu-up me-2"></i> Manage Packages
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#redeem" data-bs-toggle="tab" aria-label="Redeem Meals">
                                <i class="bi bi-qr-code me-2"></i> Redeem Meals
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#user" data-bs-toggle="tab" aria-label="User Actions">
                                <i class="bi bi-person-circle me-2"></i> User Actions
                            </a>
                        </li>
                        <li class="nav-item mt-4">
                            <a class="nav-link" href="${pageContext.request.contextPath}/logout" aria-label="Logout">
                                <i class="bi bi-box-arrow-left me-2"></i> Logout
                            </a>
                        </li>
                    </ul>
                </div>

                <!-- Main Content -->
                <div class="col-md-9 main-content">
                    <!-- Error/Success Message -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert" data-aos="fade-up">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            <c:out value="${errorMessage}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert" data-aos="fade-up">
                            <c:out value="${successMessage}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <!-- Dashboard Header -->
                    <div class="d-flex justify-content-between align-items-center mb-4" data-aos="fade-right">
                        <h2 class="text-yellow">Restaurant Management</h2>
                        <div class="balance-card p-3">
                            <h5 class="mb-0">Total Revenue: <strong>${stats.totalRevenue}</strong></h5>
                        </div>
                    </div>

                    <!-- Tab Content -->
                    <div class="tab-content">
                        <!-- Statistics Tab -->
                        <div class="tab-pane fade show active" id="stats" role="tabpanel">
                            <div class="row g-4" data-aos="zoom-in">
                                <div class="col-md-4">
                                    <div class="stat-card">
                                        <h5>Total Followers</h5>
                                        <p class="display-4">${stats.totalFollowers}</p>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="stat-card">
                                        <h5>Total Purchases</h5>
                                        <p class="display-4">${stats.totalPurchases}</p>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="stat-card">
                                        <h5>Total Revenue</h5>
                                        <p class="display-4">$${stats.totalRevenue}</p>
                                    </div>
                                </div>
                            </div>
                            <!-- Charts Section -->
                            <div class="row mt-4">
                                <!-- Monthly Purchases Chart -->
                                <div class="col-md-6">
                                    <div class="chart-container">
                                        <h5 class="chart-title">Monthly Purchases</h5>
                                        <canvas id="monthlyPurchasesChart"></canvas>
                                    </div>
                                </div>

                                <!-- Revenue Trend Chart -->
                                <div class="col-md-6">
                                    <div class="chart-container">
                                        <h5 class="chart-title">Monthly Revenue</h5>
                                        <canvas id="monthlyRevenueChart"></canvas>
                                    </div>
                                </div>

                                <!-- Package Popularity Chart -->
                                <div class="col-md-6">
                                    <div class="chart-container">
                                        <h5 class="chart-title">Package Popularity</h5>
                                        <canvas id="packagePopularityChart"></canvas>
                                    </div>
                                </div>

                                <!-- Follower Growth Chart -->
                                <div class="col-md-6">
                                    <div class="chart-container">
                                        <h5 class="chart-title">Follower Growth</h5>
                                        <canvas id="followerGrowthChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Manage Packages Tab -->
                        <div class="tab-pane fade" id="packages" role="tabpanel">
                            <div class="card" data-aos="zoom-in">
                                <div class="card-header bg-yellow text-black">
                                    <h3 class="mb-0">Manage Meal Packages</h3>
                                </div>
                                <div class="card-body">
                                    <!-- Add Package -->
                                    <h4>Add New Package</h4>
                                    <form action="${pageContext.request.contextPath}/restaurant/add-package" method="POST" class="mb-4">
                                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label for="packageName" class="form-label">Package Name</label>
                                                    <input type="text" class="form-control" id="packageName" name="packageName" required>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label for="numberOfMeals" class="form-label">Number of Meals</label>
                                                    <input type="number" class="form-control" id="numberOfMeals" name="numberOfMeals" min="1" required>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="mb-3">
                                            <label for="description" class="form-label">Description</label>
                                            <textarea class="form-control" id="description" name="description" rows="4"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="price" class="form-label">Price ($)</label>
                                            <input type="number" class="form-control" id="price" name="price" step="0.01" min="0" required>
                                        </div>
                                        <button type="submit" class="btn btn-warning" aria-label="Add Package">Add Package</button>
                                    </form>

                                    <!-- Existing Packages -->
                                    <h4>Your Packages</h4>
                                    <c:if test="${empty restaurantPackages}">
                                        <p>You haven't created any meal packages yet.</p>
                                    </c:if>
                                    <div class="row row-cols-1 row-cols-md-3 g-4">
                                        <c:forEach items="${restaurantPackages}" var="pkg" varStatus="loop">
                                            <div class="col" data-aos="fade-up" data-aos-delay="${loop.index * 100}">
                                                <div class="card h-100">
                                                    <div class="card-body">
                                                        <h5 class="card-title">${pkg.packageName}</h5>
                                                        <p class="card-text">${pkg.description}</p>
                                                        <p><strong>Meals:</strong> ${pkg.numberOfMeals}</p>
                                                        <p><strong>Price:</strong> $${pkg.price}</p>
                                                        <button class="btn btn-outline-warning edit-package-btn" 
                                                                data-package-id="${pkg.packageId}" 
                                                                data-name="${pkg.packageName}"
                                                                data-description="${pkg.description}"
                                                                data-meals="${pkg.numberOfMeals}"
                                                                data-price="${pkg.price}"
                                                                data-bs-toggle="modal" 
                                                                data-bs-target="#editPackageModal"
                                                                aria-label="Edit ${pkg.packageName}">
                                                            Edit
                                                        </button>
                                                        <button class="btn btn-danger delete-package-btn" 
                                                                data-package-id="${pkg.packageId}" 
                                                                data-csrf-token="${csrfToken}"
                                                                aria-label="Delete ${pkg.packageName}">
                                                            Delete
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Redeem Meals Tab -->
                        <div class="tab-pane fade" id="redeem" role="tabpanel">
                            <div class="card" data-aos="zoom-in">
                                <div class="card-header bg-yellow text-black">
                                    <h3 class="mb-0">Redeem User Meals</h3>
                                </div>
                                <div class="card-body">
                                    <ul class="nav nav-tabs redeem-method-tabs mb-4" id="redeemTabs" role="tablist">
                                        <li class="nav-item" role="presentation">
                                            <button class="nav-link active" id="qr-tab" data-bs-toggle="tab" data-bs-target="#qr-redeem" type="button" role="tab" aria-controls="qr-redeem" aria-selected="true">
                                                <i class="bi bi-qr-code me-2"></i>QR Code Scan
                                            </button>
                                        </li>
                                        <li class="nav-item" role="presentation">
                                            <button class="nav-link" id="code-tab" data-bs-toggle="tab" data-bs-target="#code-redeem" type="button" role="tab" aria-controls="code-redeem" aria-selected="false">
                                                <i class="bi bi-123 me-2"></i>6-Digit Code
                                            </button>
                                        </li>
                                        <li class="nav-item" role="presentation">
                                            <button class="nav-link" id="manual-tab" data-bs-toggle="tab" data-bs-target="#manual-redeem" type="button" role="tab" aria-controls="manual-redeem" aria-selected="false">
                                                <i class="bi bi-keyboard me-2"></i>Manual Entry
                                            </button>
                                        </li>
                                    </ul>

                                    <div class="tab-content" id="redeemTabContent">
                                        <!-- QR Code Redeem -->
                                        <div class="tab-pane fade show active" id="qr-redeem" role="tabpanel" aria-labelledby="qr-tab">
                                            <div id="qr-scanner"></div>
                                            <div id="qr-reader-results"></div>
                                            <form id="qrRedeemForm" action="${pageContext.request.contextPath}/restaurant/redeem-meal" method="POST" style="display:none;">
                                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                                <input type="hidden" name="redemptionCode" id="qrRedemptionCode">
                                                <button type="submit" class="btn btn-warning mt-3">Confirm Redemption</button>
                                            </form>
                                        </div>

                                        <!-- 6-Digit Code Redeem -->
                                        <div class="tab-pane fade" id="code-redeem" role="tabpanel" aria-labelledby="code-tab">
                                            <form id="codeRedeemForm" action="${pageContext.request.contextPath}/restaurant/redeem-meal" method="POST">
                                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                                <div class="mb-3">
                                                    <label for="sixDigitCode" class="form-label">Enter 6-Digit Code</label>
                                                    <div class="d-flex">
                                                        <input type="text" class="form-control text-center me-2" id="digit1" maxlength="1" pattern="[0-9]" style="width: 50px;">
                                                        <input type="text" class="form-control text-center me-2" id="digit2" maxlength="1" pattern="[0-9]" style="width: 50px;">
                                                        <input type="text" class="form-control text-center me-2" id="digit3" maxlength="1" pattern="[0-9]" style="width: 50px;">
                                                        <input type="text" class="form-control text-center me-2" id="digit4" maxlength="1" pattern="[0-9]" style="width: 50px;">
                                                        <input type="text" class="form-control text-center me-2" id="digit5" maxlength="1" pattern="[0-9]" style="width: 50px;">
                                                        <input type="text" class="form-control text-center" id="digit6" maxlength="1" pattern="[0-9]" style="width: 50px;">
                                                    </div>
                                                    <input type="hidden" name="redemptionCode" id="sixDigitCode">
                                                </div>
                                                <button type="submit" class="btn btn-warning">Redeem Meal</button>
                                            </form>
                                        </div>

                                        <!-- Manual Redeem -->
                                        <div class="tab-pane fade" id="manual-redeem" role="tabpanel" aria-labelledby="manual-tab">
                                            <form id="manualRedeemForm" action="${pageContext.request.contextPath}/restaurant/redeem-meal" method="POST">
                                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                                <div class="mb-3">
                                                    <label for="userEmail" class="form-label">User Email</label>
                                                    <input type="email" class="form-control" id="userEmail" name="userEmail">
                                                </div>
                                                <div class="mb-3">
                                                    <label for="purchaseId" class="form-label">Purchase ID</label>
                                                    <input type="number" class="form-control" id="purchaseId" name="purchaseId">
                                                </div>
                                                <button type="submit" class="btn btn-warning">Redeem Meal</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- User Actions Tab -->
                        <div class="tab-pane fade" id="user" role="tabpanel">
                            <div class="card" data-aos="zoom-in">
                                <div class="card-header bg-yellow text-black">
                                    <h3 class="mb-0">User Actions</h3>
                                </div>
                                <div class="card-body">
                                    <p>Access user features like purchasing meal packages from other restaurants.</p>
                                    <a href="${pageContext.request.contextPath}/user/dashboard" class="btn btn-warning" aria-label="Go to User Dashboard">Go to User Dashboard</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Edit Package Modal -->
        <div class="modal fade" id="editPackageModal" tabindex="-1" aria-labelledby="editPackageModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-yellow text-black">
                        <h5 class="modal-title" id="editPackageModalLabel">Edit Meal Package</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="editPackageForm" action="${pageContext.request.contextPath}/restaurant/update-package" method="POST">
                            <input type="hidden" name="csrfToken" value="${csrfToken}">
                            <input type="hidden" name="packageId" id="editPackageId">
                            <div class="mb-3">
                                <label for="editPackageName" class="form-label">Package Name</label>
                                <input type="text" class="form-control" id="editPackageName" name="packageName" required>
                            </div>
                            <div class="mb-3">
                                <label for="editDescription" class="form-label">Description</label>
                                <textarea class="form-control" id="editDescription" name="description" rows="4"></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="editNumberOfMeals" class="form-label">Number of Meals</label>
                                <input type="number" class="form-control" id="editNumberOfMeals" name="numberOfMeals" min="1" required>
                            </div>
                            <div class="mb-3">
                                <label for="editPrice" class="form-label">Price ($)</label>
                                <input type="number" class="form-control" id="editPrice" name="price" step="0.01" min="0" required>
                            </div>
                            <button type="submit" class="btn btn-warning" aria-label="Save Package Changes">Save Changes</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <footer class="footer">
            <div class="container text-center">
                <p>© 2025 DineIX. All rights reserved.</p>
                <div class="d-flex justify-content-center gap-3">
                    <a href="#" aria-label="Facebook"><i class="bi bi-facebook fs-4"></i></a>
                    <a href="#" aria-label="Twitter"><i class="bi bi-twitter fs-4"></i></a>
                    <a href="#" aria-label="Instagram"><i class="bi bi-instagram fs-4"></i></a>
                </div>
            </div>
        </footer>

        <!-- Back to Top -->
        <button id="backToTop" class="btn btn-warning position-fixed bottom-0 end-0 m-3" style="display: none;" aria-label="Back to top">Top</button>

        <!-- Scripts -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
        <script>
            // Initialize AOS
            AOS.init({duration: 1000});
            // Back to top button
            const backToTop = document.getElementById("backToTop");
            window.addEventListener("scroll", () => {
            backToTop.style.display = window.scrollY > 300 ? "block" : "none";
            });
            backToTop.addEventListener("click", () => window.scrollTo({top: 0, behavior: "smooth"}));
            // Sidebar toggle (optional, for mobile)
            const sidebar = document.getElementById("sidebar");
            document.querySelector(".navbar-toggler").addEventListener("click", () => {
            sidebar.classList.toggle("collapsed");
            });
            // Edit package modal
            document.querySelectorAll('.edit-package-btn').forEach(button => {
            button.addEventListener('click', function () {
            document.getElementById('editPackageId').value = this.getAttribute('data-package-id');
            document.getElementById('editPackageName').value = this.getAttribute('data-name');
            document.getElementById('editDescription').value = this.getAttribute('data-description');
            document.getElementById('editNumberOfMeals').value = this.getAttribute('data-meals');
            document.getElementById('editPrice').value = this.getAttribute('data-price');
            });
            });
            // Delete package
            document.querySelectorAll('.delete-package-btn').forEach(button => {
            button.addEventListener('click', function () {
            const packageId = this.getAttribute('data-package-id');
            const csrfToken = this.getAttribute('data-csrf-token');
            const loadingOverlay = document.getElementById('loadingOverlay');
            if (confirm('Are you sure you want to delete this package?')) {
            loadingOverlay.style.display = 'block';
            fetch('${pageContext.request.contextPath}/restaurant/delete-package', {
            method: 'POST',
                    headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `packageId=${packageId}&csrfToken=${csrfToken}`
            })
                    .then(response => response.json())
                    .then(data => {
                    loadingOverlay.style.display = 'none';
                    if (data.success) {
                    this.closest('.col').remove();
                    } else {
                    alert(data.message || 'An error occurred.');
                    }
                    })
                    .catch(error => {
                    loadingOverlay.style.display = 'none';
                    console.error('Error:', error);
                    alert('An error occurred while processing your request.');
                    });
            }
            });
            });
            // QR Code Scanner
            let html5QrcodeScanner;
            function onQRScanSuccess(decodedText, decodedResult) {
            document.getElementById('qr-reader-results').innerHTML = `Scanned: ${decodedText}`;
            document.getElementById('qrRedemptionCode').value = decodedText;
            document.getElementById('qrRedeemForm').style.display = 'block';
            // Stop scanning after successful scan
            html5QrcodeScanner.clear().then(_ => {
            console.log("QR Code scanning stopped.");
            }).catch(err => {
            console.log("Error stopping scanner", err);
            });
            }

            function onQRScanError(errorMessage) {
            // Handle scan error
            }

            // Initialize QR Scanner when tab is shown
            document.getElementById('qr-tab').addEventListener('shown.bs.tab', function () {
            html5QrcodeScanner = new Html5QrcodeScanner(
                    "qr-scanner",
            {fps: 10, qrbox: 250},
                    /* verbose= */ false
                    );
            html5QrcodeScanner.render(onQRScanSuccess, onQRScanError);
            });
            // 6-Digit Code Input Handling
            const digitInputs = document.querySelectorAll('#code-redeem input[type="text"]');
            digitInputs.forEach((input, index) => {
            input.addEventListener('input', function () {
            if (this.value.length === 1) {
            if (index < digitInputs.length - 1) {
            digitInputs[index + 1].focus();
            }
            }

            // Combine all digits
            let fullCode = '';
            digitInputs.forEach(digit => {
            fullCode += digit.value;
            });
            document.getElementById('sixDigitCode').value = fullCode;
            });
            // Allow backspace to move to previous input
            input.addEventListener('keydown', function (e) {
            if (e.key === 'Backspace' && this.value.length === 0 && index > 0) {
            digitInputs[index - 1].focus();
            }
            });
            });
            document.addEventListener('DOMContentLoaded', function() {
            // Monthly Purchases Chart
            const monthlyPurchasesCtx = document.getElementById('monthlyPurchasesChart').getContext('2d');
            new Chart(monthlyPurchasesCtx, {
            type: 'bar',
                    data: {
                    labels: [
            <c:forEach items="${stats.monthlyPurchases}" var="entry">
                    '${entry.key}',
            </c:forEach>
                    ],
                            datasets: [{
                            label: 'Purchases',
                                    data: [
            <c:forEach items="${stats.monthlyPurchases}" var="entry">
                ${entry.value},
            </c:forEach>
                                    ],
                                    backgroundColor: '#FFD700',
                                    borderColor: '#000000',
                                    borderWidth: 1
                            }]
                    },
                    options: {
                    responsive: true,
                            scales: {
                            y: {
                            beginAtZero: true,
                                    title: {
                                    display: true,
                                            text: 'Number of Purchases'
                                    }
                            },
                                    x: {
                                    title: {
                                    display: true,
                                            text: 'Month'
                                    }
                                    }
                            }
                    }
            });
            // Monthly Revenue Chart
            const monthlyRevenueCtx = document.getElementById('monthlyRevenueChart').getContext('2d');
            new Chart(monthlyRevenueCtx, {
            type: 'line',
                    data: {
                    labels: [
            <c:forEach items="${stats.monthlyRevenue}" var="entry">
                    '${entry.key}',
            </c:forEach>
                    ],
                            datasets: [{
                            label: 'Revenue ($)',
                                    data: [
            <c:forEach items="${stats.monthlyRevenue}" var="entry">
                ${entry.value},
            </c:forEach>
                                    ],
                                    borderColor: '#FFD700',
                                    backgroundColor: 'rgba(255, 215, 0, 0.1)',
                                    fill: true,
                                    tension: 0.4
                            }]
                    },
                    options: {
                    responsive: true,
                            scales: {
                            y: {
                            beginAtZero: true,
                                    title: {
                                    display: true,
                                            text: 'Revenue ($)'
                                    }
                            },
                                    x: {
                                    title: {
                                    display: true,
                                            text: 'Month'
                                    }
                                    }
                            }
                    }
            });
            // Package Popularity Chart
            const packagePopularityCtx = document.getElementById('packagePopularityChart').getContext('2d');
            new Chart(packagePopularityCtx, {
            type: 'doughnut',
                    data: {
                    labels: [
            <c:forEach items="${stats.packagePopularity}" var="entry">
                    '${entry.key}',
            </c:forEach>
                    ],
                            datasets: [{
                            label: 'Purchases',
                                    data: [
            <c:forEach items="${stats.packagePopularity}" var="entry">
                ${entry.value},
            </c:forEach>
                                    ],
                                    backgroundColor: [
                                            '#FFD700', '#FFCA28', '#000000', '#FFC107', '#FFA000'
                                    ],
                                    borderColor: '#FFFFFF',
                                    borderWidth: 1
                            }]
                    },
                    options: {
                    responsive: true,
                            plugins: {
                            legend: {
                            position: 'right'
                            }
                            }
                    }
            });
            // Follower Growth Chart
            const followerGrowthCtx = document.getElementById('followerGrowthChart').getContext('2d');
            new Chart(followerGrowthCtx, {
            type: 'line',
                    data: {
                    labels: [
            <c:forEach items="${stats.followerGrowth}" var="entry">
                    '${entry.key}',
            </c:forEach>
                    ],
                            datasets: [{
                            label: 'Followers',
                                    data: [
            <c:forEach items="${stats.followerGrowth}" var="entry">
                ${entry.value},
            </c:forEach>
                                    ],
                                    borderColor: '#FFD700',
                                    backgroundColor: 'rgba(255, 215, 0, 0.1)',
                                    fill: true,
                                    tension: 0.4
                            }]
                    },
                    options: {
                    responsive: true,
                            scales: {
                            y: {
                            beginAtZero: true,
                                    title: {
                                    display: true,
                                            text: 'Number of Followers'
                                    }
                            },
                                    x: {
                                    title: {
                                    display: true,
                                            text: 'Month'
                                    }
                                    }
                            }
                    }
            });
            });
        </script>
    </body>
</html>