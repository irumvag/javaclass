<%@include file="Sections/header.jsp" %>
<%@include file="Sections/auth-check.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="Sections/user-header.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="Sections/user-sidebar.jsp" %>
        <!-- Main Content -->
        <div class="col-md-9 p-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="text-yellow">Dashboard Overview</h2>
                <div class="balance-card p-3 bg-yellow rounded">
                    <h5 class="mb-0">Available Meals: <strong>15</strong></h5>
                </div>
            </div>

            <!-- Meal Packages Grid -->
            <div class="row row-cols-1 row-cols-md-3 g-4">
                <div class="col">
                    <div class="card meal-card h-100">
                        <img src="meal1.jpg" class="card-img-top" alt="Meal Package">
                        <div class="card-body">
                            <h5 class="card-title">Premium Package</h5>
                            <p class="card-text">10 meals - $150</p>
                            <button class="btn btn-warning w-100">Purchase</button>
                        </div>
                    </div>
                </div>
                <!-- Repeat other packages -->
            </div>
            <!-- Restaurant Listing Section -->
            <section id="restaurants" class="mb-5">
                <h3 class="mb-4">Popular Restaurants</h3>
                <div class="row row-cols-1 row-cols-md-2 g-4">
                    <c:forEach items="${restaurants}" var="restaurant">
                        <div class="col">
                            <div class="card h-100">
                                <img src="${restaurant.logoUrl}" class="card-img-top" 
                                     alt="${restaurant.name}">
                                <div class="card-body">
                                    <h5 class="card-title">${restaurant.name}</h5>
                                    <p class="card-text">${restaurant.description}</p>
                                    <div class="d-flex justify-content-between">
                                        <button class="btn btn-outline-warning btn-sm">
                                            <i class="bi bi-heart"></i> 
                                            <span>${restaurant.likeCount}</span>
                                        </button>
                                        <button class="btn btn-warning btn-sm">
                                            <i class="bi bi-plus-circle"></i> Follow
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>
        </div>
    </div>
</div>
<%@include file="Sections/dashfooter.jsp" %>