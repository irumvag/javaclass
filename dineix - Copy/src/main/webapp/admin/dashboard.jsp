<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard | DineIX</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Manage users, restaurants, role requests, and system analytics on DineIX.">
    <meta property="og:title" content="Admin Dashboard - DineIX">
    <meta property="og:description" content="Access the DineIX admin dashboard to oversee platform operations.">
    <meta property="og:image" content="https://images.pexels.com/photos/3184183/pexels-photo-3184183.jpeg">
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css" rel="stylesheet">
    <!-- AOS Animations -->
    <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
    <!-- Chart.js -->
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

        .chart-container {
            background: var(--card-bg);
            border-radius: 12px;
            padding: 1.5rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            margin: 0 auto;
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

        .table-responsive {
            max-height: 500px;
            overflow-y: auto;
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
                    <c:choose>
                        <c:when test="${sessionScope.userId != null}">
                            <li class="nav-item"><a class="nav-link text-warning active" href="${pageContext.request.contextPath}/admin/dashboard">Admin Dashboard</a></li>
                            <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/login">Login</a></li>
                            <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/register">Register</a></li>
                        </c:otherwise>
                    </c:choose>
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
                    <h4 class="text-black">Admin Panel</h4>
                </div>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link active" href="#analytics" data-bs-toggle="tab" aria-label="System Analytics">
                            <i class="bi bi-graph-up me-2"></i> Analytics
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#users" data-bs-toggle="tab" aria-label="Manage Users">
                            <i class="bi bi-people me-2"></i> Manage Users
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#restaurants" data-bs-toggle="tab" aria-label="Manage Restaurants">
                            <i class="bi bi-shop me-2"></i> Manage Restaurants
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#requests" data-bs-toggle="tab" aria-label="Role Requests">
                            <i class="bi bi-clipboard-check me-2"></i> Role Requests
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
                    <h2 class="text-yellow">Admin Dashboard</h2>
                </div>

                <!-- Tab Content -->
                <div class="tab-content">
                    <!-- Analytics Tab -->
                    <div class="tab-pane fade show active" id="analytics" role="tabpanel">
                        <!-- Stat Cards -->
                        <div class="row g-4 mb-4" data-aos="zoom-in">
                            <div class="col-md-4">
                                <div class="stat-card">
                                    <h5>Total Users</h5>
                                    <p class="display-4">${analytics.totalUsers}</p>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="stat-card">
                                    <h5>Total Restaurants</h5>
                                    <p class="display-4">${analytics.totalRestaurants}</p>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="stat-card">
                                    <h5>Total Revenue</h5>
                                    <p class="display-4">$${analytics.totalRevenue}</p>
                                </div>
                            </div>
                        </div>
                        <!-- Charts -->
                        <div class="row g-4" data-aos="zoom-in">
                            <!-- Overview Bar Chart -->
                            <div class="col-md-12">
                                <div class="chart-container">
                                    <h5 class="text-center">Platform Overview</h5>
                                    <canvas id="overviewChart"></canvas>
                                </div>
                            </div>
                            <!-- Monthly Users Line Chart -->
                            <div class="col-md-6">
                                <div class="chart-container">
                                    <h5 class="text-center">Monthly User Registrations</h5>
                                    <canvas id="monthlyUsersChart"></canvas>
                                </div>
                            </div>
                            <!-- Revenue by Restaurant Pie Chart -->
                            <div class="col-md-6">
                                <div class="chart-container">
                                    <h5 class="text-center">Revenue by Restaurant</h5>
                                    <canvas id="restaurantRevenueChart"></canvas>
                                </div>
                            </div>
                            <!-- Meal Packages per Restaurant Bar Chart -->
                            <div class="col-md-6">
                                <div class="chart-container">
                                    <h5 class="text-center">Meal Packages per Restaurant</h5>
                                    <canvas id="mealPackagesChart"></canvas>
                                </div>
                            </div>
                            <!-- Monthly Purchases per Restaurant Line Chart -->
                            <div class="col-md-6">
                                <div class="chart-container">
                                    <h5 class="text-center">Monthly Meal Package Purchases</h5>
                                    <canvas id="monthlyPurchasesChart"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Manage Users Tab -->
                    <div class="tab-pane fade" id="users" role="tabpanel">
                        <div class="card" data-aos="zoom-in">
                            <div class="card-header bg-yellow text-black">
                                <h3 class="mb-0">Manage Users</h3>
                            </div>
                            <div class="card-body">
                                <c:if test="${empty users}">
                                    <p>No users found.</p>
                                </c:if>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Email</th>
                                                <th>Name</th>
                                                <th>Role</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${users}" var="user">
                                                <tr>
                                                    <td>${user.email}</td>
                                                    <td>${user.fullName != null ? user.fullName : 'N/A'}</td>
                                                    <td>
                                                        <select class="form-select update-role-select" 
                                                                data-user-id="${user.userId}" 
                                                                data-csrf-token="${csrfToken}">
                                                            <option value="USER" ${user.role == 'USER' ? 'selected' : ''}>User</option>
                                                            <option value="RESTAURANT_OWNER" ${user.role == 'RESTAURANT_OWNER' ? 'selected' : ''}>Restaurant Owner</option>
                                                            <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <button class="btn btn-danger btn-sm delete-user-btn" 
                                                                data-user-id="${user.userId}" 
                                                                data-csrf-token="${csrfToken}"
                                                                aria-label="Delete ${user.email}">
                                                            Delete
                                                        </button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Manage Restaurants Tab -->
                    <div class="tab-pane fade" id="restaurants" role="tabpanel">
                        <div class="card" data-aos="zoom-in">
                            <div class="card-header bg-yellow text-black">
                                <h3 class="mb-0">Manage Restaurants</h3>
                            </div>
                            <div class="card-body">
                                <c:if test="${empty restaurants}">
                                    <p>No restaurants found.</p>
                                </c:if>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Name</th>
                                                <th>Address</th>
                                                <th>Contact</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${restaurants}" var="restaurant">
                                                <tr>
                                                    <td>${restaurant.name}</td>
                                                    <td>${restaurant.address != null ? restaurant.address : 'N/A'}</td>
                                                    <td>${restaurant.contactNumber != null ? restaurant.contactNumber : 'N/A'}</td>
                                                    <td>
                                                        <button class="btn btn-danger btn-sm delete-restaurant-btn" 
                                                                data-restaurant-id="${restaurant.restaurantId}" 
                                                                data-csrf-token="${csrfToken}"
                                                                aria-label="Delete ${restaurant.name}">
                                                            Delete
                                                        </button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Role Requests Tab -->
                    <div class="tab-pane fade" id="requests" role="tabpanel">
                        <div class="card" data-aos="zoom-in">
                            <div class="card-header bg-yellow text-black">
                                <h3 class="mb-0">Pending Role Requests</h3>
                            </div>
                            <div class="card-body">
                                <c:if test="${empty roleRequests}">
                                    <p>No pending role requests.</p>
                                </c:if>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>User Email</th>
                                                <th>Requested Role</th>
                                                <th>Restaurant Name</th>
                                                <th>Address</th>
                                                <th>Contact</th>
                                                <th>Request Date</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${roleRequests}" var="roleRequest">
                                                <tr>
                                                    <td>${roleRequest.user.email}</td>
                                                    <td>${roleRequest.requestedRole}</td>
                                                    <td>${roleRequest.restaurantName != null ? roleRequest.restaurantName : 'N/A'}</td>
                                                    <td>${roleRequest.restaurantAddress != null ? roleRequest.restaurantAddress : 'N/A'}</td>
                                                    <td>${roleRequest.restaurantContact != null ? roleRequest.restaurantContact : 'N/A'}</td>
                                                    <td>
                                                        <fmt:formatDate value="${roleRequest.requestDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                                    </td>
                                                    <td>
                                                        <button class="btn btn-success btn-sm approve-btn" 
                                                                data-request-id="${roleRequest.requestId}" 
                                                                data-csrf-token="${csrfToken}"
                                                                aria-label="Approve request for ${roleRequest.user.email}">
                                                            Approve
                                                        </button>
                                                        <button class="btn btn-danger btn-sm reject-btn" 
                                                                data-request-id="${roleRequest.requestId}" 
                                                                data-csrf-token="${csrfToken}"
                                                                aria-label="Reject request for ${roleRequest.user.email}">
                                                            Reject
                                                        </button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
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

        // Chart.js Initialization
        // Overview Bar Chart
        const overviewCtx = document.getElementById('overviewChart').getContext('2d');
        new Chart(overviewCtx, {
            type: 'bar',
            data: {
                labels: ['Total Users', 'Total Restaurants', 'Total Revenue ($)'],
                datasets: [{
                    label: 'Platform Stats',
                    data: [
                        ${analytics.totalUsers},
                        ${analytics.totalRestaurants},
                        ${analytics.totalRevenue}
                    ],
                    backgroundColor: ['#FFD700', '#FFCA28', '#000000'],
                    borderColor: ['#000000'],
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
                            text: 'Value'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    },
                    title: {
                        display: true,
                        text: 'Platform Overview'
                    }
                }
            }
        });

        // Monthly Users Line Chart
        const monthlyUsersCtx = document.getElementById('monthlyUsersChart').getContext('2d');
        new Chart(monthlyUsersCtx, {
            type: 'line',
            data: {
                labels: [<c:forEach items="${analytics.monthlyUsers}" var="entry">'${entry.key}',</c:forEach>],
                datasets: [{
                    label: 'Users Registered',
                    data: [<c:forEach items="${analytics.monthlyUsers}" var="entry">${entry.value},</c:forEach>],
                    borderColor: '#FFD700',
                    backgroundColor: 'rgba(255, 215, 0, 0.2)',
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
                            text: 'Number of Users'
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: 'Month'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: true
                    },
                    title: {
                        display: true,
                        text: 'Monthly User Registrations'
                    }
                }
            }
        });

        // Revenue by Restaurant Pie Chart
        const restaurantRevenueCtx = document.getElementById('restaurantRevenueChart').getContext('2d');
        new Chart(restaurantRevenueCtx, {
            type: 'pie',
            data: {
                labels: [<c:forEach items="${analytics.restaurantRevenue}" var="entry">'${entry.key}',</c:forEach>],
                datasets: [{
                    label: 'Revenue ($)',
                    data: [<c:forEach items="${analytics.restaurantRevenue}" var="entry">${entry.value},</c:forEach>],
                    backgroundColor: ['#FFD700', '#FFCA28', '#000000', '#FFC107', '#FFA000'],
                    borderColor: ['#FFFFFF'],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'right'
                    },
                    title: {
                        display: true,
                        text: 'Revenue by Restaurant'
                    }
                }
            }
        });

        // Meal Packages per Restaurant Bar Chart
        const mealPackagesCtx = document.getElementById('mealPackagesChart').getContext('2d');
        new Chart(mealPackagesCtx, {
            type: 'bar',
            data: {
                labels: [<c:forEach items="${analytics.mealPackagesPerRestaurant}" var="entry">'${entry.key}',</c:forEach>],
                datasets: [{
                    label: 'Meal Packages',
                    data: [<c:forEach items="${analytics.mealPackagesPerRestaurant}" var="entry">${entry.value},</c:forEach>],
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
                            text: 'Number of Meal Packages'
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: 'Restaurant'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    },
                    title: {
                        display: true,
                        text: 'Meal Packages per Restaurant'
                    }
                }
            }
        });

        // Monthly Purchases per Restaurant Line Chart
        const monthlyPurchasesCtx = document.getElementById('monthlyPurchasesChart').getContext('2d');
        new Chart(monthlyPurchasesCtx, {
            type: 'line',
            data: {
                labels: [
                    <c:forEach items="${analytics.monthlyPurchasesPerRestaurant}" var="restaurantEntry">
                        <c:forEach items="${restaurantEntry.value}" var="monthEntry">'${monthEntry.key}',</c:forEach>
                    </c:forEach>
                ].filter((v, i, a) => a.indexOf(v) === i).sort(), // Unique and sorted months
                datasets: [
                    <c:forEach items="${analytics.monthlyPurchasesPerRestaurant}" var="restaurantEntry" varStatus="loop">
                        {
                            label: '${restaurantEntry.key}',
                            data: [
                                <c:forEach items="${analytics.monthlyPurchasesPerRestaurant}" var="innerRestaurantEntry">
                                    <c:if test="${innerRestaurantEntry.key == restaurantEntry.key}">
                                        <c:forEach items="${analytics.monthlyPurchasesPerRestaurant[restaurantEntry.key]}" var="monthEntry">
                                            ${monthEntry.value},
                                        </c:forEach>
                                    </c:if>
                                </c:forEach>
                            ],
                            borderColor: ['#FFD700', '#FFCA28', '#000000', '#FFC107', '#FFA000'][${loop.index % 5}],
                            backgroundColor: ['rgba(255, 215, 0, 0.2)', 'rgba(255, 202, 40, 0.2)', 'rgba(0, 0, 0, 0.2)', 'rgba(255, 193, 7, 0.2)', 'rgba(255, 160, 0, 0.2)'][${loop.index % 5}],
                            fill: true,
                            tension: 0.4
                        },
                    </c:forEach>
                ]
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
                },
                plugins: {
                    legend: {
                        display: true
                    },
                    title: {
                        display: true,
                        text: 'Monthly Meal Package Purchases per Restaurant'
                    }
                }
            }
        });

        // Approve role request
        document.querySelectorAll('.approve-btn').forEach(button => {
            button.addEventListener('click', function () {
                const requestId = this.getAttribute('data-request-id');
                const csrfToken = this.getAttribute('data-csrf-token');
                const loadingOverlay = document.getElementById('loadingOverlay');

                loadingOverlay.style.display = 'block';
                fetch('${pageContext.request.contextPath}/admin/approve-request', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `requestId=${requestId}&csrfToken=${csrfToken}`
                })
                .then(response => response.json())
                .then(data => {
                    loadingOverlay.style.display = 'none';
                    if (data.success) {
                        this.closest('tr').remove();
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

        // Reject role request
        document.querySelectorAll('.reject-btn').forEach(button => {
            button.addEventListener('click', function () {
                const requestId = this.getAttribute('data-request-id');
                const csrfToken = this.getAttribute('data-csrf-token');
                const loadingOverlay = document.getElementById('loadingOverlay');

                if (confirm('Are you sure you want to reject this request?')) {
                    loadingOverlay.style.display = 'block';
                    fetch('${pageContext.request.contextPath}/admin/reject-request', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `requestId=${requestId}&csrfToken=${csrfToken}`
                    })
                    .then(response => response.json())
                    .then(data => {
                        loadingOverlay.style.display = 'none';
                        if (data.success) {
                            this.closest('tr').remove();
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

        // Delete user
        document.querySelectorAll('.delete-user-btn').forEach(button => {
            button.addEventListener('click', function () {
                const userId = this.getAttribute('data-user-id');
                const csrfToken = this.getAttribute('data-csrf-token');
                const loadingOverlay = document.getElementById('loadingOverlay');

                if (confirm('Are you sure you want to delete this user?')) {
                    loadingOverlay.style.display = 'block';
                    fetch('${pageContext.request.contextPath}/admin/delete-user', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `userId=${userId}&csrfToken=${csrfToken}`
                    })
                    .then(response => response.json())
                    .then(data => {
                        loadingOverlay.style.display = 'none';
                        if (data.success) {
                            this.closest('tr').remove();
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

        // Update user role
        document.querySelectorAll('.update-role-select').forEach(select => {
            select.addEventListener('change', function () {
                const userId = this.getAttribute('data-user-id');
                const role = this.value;
                const csrfToken = this.getAttribute('data-csrf-token');
                const loadingOverlay = document.getElementById('loadingOverlay');

                loadingOverlay.style.display = 'block';
                fetch('${pageContext.request.contextPath}/admin/update-user-role', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `userId=${userId}&role=${role}&csrfToken=${csrfToken}`
                })
                .then(response => response.json())
                .then(data => {
                    loadingOverlay.style.display = 'none';
                    if (!data.success) {
                        alert(data.message || 'An error occurred.');
                        this.value = this.getAttribute('data-original-role') || 'USER';
                    }
                })
                .catch(error => {
                    loadingOverlay.style.display = 'none';
                    console.error('Error:', error);
                    alert('An error occurred while processing your request.');
                    this.value = this.getAttribute('data-original-role') || 'USER';
                });
            });
        });

        // Delete restaurant
        document.querySelectorAll('.delete-restaurant-btn').forEach(button => {
            button.addEventListener('click', function () {
                const restaurantId = this.getAttribute('data-restaurant-id');
                const csrfToken = this.getAttribute('data-csrf-token');
                const loadingOverlay = document.getElementById('loadingOverlay');

                if (confirm('Are you sure you want to delete this restaurant?')) {
                    loadingOverlay.style.display = 'block';
                    fetch('${pageContext.request.contextPath}/admin/delete-restaurant', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `restaurantId=${restaurantId}&csrfToken=${csrfToken}`
                    })
                    .then(response => response.json())
                    .then(data => {
                        loadingOverlay.style.display = 'none';
                        if (data.success) {
                            this.closest('tr').remove();
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
