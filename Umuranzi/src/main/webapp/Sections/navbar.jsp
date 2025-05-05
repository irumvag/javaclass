<nav class="navbar navbar-expand-lg navbar-dark bg-yellow">
    <div class="container">
        <a class="navbar-brand text-black" href="index.jsp" style="font-size: 35px">DineIX</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <button class="btn btn-link text-black" id="themeToggle">
                        <i class="bi bi-moon-stars"></i>
                    </button>
                </li>
                <li class="nav-item"><a class="nav-link text-black" href="index.jsp">Home</a></li>
                <li class="nav-item"><a class="nav-link text-black" href="about.jsp">About</a></li>
                <li class="nav-item"><a class="nav-link text-black" href="restaurants">Restaurants</a></li>
                <li class="nav-item"><a class="nav-link text-black" href="login.jsp">Login</a></li>
                <li class="nav-item"><a class="nav-link text-black" href="register.jsp">Register</a></li>
            </ul>
        </div>
    </div>
</nav>
<script>
    const themeToggle = document.getElementById('themeToggle');
    const savedTheme = localStorage.getItem('theme') || 'light';
    
    document.documentElement.setAttribute('data-theme', savedTheme);
    
    themeToggle.addEventListener('click', () => {
        const currentTheme = document.documentElement.getAttribute('data-theme');
        const newTheme = currentTheme === 'light' ? 'dark' : 'light';
        document.documentElement.setAttribute('data-theme', newTheme);
        localStorage.setItem('theme', newTheme);
        themeToggle.innerHTML = newTheme === 'light' ? 
            '<i class="bi bi-sun"></i>' : '<i class="bi bi-moon-stars"></i>';
    });
</script>
