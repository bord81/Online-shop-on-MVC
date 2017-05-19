package pshopmvc;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CartService {

    public List<CartItem> formCart(String name, List<User> users, List<Item> allItems) {
        List<CartItem> items = new ArrayList<>();
        Map<Integer, CartItem> citems = new HashMap();
        String c_name = "new";
        String cart = "";
        if (!name.equals(c_name)) {
            for (User u : users) {
                if (u.getCookie().equals(name)) {
                    cart = u.getBasket();
                    break;
                }
            }
            try {
                int totalcount = 0;
                int cartamount = 0;
                CartList cartList = new Gson().fromJson(cart, CartList.class);
                if (cartList == null) {
                    cartList = new CartList();
                }
                Map ids = new HashMap();
                CartItem cartItem;
                for (Integer integer : cartList.items) {
                    int nextId = allItems.get(integer - 1).getId();
                    if (!ids.containsKey(nextId)) {
                        Integer count = 1;
                        ids.put(nextId, count);
                        cartItem = new CartItem(allItems.get(integer - 1).getId(), count, allItems.get(integer - 1).getImage(), allItems.get(integer - 1).getsImage(), allItems.get(integer - 1).getName(), allItems.get(integer - 1).getFullName(), allItems.get(integer - 1).getDescript(), allItems.get(integer - 1).getPrice());
                        citems.put(nextId, cartItem);
                        totalcount++;
                        cartamount = cartamount + allItems.get(integer - 1).getPrice();
                    } else {
                        Integer incr = (Integer) ids.get(nextId);
                        incr++;
                        ids.put(nextId, incr);
                        cartItem = new CartItem(allItems.get(integer - 1).getId(), incr, allItems.get(integer - 1).getImage(), allItems.get(integer - 1).getsImage(), allItems.get(integer - 1).getName(), allItems.get(integer - 1).getFullName(), allItems.get(integer - 1).getDescript(), allItems.get(integer - 1).getPrice() * incr);
                        citems.remove(nextId);
                        citems.put(nextId, cartItem);
                        totalcount++;
                        cartamount = cartamount + allItems.get(integer - 1).getPrice();
                    }
                    items = new ArrayList<CartItem>(citems.values());
                    items.add(new CartItem(-10, totalcount, "total", "total", "total", "total", "total", cartamount));
                }
            } catch (JsonSyntaxException ex) {
                citems = new HashMap();
                citems.put(0, new CartItem(0, 0, "", "", "", "", "", 0));
                items = new ArrayList<CartItem>(citems.values());
                items.add(new CartItem(-10, 0, "total", "total", "total", "total", "total", 0));
                ex.printStackTrace();
            } catch (JsonParseException ex) {
                citems = new HashMap();
                citems.put(0, new CartItem(0, 0, "", "", "", "", "", 0));
                items = new ArrayList<CartItem>(citems.values());
                items.add(new CartItem(-10, 0, "total", "total", "total", "total", "total", 0));
                ex.printStackTrace();
            }
        }
        return items;
    }

    public User removeItem(String name, List<User> users, Integer id) {
        String c_name = "new";
        String cart = "";
        User updUser = new User();
        if (!name.equals(c_name)) {
            for (User u : users) {
                if (u.getCookie().equals(name)) {
                    cart = u.getBasket();
                    updUser.setId(u.getId());
                    updUser.setCookie(u.getCookie());
                    break;
                }
            }
            CartList cartList = new Gson().fromJson(cart, CartList.class);
            if (cartList == null) {
                cartList = new CartList();
            }
            cartList.items.remove(id);
            updUser.setBasket(new Gson().toJson(cartList));
        }
        return updUser;
    }

    public User removeAllItems(String name, List<User> users) {
        String c_name = "new";
        String cart = "";
        User updUser = new User();
        if (!name.equals(c_name)) {
            for (User u : users) {
                if (u.getCookie().equals(name)) {
                    cart = u.getBasket();
                    updUser.setId(u.getId());
                    updUser.setCookie(u.getCookie());
                    break;
                }
            }
            CartList cartList = new Gson().fromJson(cart, CartList.class);
            if (cartList == null) {
                cartList = new CartList();
            }
            cartList.items.clear();
            updUser.setBasket(new Gson().toJson(cartList));
        }
        return updUser;
    }

    public User addItem(String name, List<User> users, Integer id) {
        String c_name = "new";
        String cart = "";
        User updUser = new User();
        if (!name.equals(c_name)) {
            System.out.println("pshopmvc.CartService.addItem()");
            System.out.println("User found");
            for (User u : users) {
                if (u.getCookie().equals(name)) {
                    cart = u.getBasket();
                    updUser.setId(u.getId());
                    updUser.setCookie(u.getCookie());
                    break;
                }
            }
            CartList cartList = new Gson().fromJson(cart, CartList.class);
            if (cartList == null) {
                cartList = new CartList();
                updUser.setCookie(UUID.randomUUID().toString());
                SecureRandom secureRandom = new SecureRandom();
                int idUser = secureRandom.nextInt();
                updUser.setId(idUser);
            }
            cartList.items.add(id);
            updUser.setBasket(new Gson().toJson(cartList));
        } else {
            System.out.println("pshopmvc.CartService.addItem()");
            System.out.println("User not found");
            updUser.setCookie(UUID.randomUUID().toString());
            SecureRandom secureRandom = new SecureRandom();
            int idUser = secureRandom.nextInt();
            updUser.setId(idUser);
            CartList cartList = new CartList();
            cartList.items.add(id);
            updUser.setBasket(new Gson().toJson(cartList));
        }
        return updUser;
    }
}
