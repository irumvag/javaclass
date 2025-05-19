<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Dashboard | DineIX</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Manage your DineIX account, purchase meal packages, and view redemption codes.">
    <meta property="og:title" content="User Dashboard - DineIX">
    <meta property="og:description" content="Access your DineIX dashboard to follow restaurants, buy meal packages, and more.">
    <meta property="og:image" content="https://images.pexels.com/photos/3184183/pexels-photo-3184183.jpeg">
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css" rel="stylesheet">
    <!-- AOS Animations -->
    <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
    <!-- QR Code Generation -->
    <script src="https://cdn.jsdelivr.net/npm/qrcode@1.5.3/build/qrcode.min.js"></script>
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
        .balance-card {
            background: linear-gradient(45deg, var(--primary-yellow), #ffca28);
            color: var(--secondary-black);
            border-radius: 12px;
            padding: 1rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
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
        .redemption-instructions {
            font-size: 0.9rem;
            color: #6c757d;
            margin-top: 0.5rem;
            font-weight: 500;
            visibility: visible !important;
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
                    <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/about">About</a></li>
                    <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/restaurants">Restaurants</a></li>
                    <li class="nav-item"><a class="nav-link text-warning active" href="${pageContext.request.contextPath}/user/dashboard">Dashboard</a></li>
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
                    <img src="../img/user-avatar.png" class="rounded-circle shadow" width="80" alt="User Avatar" loading="lazy">
                    <h4 class="text-black mt-2">${sessionScope.fullName}</h4>
                    <small class="text-muted">${sessionScope.email}</small>
                </div>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link active" href="#profile" data-bs-toggle="tab" aria-label="Profile Settings">
                            <i class="bi bi-person-circle me-2"></i> Profile
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#followed" data-bs-toggle="tab" aria-label="Followed Restaurants">
                            <i class="bi bi-shop me-2"></i> Followed Restaurants
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#packages" data-bs-toggle="tab" aria-label="Meal Packages">
                            <i class="bi bi-basket me-2"></i> Meal Packages
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#payments" data-bs-toggle="tab" aria-label="Payment History">
                            <i class="bi bi-credit-card me-2"></i> Payment History
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#role-request" data-bs-toggle="tab" aria-label="Role Request">
                            <i class="bi bi-person-plus me-2"></i> Role Request
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

                <!-- Notifications -->
                <div class="card mb-4" data-aos="zoom-in">
                    <div class="card-header bg-yellow text-black">
                        <h3 class="mb-0">Notifications</h3>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty notifications}">
                            <p>No new notifications.</p>
                        </c:if>
                        <ul class="list-group">
                            <c:forEach items="${notifications}" var="notification">
                                <li class="list-group-item ${notification.isRead ? '' : 'list-group-item-warning'}" 
                                    data-notification-id="${notification.id}">
                                    <p>${notification.message}</p>
                                    <small>${notification.createdAt}</small>
                                    <c:if test="${!notification.isRead}">
                                        <button class="btn btn-sm btn-warning mark-read-btn" 
                                                data-notification-id="${notification.id}"
                                                data-csrf-token="${csrfToken}"
                                                aria-label="Mark as read">Mark as Read</button>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

                <!-- Dashboard Header -->
                <div class="d-flex justify-content-between align-items-center mb-4" data-aos="fade-right">
                    <h2 class="text-yellow">Dashboard Overview</h2>
                    <div class="d-flex gap-3">
                        <div class="balance-card p-3">
                            <h5 class="mb-0">Available Meals: <strong>${totalMealsRemaining}</strong></h5>
                        </div>
                        <div class="balance-card p-3">
                            <h5 class="mb-0">Total Spent: <strong>RWF ${totalSpent==null?'0':totalSpent}</strong></h5>
                        </div>
                    </div>
                </div>

                <!-- Payment Analytics -->
                <div class="card mb-4" data-aos="fade-up">
                    <div class="card-header bg-yellow text-black">
                        <h3 class="mb-0">Payment Analytics</h3>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-4">
                                <div class="card bg-light">
                                    <div class="card-body text-center">
                                        <h4 class="card-title">Successful Payments</h4>
                                        <h2 class="text-success">${successfulPayments}</h2>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="card bg-light">
                                    <div class="card-body text-center">
                                        <h4 class="card-title">Pending Payments</h4>
                                        <h2 class="text-warning">${pendingPayments}</h2>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="card bg-light">
                                    <div class="card-body text-center">
                                        <h4 class="card-title">Failed Payments</h4>
                                        <h2 class="text-danger">${failedPayments}</h2>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Tab Content -->
                <div class="tab-content">
                    <!-- Profile Tab -->
                    <div class="tab-pane fade show active" id="profile">
                        <div class="row g-4">
                            <div class="col-12 col-lg-6">
                                <div class="card h-100">
                                    <div class="card-header bg-warning text-black">
                                        <h5 class="mb-0">Payment Analytics</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="row text-center">
                                            <div class="col-4">
                                                <div class="h4 text-success">${totalSpent}</div>
                                                <small class="text-muted">Total Spent</small>
                                            </div>
                                            <div class="col-4">
                                                <div class="h4 text-success">${successfulPayments}</div>
                                                <small class="text-muted">Successful</small>
                                            </div>
                                            <div class="col-4">
                                                <div class="h4 text-warning">${pendingPayments}</div>
                                                <small class="text-muted">Pending</small>
                                            </div>
                                        </div>
                                        <canvas id="paymentChart" class="mt-3"></canvas>
                                    </div>
                                </div>
                            </div>
<!--                            <div class="col-12 col-lg-6">
                                <div class="card">
                                    <div class="card-header bg-warning text-black">
                                        <h5 class="mb-0">Recent Notifications</h5>
                                    </div>
                                    <div class="card-body">
                                        <c:choose>
                                            <c:when test="${empty notifications}">
                                                <div class="text-center text-muted">No new notifications</div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="list-group list-group-flush">
                                                    <c:forEach var="notification" items="${notifications}">
                                                        <a href="#" class="list-group-item list-group-item-action ${notification.isRead ? '' : 'fw-bold'}">
                                                            <div class="d-flex justify-content-between">
                                                                <span>${notification.message}</span>
                                                                <small class="text-muted">${notification.createdAt}</small>
                                                            </div>
                                                        </a>
                                                    </c:forEach>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>-->
                    </div>

                    <!-- Followed Restaurants Tab -->
                    <div class="tab-pane fade" id="followed" role="tabpanel">
                        <div class="card" data-aos="zoom-in">
                            <div class="card-header bg-yellow text-black">
                                <h3 class="mb-0">Followed Restaurants</h3>
                            </div>
                            <div class="card-body">
                                <c:if test="${empty followedRestaurants}">
                                    <p>You are not following any restaurants. <a href="${pageContext.request.contextPath}/restaurants" class="text-warning">Explore now</a>.</p>
                                </c:if>
                                <div class="row row-cols-1 row-cols-md-3 g-4">
                                    <c:forEach items="${followedRestaurants}" var="restaurant" varStatus="loop">
                                        <div class="col" data-aos="fade-up" data-aos-delay="${loop.index * 100}">
                                            <div class="card h-100">
                                                <img src="${restaurant.logoUrl != null ? restaurant.logoUrl : 'img/default-restaurant.jpg'}" class="card-img-top" alt="${restaurant.name} logo" style="height: 150px; object-fit: cover;" loading="lazy">
                                                <div class="card-body">
                                                    <h5 class="card-title">${restaurant.name}</h5>
                                                    <p class="card-text">${restaurant.description}</p>
                                                    <button class="btn btn-outline-warning btn-follow followed" 
                                                            data-restaurant-id="${restaurant.restaurantId}" 
                                                            data-restaurant-name="${restaurant.name}"
                                                            data-csrf-token="${csrfToken}"
                                                            aria-label="Unfollow ${restaurant.name}">
                                                        <i class="bi bi-check-circle"></i> Following
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Meal Packages Tab -->
                    <div class="tab-pane fade" id="packages" role="tabpanel">
                        <div class="card" data-aos="zoom-in">
                            <div class="card-header bg-yellow text-black">
                                <h3 class="mb-0">Meal Packages</h3>
                            </div>
                            <div class="card-body">
                                <!-- Available Packages -->
                                <h4>Available Meal Packages</h4>
                                <c:if test="${empty availablePackages}">
                                    <p>No meal packages available. <a href="${pageContext.request.contextPath}/restaurants" class="text-warning">Explore restaurants</a>.</p>
                                </c:if>
                                <div class="row row-cols-1 row-cols-md-3 g-4 mb-4">
                                    <c:forEach items="${availablePackages}" var="mealPackage" varStatus="loop">
                                        <div class="col" data-aos="fade-up" data-aos-delay="${loop.index * 100}">
                                            <div class="card h-100">
                                                <img src="../img/meal-package.jpeg" class="card-img-top" alt="${mealPackage.packageName}" style="height: 150px; object-fit: cover;" loading="lazy">
                                                <div class="card-body">
                                                    <h5 class="card-title">${mealPackage.packageName}</h5>
                                                    <p class="card-text">${mealPackage.description}</p>
                                                    <p><strong>Meals:</strong> ${mealPackage.numberOfMeals}</p>
                                                    <p><strong>Price:</strong> $${mealPackage.price}</p>
                                                    <p><strong>Restaurant:</strong> ${mealPackage.restaurantName}</p>
                                                    <form action="${pageContext.request.contextPath}/user/purchase-package" method="POST">
                                                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                                                        <input type="hidden" name="packageId" value="${mealPackage.packageId}">
                                                        <button type="submit" class="btn btn-warning w-100" aria-label="Purchase ${mealPackage.packageName}">Purchase</button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <!-- Purchased Packages -->
                                <h4>Your Purchased Packages</h4>
                                <c:if test="${empty purchasedPackages}">
                                    <p class="redemption-instructions">You haven't purchased any meal packages. <a href="#packages" class="text-warning">Browse now</a>.</p>
                                </c:if>
                                <div class="row row-cols-1 row-cols-md-3 g-4">
                                    <c:forEach items="${purchasedPackages}" var="purchase" varStatus="loop">
                                        <div class="col" data-aos="fade-up" data-aos-delay="${loop.index * 100}" data-purchase-id="${purchase.purchaseId}">
                                            <div class="card h-100">
                                                <div class="card-body">
                                                    <h5 class="card-title">${purchase.packageName}</h5>
                                                    <p class="card-text">${purchase.description}</p>
                                                    <p><strong>Meals Remaining:</strong> ${purchase.mealsRemaining}</p>
                                                    <p><strong>Purchased On:</strong> ${purchase.purchaseDate}</p>
                                                    <c:if test="${not empty purchase.redemptionCode}">
                                                        <p><strong>Redemption Code:</strong> ${purchase.redemptionCode}</p>
                                                        
                                                        <div id="qrcode" class="mb-3"></div>
                                                        <button class="btn btn-warning redeem-btn mb-2" 
                                                                data-redemption-code="${purchase.redemptionCode}" 
                                                                data-package-name="${purchase.packageName}" 
                                                                data-csrf-token="${csrfToken}" 
                                                                aria-label="Confirm redemption for ${purchase.packageName}">
                                                            Confirm Redemption
                                                        </button>
                                                        <p class="redemption-instructions">Show this code or QR code to the restaurant to redeem a meal.</p>
                                                    </c:if>
                                                    <c:if test="${empty purchase.redemptionCode}">
                                                        <p><strong>Purchase ID:</strong> ${purchase.purchaseId}</p>
                                                        <p class="redemption-instructions">No redemption code available. Provide your purchase ID to the restaurant to redeem a meal.</p>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Payment History Tab -->
                    <div class="tab-pane fade" id="payments" role="tabpanel">
                        <%@ include file="payment-history.jsp" %>
                    </div>

                    <!-- Role Request Tab -->
                    <div class="tab-pane fade" id="role-request" role="tabpanel">
                        <div class="card" data-aos="zoom-in">
                            <div class="card-header bg-yellow text-black">
                                <h3 class="mb-0">Request Restaurant Owner Role</h3>
                            </div>
                            <div class="card-body">
                                <c:if test="${not empty roleRequest}">
                                    <p><strong>Status:</strong> ${roleRequest.status}</p>
                                    <p><strong>Requested On:</strong> ${roleRequest.requestDate}</p>
                                    <c:if test="${roleRequest.status == 'PENDING'}">
                                        <p>Your request is pending approval. We'll notify you once reviewed.</p>
                                    </c:if>
                                </c:if>
                                <c:if test="${empty roleRequest}">
                                    <form action="${pageContext.request.contextPath}/user/request-role" method="POST">
                                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                                        <div class="mb-3">
                                            <label for="requestedRole" class="form-label">Requested Role</label>
                                            <select class="form-select" id="requestedRole" name="requestedRole" required>
                                                <option value="RESTAURANT_OWNER">Restaurant Owner</option>
                                            </select>
                                        </div>
                                        <div class="mb-3">
                                            <label for="restaurantName" class="form-label">Restaurant Name</label>
                                            <input type="text" class="form-control" id="restaurantName" name="restaurantName" required>
                                        </div>
                                        <div class="mb-3">
                                            <label for="restaurantAddress" class="form-label">Restaurant Address</label>
                                            <textarea class="form-control" id="restaurantAddress" name="restaurantAddress" rows="3" required></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="restaurantContact" class="form-label">Restaurant Contact Number</label>
                                            <input type="text" class="form-control" id="restaurantContact" name="restaurantContact" required>
                                        </div>
                                        <div class="mb-3">
                                            <label for="reason" class="form-label">Reason for Request</label>
                                            <textarea class="form-control" id="reason" name="reason" rows="4" required></textarea>
                                        </div>
                                        <button type="submit" class="btn btn-warning" aria-label="Submit Role Request">Submit Request</button>
                                    </form>
                                </c:if>
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

    <!-- Back to Top -->
    <button id="backToTop" class="btn btn-warning position-fixed bottom-0 end-0 m-3" style="display: none;" aria-label="Back to top">Top</button>

    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/qrcode@1.5.3/build/qrcode.min.js"></script>
    <script>
        // Initialize AOS
        AOS.init({ duration: 1000 });

        // Initialize Charts
        function initializeUserCharts() {
            // Meals Usage Chart
            new Chart(document.getElementById('mealsUsageChart'), {
                type: 'doughnut',
                data: {
                    labels: ['Used Meals', 'Remaining Meals'],
                    datasets: [{
                        data: [${totalUsedMeals}, ${totalRemainingMeals}],
                        backgroundColor: ['#FFD700', '#FFA500']
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'bottom'
                        }
                    }
                }
            });

            // Purchase History Chart
            new Chart(document.getElementById('purchaseHistoryChart'), {
                type: 'line',
                data: {
                    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
                    datasets: [{
                        label: 'Purchases',
                        data: [2, 4, 3, 5, 4, ${userPackages.size()}],
                        borderColor: '#FFD700',
                        tension: 0.4
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                stepSize: 1
                            }
                        }
                    }
                }
            });
        }

        // Initialize charts when the page loads
        window.addEventListener('load', initializeUserCharts);

        // Back to top button
        const backToTop = document.getElementById("backToTop");
        window.addEventListener("scroll", () => {
            backToTop.style.display = window.scrollY > 300 ? "block" : "none";
        });
        backToTop.addEventListener("click", () => window.scrollTo({ top: 0, behavior: "smooth" }));

        // Sidebar toggle (optional, for mobile)
        const sidebar = document.getElementById("sidebar");
        document.querySelector(".navbar-toggler").addEventListener("click", () => {
            sidebar.classList.toggle("collapsed");
        });

        // Follow button functionality
        document.querySelectorAll('.btn-follow').forEach(button => {
                button.addEventListener('click', function () {
                    const btn = this;
                    const restaurantId = btn.getAttribute('data-restaurant-id');
                    const restaurantName = btn.getAttribute('data-restaurant-name');
                    const csrfToken = btn.getAttribute('data-csrf-token');
                    const isFollowing = btn.classList.contains('followed');
                    const action = isFollowing ? "unfollow" : "follow";

                    // Disable button to prevent multiple clicks
                    btn.disabled = true;

                    fetch('${pageContext.request.contextPath}/follow-restaurant', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'X-CSRF-Token': csrfToken
                        },
                        body: JSON.stringify({
                            restaurantId: parseInt(restaurantId),
                            action: action
                        })
                    }).then(response => {
                        if (response.ok) {
                            if (isFollowing) {
                                btn.classList.remove('followed');
                                btn.innerHTML = `<i class="bi bi-plus-circle"></i> <span>Follow</span>`;
                                btn.setAttribute('aria-label', `Follow ${restaurantName}`);
                            } else {
                                btn.classList.add('followed');
                                btn.innerHTML = `<i class="bi bi-check-circle"></i> <span>Following</span>`;
                                btn.setAttribute('aria-label', `Unfollow ${restaurantName}`);
                            }
                        } else {
                            alert('Failed to update follow status.');
                        }
                    }).catch(() => {
                        alert('Network error. Please try again.');
                    }).finally(() => {
                        btn.disabled = false;
                    });
                });
            });

        // Generate QR codes for redemption codes
        document.querySelectorAll('[id^="qrcode-"]').forEach(div => {
            const purchaseId = div.id.replace('qrcode-', '');
            const redemptionCodeElement = div.parentElement.querySelector('p strong').nextSibling;
            if (redemptionCodeElement && redemptionCodeElement.textContent.trim()) {
                const redemptionCode = redemptionCodeElement.textContent.trim();
                new QRCode(div, {
                    text: redemptionCode,
                    width: 128,
                    height: 128
                });
            } else {
                console.warn(`No redemption code found for purchase ID: ${purchaseId}`);
            }
        });

        // Confirm redemption button
        document.querySelectorAll('.redeem-btn').forEach(button => {
            button.addEventListener('click', function () {
                const redemptionCode = this.getAttribute('data-redemption-code');
                const packageName = this.getAttribute('data-package-name');
                const csrfToken = this.getAttribute('data-csrf-token');
                const loadingOverlay = document.getElementById('loadingOverlay');

                if (confirm(`Are you sure you want to confirm redemption for ${packageName}? Provide code ${redemptionCode} to the restaurant.`)) {
                    loadingOverlay.style.display = 'block';
                    fetch('${pageContext.request.contextPath}/user/redeem-meal', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `redemptionCode=${redemptionCode}&csrfToken=${csrfToken}`
                    })
                    .then(response => response.json())
                    .then(data => {
                        loadingOverlay.style.display = 'none';
                        if (data.success) {
                            alert(`${data.message}\n\nDetails:\n- Package: ${data.details.packageName}\n- Meals Remaining: ${data.details.mealsRemaining}\n- Restaurant: ${data.details.restaurantName}`);
                            location.reload();
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

        // Mark notification as read
        document.querySelectorAll('.mark-read-btn').forEach(button => {
            button.addEventListener('click', function () {
                const notificationId = this.getAttribute('data-notification-id');
                const csrfToken = this.getAttribute('data-csrf-token');
                fetch('${pageContext.request.contextPath}/mark-notification-read', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `notificationId=${notificationId}&csrfToken=${csrfToken}`
                })
                .then(response => {
                    if (response.ok) {
                        this.closest('.list-group-item').classList.remove('list-group-item-warning');
                        this.remove();
                    } else {
                        alert('Error marking notification as read.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('An error occurred.');
                });
            });
        });
// Initialize payment status chart
const paymentCtx = document.getElementById('paymentChart').getContext('2d');
new Chart(paymentCtx, {
    type: 'doughnut',
    data: {
        labels: ['Successful', 'Pending', 'Failed'],
        datasets: [{
            data: [${successfulPayments}, ${pendingPayments}, ${failedPayments}],
            backgroundColor: ['#28a745', '#ffc107', '#dc3545']
        }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: { position: 'bottom' },
            tooltip: { enabled: true }
        }
    }
});
</script>
</body>
</html>
