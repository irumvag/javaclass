<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Umuranzi |System</title>

        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css">

        <!-- Custom CSS -->
        <style>
            :root {
                --primary-yellow: #FFD700;
                --secondary-black: #000000;
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
        </style>
    </head>