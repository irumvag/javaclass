 <nav class="sticky-top w-full bg-yellow shadow-lg z-50">
        <div class="container mx-auto px-6 py-4">
            <div class="flex items-center justify-between">
                <div class="flex items-center">
                    <a href="index.jsp" ><img src="img/logo.png" alt="Duneix Logo" class="h-10"></a>
                </div>
                <div class="hidden md:flex items-center space-x-8">
                    <button class="btn btn-link text-black" id="themeToggle">
                            <i class="bi bi-moon-stars"></i>
                    </button>
                    <a class="nav-link text-black" href="#">Home</a>
                    <a class="nav-link text-black" href="about.jsp">About</a>
                    <a class="nav-link text-black" href="restaurants">Restaurants</a>
                    <a class="nav-link text-black" href="login.jsp">Login</a>
                    <a class="nav-link text-black" href="register.jsp">Register</a>
                </div>
                <button class="md:hidden" onclick="toggleMobileMenu()">
                    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16m-7 6h7"></path>
                    </svg>
                </button>
            </div>
        </div>
    </nav>
<!--<nav class="navbar navbar-expand-lg navbar-light bg-warning shadow-sm sticky-top">
  <div class="container">
    <a class="navbar-brand" href="#">
      <img src="img/logo.png" alt="DineIX Logo" height="40">
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ms-auto">
        <li class="nav-item">
          <a class="nav-link text-dark" href="#">Home</a>
        </li>
        <li class="nav-item">
          <a class="nav-link text-dark" href="about.jsp">About</a>
        </li>
        <li class="nav-item">
          <a class="nav-link text-dark" href="restaurants">Restaurants</a>
        </li>
        <li class="nav-item">
          <a class="nav-link text-dark" href="login.jsp">Login</a>
        </li>
        <li class="nav-item">
          <a class="nav-link text-dark" href="register.jsp">Register</a>
        </li>
        <li class="nav-item">
          <button class="btn btn-link nav-link text-dark" id="themeToggle">
            <i class="bi bi-moon-stars"></i>
          </button>
        </li>
      </ul>
    </div>
  </div>
</nav>-->