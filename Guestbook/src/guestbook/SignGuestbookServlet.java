package guestbook; 
import java.io.IOException; 
import java.util.Date; 
import javax.persistence.*;
import javax.servlet.http.*; 
import com.google.appengine.api.users.*; 
import guestbook.Greeting; 
import guestbook.EMF; 
public class SignGuestbookServlet extends HttpServlet { 
 
    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException { 
        UserService userService = UserServiceFactory.getUserService(); 
        User user = userService.getCurrentUser(); 
 
        String content = req.getParameter("content"); 
        Date date = new Date(); 
        Greeting greeting = new Greeting(user, content, date); 
 
        EntityManager em = EMF.get().createEntityManager(); 
        try { 
            em.persist(greeting); 
        } finally { 
            em.close(); 
        } 
        resp.sendRedirect("/guestbook.jsp"); 
    } 
}
