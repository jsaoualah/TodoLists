package tudu.security;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tudu.domain.RolesEnum;
import tudu.service.UserService;
import tudu.domain.User;
import tudu.domain.Role;
import org.mockito.MockitoAnnotations;


public class Level2UserDetailsServiceImplMockitoTest {

    User user = new User();

    @Mock
    UserService userService;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();

    @Before
    public void Before() {
        MockitoAnnotations.initMocks(this);
        user.setLogin("login_test");
        user.setPassword("pswrd_test");
        user.setEnabled(true);
        Role role = new Role();
        role.setRole(RolesEnum.ROLE_USER.toString());
        user.getRoles().add(role);

    }
    /*
    * Simuler une levée d'exceptions - tester que la methode lève bien une UsernameNotFoundException si la méthode findBy lève une ObjectRetrievalFailureException
    * Méhode : loadUserByUsername

    */
    @Test(expected = UsernameNotFoundException.class)
    public void loadByUsername_throw_UsernameNotFoundException() {

        Mockito.when(userService.findUser("login_test")).thenThrow(new ObjectRetrievalFailureException(User.class, user.getLogin()));

        userDetailsService.loadUserByUsername(user.getLogin());
    }
}
