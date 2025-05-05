<%@include file="Sections/header.jsp" %>
<%@include file="Sections/auth-check.jsp" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="Sections/admin-header.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="Sections/admin-sidebar.jsp" %>
        
        <div class="col-md-9 p-4">
            <h2>Admin Dashboard</h2>
            
            <%-- Role Requests --%>
            <div class="card mb-4">
                <div class="card-header bg-yellow">
                    <h4>Pending Role Requests</h4>
                </div>
                <div class="card-body">
                    <table class="table">
                        <c:forEach items="${roleRequests}" var="request">
                            <tr>
                                <td>${request.user.email}</td>
                                <td>
                                    <a href="/uploads/${request.documentPath}" target="_blank">
                                        View Document
                                    </a>
                                </td>
                                <td>
                                    <button class="btn btn-success btn-sm approve-btn" 
                                            data-request-id="${request.requestId}">
                                        Approve
                                    </button>
                                    <button class="btn btn-danger btn-sm reject-btn" 
                                            data-request-id="${request.requestId}">
                                        Reject
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="Sections/dashfooter.jsp" %>