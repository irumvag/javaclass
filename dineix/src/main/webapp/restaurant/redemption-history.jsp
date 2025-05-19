<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Redemption History | DineIX</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="View meal redemption history for your restaurant on DineIX.">
        <meta property="og:title" content="Redemption History - DineIX">
        <meta property="og:description" content="Access your DineIX restaurant redemption history.">
        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
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
                margin-bottom: 20px;
            }

            .card:hover {
                transform: translateY(-5px);
            }

            .btn-warning {
                background-color: var(--primary-yellow);
                border-color: var(--primary-yellow);
                color: var(--secondary-black);
                font-weight: 500;
            }

            .btn-warning:hover {
                background-color: #e6c200;
                border-color: #e6c200;
                color: var(--secondary-black);
            }

            .table th {
                font-weight: 600;
                background-color: rgba(255, 215, 0, 0.1);
            }

            .badge-success {
                background-color: #28a745;
                color: white;
            }

            .badge-danger {
                background-color: #dc3545;
                color: white;
            }

            .stat-card {
                border-radius: 10px;
                padding: 20px;
                margin-bottom: 20px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                transition: transform 0.3s ease;
            }

            .stat-card:hover {
                transform: translateY(-5px);
            }

            .stat-card .stat-icon {
                font-size: 2.5rem;
                margin-bottom: 10px;
            }

            .stat-card .stat-value {
                font-size: 1.8rem;
                font-weight: 700;
            }

            .stat-card .stat-label {
                font-size: 1rem;
                color: #6c757d;
            }

            .bg-light-yellow {
                background-color: rgba(255, 215, 0, 0.15);
            }

            .bg-light-green {
                background-color: rgba(40, 167, 69, 0.15);
            }

            .bg-light-red {
                background-color: rgba(220, 53, 69, 0.15);
            }
        </style>
    </head>
    <body>
        <!-- Navigation Bar -->
        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3 col-lg-2 d-md-block sidebar collapse" id="sidebarMenu">
                    <div class="position-sticky">
                        <div class="text-center mb-4">
                            <a href="${pageContext.request.contextPath}/" class="d-flex align-items-center justify-content-center text-decoration-none">
                                <span class="fs-4 fw-bold">Dine<span class="text-black">IX</span></span>
                            </a>
                        </div>
                        <ul class="nav flex-column">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/restaurant/dashboard">
                                    <i class="bi bi-speedometer2 me-2"></i> Dashboard
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/restaurant/redemption-history">
                                    <i class="bi bi-clock-history me-2"></i> Redemption History
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/">
                                    <i class="bi bi-house-door me-2"></i> Home
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                                    <i class="bi bi-box-arrow-right me-2"></i> Logout
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>

                <!-- Main Content -->
                <div class="col-md-9 ms-sm-auto col-lg-10 px-md-4 main-content">
                    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                        <h1 class="h2">Redemption History</h1>
                    </div>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            ${fn:escapeXml(errorMessage)}
                        </div>
                    </c:if>

                    <!-- Stats Cards -->
                    <div class="row mb-4">
                        <div class="col-md-4">
                            <div class="stat-card bg-light-yellow">
                                <div class="stat-icon text-warning">
                                    <i class="bi bi-receipt"></i>
                                </div>
                                <div class="stat-value">${redemptionStats.totalRedemptions}</div>
                                <div class="stat-label">Total Redemptions</div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="stat-card bg-light-green">
                                <div class="stat-icon text-success">
                                    <i class="bi bi-check-circle"></i>
                                </div>
                                <div class="stat-value">${redemptionStats.successfulRedemptions}</div>
                                <div class="stat-label">Successful Redemptions</div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="stat-card bg-light-red">
                                <div class="stat-icon text-danger">
                                    <i class="bi bi-x-circle"></i>
                                </div>
                                <div class="stat-value">${redemptionStats.failedRedemptions}</div>
                                <div class="stat-label">Failed Redemptions</div>
                            </div>
                        </div>
                    </div>

                    <!-- Redemption History Table -->
                    <div class="card" data-aos="fade-up">
                        <div class="card-header bg-yellow text-black">
                            <h3 class="mb-0">Meal Redemption History</h3>
                        </div>
                        <div class="card-body">
                            <c:if test="${empty redemptionHistory}">
                                <p class="text-center">No redemption history found for your restaurant.</p>
                            </c:if>
                            <c:if test="${not empty redemptionHistory}">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Date & Time</th>
                                                <th>User</th>
                                                <th>Package</th>
                                                <th>Redemption Code</th>
                                                <th>Status</th>
                                                <th>Message</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="redemption" items="${redemptionHistory}">
                                                <tr>
                                                    <td>${fn:escapeXml(redemption.timestamp)}</td>
                                                    <td>
                                                        <div>${fn:escapeXml(redemption.userName)}</div>
                                                        <small class="text-muted">${fn:escapeXml(redemption.userEmail)}</small>
                                                    </td>
                                                    <td>${fn:escapeXml(redemption.packageName)}</td>
                                                    <td>${fn:escapeXml(redemption.redemptionCode)}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${redemption.success}">
                                                                <span class="badge bg-success">Success</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-danger">Failed</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${fn:escapeXml(redemption.message)}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- AOS Animation Library -->
        <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
        <script>
            // Initialize AOS animations
            AOS.init({
                duration: 800,
                easing: 'ease-in-out'
            });
        </script>
    </body>
</html>