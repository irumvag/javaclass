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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <!-- Bootstrap Icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css" rel="stylesheet">
        <!-- AOS Animations -->
        <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
        <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/qrcodejs/1.0.0/qrcode.min.js"></script>
        <!-- Poppins Font -->
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">

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
            }
            .navbar-dark.bg-black {
                background-color: var(--secondary-black) !important;
            }
            .text-yellow {
                color: var(--primary-yellow) !important;
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
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navContent" aria-controls="navContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navContent">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/">Home</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/about">About</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/restaurants">Restaurants</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/user/purchase-history">Purchase History</a></li>
                        <li class="nav-item"><a class="nav-link text-warning active" href="${pageContext.request.contextPath}/user/dashboard" aria-current="page">Dashboard</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Main Content -->
        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3 col-lg-2 sidebar" id="sidebar">
                    <div class="text-center mb-4">
                        <img src="${pageContext.request.contextPath}/img/user-avatar.png" class="rounded-circle shadow" width="80" alt="User Avatar" loading="lazy">
                        <h4 class="text-black mt-2"><c:out value="${sessionScope.fullName}"/></h4>
                        <small class="text-muted"><c:out value="${sessionScope.email}"/></small>
                    </div>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link active" href="#profile" data-bs-toggle="tab" aria-label="Profile Settings">
                                <i class="bi bi-person-circle me-2"></i> <span class="d-none d-md-inline">Profile</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#followed" data-bs-toggle="tab" aria-label="Followed Restaurants">
                                <i class="bi bi-shop me-2"></i> <span class="d-none d-md-inline">Followed Restaurants</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#packages" data-bs-toggle="tab" aria-label="Meal Packages">
                                <i class="bi bi-basket me-2"></i> <span class="d-none d-md-inline">Meal Packages</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#role-request" data-bs-toggle="tab" aria-label="Role Request">
                                <i class="bi bi-person-plus me-2"></i> <span class="d-none d-md-inline">Role Request</span>
                            </a>
                        </li>
                        <li class="nav-item mt-4">
                            <a class="nav-link" href="${pageContext.request.contextPath}/logout" aria-label="Logout">
                                <i class="bi bi-box-arrow-left me-2"></i> <span class="d-none d-md-inline">Logout</span>
                            </a>
                        </li>
                    </ul>
                </div>

                <!-- Main Content -->
                <div class="col-md-9 col-lg-10 main-content">
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
                                        <p><c:out value="${notification.message}"/></p>
                                        <small><c:out value="${notification.createdAt}"/></small>
                                        <c:if test="${!notification.isRead}">
                                            <button class="btn btn-sm btn-warning mark-read-btn"
                                                    data-notification-id="${notification.id}"
                                                    data-csrf-token="${csrfToken}"
                                                    aria-label="Mark notification as read">Mark as Read</button>
                                        </c:if>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>

                    <!-- Dashboard Header -->
                    <div class="d-flex justify-content-between align-items-center mb-4" data-aos="fade-right">
                        <h2 class="text-yellow">Dashboard Overview</h2>
                        <div class="balance-card p-3">
                            <h5 class="mb-0">Available Meals: <strong><c:out value="${totalMealsRemaining}"/></strong></h5>
                        </div>
                    </div>

                    <!-- Tab Content -->
                    <div class="tab-content">
                        <!-- Profile Tab -->
                        <div class="tab-pane fade show active" id="profile" role="tabpanel">
                            <div class="card" data-aos="zoom-in">
                                <div class="card-header bg-yellow text-black">
                                    <h3 class="mb-0">Profile Settings</h3>
                                </div>
                                <div class="card-body">
                                    <form action="${pageContext.request.contextPath}/user/updateduser" method="post">
                                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                                        <div class="mb-3">
                                            <label for="fullName" class="form-label">Full Name</label>
                                            <input type="text" class="form-control" id="fullName" name="fullName" value="<c:out value='${sessionScope.fullName}'/>" required>
                                        </div>
                                        <div class="mb-3">
                                            <label for="email" class="form-label">Email</label>
                                            <input type="email" class="form-control" id="email" name="email" value="<c:out value='${sessionScope.email}'/>" readonly>
                                        </div>
                                        <div class="mb-3">
                                            <label for="themePreference" class="form-label">Theme Preference</label>
                                            <select class="form-select" id="themePreference" name="themePreference">
                                                <option value="light" ${sessionScope.themePreference == 'light' ? 'selected' : ''}>Light</option>
                                                <option value="dark" ${sessionScope.themePreference == 'dark' ? 'selected' : ''}>Dark</option>
                                            </select>
                                        </div>
                                        <button type="submit" class="btn btn-warning" aria-label="Update Profile">Update Profile</button>
                                    </form>
                                </div>
                            </div>
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
                                                    <img src="${fn:escapeXml(restaurant.logoUrl != null ? restaurant.logoUrl : pageContext.request.contextPath.concat('/img/default-restaurant.jpg'))}" class="card-img-top" alt="${fn:escapeXml(restaurant.name)} logo" style="height: 150px; object-fit: cover;" loading="lazy">
                                                    <div class="card-body">
                                                        <h5 class="card-title"><c:out value="${restaurant.name}"/></h5>
                                                        <p class="card-text"><c:out value="${restaurant.description}"/></p>
                                                        <button class="btn btn-outline-warning btn-follow followed" 
                                                                data-restaurant-id="${restaurant.restaurantId}" 
                                                                data-restaurant-name="${fn:escapeXml(restaurant.name)}"
                                                                data-csrf-token="${csrfToken}"
                                                                aria-label="Unfollow ${fn:escapeXml(restaurant.name)}">
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
                                    <hr>
                                    <center>Redeem now</center>
                                    <hr>
                                    <!-- Purchased Packages -->
                                    <h4 class="m-3" style="color:green">Your Purchased Packages</h4>
                                    <c:if test="${empty purchasedPackages}">
                                        <p class="redemption-instructions">You haven't purchased any meal packages. <a href="#packages" class="text-warning">Browse now</a>.</p>
                                    </c:if>
                                    <div class="row row-cols-1 row-cols-md-3 g-4">
                                        <c:forEach items="${purchasedPackages}" var="purchase" varStatus="loop">
                                            <div class="col" data-aos="fade-up" data-aos-delay="${loop.index * 100}" data-purchase-id="${purchase.purchaseId}">
                                                <div class="card h-100">
                                                    <div class="card-body">
                                                        <h5 class="card-title"><c:out value="${purchase.packageName}"/></h5>
                                                        <p class="card-text"><c:out value="${purchase.description}"/></p>
                                                        <p><strong>Meals Remaining:</strong> <span class="meals-remaining"><c:out value="${purchase.mealsRemaining}"/></span></p>
                                                        <p><strong>Purchased On:</strong> <c:out value="${purchase.purchaseDate}"/></p>
                                                        <c:if test="${not empty purchase.redemptionCode}">
                                                            <p><strong>Redemption Code:</strong> <c:out value="${purchase.redemptionCode}"/></p>
                                                            <div class="qr-placeholder p-5 mx-auto" 
                                                                 data-purchase-id="${fn:escapeXml(purchase.purchaseId)}" 
                                                                 data-redemption-code="${pageContext.request.contextPath}/redeem-meal/${fn:escapeXml(purchase.redemptionCode)}">
                                                            </div>
                                                            <button class="btn btn-warning redeem-btn mb-2" 
                                                                    data-redemption-code="${fn:escapeXml(purchase.redemptionCode)}" 
                                                                    data-package-name="${fn:escapeXml(purchase.packageName)}" 
                                                                    data-csrf-token="${csrfToken}" 
                                                                    aria-label="Confirm redemption for ${fn:escapeXml(purchase.packageName)}">
                                                                Confirm Redemption
                                                            </button>
                                                            <p class="redemption-instructions">Show this code or QR code to the restaurant to redeem a meal.</p>
                                                        </c:if>
                                                        <c:if test="${empty purchase.redemptionCode}">
                                                            <p><strong>Purchase ID:</strong> <c:out value="${purchase.purchaseId}"/></p>
                                                            <p class="redemption-instructions">No redemption code available. Provide your purchase ID to the restaurant to redeem a meal.</p>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    <hr>
                                    <center>Buy your favorite package now!!!</center>
                                    <hr>
                                    <!-- Available Packages -->
                                    <h4>Available Meal Packages</h4>
                                    <c:if test="${empty availablePackages}">
                                        <p>No meal packages available. <a href="${pageContext.request.contextPath}/restaurants" class="text-warning">Explore restaurants</a>.</p>
                                    </c:if>
                                    <div class="row row-cols-1 row-cols-md-3 g-4 mb-4">
                                        <c:forEach items="${availablePackages}" var="mealPackage" varStatus="loop">
                                            <div class="col" data-aos="fade-up" data-aos-delay="${loop.index * 100}">
                                                <div class="card h-100">
                                                    <img src="${pageContext.request.contextPath}/img/meal-package.jpeg" class="card-img-top" alt="${fn:escapeXml(mealPackage.packageName)}" style="height: 150px; object-fit: cover;" loading="lazy">
                                                    <div class="card-body">
                                                        <h5 class="card-title"><c:out value="${mealPackage.packageName}"/></h5>
                                                        <p class="card-text"><c:out value="${mealPackage.description}"/></p>
                                                        <p><strong>Meals:</strong> <c:out value="${mealPackage.numberOfMeals}"/></p>
                                                        <p><strong>Price:</strong> $<c:out value="${mealPackage.price}"/></p>
                                                        <p><strong>Restaurant:</strong> <c:out value="${mealPackage.restaurantName}"/></p>
                                                        <form action="${pageContext.request.contextPath}/user/purchase-package" method="POST">
                                                            <input type="hidden" name="csrfToken" value="${csrfToken}">
                                                            <input type="hidden" name="packageId" value="${mealPackage.packageId}">
                                                            <button type="submit" class="btn btn-warning w-100" aria-label="Purchase ${fn:escapeXml(mealPackage.packageName)}">Purchase</button>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Role Request Tab -->
                        <div class="tab-pane fade" id="role-request" role="tabpanel">
                            <div class="card" data-aos="zoom-in">
                                <div class="card-header bg-yellow text-black">
                                    <h3 class="mb-0">Request Restaurant Owner Role</h3>
                                </div>
                                <div class="card-body">
                                    <c:if test="${not empty roleRequest}">
                                        <p><strong>Status:</strong> <c:out value="${roleRequest.status}"/></p>
                                        <p><strong>Requested On:</strong> <c:out value="${roleRequest.requestDate}"/></p>
                                        <c:if test="${roleRequest.status == 'PENDING'}">
                                            <p>Your request is pending approval. We'll notify you once reviewed.</p>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${empty roleRequest}">
                                        <form id="roleRequestForm">
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
                                                <input type="tel" class="form-control" id="restaurantContact" name="restaurantContact" required>
                                            </div>
                                            <div class="mb-3">
                                                <label for="reason" class="form-label">Reason for Request</label>
                                                <textarea class="form-control" id="reason" name="reason" rows="4" required></textarea>
                                            </div>
                                            <button type="submit" class="btn btn-warning" id="submitRequestBtn" aria-label="Submit Role Request">Submit Request</button>
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
        <script>
            // Initialize AOS
            AOS.init({duration: 1000, once: true});

            // Back to top button
            const backToTop = document.getElementById("backToTop");
            window.addEventListener("scroll", () => {
                backToTop.style.display = window.scrollY > 300 ? "block" : "none";
            });
            backToTop.addEventListener("click", () => window.scrollTo({top: 0, behavior: "smooth"}));

            // Sidebar toggle for mobile
            const sidebar = document.getElementById("sidebar");
            document.querySelector(".navbar-toggler").addEventListener("click", () => {
                sidebar.classList.toggle("collapsed");
            });

            // Follow/unfollow button handler
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
            document.addEventListener('DOMContentLoaded', function () {
                document.querySelectorAll('.qr-placeholder').forEach(function (el) {
                    let rawId = el.getAttribute('data-purchase-id') || '';
                    let cleanId = rawId.trim().replace(/[^a-zA-Z0-9_-]/g, '');
                    if (!cleanId) {
                        cleanId = 'qr-' + Math.random().toString(36).substring(2, 10);
                    }
                    el.id = 'qrcode-' + cleanId;
                    let redemptionCode = el.getAttribute('data-redemption-code');
                    if (redemptionCode) {
                        try {
                            new QRCode(document.getElementById(el.id), {
                                text: redemptionCode,
                                width: 128,
                                height: 128,
                                colorDark: "#000000",
                                colorLight: "#ffffff",
                                correctLevel: QRCode.CorrectLevel.H
                            });
                        } catch (error) {
                            console.error('QR Code generation failed:', error);
                        }
                    }
                });
            });

            // Mark notification as read
            document.querySelectorAll('.mark-read-btn').forEach(button => {
                button.addEventListener('click', function () {
                    const btn = this;
                    const notificationId = btn.getAttribute('data-notification-id');
                    const csrfToken = btn.getAttribute('data-csrf-token');
                    const listItem = btn.closest('.list-group-item');

                    // Disable button to prevent multiple clicks
                    btn.disabled = true;

                    fetch('${pageContext.request.contextPath}/mark-notification-read', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'X-CSRF-Token': csrfToken
                        },
                        body: JSON.stringify({
                            notificationId: parseInt(notificationId)
                        })
                    }).then(response => {
                        if (response.ok) {
                            // Remove the warning styling and the button
                            listItem.classList.remove('list-group-item-warning');
                            btn.remove();
                        } else {
                            alert('Failed to mark notification as read.');
                        }
                    }).catch(() => {
                        alert('Network error. Please try again.');
                    }).finally(() => {
                        btn.disabled = false;
                    });
                });
            });

            // Role request form handler
            document.getElementById('roleRequestForm').addEventListener('submit', function (e) {
                e.preventDefault();

                const form = e.target;
                const submitBtn = document.getElementById('submitRequestBtn');
                const originalBtnText = submitBtn.innerHTML;

                // Show loading state
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status"></span> Processing...';

                // Get form data
                const formData = new FormData(form);
                const requestData = {
                    csrfToken: formData.get('csrfToken'),
                    requestedRole: formData.get('requestedRole'),
                    restaurantName: formData.get('restaurantName'),
                    restaurantAddress: formData.get('restaurantAddress'),
                    restaurantContact: formData.get('restaurantContact'),
                    reason: formData.get('reason')
                };

                fetch('${pageContext.request.contextPath}/user/request-role', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-Token': requestData.csrfToken
                    },
                    body: JSON.stringify(requestData)
                })
                        .then(response => {
                            if (!response.ok) {
                                return response.text().then(text => {
                                    throw new Error(text || 'Request failed')
                                });
                            }
                            return response.text();
                        })
                        .then(message => {
                            // Show success message
                            alert('Success: ' + message);
                            // Optionally reset form
                            form.reset();
                            // Reload or update UI as needed
                            window.location.reload();
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            alert('Error: ' + error.message);
                        })
                        .finally(() => {
                            submitBtn.disabled = false;
                            submitBtn.innerHTML = originalBtnText;
                        });
            });

            // Confirm redemption
            document.querySelectorAll('.redeem-btn').forEach(button => {
                button.addEventListener('click', function () {
                    const redemptionCode = this.getAttribute('data-redemption-code');
                    const packageName = this.getAttribute('data-package-name');
                    const csrfToken = this.getAttribute('data-csrf-token');
                    const loadingOverlay = document.getElementById('loadingOverlay');
                    const card = this.closest('.card');
                    const mealsRemainingElement = card.querySelector('.meals-remaining');

                    if (!redemptionCode || !csrfToken) {
                        alert('Missing redemption code or CSRF token.');
                        return;
                    }

                    if (confirm(`Are you sure you want to redeem a meal from ${packageName}?`)) {
                        loadingOverlay.style.display = 'flex';
                        this.disabled = true; // Disable button to prevent multiple clicks

                        const formData = new FormData();
                        formData.append('redemptionCode', redemptionCode);
                        formData.append('csrfToken', csrfToken);

                        fetch('${pageContext.request.contextPath}/user/redeem-meal', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => {
                                if (!response.ok) {
                                    return response.json().then(err => { throw new Error(err.message || 'Request failed'); });
                                }
                                return response.json();
                            })
                            .then(data => {
                                loadingOverlay.style.display = 'none';
                                if (data.success) {
                                    // Update the UI with the new remaining meals count
                                    if (mealsRemainingElement) {
                                        mealsRemainingElement.textContent = data.details.mealsRemaining;
                                    }

                                    // Show success message
                                    const successAlert = document.createElement('div');
                                    successAlert.className = 'alert alert-success alert-dismissible fade show';
                                    successAlert.setAttribute('role', 'alert');
                                    successAlert.innerHTML = `
                                        <i class="bi bi-check-circle-fill me-2"></i>
                                        ${data.message}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                    `;

                                    // Insert the alert at the top of the main content
                                    const mainContent = document.querySelector('.main-content');
                                    if (mainContent) {
                                        mainContent.insertBefore(successAlert, mainContent.firstChild);
                                    }

                                    // Scroll to the top to show the message
                                    window.scrollTo({ top: 0, behavior: 'smooth' });

                                    // Reload the page after 3 seconds to ensure all data is fresh
                                    setTimeout(() => location.reload(), 3000);
                                } else {
                                    alert(data.message || 'Failed to redeem meal.');
                                }
                            })
                            .catch(error => {
                                loadingOverlay.style.display = 'none';
                                console.error('Redemption error:', error);
                                alert(error.message || 'An error occurred while processing your request.');
                            })
                            .finally(() => {
                                this.disabled = false; // Re-enable button
                            });
                    }
                });
            });
        </script>
    </body>
</html>