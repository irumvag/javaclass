<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Restaurants | DineIX</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Explore DineIX's partner restaurants and purchase flexible meal packages.">
        <meta property="og:title" content="Restaurants - DineIX">
        <meta property="og:description" content="Discover a variety of restaurants on DineIX and enjoy seamless dining with meal packages.">
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

            #backToTop {
                z-index: 1000;
            }
        </style>
    </head>
    <body>
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
                        <li class="nav-item"><a class="nav-link text-warning active" href="${pageContext.request.contextPath}/restaurants">Restaurants</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/login">Login</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/register">Register</a></li>
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

                <!-- Search and Sort -->
                <div class="row mb-4">
                    <div class="col-md-6">
                        <form id="searchForm" action="${pageContext.request.contextPath}/restaurants" method="GET">
                            <div class="input-group">
                                <input type="text" class="form-control" placeholder="Search restaurants..." name="searchQuery" value="${param.searchQuery}">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                <button class="btn btn-warning" type="submit" aria-label="Search">
                                    <i class="bi bi-search"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                    <div class="col-md-6 text-end">
                        <div class="btn-group">
                            <button class="btn btn-warning dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                Sort By
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/restaurants?sort=name${param.searchQuery != null ? '&searchQuery=' + param.searchQuery : ''}">Name</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/restaurants?sort=popular${param.searchQuery != null ? '&searchQuery=' + param.searchQuery : ''}">Most Popular</a></li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Restaurant Cards -->
                <div class="row row-cols-1 row-cols-md-3 g-4">
                    <c:forEach items="${restaurants}" var="restaurant" varStatus="loop">
                        <div class="col" data-aos="fade-up" data-aos-delay="${loop.index * 100}">
                            <div class="card restaurant-card h-100 shadow">
                                <img src="${restaurant.logoUrl != null ? restaurant.logoUrl : 'img/default-restaurant.jpg'}" class="card-img-top restaurant-logo" alt="${restaurant.name} logo" loading="lazy">
                                <div class="card-body">
                                    <h3 class="card-title">${restaurant.name}</h3>
                                    <p class="card-text">${restaurant.description}</p>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <c:choose>
                                            <c:when test="${sessionScope.userId != null}">
                                                <button class="btn btn-outline-warning btn-follow ${follows.containsKey(restaurant.restaurantId) ? 'followed' : ''}" 
                                                        data-restaurant-id="${restaurant.restaurantId}" 
                                                        data-restaurant-name="${restaurant.name}"
                                                        data-csrf-token="${csrfToken}"
                                                        aria-label="${follows.containsKey(restaurant.restaurantId) ? 'Unfollow' : 'Follow'} ${restaurant.name}">
                                                    <i class="bi bi-${follows.containsKey(restaurant.restaurantId) ? 'check-circle' : 'plus-circle'}"></i>
                                                    <span>${follows.containsKey(restaurant.restaurantId) ? 'Following' : 'Follow'}</span>
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-warning" aria-label="Login to follow ${restaurant.name}">
                                                    <i class="bi bi-plus-circle"></i> Follow
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                        <span class="badge bg-yellow text-black">
                                            <i class="bi bi-geo-alt"></i> ${restaurant.address}
                                        </span>
                                    </div>
                                    <p class="mt-2 mb-0"><i class="bi bi-telephone"></i> ${restaurant.contactNumber}</p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- Footer -->
        <footer class="footer py-5">
            <div class="container">
                <div class="row g-4">
                    <div class="col-md-3">
                        <h5>About DineIX</h5>
                        <p>Digital meal ticket platform making dining more convenient and affordable.</p>
                    </div>
                    <div class="col-md-3">
                        <h5>Quick Links</h5>
                        <ul class="list-unstyled">
                            <li><a href="${pageContext.request.contextPath}/register">Get Started</a></li>
                            <li><a href="${pageContext.request.contextPath}/#how-it-works">How It Works</a></li>
                            <li><a href="${pageContext.request.contextPath}/restaurants">Restaurants</a></li>
                        </ul>
                    </div>
                    <div class="col-md-3">
                        <h5>Contact</h5>
                        <ul class="list-unstyled">
                            <li>Email: info@dineix.com</li>
                            <li>Phone: (123) 456-7890</li>
                            <li>Address: Kiyovu street</li>
                        </ul>
                    </div>
                    <div class="col-md-3">
                        <h5>Follow Us</h5>
                        <div class="d-flex gap-3">
                            <a href="#" aria-label="Facebook"><i class="bi bi-facebook fs-4"></i></a>
                            <a href="#" aria-label="Twitter"><i class="bi bi-twitter fs-4"></i></a>
                            <a href="#" aria-label="Instagram"><i class="bi bi-instagram fs-4"></i></a>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="text-center mt-5 pt-8">
                    <p>© 2025 DineIX. All rights reserved.</p>
                </div>
            </div>
        </footer>

        <!-- Back to Top -->
        <button id="backToTop" class="btn btn-warning position-fixed bottom-0 end-0 m-3" style="display: none;" aria-label="Back to top">Top</button>

        <!-- Scripts -->
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

            // Follow button functionality
            document.querySelectorAll('.btn-follow').forEach(button => {
                button.addEventListener('click', function () {
                    const restaurantId = this.getAttribute('data-restaurant-id');
                    const restaurantName = this.getAttribute('data-restaurant-name');
                    const csrfToken = this.getAttribute('data-csrf-token');
                    const isFollowed = this.classList.contains('followed');

                    fetch('${pageContext.request.contextPath}/follow-restaurant', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `restaurantId=${restaurantId}&action=${isFollowed ? 'unfollow' : 'follow'}&csrfToken=${csrfToken}`
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            this.classList.toggle('followed');
                            this.querySelector('span').textContent = isFollowed ? 'Follow' : 'Following';
                            this.querySelector('i').className = `bi bi-${isFollowed ? 'plus-circle' : 'check-circle'}`;
                            this.setAttribute('aria-label', isFollowed ? `Follow ${restaurantName}` : `Unfollow ${restaurantName}`);
                        } else {
                            alert(data.message || 'An error occurred.');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('An error occurred while processing your request.');
                    });
                });
            });
        </script>
    </body>
</html>
