<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="Sections/header.jsp" %>
<%@include file="Sections/navbar.jsp" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-5 vh-100">
    <h1 class="text-center mb-4">Our Partner Restaurants</h1>
    <%-- Add below the page title --%>
    <div class="row mb-4">
        <div class="col-md-6">
            <form id="searchForm">
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="Search restaurants..." name="searchQuery">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <button class="btn btn-warning" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </form>
        </div>
        <div class="col-md-6 text-end">
            <div class="btn-group">
                <button class="btn btn-warning dropdown-toggle" data-bs-toggle="dropdown">
                    Sort By
                </button>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="?sort=name">Name</a></li>
                    <li><a class="dropdown-item" href="?sort=popular">Most Popular</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="row row-cols-1 row-cols-md-3 g-4">
        <c:forEach items="${restaurants}" var="restaurant">
            <div class="col">
                <div class="card h-100 shadow">
                    <img src="${restaurant.logoUrl}" class="card-img-top restaurant-logo" alt="${restaurant.name}">
                    <div class="card-body">
                        <h3 class="card-title">${restaurant.name}</h3>
                        <p class="card-text">${restaurant.description}</p>
                        <div class="d-flex justify-content-between align-items-center">
                            <button class="btn btn-outline-warning btn-follow" 
                                    data-restaurant-id="${restaurant.restaurantId}">
                                <i class="bi bi-plus-circle"></i> 
                                <%--<span>${follows.containsKey(restaurant.restaurantId) ? 'Following' : 'Follow'}</span>--%>
                            </button>
                            <span class="badge bg-yellow text-black">
                                <i class="bi bi-geo-alt"></i> ${restaurant.address}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <%-- Pagination 
    <nav>
        <ul class="pagination justify-content-center">
            <c:forEach begin="1" end="${totalPages}" var="i">
                <li class="page-item ${currentPage == i ? 'active' : ''}">
                    <a class="page-link bg-yellow text-black" href="?page=${i}">${i}</a>
                </li>
            </c:forEach>
        </ul>
    </nav>
    --%>
</div>

<%@include file="Sections/footer.jsp" %>