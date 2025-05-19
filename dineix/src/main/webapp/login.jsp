<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login | DineIX</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Log in to DineIX to access your meal packages and dine at your favorite restaurants.">
    <meta property="og:title" content="Login - DineIX">
    <meta property="og:description" content="Sign in to DineIX to enjoy flexible meal packages and seamless dining experiences.">
    <meta property="og:image" content="${pageContext.request.contextPath}/img/hero-bg.jpg">
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
            --text-color: #212529;
            --bg-image:url('https://images.pexels.com/photos/3184183/pexels-photo-3184183.jpeg');
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
            background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)), var(--bg-image);
            background-size: cover;
            background-position: center;
            min-height: 60vh;
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
            border-radius: 8px;
            transition: background-color 0.3s ease;
        }

        .btn-warning:hover {
            background-color: #e6c200;
        }

        .footer {
            background-color: var(--secondary-black);
            color: var(--primary-yellow);
            padding: 3rem 0;
        }

        .footer a {
            color: var(--primary-yellow);
            text-decoration: none;
        }

        .footer a:hover {
            color: #fff;
        }

        .nav-link.text-warning {
            color: var(--primary-yellow) !important;
        }

        .nav-link.text-warning:hover {
            color: #e6c200 !important;
        }

        .login-card {
            border: 2px solid var(--primary-yellow);
            border-radius: 15px;
            background-color: #ffffff;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .form-control:focus {
            border-color: var(--primary-yellow);
            box-shadow: 0 0 0 0.2rem rgba(255, 215, 0, 0.25);
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
            <<a class="navbar-brand" href="${pageContext.request.contextPath}/">
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
                    <li class="nav-item"><a class="nav-link text-warning active" href="${pageContext.request.contextPath}/login">Login</a></li>
                    <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/register">Register</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero -->
    <section class="hero-section" data-aos="fade-up">
        <div class="container">
            <h1 class="display-4 fw-bold">Welcome Back to DineIX</h1>
            <p class="lead">Log in to access your meal packages and dine anywhere, anytime.</p>
        </div>
    </section>

    <!-- Login Form -->
    <section class="py-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6 col-lg-5">
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

                    <div class="card login-card shadow-lg" data-aos="zoom-in" data-aos-delay="100">
                        <div class="card-header bg-yellow text-black">
                            <h3 class="text-center mb-0">Sign In</h3>
                        </div>
                        <div class="card-body p-4">
                            <form action="${pageContext.request.contextPath}/login" method="POST" id="loginForm">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                <div class="mb-3">
                                    <label for="email" class="form-label">Email Address</label>
                                    <input type="email" class="form-control" id="email" name="email" required aria-required="true" aria-describedby="emailHelp">
                                    <div id="emailHelp" class="form-text">Enter the email you registered with.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="password" class="form-control" id="password" name="password" required aria-required="true" aria-describedby="passwordHelp">
                                    <div id="passwordHelp" class="form-text">Enter your account password.</div>
                                </div>
                                <div class="mb-3 form-check">
                                    <input type="checkbox" class="form-check-input" id="rememberMe" name="rememberMe">
                                    <label class="form-check-label" for="rememberMe">Remember Me</label>
                                </div>
                                <button type="submit" class="btn btn-warning w-100" aria-label="Login">Login</button>
                                <div class="text-center mt-3">
                                    <p><a href="${pageContext.request.contextPath}/forgot-password" class="text-warning">Forgot Password?</a></p>
                                    <p>No account? <a href="${pageContext.request.contextPath}/register" class="text-warning">Create one now</a></p>
                                    <p class="text-muted small">By logging in, you agree to our <a href="${pageContext.request.contextPath}/terms" class="text-warning">Terms and Policies</a>.</p>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="footer">
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
                        <li>Address: Kiyovu Street, Kigali</li>
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
            <hr class="border-secondary">
            <div class="text-center mt-4">
                <p>© 2025 DineIX. All rights reserved.</p>
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

        // Client-side form validation
        document.getElementById('loginForm').addEventListener('submit', function (e) {
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

            if (!email || !password) {
                e.preventDefault();
                alert('Please fill in all fields.');
            } else if (!emailRegex.test(email)) {
                e.preventDefault();
                alert('Please enter a valid email address.');
            }
        });
    </script>
</body>
</html>