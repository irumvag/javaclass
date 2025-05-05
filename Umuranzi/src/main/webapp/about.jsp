<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="Sections/header.jsp" %>
<%@include file="Sections/navbar.jsp" %>
<style>
    .feature-card {
        border: 2px solid var(--primary-yellow);
        border-radius: 15px;
        transition: transform 0.3s;
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
</style>
<div class="container my-5">
    <div class="row">
        <div class="col-md-8 mx-auto">
            <h1 class="text-center mb-4">About DineIX</h1>
            <div class="card shadow">
                <div class="card-body">
                    <h3 class="text-yellow">Revolutionizing Meal Experiences</h3>
                    <p class="lead">DineIX is an innovative meal ticket system that
                        connects food lovers with their favorite restaurants. Like This
                        project is a restaurant meal ticket system that allows users to purchase
                        meal packages from multiple restaurants and redeem them. Instead of traditional 
                        subscriptions, users can buy a set number of meals (e.g., 10 or 20 meals) from different
                        restaurants and use them whenever they want by generating a ticket.</p>
                    
                   
                                <!-- Core Features -->
            <div class="card shadow-lg mb-5">
                <div class="card-body">
                    <h2 class="text-center mb-4 text-yellow">Why Choose DineIX?</h2>
                    
                    <div class="row g-4">
                        <!-- Feature Cards -->
                        <div class="col-md-6">
                            <div class="feature-card p-4">
                                <div class="feature-icon bg-yellow text-black rounded-circle mb-3">
                                    <i class="bi bi-shop-window fs-1"></i>
                                </div>
                                <h4>Multi-Restaurant Access</h4>
                                <p>Purchase meal packages from various partner restaurants in one platform</p>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="feature-card p-4">
                                <div class="feature-icon bg-yellow text-black rounded-circle mb-3">
                                    <i class="bi bi-qr-code fs-1"></i>
                                </div>
                                <h4>Instant Ticket Generation</h4>
                                <p>Generate secure QR tickets instantly for meal redemption</p>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="feature-card p-4">
                                <div class="feature-icon bg-yellow text-black rounded-circle mb-3">
                                    <i class="bi bi-shield-lock fs-1"></i>
                                </div>
                                <h4>Secure Transactions</h4>
                                <p>Bank-grade encryption and CSRF protection for all transactions</p>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="feature-card p-4">
                                <div class="feature-icon bg-yellow text-black rounded-circle mb-3">
                                    <i class="bi bi-graph-up fs-1"></i>
                                </div>
                                <h4>Real-time Analytics</h4>
                                <p>Track your meal usage and spending patterns in real-time</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Platform Highlights -->
            <div class="row g-4 mb-5">
                <div class="col-md-6">
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

                <div class="col-md-6">
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
            <div class="card border-yellow mb-5">
                <div class="card-header bg-yellow">
                    <h3 class="mb-0">Our Technology</h3>
                </div>
                <div class="card-body">
                    <div class="row text-center g-4">
                        <div class="col">
                            <div class="tech-logo">
                                <i class="bi bi-filetype-java fs-1 text-yellow"></i>
                                <div>Java EE</div>
                            </div>
                        </div>
                        <div class="col">
                            <div class="tech-logo">
                                <i class="bi bi-database fs-1 text-yellow"></i>
                                <div>MySQL</div>
                            </div>
                        </div>
                        <div class="col">
                            <div class="tech-logo">
                                <i class="bi bi-bootstrap fs-1 text-yellow"></i>
                                <div>Bootstrap 5</div>
                            </div>
                        </div>
                        <div class="col">
                            <div class="tech-logo">
                                <i class="bi bi-shield-check fs-1 text-yellow"></i>
                                <div>JWT Auth</div>
                            </div>
                        </div>
                    </div>
                </div>
                </div>
            </div>
        </div>
    </div>
    </div>
</div>

<%@include file="Sections/footer.jsp" %>