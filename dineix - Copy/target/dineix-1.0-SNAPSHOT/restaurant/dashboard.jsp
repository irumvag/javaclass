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
    <!-- Statistics Cards -->
<div class="row mb-4">
    <div class="col-md-4" data-aos="fade-up">
        <div class="card h-100">
            <div class="card-header bg-yellow text-black">
                <h5 class="mb-0">Total Purchases</h5>
            </div>
            <div class="card-body">
                <h2 class="display-4">${totalPurchases}</h2>
                <p class="text-muted">Meal packages sold</p>
            </div>
        </div>
    </div>
    <div class="col-md-4" data-aos="fade-up" data-aos-delay="100">
        <div class="card h-100">
            <div class="card-header bg-yellow text-black">
                <h5 class="mb-0">Total Revenue</h5>
            </div>
            <div class="card-body">
                <h2 class="display-4">RWF <fmt:formatNumber value="${totalRevenue}" type="number"/></h2>
            </div>
        </div>
    </div>
    <div class="col-md-4" data-aos="fade-up" data-aos-delay="200">
        <div class="card h-100">
            <div class="card-header bg-yellow text-black">
                <h5 class="mb-0">Active Packages</h5>
            </div>
            <div class="card-body">
                <h2 class="display-4">${fn:length(packagePerformance)}</h2>
                <p class="text-muted">Available meal packages</p>
            </div>
        </div>
    </div>
</div>

<!-- Performance Chart -->
<div class="card mb-4" data-aos="zoom-in">
    <div class="card-header bg-yellow text-black">
        <h3 class="mb-0">Package Performance</h3>
    </div>
    <div class="card-body">
        <canvas id="performanceChart" style="height: 300px;"></canvas>
    </div>
</div>

<!-- Recent Transactions -->
<div class="card mb-4" data-aos="zoom-in">
    <div class="card-header bg-yellow text-black d-flex justify-content-between align-items-center">
        <h3 class="mb-0">Recent Transactions</h3>
        <button class="btn btn-dark" onclick="refreshData()">
            <i class="bi bi-arrow-clockwise"></i> Refresh
        </button>
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty recentTransactions}">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Package</th>
                                <th>Purchase Date</th>
                                <th>Meals Remaining</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentTransactions}" var="txn">
                                <tr>
                                    <td>${txn.package}</td>
                                    <td><fmt:formatDate value="${txn.date}" pattern="dd MMM yyyy HH:mm"/></td>
                                    <td>${txn.remaining}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">No recent transactions found</div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- QR Code Scanner -->
    <script src="https://unpkg.com/html5-qrcode"></script>
    <!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
<script>
function initializeChart() {
    const ctx = document.getElementById('performanceChart');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: [
                <c:forEach items="${packagePerformance}" var="perf">
                    '${perf.package}',
                </c:forEach>
            ],
            datasets: [{
                label: 'Sales Count',
                data: [
                    <c:forEach items="${packagePerformance}" var="perf">
                        ${perf.sales},
                    </c:forEach>
                ],
                backgroundColor: 'rgba(255, 215, 0, 0.7)',
                borderColor: 'rgba(255, 215, 0, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return value.toLocaleString();
                        }
                    }
                }
            }
        }
    });
}

function refreshData() {
    fetch(window.location.href)
        .then(response => response.text())
        .then(html => {
            const parser = new DOMParser();
            const newDoc = parser.parseFromString(html, 'text/html');
            document.querySelector('.main-content').innerHTML = 
                newDoc.querySelector('.main-content').innerHTML;
            initializeChart();
        });
}

document.addEventListener('DOMContentLoaded', initializeChart);
</script>
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
                    <img src="../img/default-restaurant.jpg" class="rounded-circle shadow" width="80" alt="Owner Avatar" loading="lazy">
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
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/user/dashboard" aria-label="Switch to User Account">
                            <i class="bi bi-person-circle me-2"></i> Switch to User Account
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
                        <!-- Statistics Cards -->
