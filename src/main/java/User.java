package pshopmvc;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User implements Serializable{

    @Id
    private int id;
    private String cookie;
    private String basket;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getBasket() {
        return basket;
    }

    public void setBasket(String basket) {
        this.basket = basket;
    }

    public User() {
    }

    public User(int id, String cookie, String basket) {
        this.id = id;
        this.cookie = cookie;
        this.basket = basket;
    }
}
