<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Restaurants | DineIX</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <meta name="description" content="Explore DineIX's partner restaurants and purchase flexible meal packages." />
        <meta property="og:title" content="Restaurants - DineIX" />
        <meta property="og:description" content="Discover a variety of restaurants on DineIX and enjoy seamless dining with meal packages." />
        <meta property="og:image" content="https://images.pexels.com/photos/3184183/pexels-photo-3184183.jpeg" />
        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <!-- Bootstrap Icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css" rel="stylesheet" />
        <!-- AOS Animations -->
        <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet" />
        <style>
            /* Custom styles (as needed) */
            :root {
                --primary-yellow: #FFD700;
                --secondary-black: #000000;
                --bg-color: #ffffff;
                --text-color: #000000;
                --bg-image: url('https://images.pexels.com/photos/3184183/pexels-photo-3184183.jpeg');
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
            .hero-section {
                background: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)), var(--bg-image);
                background-size: cover;
                background-position: center;
                height: 50vh;
                display: flex;
                align-items: center;
                justify-content: center;
                text-align: center;
                color: #fff;
            }
            .btn-warning {
                background-color: var(--primary-yellow);
                border: none;
                color: var(--secondary-black);
                font-weight: 600;
            }
            .btn-warning:hover {
                background-color: #e6c200;
            }
            .footer {
                background-color: var(--secondary-black);
                color: var(--primary-yellow);
                border-top: 1px solid #444;
            }
            .footer a {
                color: var(--primary-yellow);
                text-decoration: none;
            }
            .footer a:hover {
                color: #fff;
            }
            .nav-link.text-warning {
                color: #e6c200 !important;
            }
            .restaurant-card {
                border: 2px solid var(--primary-yellow);
                border-radius: 15px;
                transition: transform 0.3s ease;
            }
            .restaurant-card:hover {
                transform: translateY(-5px);
            }
            .restaurant-logo {
                height: 200px;
                object-fit: cover;
            }
            .btn-follow {
                transition: background-color 0.3s ease;
            }
            .btn-follow.followed {
                background-color: var(--primary-yellow);
                color: var(--secondary-black);
            }
            #loadingOverlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
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
                        <li class="nav-item"><a class="nav-link text-warning active" href="${pageContext.request.contextPath}/restaurants">Restaurants</a></li>
                            <c:choose>
                                <c:when test="${sessionScope.userId != null}">
                                <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/user/dashboard">Dashboard</a></li>
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

        <!-- Hero -->
        <section class="hero-section" data-aos="fade-up">
            <div class="container">
                <h1 class="display-4 fw-bold">Discover Our Partner Restaurants</h1>
                <p class="lead">Explore a variety of dining options and purchase meal packages.</p>
            </div>
        </section>

        <!-- Restaurants List -->
        <section class="py-5">
            <div class="container">

                <!-- Messages -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert" data-aos="fade-up">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <c:out value="${errorMessage}" />
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert" data-aos="fade-up">
                        <c:out value="${successMessage}" />
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Search and Sort -->
                <div class="row mb-4">
                    <div class="col-md-6">
                        <form id="searchForm" action="${pageContext.request.contextPath}/restaurants" method="GET" role="search">
                            <div class="input-group">
                                <input
                                    type="search"
                                    class="form-control"
                                    name="searchQuery"
                                    placeholder="Search restaurants..."
                                    value="${fn:escapeXml(searchQuery)}"
                                    aria-label="Search restaurants"
                                    />
                                <input type="hidden" name="csrfToken" value="${csrfToken}" />
                                <button class="btn btn-warning" type="submit" aria-label="Search">
                                    <i class="bi bi-search"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                    <div class="col-md-6 text-end">
                        <div class="btn-group">
                            <button class="btn btn-warning dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" aria-haspopup="true" aria-label="Sort restaurants">
                                Sort By
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li>
                                    <c:url var="sortByNameUrl" value="/restaurants">
                                        <c:param name="sort" value="name"/>
                                        <c:if test="${not empty searchQuery}">
                                            <c:param name="searchQuery" value="${searchQuery}"/>
                                        </c:if>
                                    </c:url>
                                    <a class="dropdown-item" href="${sortByNameUrl}">Name</a>
                                </li>
                                <li>
                                    <c:url var="sortByPopularUrl" value="/restaurants">
                                        <c:param name="sort" value="popular"/>
                                        <c:if test="${not empty searchQuery}">
                                            <c:param name="searchQuery" value="${searchQuery}"/>
                                        </c:if>
                                    </c:url>
                                    <a class="dropdown-item" href="${sortByPopularUrl}">Most Popular</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Restaurants Grid -->
                <c:if test="${empty restaurants}">
                    <div class="text-center" data-aos="fade-up">
                        <p>No restaurants found. Try adjusting your search or sort options.</p>
                    </div>
                </c:if>
                <div class="row row-cols-1 row-cols-md-3 g-4">
                    <c:forEach items="${restaurants}" var="restaurant" varStatus="loop">
                        <div class="col" data-aos="fade-up" data-aos-delay="${loop.index * 100}">
                            <div class="card restaurant-card h-100 shadow">
                                <img
                                    src="${fn:escapeXml(restaurant.logoUrl != null ? restaurant.logoUrl : 'img/default-restaurant.jpg')}"
                                    class="card-img-top restaurant-logo"
                                    alt="${fn:escapeXml(restaurant.name)} logo"
                                    loading="lazy"
                                    />
                                <div class="card-body d-flex flex-column">
                                    <h3 class="card-title">${fn:escapeXml(restaurant.name)}</h3>
                                    <p class="card-text flex-grow-1">${fn:escapeXml(restaurant.description)}</p>

                                    <div class="d-flex justify-content-between align-items-center mt-3">
                                        <c:choose>
                                            <c:when test="${sessionScope.userId != null}">
                                                <button
                                                    class="btn btn-outline-warning btn-follow ${follows.containsKey(restaurant.restaurantId) ? 'followed' : ''}"
                                                    data-restaurant-id="${restaurant.restaurantId}"
                                                    data-restaurant-name="${fn:escapeXml(restaurant.name)}"
                                                    data-csrf-token="${csrfToken}"
                                                    aria-label="${follows.containsKey(restaurant.restaurantId) ? 'Unfollow' : 'Follow'} ${fn:escapeXml(restaurant.name)}"
                                                    >
                                                    <i class="bi bi-${follows.containsKey(restaurant.restaurantId) ? 'check-circle' : 'plus-circle'}"></i>
                                                    <span>${follows.containsKey(restaurant.restaurantId) ? 'Following' : 'Follow'}</span>
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-warning" aria-label="Login to follow ${fn:escapeXml(restaurant.name)}">
                                                    <i class="bi bi-plus-circle"></i> Follow
                                                </a>
                                            </c:otherwise>
                                        </c:choose>

                                        <span class="badge bg-yellow text-black">
                                            <i class="bi bi-geo-alt"></i> ${fn:escapeXml(restaurant.address)}
                                        </span>
                                    </div>
                                    <p class="mt-2 mb-0">
                                        <i class="bi bi-telephone"></i>
                                        ${restaurant.contactNumber != null ? fn:escapeXml(restaurant.contactNumber) : 'N/A'}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- Back To Top Button -->
        <button
            id="backToTop"
            class="btn btn-warning position-fixed bottom-0 end-0 m-4"
            style="display:none;"
            aria-label="Back to top"
            >
            <i class="bi bi-arrow-up"></i>
        </button>

        <!-- Footer -->
        <footer class="footer py-3 mt-5">
            <div class="container text-center">
                <p class="mb-0">&copy; 2025 DineIX. All rights reserved.</p>
            </div>
        </footer>

        <!-- JS: Bootstrap, AOS, and your scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
        <script>
            AOS.init();

            // Show loading overlay on page load
            window.addEventListener('load', function () {
                document.getElementById('loadingOverlay').style.display = 'none';
            });

            // Back to top button
            const backToTopBtn = document.getElementById('backToTop');
            window.addEventListener('scroll', () => {
                if (window.scrollY > 300) {
                    backToTopBtn.style.display = 'block';
                } else {
                    backToTopBtn.style.display = 'none';
                }
            });
            backToTopBtn.addEventListener('click', () => {
                window.scrollTo({top: 0, behavior: 'smooth'});
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
        </script>
    </body>
</html>
