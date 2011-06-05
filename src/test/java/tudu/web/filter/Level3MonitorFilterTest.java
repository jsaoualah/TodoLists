package tudu.web.filter;

import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

public class Level3MonitorFilterTest {

    /*
  Verifier que l on passe bien dans les branches
   *               if (principal instanceof String) {
                       userName = "anonymous";
                   } else {
                       User springSecurityUser = (User) principal;
                       userName = springSecurityUser.getUsername();
                   }

    *
    * Duree max : 10 minutes
    */
    @Test
    public void test_authentication_1() {
    }
    @Test
    public void test_authentication_2() {
    }
}
