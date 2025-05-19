<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restaurant Dashboard - Dineix</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">
    
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    
    <!-- AOS Library for Animations -->
    <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
    
    <style>
        .bg-yellow { background-color: #FFD700; }
        .text-yellow { color: #FFD700; }
        .card { border-radius: 15px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }
        .card-header { border-radius: 15px 15px 0 0; }
        .stats-card { transition: transform 0.3s; }
        .stats-card:hover { transform: translateY(-5px); }
    </style>
</head>
<body class="bg-light">

<div class="container-fluid py-4">
    <!-- Error Message Display -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Stats Overview -->
    <div class="row mb-4">
        <div class="col-md-4" data-aos="fade-up">
            <div class="card h-100 stats-card">
                <div class="card-header bg-yellow text-black">
                    <h5 class="mb-0">Total Followers</h5>
                </div>
                <div class="card-body">
                    <h2 class="display-4">${stats.totalFollowers}</h2>
                    <p class="text-muted">Restaurant followers</p>
                </div>
            </div>
        </div>
        <div class="col-md-4" data-aos="fade-up" data-aos-delay="100">
            <div class="card h-100 stats-card">
                <div class="card-header bg-yellow text-black">
                    <h5 class="mb-0">Total Revenue</h5>
                </div>
                <div class="card-body">
                    <h2 class="display-4">RWF <fmt:formatNumber value="${stats.totalRevenue}" type="number"/></h2>
                    <p class="text-muted">Total earnings</p>
                </div>
            </div>
        </div>
        <div class="col-md-4" data-aos="fade-up" data-aos-delay="200">
            <div class="card h-100 stats-card">
                <div class="card-header bg-yellow text-black">
                    <h5 class="mb-0">Total Purchases</h5>
                </div>
                <div class="card-body">
                    <h2 class="display-4">${stats.totalPurchases}</h2>
                    <p class="text-muted">Completed orders</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Charts Section -->
    <div class="row mb-4">
        <div class="col-md-6" data-aos="fade-right">
            <div class="card h-100">
                <div class="card-header bg-yellow text-black">
                    <h5 class="mb-0">Monthly Purchases</h5>
                </div>
                <div class="card-body">
                    <canvas id="purchasesChart"></canvas>
                </div>
            </div>
        </div>
        <div class="col-md-6" data-aos="fade-left">
            <div class="card h-100">
                <div class="card-header bg-yellow text-black">
                    <h5 class="mb-0">Monthly Revenue</h5>
                </div>
                <div class="card-body">
                    <canvas id="revenueChart"></canvas>
                </div>
            </div>
        </div>
    </div>

    <!-- Meal Packages Section -->
    <div class="card mb-4" data-aos="zoom-in">
        <div class="card-header bg-yellow text-black d-flex justify-content-between align-items-center">
            <h5 class="mb-0">Active Meal Packages</h5>
            <a href="${pageContext.request.contextPath}/restaurant/packages" class="btn btn-dark">
                <i class="bi bi-plus-circle"></i> Manage Packages
            </a>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Package Name</th>
                            <th>Price (RWF)</th>
                            <th>Status</th>
                            <th>Sales</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${restaurantPackages}" var="package">
                            <tr>
                                <td>${package.name}</td>
                                <td><fmt:formatNumber value="${package.price}" type="number"/></td>
                                <td>
                                    <span class="badge ${package.active ? 'bg-success' : 'bg-danger'}">
                                        ${package.active ? 'Active' : 'Inactive'}
                                    </span>
                                </td>
                                <td>${package.totalSales}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- AOS Library -->
<script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>

<script>
    // Initialize AOS
    AOS.init();

    // Prepare data for charts
    const monthlyPurchasesData = {
        labels: Object.keys(${stats.monthlyPurchases}),
        datasets: [{
            label: 'Purchases',
            data: Object.values(${stats.monthlyPurchases}),
            backgroundColor: '#FFD700',
            borderColor: '#000000',
            borderWidth: 1
        }]
    };

    const monthlyRevenueData = {
        labels: Object.keys(${stats.monthlyRevenue}),
        datasets: [{
            label: 'Revenue (RWF)',
            data: Object.values(${stats.monthlyRevenue}),
            backgroundColor: '#FFD700',
            borderColor: '#000000',
            borderWidth: 1,
            fill: true
        }]
    };

    // Initialize Charts
    new Chart(document.getElementById('purchasesChart'), {
        type: 'bar',
        data: monthlyPurchasesData,
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Number of Purchases'
                    }
                }
            }
        }
    });

    new Chart(document.getElementById('revenueChart'), {
        type: 'line',
        data: monthlyRevenueData,
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Revenue (RWF)'
                    }
                }
            }
        }
    });
</script>

</body>
</html>