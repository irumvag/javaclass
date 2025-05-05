<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="col-md-3 bg-yellow vh-100 p-4">
    <div class="text-center mb-4">
        <img src="${restaurant.logoUrl}" class="rounded-circle shadow" width="100" alt="Restaurant Logo">
        <h4 class="text-black mt-2">${restaurant.name}</h4>
        <small class="text-muted">${restaurant.address}</small>
    </div>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link text-black" href="restaurant-dashboard.jsp#menu">
                <i class="bi bi-menu-button"></i> Menu Management
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-black" href="restaurant-dashboard.jsp#analytics">
                <i class="bi bi-graph-up"></i> Sales Analytics
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-black" href="restaurant-dashboard.jsp#posts">
                <i class="bi bi-megaphone"></i> Manage Posts
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-black" href="restaurant-dashboard.jsp#settings">
                <i class="bi bi-gear"></i> Restaurant Settings
            </a>
        </li>
        <li class="nav-item mt-4">
            <a class="nav-link text-black" href="logout">
                <i class="bi bi-box-arrow-left"></i> Logout
            </a>
        </li>
    </ul>
</div>