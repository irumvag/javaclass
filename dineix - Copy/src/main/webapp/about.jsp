<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>About DineIX | Meal Freedom</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Learn about DineIX, a digital meal ticket platform revolutionizing dining with flexible meal packages.">
        <meta property="og:title" content="About DineIX - Meal Freedom">
        <meta property="og:description" content="Discover how DineIX connects food lovers with restaurants through innovative meal packages.">
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
                height: 70vh;
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

            .meal-card:hover {
                transform: translateY(-10px);
            }

            .feature-icon {
                font-size: 2.5rem;
                margin-bottom: 1rem;
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

            .feature-card {
                border: 2px solid var(--primary-yellow);
                border-radius: 15px;
                transition: transform 0.3s ease;
            }

            .feature-card:hover {
                transform: translateY(-5px);
            }

            .feature-icon {
                width: 60px;
                height: 60px;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .tech-logo {
                padding: 15px;
                border: 2px solid var(--primary-yellow);
                border-radius: 10px;
                margin-bottom: 10px;
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
                <a class="navbar-brand" href="${pageContext.request.contextPath}/"><img src="img/logo.png" alt="DineIX Logo" height="40"></a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navContent" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navContent">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/">Home</a></li>
                        <li class="nav-item"><a class="nav-link text-warning active" href="#">About</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/restaurants">Restaurants</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/login">Login</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/register">Register</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Main Content -->
        <div class="container my-5">
            <div class="row">
                <div class="col-md-8 mx-auto">
                    <h1 class="text-center mb-4" data-aos="fade-up">About DineIX</h1>
                    <div class="card shadow" data-aos="fade-up" data-aos-delay="100">
                        <div class="card-body">
                            <h3 class="text-yellow">Revolutionizing Meal Experiences</h3>
                            <p class="lead">DineIX is an innovative meal ticket system that connects food lovers with their favorite restaurants. Our platform allows users to purchase meal packages from multiple restaurants and redeem them flexibly using secure QR tickets.</p>

                            <!-- Core Features -->
                            <div class="card shadow-lg mb-5" data-aos="fade-up" data-aos-delay="200">
                                <div class="card-body">
                                    <h2 class="text-center mb-4 text-yellow">Why Choose DineIX?</h2>
                                    <div class="row g-4">
                                        <div class="col-md-6" data-aos="zoom-in" data-aos-delay="100">
                                            <div class="feature-card p-4">
                                                <div class="feature-icon bg-yellow text-black rounded-circle mb-3">
                                                    <i class="bi bi-shop-window fs-1"></i>
                                                </div>
                                                <h4>Multi-Restaurant Access</h4>
                                                <p>Purchase meal packages from various partner restaurants in one platform.</p>
                                            </div>
                                        </div>
                                        <div class="col-md-6" data-aos="zoom-in" data-aos-delay="200">
                                            <div class="feature-card p-4">
                                                <div class="feature-icon bg-yellow text-black rounded-circle mb-3">
                                                    <i class="bi bi-qr-code fs-1"></i>
                                                </div>
                                                <h4>Instant Ticket Generation</h4>
                                                <p>Generate secure QR tickets instantly for meal redemption.</p>
                                            </div>
                                        </div>
                                        <div class="col-md-6" data-aos="zoom-in" data-aos-delay="300">
                                            <div class="feature-card p-4">
                                                <div class="feature-icon bg-yellow text-black rounded-circle mb-3">
                                                    <i class="bi bi-shield-lock fs-1"></i>
                                                </div>
                                                <h4>Secure Transactions</h4>
                                                <p>Bank-grade encryption and CSRF protection for all transactions.</p>
                                            </div>
                                        </div>
                                        <div class="col-md-6" data-aos="zoom-in" data-aos-delay="400">
                                            <div class="feature-card p-4">
                                                <div class="feature-icon bg-yellow text-black rounded-circle mb-3">
                                                    <i class="bi bi-graph-up fs-1"></i>
                                                </div>
                                                <h4>Real-time Analytics</h4>
                                                <p>Track your meal usage and spending patterns in real-time.</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Platform Highlights -->
                            <div class="row g-4 mb-5">
                                <div class="col-md-6" data-aos="fade-up" data-aos-delay="100">
                                    <div class="card h-100 bg-yellow text-black">
                                        <div class="card-body">
                                            <h3><i class="bi bi-phone"></i> Mobile-First Design</h3>
                                            <ul class="list-unstyled">
                                                <li>✓ Responsive on all devices</li>
                                                <li>✓ Progressive Web App support</li>
                                                <li>✓ Online accessibility</li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6" data-aos="fade-up" data-aos-delay="200">
                                    <div class="card h-100 bg-yellow text-black">
                                        <div class="card-body">
                                            <h3><i class="bi bi-star"></i> Premium Features</h3>
                                            <ul class="list-unstyled">
                                                <li>✓ Favorite restaurants list</li>
                                                <li>✓ Meal package sharing</li>
                                                <li>✓ Gift meal packages</li>
                                                <li>✓ Priority customer support</li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Technology Stack -->
                            <div class="card border-yellow mb-5" data-aos="fade-up" data-aos-delay="300">
                                <div class="card-header bg-yellow">
                                    <h3 class="mb-0">Our Technology</h3>
                                </div>
                                <div class="card-body">
                                    <div class="row text-center g-4">
                                        <c:forEach var="tech" items="${techStack}">
                                            <div class="col" data-aos="zoom-in" data-aos-delay="${loop.index * 100}">
                                                <div class="tech-logo" data-bs-toggle="tooltip" title="${tech.description}">
                                                    <i class="bi bi-${tech.icon} fs-1 text-yellow"></i>
                                                    <div>${tech.name}</div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

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

            // Initialize Bootstrap tooltips
            const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
            const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));

            // Back to top button
            const backToTop = document.getElementById("backToTop");
            window.addEventListener("scroll", () => {
                backToTop.style.display = window.scrollY > 300 ? "block" : "none";
            });
            backToTop.addEventListener("click", () => window.scrollTo({ top: 0, behavior: "smooth" }));
        </script>
    </body>
</html>