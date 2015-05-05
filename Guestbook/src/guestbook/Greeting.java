package guestbook;

import com.google.appengine.api.datastore.Key; 
import com.google.appengine.api.users.User; 
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Greeting { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key; 
    private User author; 
    private String content; 
    private Date date; 
 
    public Greeting(User author, String content, Date date) { 
        this.author = author; 
        this.content = content; 
        this.date = date; 
    } 
    public Key getKey() { 
        return key; 
    } 
    public User getAuthor() { 
        return author; 
    } 
    public String getContent() { 
        return content; 
    } 
    public Date getDate() { 
        return date; 
    } 
    public void setAuthor(User author) { 
        this.author = author; 
    } 
    public void setContent(String content) { 
        this.content = content; 
    } 
    public void setDate(Date date) { 
        this.date = date; 
    } 
}

