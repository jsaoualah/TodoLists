package tudu.web.filter;

import org.junit.Test;
import java.security.Principal;
import tudu.domain.User;

public class Level3MonitorFilterTest {


    User springSecurityUser = new User();

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
