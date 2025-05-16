<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>DineIX | Meal Freedom</title>
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css">
        <!-- SwiperJS CSS for sliders -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.css"/>
        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
        <script src="https://cdn.tailwindcss.com"></script>
        <!-- Bootstrap Icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
        <!-- AOS Animation -->
        <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
        <style>
            :root {
                --primary-yellow: #FFD700;
                --secondary-black: #000000;
                --bg-color: #ffffff;
                --text-color: #000000;
                --card-bg: #f8f9fa;
            }
            [data-theme="dark"] {
                --bg-color: #1a1a1a;
                --text-color: #ffffff;
                --card-bg: #2d2d2d;
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
                    url('https://images.pexels.com/photos/3184183/pexels-photo-3184183.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260');
                background-size: cover;
                background-position: center;
                height: 70vh;
                display: flex;
                align-items: center;
                justify-content: center;
                text-align: center;
                color: #fff;
            }
            .hero-section h1 {
                font-size: 3rem;
                font-weight: 700;
            }
            .hero-section p {
                font-size: 1.25rem;
                margin-bottom: 1.5rem;
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
            .meal-card {
                transition: transform 0.3s;
            }
            .meal-card:hover {
                transform: translateY(-10px);
            }
            .swiper {
                width: 100%;
                padding: 2rem 0;
            }
            .swiper-slide {
                text-align: center;
                font-size: 1rem;
                background: var(--card-bg);
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                padding: 1rem;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            }
            .partner-logos img {
                max-height: 60px;
                margin: 0 1rem;
                filter: grayscale(100%);
                transition: filter 0.3s;
            }
            .partner-logos img:hover {
                filter: none;
            }
            .counter {
                font-size: 2rem;
                font-weight: 700;
                color: var(--primary-yellow);
            }
            .hero-video {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                object-fit: cover;
                z-index: -1;
            }

            .gradient-overlay {
                background: linear-gradient(
                    45deg,
                    rgba(0, 0, 0, 0.7),
                    rgba(0, 0, 0, 0.4)
                    );
            }

            .feature-card {
                transition: transform 0.3s ease;
            }

            .feature-card:hover {
                transform: translateY(-10px);
            }

            @media (max-width: 768px) {
                .hero-content {
                    padding: 2rem;
                }
            }
            #mobileMenu {
                display: none;
            }

            #mobileMenu.show {
                display: flex;
                flex-direction: column;
            }
        </style>
    </head>
    <body data-theme="${not empty user ? user.themePreference : cookie.theme.value}">