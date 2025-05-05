<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="col-md-3 bg-yellow  p-4">
    <div class="text-center mb-4">
        <img src="img/user-avatar.png" class="rounded-circle shadow" width="100" alt="User Avatar">
        <h4 class="text-black mt-2">${user.fullName}</h4>
        <small class="text-muted">${user.email}</small>
    </div>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link text-black" href="account-settings">
                <i class="bi bi-person-circle"></i> Account Settings
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-black" href="user-dashboard.jsp#packages">
                <i class="bi bi-basket"></i> My Meal Packages
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-black" href="#Restaurant">
                <i class="bi bi-shop"></i> Browse Restaurants
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-black" href="user-dashboard.jsp#history">
                <i class="bi bi-clock-history"></i> Order History
            </a>
        </li>
        <li class="nav-item mt-4">
            <a class="nav-link text-black" href="logout">
                <i class="bi bi-box-arrow-left"></i> Logout
            </a>
        </li>
    </ul>
</div>