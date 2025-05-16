<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Restaurant Dashboard | DineIX</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Manage your restaurant on DineIX, view stats, and redeem meals.">
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css" rel="stylesheet">
    <!-- AOS Animations -->
    <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
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
        }
        .sidebar .nav-link {
            color: var(--secondary-black);
            font-weight: 500;
            padding: 0.75rem;
            border-radius: 8px;
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
            <a class="navbar-brand" href="${pageContext.request.contextPath}/"><img src="/img/logo.png" alt="DineIX Logo" height="40" loading="lazy"></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navContent" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navContent">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/">Home</a></li>
                    <li class="nav-item"><a class="nav-link text-warning active" href="${pageContext.request.contextPath}/restaurant/dashboard">Dashboard</a></li>
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
                    <img src="/img/user-avatar.png" class="rounded-circle shadow" width="80" alt="Owner Avatar" loading="lazy">
                    <h4 class="text-black mt-2">${sessionScope.fullName}</h4>
                    <small class="text-muted">${sessionScope.email}</small>
                </div>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link active" href="#overview" data-bs-toggle="tab" aria-label="Overview">
                            <i class="bi bi-house me-2"></i> Overview
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#packages" data-bs-toggle="tab" aria-label="Meal Packages">
                            <i class="bi bi-basket me-2"></i> Meal Packages
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#redeem" data-bs-toggle="tab" aria-label="Redeem Meals">
                            <i class="bi bi-check-circle me-2"></i> Redeem Meals
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
                    <h2 class="text-yellow">Restaurant Dashboard</h2>
                </div>

                <!-- Tab Content -->
                <div class="tab-content">
                    <!-- Overview Tab -->
                    <div class="tab-pane fade show active" id="overview" role="tabpanel">
                        <div class="card" data-aos="zoom-in">
                            <div class="card-header bg-yellow text-black">
                                <h3 class="mb-0">Restaurant Statistics</h3>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-4">
                                        <div class="card">
                                            <div class="card-body text-center">
                                                <h5>Total Followers</h5>
                                                <h3>${stats.totalFollowers}</h3>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="card">
                                            <div class="card-body text-center">
                                                <h5>Total Purchases</h5>
                                                <h3>${stats.totalPurchases}</h3>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="card">
                                            <div class="card-body text-center">
                                                <h5>Total Revenue</h5>
                                                <h3>$${stats.totalRevenue}</h3>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Meal Packages Tab -->
                    <div class="tab-pane fade" id="packages" role="tabpanel">
                        <div class="card" data-aos="zoom-in">
                            <div class="card-header bg-yellow text-black">
                                <h3 class="mb-0">Your Meal Packages</h3>
                            </div>
                            <div class="card-body">
                                <c:if test="${empty restaurantPackages}">
                                    <p>No meal packages available.</p>
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
                                <h3 class="mb-0">Redeem Meals</h3>
                            </div>
                            <div class="card-body">
                                <form id="redeemForm">
                                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                                    <div class="mb-3">
                                        <label for="redemptionCode" class="form-label">Redemption Code (Optional)</label>
                                        <input type="text" class="form-control" id="redemptionCode" name="redemptionCode" placeholder="Enter 6-digit code">
                                    </div>
                                    <div class="mb-3">
                                        <label for="purchaseId" class="form-label">Purchase ID (If no code)</label>
                                        <input type="text" class="form-control" id="purchaseId" name="purchaseId" placeholder="Enter purchase ID">
                                    </div>
                                    <div class="mb-3">
                                        <label for="userEmail" class="form-label">User Email (If no code)</label>
                                        <input type="email" class="form-control" id="userEmail" name="userEmail" placeholder="Enter user email">
                                    </div>
                                    <button type="submit" class="btn btn-warning" aria-label="Redeem Meal">Redeem Meal</button>
                                </form>
                                <div id="redeemResult" class="mt-3"></div>
                            </div>
                        </div>
                    </div>
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

    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
    <script>
        AOS.init({ duration: 1000 });

        // Redeem form submission
        document.getElementById('redeemForm').addEventListener('submit', function (e) {
            e.preventDefault();
            const formData = new FormData(this);
            const loadingOverlay = document.getElementById('loadingOverlay');
            const redeemResult = document.getElementById('redeemResult');

            loadingOverlay.style.display = 'block';
            fetch('${pageContext.request.contextPath}/restaurant/dashboard', {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
            .then(response => response.json())
            .then(data => {
                loadingOverlay.style.display = 'none';
                if (data.success) {
                    redeemResult.innerHTML = `<div class="alert alert-success">${data.message} Meals remaining: ${data.mealsRemaining}</div>`;
                    this.reset();
                } else {
                    redeemResult.innerHTML = `<div class="alert alert-danger">${data.message}</div>`;
                }
            })
            .catch(error => {
                loadingOverlay.style.display = 'none';
                redeemResult.innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
                console.error('Error:', error);
            });
        });
    </script>
</body>
</html>