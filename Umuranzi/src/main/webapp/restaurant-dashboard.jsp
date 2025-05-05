<%@include file="Sections/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="Sections/restaurant-header.jsp" %>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-3 bg-yellow vh-100 p-4">
            <h3 class="text-black mb-4">Restaurant Dashboard</h3>
            <ul class="nav flex-column">
                <li class="nav-item"><a class="nav-link text-black" href="#manage">Manage Packages</a></li>
                <li class="nav-item"><a class="nav-link text-black" href="#redeem">Redeem Tickets</a></li>
                <li class="nav-item"><a class="nav-link text-black" href="#analytics">Analytics</a></li>
            </ul>
        </div>

        <div class="col-md-9 p-4">
            <h2>Restaurant Management</h2>
            <!-- Add package management and redemption sections -->
            <section id="analytics" class="mb-5">
                <h3 class="mb-4">Restaurant Analytics</h3>
                <div class="row">
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-body">
                                <h5>Total Followers</h5>
                                <p class="display-4">${restaurant.followerCount}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-body">
                                <h5>Total Likes</h5>
                                <p class="display-4">${restaurant.likeCount}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section id="posts" class="mb-5">
                <div class="d-flex justify-content-between mb-4">
                    <h3>Latest Posts</h3>
                    <button class="btn btn-warning" data-bs-toggle="modal" 
                            data-bs-target="#createPostModal">
                        <i class="bi bi-plus-lg"></i> Create Post
                    </button>
                </div>

                <div class="row g-4">
                    <c:forEach items="${posts}" var="post">
                        <div class="col-md-6">
                            <div class="card">
                                <div class="card-body">
                                    <div class="d-flex align-items-center mb-3">
                                        <img src="${post.authorAvatar}" 
                                             class="rounded-circle me-2" width="40">
                                        <div>
                                            <h5>${post.authorName}</h5>
                                            <small class="text-muted">${post.postDate}</small>
                                        </div>
                                    </div>
                                    <p>${post.content}</p>
                                    <div class="d-flex gap-2">
                                        <button class="btn btn-outline-warning btn-sm">
                                            <i class="bi bi-heart"></i> ${post.likes}
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