<footer class="bg-yellow text-black py-4 mt-0">
    <div class="container text-center">
        <p>&copy; 2025 Umuranzi System. All rights reserved.</p>
    </div>
</footer>
<script>
// Universal form handler
document.querySelectorAll('form').forEach(form => {
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const loadingOverlay = document.getElementById('loadingOverlay');
        
        try {
            loadingOverlay.style.display = 'block';
            
            // For AJAX forms
            if(form.dataset.ajax) {
                const formData = new FormData(form);
                const response = await fetch(form.action, {
                    method: 'POST',
                    body: formData,
                    headers: {
                        'X-CSRF-TOKEN': form.csrfToken.value
                    }
                });

                if(!response.ok) throw new Error('Server error');
                window.location.reload(); // Or handle response
            }
            // For regular forms
            else {
                form.submit();
            }
        } catch (error) {
            console.error('Submission error:', error);
            alert('Operation failed: ' + error.message);
        } finally {
            loadingOverlay.style.display = 'none';
        }
    });
});

// Handle back-end initiated loading states
window.addEventListener('beforeunload', () => {
    document.getElementById('loadingOverlay').style.display = 'block';
});

window.addEventListener('load', () => {
    document.getElementById('loadingOverlay').style.display = 'none';
});
</script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>