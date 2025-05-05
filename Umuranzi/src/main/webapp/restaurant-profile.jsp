<%@include file="Sections/header.jsp" %>
<%@include file="Sections/auth-check.jsp" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="Sections/restaurant-header.jsp" %>
<div class="container mt-5">
    <div class="card">
        <div class="card-header bg-yellow">
            <div class="row align-items-center">
                <div class="col-md-2">
                    <img src="${restaurant.logoUrl}" 
                         class="img-fluid rounded-circle" alt="Logo">
                </div>
                <div class="col-md-10">
                    <h1>${restaurant.name}</h1>
                    <p class="lead">${restaurant.description}</p>
                </div>
            </div>
        </div>
        
        <div class="card-body">
            <div class="row">
                <div class="col-md-4">
                    <div class="card mb-3">
                        <div class="card-body">
                            <h5><i class="bi bi-geo-alt"></i> Location</h5>
                            <p>${restaurant.address}</p>
                            <div id="map" style="height: 200px"></div>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-8">
                    <%-- Posts Feed --%>
                    <c:forEach items="${posts}" var="post">
                        <div class="card mb-3">
                            <div class="card-body">
                                ${post.content}
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="Sections/footer.jsp" %>