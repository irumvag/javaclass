package com.mycompany.umuranzi.UserServlet;

import com.mycompany.umuranzi.Utils.RoleRequestDAO;
import com.mycompany.umuranzi.Utils.UserDAO;
import com.mycompany.umuranzi.models.RoleRequest;
import com.mycompany.umuranzi.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class UserServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UserServlet.class.getName());
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        "pdf", "doc", "docx", "jpg", "jpeg", "png"
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        request.setAttribute("user", user);
        request.getRequestDispatcher("account-settings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            sendError(request, response, "Invalid action requested");
            return;
        }

        try {
            switch (action) {
                case "updateProfile":
                    handleProfileUpdate(request, response, user);
                    break;
                case "changePassword":
                    handlePasswordChange(request, response, user);
                    break;
                case "deleteAccount":
                    handleAccountDeletion(request, response, user);
                    break;
                case "updateTheme":
                    handleThemeUpdate(request, response, user);
                    break;
                case "requestOwnerRole":
                    handleOwnerRequest(request, response, user);
                    break;
                default:
                    sendError(request, response, "Invalid action requested");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing request", e);
            sendError(request, response, "Operation failed: " + e.getMessage());
        }
    }

    private void handleOwnerRequest(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            if ("RESTAURANT_OWNER".equals(user.getRole())) {
                sendError(request, response, "You are already a restaurant owner");
                return;
            }

            RoleRequestDAO roleRequestDAO = new RoleRequestDAO();
            if (roleRequestDAO.hasPendingRequest(user.getUserId())) {
                sendError(request, response, "You already have a pending request");
                return;
            }

            Part filePart = request.getPart("document");
            String fileName = filePart.getSubmittedFileName();
            String fileExtension = getFileExtension(fileName);

            if (!isValidFileExtension(fileExtension)) {
                sendError(request, response, 
                    "Invalid file type. Allowed: PDF, DOC, DOCX, JPG, JPEG, PNG");
                return;
            }

            String documentPath = saveUploadedFile(filePart);
            String message = request.getParameter("message");

            RoleRequest roleRequest = new RoleRequest();
            roleRequest.setUserId(user.getUserId());
            roleRequest.setDocumentPath(documentPath);
            roleRequest.setMessage(message);
            roleRequestDAO.createRequest(roleRequest);

            sendSuccess(request, response, 
                "Request submitted! Admin will review your application.");

        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "Database error", e);
            sendError(request, response, "Request submission failed");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "File upload error", e);
            sendError(request, response, "File upload failed");
        }
    }

    private String saveUploadedFile(Part filePart) throws IOException {
        String fileName = UUID.randomUUID() + "_" + filePart.getSubmittedFileName();
        Path uploadDir = Paths.get(getServletContext().getRealPath("/uploads/docs"));
        
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(filePart.getInputStream(), filePath);
        return fileName;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0) ? fileName.substring(dotIndex + 1).toLowerCase() : "";
    }

    private boolean isValidFileExtension(String extension) {
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response, User user)
            throws SQLException, NamingException, ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email").trim();

        if (!isValidEmail(email)) {
            sendError(request, response, "Invalid email format");
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (!user.getEmail().equalsIgnoreCase(email) && userDAO.emailExists(email)) {
            sendError(request, response, "Email already registered");
            return;
        }

        user.setFullName(fullName);
        user.setEmail(email);
        userDAO.updateUser(user);
        updateSession(request, user);
        sendSuccess(request, response, "Profile updated successfully");
    }

    private void handlePasswordChange(HttpServletRequest request, HttpServletResponse response, User user)
            throws SQLException, NamingException, ServletException, IOException {
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");

        if (newPassword.length() < 8) {
            sendError(request, response, "Password must be at least 8 characters");
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (!userDAO.verifyPassword(user.getUserId(), currentPassword)) {
            sendError(request, response, "Current password is incorrect");
            return;
        }

        userDAO.updatePassword(user.getUserId(), newPassword);
        user.setPassword(newPassword);
        updateSession(request, user);
        sendSuccess(request, response, "Password changed successfully");
    }

    private void handleAccountDeletion(HttpServletRequest request, HttpServletResponse response, User user)
            throws SQLException, NamingException, ServletException, IOException {
        String password = request.getParameter("password");
        UserDAO userDAO = new UserDAO();

        if (!userDAO.verifyPassword(user.getUserId(), password)) {
            sendError(request, response, "Incorrect password - account not deleted");
            return;
        }

        userDAO.deleteUser(user.getUserId());
        request.getSession().invalidate();
        response.sendRedirect("login.jsp?accountDeleted=true");
    }

    private void handleThemeUpdate(HttpServletRequest request, HttpServletResponse response, User user)
            throws SQLException, NamingException, ServletException, IOException {
        String theme = request.getParameter("theme");
        UserDAO userDAO = new UserDAO();
        userDAO.updateThemePreference(user.getUserId(), theme);
        user.setThemePreference(theme);
        updateSession(request, user);
        sendSuccess(request, response, "Theme preference updated");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private void updateSession(HttpServletRequest request, User user) {
        request.getSession().setAttribute("user", user);
    }

    private void sendSuccess(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("successMessage", message);
        request.getRequestDispatcher("account-settings.jsp").forward(request, response);
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("errorMessage", error);
        request.getRequestDispatcher("account-settings.jsp").forward(request, response);
    }
}