<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Register | DineIX</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Join DineIX to purchase flexible meal packages and dine at your favorite restaurants.">
        <meta property="og:title" content="Register - DineIX">
        <meta property="og:description" content="Sign up for DineIX to enjoy flexible meal packages and seamless dining experiences.">
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

            .register-card {
                border: 2px solid var(--primary-yellow);
                border-radius: 15px;
            }

            .password-strength .progress {
                height: 5px;
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
                        <li class="nav-item"><a class="nav-link text-warning" href="restaurants">Restaurants</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="login">Login</a></li>
                        <li class="nav-item"><a class="nav-link text-warning active" href="${pageContext.request.contextPath}/register">Register</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Hero -->
        <section class="hero-section" data-aos="fade-up">
            <div class="container">
                <h1 class="display-4 fw-bold">Join DineIX Today</h1>
                <p class="lead">Sign up to enjoy flexible meal packages and dine anywhere, anytime.</p>
            </div>
        </section>

        <!-- Register Form -->
        <section class="py-5">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-8">
                        <!-- Error/Success Message -->
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert" data-aos="fade-up">
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

                        <div class="card register-card shadow-lg" data-aos="zoom-in" data-aos-delay="100">
                            <div class="card-header bg-yellow text-black">
                                <h3 class="text-center mb-0">Create Account</h3>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/register" method="POST" id="registerForm">
                                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="fullName" class="form-label">Full Name</label>
                                                <input type="text" class="form-control" id="fullName" name="fullName" required aria-describedby="fullNameHelp">
                                                <div id="fullNameHelp" class="form-text">Enter your full name as it appears on your ID.</div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="email" class="form-label">Email Address</label>
                                                <input type="email" class="form-control" id="email" name="email" required aria-describedby="emailHelp">
                                                <div id="emailHelp" class="form-text">We'll send a verification link to this email.</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="password" class="form-label">Password</label>
                                                <input type="password" class="form-control" id="password" name="password" required aria-describedby="passwordHelp">
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="confirmPassword" class="form-label">Confirm Password</label>
                                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="password-strength mb-4">
                                        <div class="progress mb-2">
                                            <div class="progress-bar" role="progressbar" style="width: 0%;" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
                                        </div>
                                        <small class="text-muted">Password must be at least 8 characters, including 1 number and 1 special character.</small>
                                    </div>
                                    <button type="submit" class="btn btn-warning w-100">Register</button>
                                    <p class="text-center mt-3">
                                        Already have an account? <a href="login" class="text-warning">Login here</a>
                                    </p>
                                    <p class="text-center text-muted small">
                                        After registering, you can request to become a Restaurant Owner or Admin from your profile.
                                    </p>
                                </form>
                            </div>
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
                            <li><a href="register">Get Started</a></li>
                            <li><a href="${pageContext.request.contextPath}/#how-it-works">How It Works</a></li>
                            <li><a href="restaurants">Restaurants</a></li>
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

            // Password strength indicator
            document.getElementById('password').addEventListener('input', function (e) {
                const strength = {
                    0: { color: '#dc3545', width: '25%' },
                    1: { color: '#ffc107', width: '50%' },
                    2: { color: '#28a745', width: '100%' }
                };
                const progress = document.querySelector('.progress-bar');
                const value = e.target.value;
                let score = 0;
                if (value.length >= 8) score++;
                if (/\d/.test(value)) score++;
                if (/[!@#$%^&*]/.test(value)) score++;
                progress.style.width = strength[score].width;
                progress.style.backgroundColor = strength[score].color;
                progress.setAttribute('aria-valuenow', score * 33.3);
            });

            // Client-side form validation
            document.getElementById('registerForm').addEventListener('submit', function (e) {
                const password = document.getElementById('password').value;
                const confirmPassword = document.getElementById('confirmPassword').value;
                if (password !== confirmPassword) {
                    e.preventDefault();
                    alert('Passwords do not match!');
                }
            });
        </script>
    </body>
</html>