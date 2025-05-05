<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>DineIX |System</title>

        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css">

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

            .bg-yellow {
                background-color: var(--primary-yellow) !important;
            }

            .text-black {
                color: var(--secondary-black) !important;
            }

            .hero-section {
                background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)),
                    url('food-bg.jpg');
                background-size: cover;
                height: 70vh;
            }

            .meal-card {
                transition: transform 0.3s;
            }

            .meal-card:hover {
                transform: translateY(-10px);
            }
            .alert-yellow {
                background-color: var(--primary-yellow);
                border: 2px solid var(--secondary-black);
                color: var(--secondary-black);
            }

            .alert-yellow .alert-link {
                color: #0000EE;
                text-decoration: underline;
            }
            body {
                background-color: var(--bg-color);
                color: var(--text-color);
            }

            .card {
                background-color: var(--card-bg);
            }
            .loading {
                position: relative;
                pointer-events: none;
            }

            .loading::after {
                content: "";
                position: absolute;
                top: 50%;
                left: 50%;
                width: 1.5rem;
                height: 1.5rem;
                border: 3px solid var(--primary-yellow);
                border-radius: 50%;
                border-top-color: transparent;
                animation: spin 0.8s linear infinite;
            }

            @keyframes spin {
                to {
                    transform: translate(-50%, -50%) rotate(360deg);
                }
            }
            .loading-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
                z-index: 9999;
                display: none;
            }

            .loading-spinner {
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                color: var(--primary-yellow);
            }
            .loading-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.7);
                z-index: 9999;
                display: flex;
                align-items: center;
                justify-content: center;
            }
        </style>
    </head>
    <body data-theme="${not empty user ? user.themePreference : cookie.theme.value}">