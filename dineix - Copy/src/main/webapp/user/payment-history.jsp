<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="card" data-aos="zoom-in">
    <div class="card-header bg-yellow text-black d-flex justify-content-between align-items-center">
        <h3 class="mb-0">Payment History</h3>
        <button class="btn btn-dark" id="refreshPayments" aria-label="Refresh Payment History">
            <i class="bi bi-arrow-clockwise"></i> Refresh
        </button>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Package</th>
                        <th>Amount</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="paymentHistoryBody">
                    <c:forEach items="${paymentHistory}" var="payment">
                        <tr>
                            <td><fmt:formatDate value="${payment.createdAt}" pattern="yyyy-MM-dd HH:mm" /></td>
                            <td>${payment.packageName}</td>
                            <td><fmt:formatNumber value="${payment.amount}" type="currency" currencySymbol="RWF " /></td>
                            <td>
                                <span class="badge ${payment.status == 'SUCCESSFUL' ? 'bg-success' : 
                                                   payment.status == 'PENDING' ? 'bg-warning' : 
                                                   payment.status == 'FAILED' ? 'bg-danger' : 'bg-secondary'}">
                                    ${payment.status}
                                </span>
                            </td>
                            <td>
                                <c:if test="${payment.status == 'PENDING'}">
                                    <button class="btn btn-sm btn-warning check-status-btn" 
                                            data-transaction-id="${payment.transactionId}"
                                            data-reference-id="${payment.referenceId}"
                                            aria-label="Check Payment Status">
                                        Check Status
                                    </button>
                                </c:if>
                                <button class="btn btn-sm btn-info view-details-btn"
                                        data-transaction-id="${payment.transactionId}"
                                        aria-label="View Payment Details">
                                    Details
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Payment Details Modal -->
<div class="modal fade" id="paymentDetailsModal" tabindex="-1" aria-labelledby="paymentDetailsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-yellow">
                <h5 class="modal-title" id="paymentDetailsModalLabel">Payment Details</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div id="paymentDetails"></div>
            </div>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Check Payment Status
        document.querySelectorAll('.check-status-btn').forEach(button => {
            button.addEventListener('click', async function() {
                const transactionId = this.dataset.transactionId;
                const referenceId = this.dataset.referenceId;
                
                try {
                    const response = await fetch(`${pageContext.request.contextPath}/user/process-mtn-payment?transactionId=${transactionId}&referenceId=${referenceId}`);
                    const data = await response.json();
                    
                    if (data.success) {
                        location.reload(); // Refresh to show updated status
                    } else {
                        alert('Error checking payment status: ' + data.message);
                    }
                } catch (error) {
                    console.error('Error:', error);
                    alert('Error checking payment status');
                }
            });
        });

        // View Payment Details
        document.querySelectorAll('.view-details-btn').forEach(button => {
            button.addEventListener('click', async function() {
                const transactionId = this.dataset.transactionId;
                
                try {
                    const response = await fetch(`${pageContext.request.contextPath}/user/payment-details?transactionId=${transactionId}`);
                    const data = await response.json();
                    
                    if (data.success) {
                        const detailsHtml = `
                            <div class="mb-3">
                                <strong>Transaction ID:</strong> ${data.details.transactionId}
                            </div>
                            <div class="mb-3">
                                <strong>Package:</strong> ${data.details.packageName}
                            </div>
                            <div class="mb-3">
                                <strong>Amount:</strong> RWF ${data.details.amount}
                            </div>
                            <div class="mb-3">
                                <strong>Status:</strong> ${data.details.status}
                            </div>
                            <div class="mb-3">
                                <strong>Phone Number:</strong> ${data.details.phoneNumber}
                            </div>
                            <div class="mb-3">
                            //                                <strong>Date:</strong> new Date(data.details.createdAt).toLocaleDateString('en-US', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })}
                            </div>
                        `;
                        document.getElementById('paymentDetails').innerHTML = detailsHtml;
                        new bootstrap.Modal(document.getElementById('paymentDetailsModal')).show();
                    } else {
                        alert('Error fetching payment details: ' + data.message);
                    }
                } catch (error) {
                    console.error('Error:', error);
                    alert('Error fetching payment details');
                }
            });
        });

        // Refresh Payments
        document.getElementById('refreshPayments').addEventListener('click', function() {
            location.reload();
        });
    });
</script>