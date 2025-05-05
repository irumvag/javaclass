<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="Sections/header.jsp" %>
<%@include file="Sections/auth-check.jsp" %>
<%@include file="Sections/user-header.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="Sections/user-sidebar.jsp" %>  
        <div class="col-md-9 p-5">
            <%-- Alert Messages --%>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show">
                    ${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not empty successMessage}">
                <div class="alert alert-success alert-dismissible fade show">
                    ${successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0"><i class="bi bi-gear"></i> Account Settings</h4>
                </div>

                <div class="card-body">
                    <%-- Profile Update Form --%>
                    <form action="account-settings" method="POST">
                        <input type="hidden" name="action" value="updateProfile">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">

                        <div class="mb-3">
                            <label class="form-label">Full Name</label>
                            <input type="text" class="form-control" name="fullName" 
                                   value="${user.fullName}" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Email Address</label>
                            <input type="email" class="form-control" name="email" 
                                   value="${user.email}" required>
                        </div>

                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-save"></i> Update Profile
                        </button>
                    </form>

                    <hr>

                    <%-- Password Change Form --%>
                    <form action="account-settings" method="POST">
                        <input type="hidden" name="action" value="changePassword">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">

                        <div class="mb-3">
                            <label class="form-label">Current Password</label>
                            <input type="password" class="form-control" name="currentPassword" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">New Password</label>
                            <input type="password" class="form-control" name="newPassword" required>
                            <small class="form-text text-muted">Minimum 8 characters</small>
                        </div>

                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-key"></i> Change Password
                        </button>
                    </form>

                    <hr>

                    <%-- Theme Selection --%>
                    <form action="account-settings" method="POST">
                        <input type="hidden" name="action" value="updateTheme">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">

                        <div class="mb-3">
                            <label class="form-label">Theme Preference</label>
                            <select name="theme" class="form-select" onchange="this.form.submit()">
                                <option value="light" ${user.themePreference == 'light' ? 'selected' : ''}>
                                    Light Mode
                                </option>
                                <option value="dark" ${user.themePreference == 'dark' ? 'selected' : ''}>
                                    Dark Mode
                                </option>
                            </select>
                        </div>
                    </form>
                    <hr>

                    <%-- Restaurant Owner Request --%>
                    <div class="card border-warning">
                        <div class="card-header bg-warning text-black">
                            <h5 class="mb-0"><i class="bi bi-shop"></i> Become a Restaurant Owner</h5>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${user.role == 'RESTAURANT_OWNER'}">
                                    <div class="alert alert-info">
                                        You already have a restaurant owner account
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <form action="account-settings" method="POST" enctype="multipart/form-data">
                                        <input type="hidden" name="action" value="requestOwnerRole">
                                        <input type="hidden" name="csrfToken" value="${csrfToken}">

                                        <div class="mb-3">
                                            <label class="form-label">Business Documentation</label>
                                            <input type="file" class="form-control" name="document" required
                                                   accept=".pdf,.doc,.docx,image/*">
                                            <small class="form-text text-muted">
                                                Upload business registration documents (PDF/DOC/Images)
                                            </small>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Additional Information</label>
                                            <textarea class="form-control" name="message" rows="3" 
                                                      placeholder="Describe your restaurant concept..."></textarea>
                                        </div>

                                        <button type="submit" class="btn btn-warning">
                                            <i class="bi bi-send"></i> Submit Request
                                        </button>
                                    </form>

                                    <c:if test="${not empty roleRequestStatus}">
                                        <div class="mt-3 alert ${roleRequestStatus.contains('success') ? 'alert-success' : 'alert-danger'}">
                                            ${roleRequestStatus}
                                        </div>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <hr>

                    <%-- Account Deletion --%>
                    <div class="card border-danger">
                        <div class="card-header bg-danger text-white">
                            <h5 class="mb-0"><i class="bi bi-exclamation-triangle"></i> Danger Zone</h5>
                        </div>
                        <div class="card-body">
                            <form action="account-settings" method="POST" 
                                  onsubmit="return confirm('This will permanently delete your account! Continue?')">
                                <input type="hidden" name="action" value="deleteAccount">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">

                                <div class="mb-3">
                                    <label class="form-label text-danger">Confirm Password to Delete Account</label>
                                    <input type="password" class="form-control" name="password" required>
                                </div>

                                <button type="submit" class="btn btn-danger">
                                    <i class="bi bi-trash"></i> Delete Account Permanently
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
document.addEventListener('DOMContentLoaded', function() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    
    function toggleLoading(show) {
        if (loadingOverlay) {
            loadingOverlay.style.display = show ? 'flex' : 'none';
        }
    }

    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const isAjax = form.hasAttribute('data-ajax');
            
            try {
                toggleLoading(true);
                
                if (isAjax) {
                    const formData = new FormData(form);
                    const response = await fetch(form.action, {
                        method: 'POST',
                        body: formData,
                        headers: {
                            'X-CSRF-TOKEN': form.querySelector('[name="csrfToken"]').value
                        }
                    });

                    if (!response.ok) throw new Error('Server error');
                    window.location.reload();
                } else {
                    form.submit();
                }
            } catch (error) {
                console.error('Submission error:', error);
                alert('Operation failed: ' + error.message);
            } finally {
                toggleLoading(false);
            }
        });
    });
});
</script>
<%@include file="Sections/dashfooter.jsp" %>