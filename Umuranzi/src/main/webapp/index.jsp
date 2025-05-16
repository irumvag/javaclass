<%@include file="Sections/header.jsp" %>
<%@include file="Sections/navbar.jsp" %>
<!-- Hero -->
<section class="hero-section">
    <div class="container">
        <h1>Freedom to Dine Anywhere</h1>
        <p>Buy flexible meal packages. Redeem them whenever, wherever.</p>
        <a href="register" class="btn btn-warning btn-lg">Get Started</a>
    </div>
</section>

<!-- How It Works -->
<section class="py-5">
    <div class="container" id="how-it-works">
        <h2 class="text-center mb-4">How It Works</h2>
        <div class="row text-center g-4">
            <div class="col-md-3">
                <div class="card p-4 meal-card shadow-sm">
                    <i class="bi bi-person-plus display-4 text-warning mb-3"></i>
                    <h5>1. Sign Up</h5>
                    <p>Create your free account in seconds.</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card p-4 meal-card shadow-sm">
                    <i class="bi bi-shop display-4 text-warning mb-3"></i>
                    <h5>2. Choose Restaurant</h5>
                    <p>Explore our partner restaurants.</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card p-4 meal-card shadow-sm">
                    <i class="bi bi-bag-check display-4 text-warning mb-3"></i>
                    <h5>3. Buy Package</h5>
                    <p>Select and pay for your meal pack.</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card p-4 meal-card shadow-sm">
                    <i class="bi bi-emoji-smile display-4 text-warning mb-3"></i>
                    <h5>4. Redeem Anytime</h5>
                    <p>Use your code at any partner location.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Meal Counter -->
<section class="py-5 bg-light">
    <div class="container text-center">
        <h2>Meals Redeemed</h2>
        <p class="counter" id="mealCounter">0</p>
        <p>and counting!</p>
    </div>
</section>
<!-- Hero Section -->
<section>
    <div class="relative h-screen flex items-center">
        <video autoplay loop muted playsinline class="hero-video">
            <source src="videos/hero.mp4" type="video/mp4">
        </video>
        <div class="absolute inset-0 gradient-overlay"></div>
        <div class="container mx-auto px-6 relative z-10">
            <div class="hero-content text-white max-w-2xl" data-aos="fade-up">
                <h1 class="text-5xl font-bold mb-6">Digital Meal Tickets for Modern Dining</h1>
                <p class="text-xl mb-8">Experience the convenience of digital meal tickets across multiple restaurants. Save money, enjoy variety, and dine with ease.</p>
                <div class="flex space-x-4">
                    <a href="register.jsp" class="px-8 py-3 bg-yellow-500 text-white rounded-full hover:bg-blue-600 transition">Get Started</a>
                    <a href="#how-it-works" class="px-8 py-3 border-2 border-white text-white rounded-full hover:bg-white hover:text-gray-900 transition">Learn More</a>
                </div>
            </div>
        </div>
    </div>
</section>

<section>
         <div>
             <h2 class="text-center mb-4 mt-5">Benefit of joining us!</h2>
            <div class="grid md:grid-cols-3 gap-8">
                <div class="feature-card p-6 bg-gray-50 rounded-xl text-center" data-aos="fade-up">
                    <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-6">
                        <svg class="w-8 h-8 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                    </div>
                    <h3 class="text-xl font-semibold mb-4">Save Money</h3>
                    <p class="text-gray-600">Get exclusive discounts and package deals from multiple restaurants.</p>
                </div>

                <div class="feature-card p-6 bg-gray-50 rounded-xl text-center" data-aos="fade-up" data-aos-delay="100">
                    <div class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6">
                        <svg class="w-8 h-8 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"></path>
                        </svg>
                    </div>
                    <h3 class="text-xl font-semibold mb-4">Digital Convenience</h3>
                    <p class="text-gray-600">No more paper tickets. Access your meal tickets instantly on your phone.</p>
                </div>

                <div class="feature-card p-6 bg-gray-50 rounded-xl text-center" data-aos="fade-up" data-aos-delay="200">
                    <div class="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-6">
                        <svg class="w-8 h-8 text-purple-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path>
                        </svg>
                    </div>
                    <h3 class="text-xl font-semibold mb-4">Multiple Choices</h3>
                    <p class="text-gray-600">Choose from a variety of restaurants and cuisines with a single subscription.</p>
                </div>
            </div>
        </div>
    </section>
<!-- Testimonials -->
<section class="py-5">
    <div class="container">
        <h2 class="text-center mb-4">What Our Users Say</h2>
        <div class="swiper mySwiper">
            <div class="swiper-wrapper">
                <div class="swiper-slide">
                    <p>"DineIX changed my eating habits. So convenient!"</p>
                    <h6>- Auris T.</h6>
                </div>
                <div class="swiper-slide">
                    <p>"Affordable and easy to use. I love it."</p>
                    <h6>- Patrick M.</h6>
                </div>
                <div class="swiper-slide">
                    <p>"I dine out more freely thanks to DineIX!"</p>
                    <h6>- Kevia M.</h6>
                </div>
            </div>
            <div class="swiper-pagination mt-3"></div>
        </div>
    </div>
</section>

<!-- Partners -->
<section class="py-5 bg-light">
    <div class="container text-center">
        <h2 class="mb-4">Trusted By</h2>
        <div class="d-flex flex-wrap justify-content-center partner-logos">
            <img src="https://via.placeholder.com/150x60?text=KFC" alt="KFC"/>
            <img src="https://via.placeholder.com/150x60?text=McD" alt="McDonald's"/>
            <img src="https://via.placeholder.com/150x60?text=Subway" alt="Subway"/>
            <img src="https://via.placeholder.com/150x60?text=Dominos" alt="Dominos"/>
        </div>
    </div>
</section>

<%@include file="Sections/footer.jsp" %>