<div class="row mb-4">
    <div class="col-md-4" data-aos="fade-up">
        <div class="card h-100">
            <div class="card-header bg-yellow text-black">
                <h5 class="mb-0">Total Purchases</h5>
            </div>
            <div class="card-body">
                <h2 class="display-4">${totalPurchases}</h2>
                <p class="text-muted">Meal packages sold</p>
            </div>
        </div>
    </div>
    <div class="col-md-4" data-aos="fade-up" data-aos-delay="100">
        <div class="card h-100">
            <div class="card-header bg-yellow text-black">
                <h5 class="mb-0">Total Revenue</h5>
            </div>
            <div class="card-body">
                <h2 class="display-4">RWF <fmt:formatNumber value="${totalRevenue}" type="number"/></h2>
            </div>
        </div>
    </div>
    <div class="col-md-4" data-aos="fade-up" data-aos-delay="200">
        <div class="card h-100">
            <div class="card-header bg-yellow text-black">
                <h5 class="mb-0">Active Packages</h5>
            </div>
            <div class="card-body">
                <h2 class="display-4">${fn:length(packagePerformance)}</h2>
                <p class="text-muted">Available meal packages</p>
            </div>
        </div>
    </div>
</div>

<!-- Performance Chart -->
<div class="card mb-4" data-aos="zoom-in">
    <div class="card-header bg-yellow text-black">
        <h3 class="mb-0">Package Performance</h3>
    </div>
    <div class="card-body">
        <canvas id="performanceChart" style="height: 300px;"></canvas>
    </div>
</div>

