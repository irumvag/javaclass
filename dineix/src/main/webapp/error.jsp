<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Error | DineIX</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="An error occurred on DineIX. Return to the homepage to continue exploring meal packages.">
        <meta property="og:title" content="Error - DineIX">
        <meta property="og:description" content="An error occurred on DineIX. Return to the homepage to continue exploring meal packages.">
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

            .error-card {
                border: 2px solid var(--primary-yellow);
                border-radius: 15px;
                transition: transform 0.3s ease;
            }

            .error-icon {
                font-size: 4rem;
                color: var(--primary-yellow);
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
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/restaurants">Restaurants</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/login">Login</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/register">Register</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Hero -->
        <section class="hero-section" data-aos="fade-up">
            <div class="container">
                <h1 class="display-4 fw-bold">Oops! Something Went Wrong</h1>
                <p class="lead">We couldn't find what you were looking for.</p>
            </div>
        </section>

        <!-- Error Content -->
        <section class="py-5">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-6">
                        <div class="error-card p-4 shadow text-center" data-aos="zoom-in" data-aos-delay="100">
                            <i class="bi bi-exclamation-triangle error-icon mb-3"></i>
                            <h2>Error <c:out value="${pageContext.errorData.statusCode}"/></h2>
                            <p>
                                <c:choose>
                                    <c:when test="${pageContext.errorData.statusCode == 404}">
                                        The page you requested was not found.
                                    </c:when>
                                    <c:when test="${pageContext.errorData.statusCode == 500}">
                                        An unexpected server error occurred.
                                    </c:when>
                                    <c:otherwise>
                                        An error occurred. Please try again later.
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <a href="${pageContext.request.contextPath}/" class="btn btn-warning">Back to Home</a>
                        </div>
                    </div>
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
ifax
                            <li><a href="register.html">Get Started</a></li>
                            <li><a href="${pageContext.request.contextPath}/#how-it-works">How It Works</a></li>
                            <li><a href="restaurants.html">Restaurants</a></li>
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
        </script>
    </body>
</html>