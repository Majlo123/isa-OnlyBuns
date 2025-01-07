package rs.ac.uns.ftn.informatika.rest.utils;

import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RateLimiterInterceptor implements HandlerInterceptor {
    private final RateLimiter rateLimiter = new RateLimiter();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false); // Get the current session if it exists
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Session expired. Please log in.");
            return false; // Stop the request processing if session is not found
        }

        String email = (String) session.getAttribute("email"); // Retrieve the email from the session
        if (email == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not authenticated.");
            return false;
        }

        if (!rateLimiter.isRequestAllowed(email)) {
            response.setStatus(429);
            response.getWriter().write("Too many requests. Please try again later.");
            return false; // Prevent further request processing
        }

        return true; // Allow the request to proceed
    }
}
