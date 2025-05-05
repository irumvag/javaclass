<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="col-md-3 bg-dark vh-100 p-4">
    <div class="text-center mb-4">
        <h4 class="text-yellow" style="color:yellow">Admin Panel</h4>
    </div>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link text-yellow" href="admin-dashboard.jsp#users">
                <i class="bi bi-people"></i> Manage Users
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-yellow" href="admin-dashboard.jsp#requests">
                <i class="bi bi-clipboard-check"></i> Role Requests
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-yellow" href="admin-dashboard.jsp#restaurants">
                <i class="bi bi-shop"></i> Manage Restaurants
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-yellow" href="admin-dashboard.jsp#analytics">
                <i class="bi bi-graph-up"></i> System Analytics
            </a>
        </li>
        <li class="nav-item mt-4">
            <a class="nav-link text-yellow" href="logout">
                <i class="bi bi-box-arrow-left"></i> Logout
            </a>
        </li>
    </ul>
</div>