<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Dashboard | DineIX</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Manage your DineIX account, purchase meal packages, and redeem meals.">
    <meta property="og:title" content="User Dashboard - DineIX">
    <meta property="og:description" content="Access your DineIX dashboard to follow restaurants, buy meal packages, and more.">
    <meta property="og:image" content="https://images.pexels.com/photos/3184183/pexels-photo-3184183.jpeg">
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
            <a class="navbar-brand" href="${pageContext.request.contextPath}/"><img src="img/logo.png" alt="DineIX Logo" height="40" loading="lazy"></a>
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
                    <img src="img/user-avatar.png" class="rounded-circle shadow" width="80" alt="User Avatar" loading="lazy">
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

                <!-- Dashboard Header -->
                <div class="d-flex justify-content-between align-items-center mb-4" data-aos="fade-right">
                    <h2 class="text-yellow">Dashboard Overview</h2>
                    <div class="balance-card p-3">
                        <h5 class="mb-0">Available Meals: <strong>${totalMealsRemaining}</strong></h5>
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
                                <form action="${pageContext.request.contextPath}/user/update-profile" method="POST">
                                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                                    <div class="mb-3">
                                        <label for="fullName" class="form-label">Full Name</label>
                                        <input type="text" class="form-control" id="fullName" name="fullName" value="${sessionScope.fullName}" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="email" class="form-label">Email</label>
                                        <input type="email" class="form-control" id="email" name="email" value="${sessionScope.email}" readonly>
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
                                                <img src="${restaurant.logoUrl != null ? restaurant.logoUrl : 'img/default-restaurant.jpg'}" class="card-img-top" alt="${restaurant.name} logo" style="height: 150px; object-fit: cover;" loading="lazy">
                                                <div class="card-body">
                                                    <h5 class="card-title">${restaurant.name}</h5>
                                                    <p class="card-text">${restaurant.description}</p>
                                                    <button class="btn btn-outline-warning btn-follow followed" 
                                                            data-restaurant-id="${restaurant.restaurantId}" 
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
                                <div class="row row-cols-1 row-cols-md-3 g-4 mb-4">
                                    <c:forEach items="${availablePackages}" var="package" varStatus="loop">
                                        <div class="col" data-aos="fade-up" data-aos-delay="${loop.index * 100}">
                                            <div class="card h-100">
                                                <img src="img/meal-package.jpg" class="card-img-top" alt="${package.packageName}" style="height: 150px; object-fit: cover;" loading="lazy">
                                                <div class="card-body">
                                                    <h5 class="card-title">${package.packageName}</h5>
                                                    <p class="card-text">${package.description}</p>
                                                    <p><strong>Meals:</strong> ${package.numberOfMeals}</p>
                                                    <p><strong>Price:</strong> $${package.price}</p>
                                                    <p><strong>Restaurant:</strong> ${package.restaurantName}</p>
                                                    <form action="${pageContext.request.contextPath}/user/purchase-package" method="POST">
                                                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                                                        <input type="hidden" name="packageId" value="${package.packageId}">
                                                        <button type="submit" class="btn btn-warning w-100" aria-label="Purchase ${package.packageName}">Purchase</button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <!-- Purchased Packages -->
                                <h4>Your Purchased Packages</h4>
                                <c:if test="${empty purchasedPackages}">
                                    <p>You haven't purchased any meal packages. <a href="#packages" class="text-warning">Browse now</a>.</p>
                                </c:if>
                                <div class="row row-cols-1 row-cols-md-3 g-4">
                                    <c:forEach items="${purchasedPackages}" var="purchase" varStatus="loop">
                                        <div class="col" data-aos="fade-up" data-aos-delay="${loop.index * 100}">
                                            <div class="card h-100">
                                                <div class="card-body">
                                                    <h5 class="card-title">${purchase.packageName}</h5>
                                                    <p class="card-text">${purchase.description}</p>
                                                    <p><strong>Meals Remaining:</strong> ${purchase.mealsRemaining}</p>
                                                    <p><strong>Purchased On:</strong> ${purchase.purchaseDate}</p>
                                                    <button class="btn btn-warning redeem-btn w-100" 
                                                            data-purchase-id="${purchase.purchaseId}" 
                                                            data-csrf-token="${csrfToken}"
                                                            aria-label="Redeem meal from ${purchase.packageName}">
                                                        Redeem Meal
                                                    </button>
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
    <script>
        // Initialize AOS
        AOS.init({ duration: 1000 });

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
                const restaurantId = this.getAttribute('data-restaurant-id');
                const csrfToken = this.getAttribute('data-csrf-token');
                const isFollowed = this.classList.contains('followed');
                const loadingOverlay = document.getElementById('loadingOverlay');

                loadingOverlay.style.display = 'block';
                fetch('${pageContext.request.contextPath}/follow-restaurant', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `restaurantId=${restaurantId}&action=${isFollowed ? 'unfollow' : 'follow'}&csrfToken=${csrfToken}`
                })
                .then(response => response.json())
                .then(data => {
                    loadingOverlay.style.display = 'none';
                    if (data.success) {
                        this.classList.toggle('followed');
                        this.innerHTML = isFollowed ? '<i class="bi bi-plus-circle"></i> Follow' : '<i class="bi bi-check-circle"></i> Following';
                        this.setAttribute('aria-label', `${isFollowed ? 'Follow' : 'Unfollow'} ${this.closest('.card-body').querySelector('.card-title').textContent}`);
                        if (isFollowed) {
                            this.closest('.col').remove();
                        }
                    } else {
                        alert(data.message || 'An error occurred.');
                    }
                })
                .catch(error => {
                    loadingOverlay.style.display = 'none';
                    console.error('Error:', error);
                    alert('An error occurred while processing your request.');
                });
            });
        });

        // Redeem meal functionality
        document.querySelectorAll('.redeem-btn').forEach(button => {
            button.addEventListener('click', function () {
                const purchaseId = this.getAttribute('data-purchase-id');
                const csrfToken = this.getAttribute('data-csrf-token');
                const loadingOverlay = document.getElementById('loadingOverlay');

                if (confirm('Are you sure you want to redeem a meal? Please visit the restaurant to complete this action.')) {
                    loadingOverlay.style.display = 'block';
                    fetch('${pageContext.request.contextPath}/user/redeem-meal', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `purchaseId=${purchaseId}&csrfToken=${csrfToken}`
                    })
                    .then(response => response.json())
                    .then(data => {
                        loadingOverlay.style.display = 'none';
                        if (data.success) {
                            alert('Meal redemption requested. Please show this confirmation to the restaurant staff.');
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
    </script>
</body>
</html>