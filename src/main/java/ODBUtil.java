package pshopmvc;

import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class ODBUtil {

    private static volatile String dbPath;
    private static volatile List<Item> itemList;
    private static volatile EntityManagerFactory entityManagerFactory;

    public List<Item> getItems() {
        if (itemList == null) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            Query query = entityManager.createQuery("select item from Item item", Item.class);
            itemList = (List<Item>) query.getResultList();
            entityManager.close();
        }
        return itemList;
    }

    public List<User> getUsers() {
        List<User> userList;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select user from User user", User.class);
        userList = (List<User>) query.getResultList();
        entityManager.close();
        return userList;
    }

    public void updateDb(User u) {
        if (u.getBasket() != null && u.getCookie() != null) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CartList cartList = new Gson().fromJson(u.getBasket(), CartList.class);
            if (cartList.items.isEmpty() || cartList == null) {
                User u_remove = entityManager.find(User.class, u);
                entityManager.remove(u_remove);
                System.out.println("pshopmvc.ODBUtil.updateDb()");
                System.out.println("removed empty");
            } else {
                entityManager.merge(u);
            }
            entityManager.getTransaction().commit();
            System.out.println("pshopmvc.ODBUtil.updateDb()");
            entityManager.close();
        } else {
            System.out.println("pshopmvc.ODBUtil.updateDb()");
            System.out.println("Null user!!!!!!!!!!");
        }
    }

    @PostConstruct
    private void dbInit() {
        try {
            String sep = File.separator;
            String relPath = "BOOT-INF" + sep + "classes" + sep + "mvc_test.odb";
            String userDir = System.getProperty("user.dir");
            dbPath = userDir + sep + "ROOT_TEMP" + sep + "TEMP_JAVA" + sep + "mvc_test.odb";
            Path path = Paths.get(dbPath);
            if (!Files.exists(path)) {
                String jarPath = System.getProperty("java.class.path");
                ZipFile jf = new ZipFile(jarPath);
                Enumeration<? extends ZipEntry> e = jf.entries();
                while (e.hasMoreElements()) {
                    ZipEntry ze = (ZipEntry) e.nextElement();
                    if (!ze.isDirectory() && ze.getName().equals(relPath)) {
                        BufferedInputStream bis = new BufferedInputStream(new BufferedInputStream(jf.getInputStream(ze)), 263 * 1024);
                        byte[] dbFile = IOUtils.toByteArray(bis);
                        Files.createDirectories(Paths.get(userDir + sep + "ROOT_TEMP" + sep + "TEMP_JAVA"));
                        Files.write(path, dbFile);
                        bis.close();
                        break;
                    }
                }
            }
            entityManagerFactory = Persistence.createEntityManagerFactory(dbPath);
        } catch (IOException ex) {
            Logger.getLogger(ODBUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
