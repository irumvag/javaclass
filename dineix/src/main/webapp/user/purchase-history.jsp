<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Purchase History | DineIX</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="View your meal package purchase history and redemption logs on DineIX.">
        <meta property="og:title" content="Purchase History - DineIX">
        <meta property="og:description" content="Access your DineIX purchase history and redemption logs.">
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

            .nav-tabs .nav-link {
                color: var(--text-color);
                font-weight: 500;
                border: none;
                padding: 10px 20px;
                border-radius: 0;
                border-bottom: 3px solid transparent;
            }

            .nav-tabs .nav-link.active {
                color: var(--secondary-black);
                background-color: transparent;
                border-bottom: 3px solid var(--primary-yellow);
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

            .progress {
                height: 10px;
                border-radius: 5px;
            }

            .progress-bar-yellow {
                background-color: var(--primary-yellow);
            }
        </style>
    </head>
    <body>
        
        <!-- Navigation Bar -->
        <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm sticky-top">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                    <span class="fw-bold">Dine<span class="text-warning">IX</span></span>
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/user/dashboard">
                                <i class="bi bi-speedometer2 me-1"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/user/purchase-history">
                                <i class="bi bi-clock-history me-1"></i> Purchase History
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/restaurants">
                                <i class="bi bi-shop me-1"></i> Restaurants
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                                <i class="bi bi-box-arrow-right me-1"></i> Logout
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Main Content -->
        <div class="container py-5">
            <h1 class="mb-4">Your Purchase History</h1>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    ${fn:escapeXml(errorMessage)}
                </div>
            </c:if>
            
            <!-- Tabs Navigation -->
            <ul class="nav nav-tabs mb-4" id="historyTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="purchases-tab" data-bs-toggle="tab" data-bs-target="#purchases" type="button" role="tab">
                        <i class="bi bi-bag-check me-1"></i> Purchases
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="redemptions-tab" data-bs-toggle="tab" data-bs-target="#redemptions" type="button" role="tab">
                        <i class="bi bi-receipt me-1"></i> Redemptions
                    </button>
                </li>
            </ul>
            
            <!-- Tab Content -->
            <div class="tab-content" id="historyTabsContent">
                <!-- Purchases Tab -->
                <div class="tab-pane fade show active" id="purchases" role="tabpanel">
                    <div class="card" data-aos="fade-up">
                        <div class="card-header bg-yellow text-black">
                            <h3 class="mb-0">Meal Package Purchases</h3>
                        </div>
                        <div class="card-body">
                            <c:if test="${empty purchaseHistory}">
                                <p class="text-center">You haven't purchased any meal packages yet.</p>
                            </c:if>
                            <c:if test="${not empty purchaseHistory}">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Date</th>
                                                <th>Package</th>
                                                <th>Restaurant</th>
                                                <th>Price</th>
                                                <th>Meals Usage</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="purchase" items="${purchaseHistory}">
                                                <tr>
                                                    <td>${fn:escapeXml(purchase.purchaseDate)}</td>
                                                    <td>${fn:escapeXml(purchase.packageName)}</td>
                                                    <td>${fn:escapeXml(purchase.restaurantName)}</td>
                                                    <td>RWF ${fn:escapeXml(purchase.price)}</td>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <div class="progress flex-grow-1 me-2">
                                                                <div class="progress-bar progress-bar-yellow" role="progressbar" 
                                                                     style="width: ${(purchase.mealsUsed / purchase.totalMeals) * 100}%" 
                                                                     aria-valuenow="${purchase.mealsUsed}" aria-valuemin="0" aria-valuemax="${purchase.totalMeals}">
                                                                </div>
                                                            </div>
                                                            <span class="small">${purchase.mealsUsed}/${purchase.totalMeals}</span>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${purchase.mealsRemaining == 0}">
                                                                <span class="badge bg-secondary">Completed</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-success">${purchase.mealsRemaining} meals left</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
                
                <!-- Redemptions Tab -->
                <div class="tab-pane fade" id="redemptions" role="tabpanel">
                    <div class="card" data-aos="fade-up">
                        <div class="card-header bg-yellow text-black">
                            <h3 class="mb-0">Meal Redemption History</h3>
                        </div>
                        <div class="card-body">
                            <c:if test="${empty redemptionHistory}">
                                <p class="text-center">You haven't redeemed any meals yet.</p>
                            </c:if>
                            <c:if test="${not empty redemptionHistory}">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Date & Time</th>
                                                <th>Restaurant</th>
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
                                                    <td>${fn:escapeXml(redemption.restaurantName)}</td>
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

        <!-- Footer -->
        <footer class="bg-dark text-white py-4 mt-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-6">
                        <h5>DineIX</h5>
                        <p class="small">The smart way to manage your meal packages.</p>
                    </div>
                    <div class="col-md-6 text-md-end">
                        <p class="small">&copy; 2025 DineIX. All rights reserved.</p>
                    </div>
                </div>
            </div>
        </footer>

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