<!-- Recent Transactions -->
<div class="card mb-4" data-aos="zoom-in">
    <div class="card-header bg-yellow text-black d-flex justify-content-between align-items-center">
        <h3 class="mb-0">Recent Transactions</h3>
        <button class="btn btn-dark" onclick="refreshData()">
            <i class="bi bi-arrow-clockwise"></i> Refresh
        </button>
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty recentTransactions}">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Package</th>
                                <th>Purchase Date</th>
                                <th>Meals Remaining</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentTransactions}" var="txn">
                                <tr>
                                    <td>${txn.package}</td>
                                    <td><fmt:formatDate value="${txn.date}" pattern="dd MMM yyyy HH:mm"/></td>
                                    <td>${txn.remaining}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">No recent transactions found</div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- QR Code Scanner -->
                        <div class="card mb-4" data-aos="zoom-in">
                            <div class="card-header bg-yellow text-black">
                                <h3 class="mb-0">QR Code Scanner</h3>
                            </div>
                            <div class="card-body">
                                <div id="qr-reader" style="width: 100%"></div>
                                <div id="qr-reader-results" class="mt-3"></div>
                                <script>
                                    function onScanSuccess(decodedText, decodedResult) {
                                        const resultContainer = document.getElementById('qr-reader-results');
                                        if (decodedText.startsWith('DINEIX-MEAL-')) {
                                            const parts = decodedText.split('-');
                                            const packageId = parts[2];
                                            const userId = parts[3];
                                            
                                            // Show loading state
                                            resultContainer.innerHTML = '<div class="alert alert-info">Processing redemption...</div>';
                                            
                                            // Send redemption request to server
                                            fetch('${pageContext.request.contextPath}/api/redeem-meal', {
                                                method: 'POST',
                                                headers: {
                                                    'Content-Type': 'application/json',
                                                    'X-CSRF-Token': '${csrfToken}'
                                                },
                                                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                                                body: new URLSearchParams({
                                                    'redemptionCode': `DINEIX-MEAL-${packageId}-${userId}`,
                                                    'csrfToken': document.querySelector('[name=csrfToken]').value
                                                })
                                            })
                                            .then(response => response.json())
                                            .then(data => {
                                                if (data.success) {
                                                    resultContainer.innerHTML = '<div class="alert alert-success">Meal redeemed successfully!</div>';
                                                    // Refresh page after 2 seconds
                                                    setTimeout(() => window.location.reload(), 2000);
                                                } else {
                                                    resultContainer.innerHTML = `<div class="alert alert-danger">${data.message}</div>`;
                                                }
                                            })
                                            .catch(error => {
                                                resultContainer.innerHTML = '<div class="alert alert-danger">Error processing redemption. Please try again.</div>';
                                                console.error('Error:', error);
                                            });
                                        } else {
                                            resultContainer.innerHTML = '<div class="alert alert-warning">Invalid QR code format. Please scan a valid DineIX meal QR code.</div>';
                                        }
                                    }

                                    function onScanFailure(error) {
                                        console.warn(`QR scan error: ${error}`);
                                    }

                                    // Initialize QR scanner
                                    let html5QrcodeScanner = new Html5QrcodeScanner(
                                        "qr-reader",
                                        { fps: 10, qrbox: {width: 250, height: 250} },
                                        false
                                    );
                                    html5QrcodeScanner.render(onScanSuccess, onScanFailure);
                                </script>
                            </div>
                        </div>
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
                                <div class="row mb-4">
                                    <div class="col-md-4">
                                        <div class="card">
                                            <div class="card-body text-center">
                                                <h5>Total Followers</h5>
                                                <h3>${stats.totalFollowers}</h3>
                                                <canvas id="followersChart" width="100" height="100"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="card">
                                            <div class="card-body text-center">
                                                <h5>Total Purchases</h5>
                                                <h3>${stats.totalPurchases}</h3>
                                                <canvas id="purchasesChart" width="100" height="100"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="card">
                                            <div class="card-body text-center">
                                                <h5>Total Revenue</h5>
                                                <h3>$${stats.totalRevenue}</h3>
                                                <canvas id="revenueChart" width="100" height="100"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-4">
                                        <div class="card">
                                            <div class="card-body">
                                                <h5 class="card-title">Sales Trends</h5>
                                                <canvas id="salesTrendChart"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 mb-4">
                                        <div class="card">
                                            <div class="card-body">
                                                <h5 class="card-title">Peak Hours Analysis</h5>
                                                <canvas id="peakHoursChart"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-4">
                                        <div class="card">
                                            <div class="card-body">
                                                <h5 class="card-title">Package Performance</h5>
                                                <canvas id="packagePerformanceChart"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 mb-4">
                                        <div class="card">
                                            <div class="card-body">
                                                <h5 class="card-title">Customer Demographics</h5>
                                                <canvas id="demographicsChart"></canvas>
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
                                <div class="row mb-4">
                                    <div class="col-md-6">
                                        <div class="card h-100">
                                            <div class="card-body">
                                                <h5 class="card-title">QR Code Scanner</h5>
                                                <div id="qr-reader" class="mb-3"></div>
                                                <div id="qr-reader-results"></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="card h-100">
                                            <div class="card-body">
                                                <h5 class="card-title">Manual Redemption</h5>
                                                <form id="redeemForm">
                                                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                                                    <div class="mb-3">
                                                        <label for="redemptionCode" class="form-label">Redemption Code</label>
                                                        <input type="text" class="form-control" id="redemptionCode" name="redemptionCode" placeholder="Enter 6-digit code" maxlength="6" pattern="[0-9]{6}">
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="purchaseId" class="form-label">Purchase ID (Alternative)</label>
                                                        <input type="text" class="form-control" id="purchaseId" name="purchaseId" placeholder="Enter purchase ID">
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="userEmail" class="form-label">User Email (Alternative)</label>
                                                        <input type="email" class="form-control" id="userEmail" name="userEmail" placeholder="Enter user email">
                                                    </div>
                                                    <button type="submit" class="btn btn-warning w-100" aria-label="Redeem Meal">Redeem Meal</button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="text-center mb-4">
                                    <a href="${pageContext.request.contextPath}/user/userdashboard" class="btn btn-outline-warning">
                                        <i class="bi bi-person-circle me-2"></i>Switch to User Dashboard
                                    </a>
                                </div>
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

        // Initialize Charts
        function initializeCharts() {
            // Followers Growth Chart
            new Chart(document.getElementById('followersChart'), {
                type: 'line',
                data: {
                    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
                    datasets: [{
                        label: 'Followers Growth',
                        data: [30, 45, 60, 80, 95, ${stats.totalFollowers}],
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
                    }
                }
            });

            // Purchases Chart
            new Chart(document.getElementById('purchasesChart'), {
                type: 'bar',
                data: {
                    labels: ['Last Week', 'This Week'],
                    datasets: [{
                        label: 'Weekly Purchases',
                        data: [${stats.totalPurchases - 20}, ${stats.totalPurchases}],
                        backgroundColor: '#FFD700'
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            display: false
                        }
                    }
                }
            });

            // Revenue Chart
            new Chart(document.getElementById('revenueChart'), {
                type: 'doughnut',
                data: {
                    labels: ['Food', 'Beverages', 'Packages'],
                    datasets: [{
                        data: [40, 20, 40],
                        backgroundColor: ['#FFD700', '#FFA500', '#FF8C00']
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            display: false
                        }
                    }
                }
            });

            // Sales Trend Chart
            new Chart(document.getElementById('salesTrendChart'), {
                type: 'line',
                data: {
                    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                    datasets: [{
                        label: 'Sales',
                        data: [65, 59, 80, 81, 56, 55, 40],
                        fill: false,
                        borderColor: '#FFD700',
                        tension: 0.1
                    }]
                },
                options: {
                    responsive: true
                }
            });

            // Peak Hours Chart
            new Chart(document.getElementById('peakHoursChart'), {
                type: 'bar',
                data: {
                    labels: ['8AM', '10AM', '12PM', '2PM', '4PM', '6PM', '8PM'],
                    datasets: [{
                        label: 'Customer Traffic',
                        data: [30, 45, 90, 70, 50, 85, 60],
                        backgroundColor: '#FFD700'
                    }]
                },
                options: {
                    responsive: true
                }
            });

            // Package Performance Chart
            new Chart(document.getElementById('packagePerformanceChart'), {
                type: 'radar',
                data: {
                    labels: ['Value', 'Popularity', 'Retention', 'Growth', 'Satisfaction'],
                    datasets: [{
                        label: 'Basic Package',
                        data: [65, 59, 90, 81, 56],
                        fill: true,
                        backgroundColor: 'rgba(255, 215, 0, 0.2)',
                        borderColor: '#FFD700',
                        pointBackgroundColor: '#FFD700'
                    }]
                },
                options: {
                    responsive: true
                }
            });

            // Demographics Chart
            new Chart(document.getElementById('demographicsChart'), {
                type: 'pie',
                data: {
                    labels: ['18-24', '25-34', '35-44', '45-54', '55+'],
                    datasets: [{
                        data: [15, 30, 25, 20, 10],
                        backgroundColor: [
                            '#FFD700',
                            '#FFA500',
                            '#FF8C00',
                            '#FF7F50',
                            '#FF6347'
                        ]
                    }]
                },
                options: {
                    responsive: true
                }
            });
        }

        // Initialize charts when the page loads
        window.addEventListener('load', initializeCharts);

        // Initialize QR Code Scanner
        function onScanSuccess(decodedText, decodedResult) {
            document.getElementById('redemptionCode').value = decodedText;
            document.getElementById('redeemForm').dispatchEvent(new Event('submit'));
        }

        function onScanFailure(error) {
            // Handle scan failure, usually better to ignore and keep scanning
            console.warn(`QR Code scanning failed = ${error}`);
        }

        let html5QrcodeScanner = new Html5QrcodeScanner(
            "qr-reader",
            { fps: 10, qrbox: {width: 250, height: 250} },
            false
        );
        html5QrcodeScanner.render(onScanSuccess, onScanFailure);

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