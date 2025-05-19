<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>DineIX | Meal Freedom</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
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

            .hero-section {
                background: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)),
                    url('https://images.pexels.com/photos/3184183/pexels-photo-3184183.jpeg');
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
            }

            .footer a {
                color: var(--primary-yellow);
                text-decoration: none;
            }

            .footer a:hover {
                color: #fff;
            }
        </style>
    </head>
    <body>

        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-black sticky-top">
            <div class="container">
                <a class="navbar-brand" href="#"><img src="img/logo.png" alt="DineIX" height="40"></a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navContent">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navContent">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item"><a class="nav-link text-warning" href="#">Home</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/about">About</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/restaurants">Restaurants</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/login">Login</a></li>
                        <li class="nav-item"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/register">Register</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Hero -->
        <section class="hero-section">
            <div class="container">
                <h1 class="display-4 fw-bold">Freedom to Dine Anywhere</h1>
                <p class="lead">Buy flexible meal packages. Redeem them whenever, wherever.</p>
                <a href="register" class="btn btn-warning btn-lg">Get Started</a>
            </div>
        </section>

        <!-- How It Works -->
        <section class="py-5">
            <div class="container text-center">
                <h2 class="mb-4">How It Works</h2>
                <div class="row g-4">
                    <div class="col-md-3">
                        <div class="card p-4 shadow-sm meal-card">
                            <i class="bi bi-person-plus feature-icon text-warning"></i>
                            <h5>1. Sign Up</h5>
                            <p>Create your free account in seconds.</p>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card p-4 shadow-sm meal-card">
                            <i class="bi bi-shop feature-icon text-warning"></i>
                            <h5>2. Choose Restaurant</h5>
                            <p>Explore our partner restaurants.</p>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card p-4 shadow-sm meal-card">
                            <i class="bi bi-bag-check feature-icon text-warning"></i>
                            <h5>3. Buy Package</h5>
                            <p>Select and pay for your meal pack.</p>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card p-4 shadow-sm meal-card">
                            <i class="bi bi-emoji-smile feature-icon text-warning"></i>
                            <h5>4. Redeem Anytime</h5>
                            <p>Use your code at any partner location.</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Benefit Section -->
        <section class="py-5 bg-light">
            <div class="container text-center">
                <h2 class="mb-4">Benefits of Joining Us</h2>
                <div class="row g-4">
                    <div class="col-md-4">
                        <div class="card p-4 h-100">
                            <i class="bi bi-currency-dollar feature-icon text-success"></i>
                            <h5>Save Money</h5>
                            <p>Get exclusive discounts and package deals from multiple restaurants.</p>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card p-4 h-100">
                            <i class="bi bi-phone feature-icon text-primary"></i>
                            <h5>Digital Convenience</h5>
                            <p>No more paper tickets. Access your meal tickets instantly on your phone.</p>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card p-4 h-100">
                            <i class="bi bi-layout-text-sidebar feature-icon text-danger"></i>
                            <h5>Multiple Choices</h5>
                            <p>Choose from a variety of restaurants and cuisines with one subscription.</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Menu Section (Example) -->
        <section id="menu" class="container py-5">
            <h2 class="text-center mb-4">Our Menu</h2>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="card h-100">
                        <img src="https://source.unsplash.com/400x300/?pizza" class="card-img-top" alt="Pizza">
                        <div class="card-body">
                            <h5 class="card-title">Delicious Pizza</h5>
                            <p class="card-text">Wood-fired pizza with fresh ingredients and traditional recipes.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100">
                        <img src="https://source.unsplash.com/400x300/?burger" class="card-img-top" alt="Burger">
                        <div class="card-body">
                            <h5 class="card-title">Juicy Burgers</h5>
                            <p class="card-text">Flame-grilled gourmet burgers made to order.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100">
                        <img src="https://source.unsplash.com/400x300/?pasta" class="card-img-top" alt="Pasta">
                        <div class="card-body">
                            <h5 class="card-title">Homemade Pasta</h5>
                            <p class="card-text">Authentic Italian pasta with a variety of sauces.</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Meal Counter -->
        <section class="py-5 text-center">
            <div class="container">
                <h2>Meals Redeemed</h2>
                <p class="display-5 text-warning" id="mealCounter">0</p>
                <p>and counting!</p>
            </div>
        </section>

        <!-- Partners -->
        <section class="py-5 bg-light">
            <div class="container text-center">
                <h2 class="mb-4">Trusted By</h2>
                <div class="row justify-content-center align-items-center">
                    <div class="col-auto"><img src="https://via.placeholder.com/150x60?text=KFC" alt="KFC" class="img-fluid"></div>
                    <div class="col-auto"><img src="https://via.placeholder.com/150x60?text=McD" alt="McDonald's" class="img-fluid"></div>
                    <div class="col-auto"><img src="https://via.placeholder.com/150x60?text=Subway" alt="Subway" class="img-fluid"></div>
                    <div class="col-auto"><img src="https://via.placeholder.com/150x60?text=Dominos" alt="Dominos" class="img-fluid"></div>
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
                            <li><a href="#how-it-works">How It Works</a></li>
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
                            <a href="#"><i class="bi bi-facebook fs-4"></i></a>
                            <a href="#"><i class="bi bi-twitter fs-4"></i></a>
                            <a href="#"><i class="bi bi-instagram fs-4"></i></a>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="border-t border-gray-800 mt-5 pt-8 text-center text-gray-400">
                    <p>&copy; 2025 DineIX. All rights reserved.</p>
                </div>
            </div>

        </footer>

        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Sample animated counter
            let count = 0;
            const counter = document.getElementById("mealCounter");
            const interval = setInterval(() => {
                counter.textContent = ++count;
                if (count >= 1500)
                    clearInterval(interval);
            }, 20);
        </script>
    </body>
</html>
