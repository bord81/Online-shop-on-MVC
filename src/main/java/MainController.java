package pshopmvc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/")
@Controller
public class MainController {

    @Autowired
    private ODBUtil dBUtil;

    @Autowired
    private CartService cartService;

    @Autowired
    private MailSender mailSender;

    @RequestMapping(method = RequestMethod.GET)
    public String startPath() {
        return "main";
    }

    @RequestMapping(path = "/main")
    public String mainPath() {
        return "main";
    }

    @RequestMapping(path = "/shop")
    public String shopPath(ModelMap mm) {
        List<Item> items = dBUtil.getItems();
        mm.addAttribute("items", items);
        return "shop";
    }

    @RequestMapping(path = "/shop/add")
    public String shopAdd(@RequestParam String id, ModelMap mm, @CookieValue(value = "c0ntAct", defaultValue = "new") String cookie, HttpServletResponse response) {
        response.addCookie(addItem(cookie, id));
        return "redirect:/shop";
    }

    @RequestMapping(path = "/item")
    public String showItem(@RequestParam String id, ModelMap mm) {
        List<Item> items = dBUtil.getItems();
        int int_id = Integer.parseInt(id);
        Iterator<Item> iter = items.iterator();
        Item nextItem = new Item();
        while (iter.hasNext()) {
            nextItem = iter.next();
            if (nextItem.getId() == int_id) {
                break;
            }
        }
        mm.addAttribute("one_item", nextItem);
        return "item";
    }

    @RequestMapping(path = "/item/add")
    public String itemAdd(@RequestParam String id, ModelMap mm, @CookieValue(value = "c0ntAct", defaultValue = "new") String cookie, HttpServletResponse response) {
        response.addCookie(addItem(cookie, id));
        return "redirect:/item?id=" + id;
    }

    @RequestMapping(path = "/cart")
    public String cartPath(ModelMap mm, @CookieValue(value = "c0ntAct", defaultValue = "new") String cookie) {
        mm = updateCart(mm, cookie);
        return "cart";
    }

    @RequestMapping(path = "/cart/remove")
    public String cartRemove(@RequestParam String id, ModelMap mm, @CookieValue(value = "c0ntAct", defaultValue = "new") String cookie, HttpServletResponse response) {
        List<User> users = dBUtil.getUsers();
        Integer int_id = Integer.valueOf(id);
        User updUser = cartService.removeItem(cookie, users, int_id);
        dBUtil.updateDb(updUser);
        mm = updateCart(mm, cookie);
        return "redirect:/cart";
    }

    @RequestMapping(path = "/cart/remove/all")
    public String cartRemoveAll(@RequestParam String id, ModelMap mm, @CookieValue(value = "c0ntAct", defaultValue = "new") String cookie, HttpServletResponse response) {
        int count = Integer.parseInt(id);
        if (count > 0) {
            List<User> users = dBUtil.getUsers();
            User updUser = cartService.removeAllItems(cookie, users);
            dBUtil.updateDb(updUser);
        }
        mm = updateCart(mm, cookie);
        return "redirect:/cart";
    }

    @RequestMapping(path = "/contact")
    public String contactForm(ModelMap mm) {
        mm.addAttribute(new ContactM());
        return "contact";
    }

    @RequestMapping(path = "/contact/send")
    public String contactForm(ModelMap mm, @ModelAttribute("contactM") @Valid ContactM contactM, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "contact";
        }
        mailSender.sendMail(contactM.getFirstname(), contactM.getFullname(), contactM.getSenderemail(), contactM.getRecemail(), contactM.getMessage(), contactM.getHost(), contactM.getPort(), contactM.getMlogin(), contactM.getMpass());
        return "redirect:/sent";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    private ModelMap updateCart(ModelMap mm, String cookie) {
        List<Item> items = dBUtil.getItems();
        List<User> users = dBUtil.getUsers();
        List<CartItem> cartItems = cartService.formCart(cookie, users, items);
        Iterator<CartItem> iter = cartItems.iterator();
        CartItem ci;
        int totalcount = 0;
        int cartamount = 0;
        while (iter.hasNext()) {
            ci = iter.next();
            if (ci.getId() == -10) {
                totalcount = ci.getCount();
                cartamount = ci.getTotalprice();
                cartItems.remove(ci);
                break;
            }
        }
        mm.addAttribute("totalcount", totalcount);
        mm.addAttribute("cartamount", cartamount);
        mm.addAttribute("items", cartItems);
        return mm;
    }

    private Cookie addItem(String cookie, String id) {
        List<User> users = dBUtil.getUsers();
        Integer int_id = Integer.valueOf(id);
        User updUser = cartService.addItem(cookie, users, int_id);
        dBUtil.updateDb(updUser);
        Cookie newCookie = new Cookie("new", "");
        try {
            newCookie = new Cookie("c0ntAct", URLEncoder.encode(updUser.getCookie(), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        newCookie.setMaxAge(31536000);
        newCookie.setPath("/");
        return newCookie;
    }

}
