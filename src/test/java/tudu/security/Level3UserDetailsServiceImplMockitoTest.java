package tudu.security;

import org.easymock.EasyMock;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import tudu.domain.Role;
import tudu.domain.RolesEnum;
import tudu.domain.User;
import tudu.service.UserService;
import tudu.service.impl.UserServiceImpl;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class Level3UserDetailsServiceImplMockitoTest {


    @Mock
    UserServiceImpl userService;

    @InjectMocks
    UserDetailsServiceImpl authenticationDAO = new UserDetailsServiceImpl();

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    /* Le test suivant a été ecris avec EasyMock. L'écrire à nouveau en utilisant la syntaxe BDD Mockito et en utilisant les assertions de fest assert a la place des assertEquals
   * assertNotNull*/
   @Test
    public void testLoadUserByUsername() {
        UserDetailsServiceImpl authenticationDAO = new UserDetailsServiceImpl();
        UserService userService = EasyMock.createMock(UserService.class);
        ReflectionTestUtils.setField(authenticationDAO, "userService", userService);

        User user = new User();
        user.setLogin("test_user");
        user.setPassword("password");
        user.setEnabled(true);
        Role userRole = new Role();
        userRole.setRole(RolesEnum.ROLE_USER.toString());
        user.getRoles().add(userRole);
        expect(userService.findUser("test_user")).andReturn(user);


        replay(userService);

        UserDetails springSecurityUser = authenticationDAO
                .loadUserByUsername("test_user");

        assertEquals(user.getLogin(), springSecurityUser.getUsername());
        assertEquals(user.getPassword(), springSecurityUser.getPassword());
        assertNotNull(user.getLastAccessDate());
        assertEquals(1, springSecurityUser.getAuthorities().size());
        assertEquals(RolesEnum.ROLE_USER.toString(),
                springSecurityUser.getAuthorities().iterator().next().getAuthority());

        verify(userService);
    }
    // Pour aller plus loin : Should you only mock types that you own ?: http://stackoverflow.com/questions/1906344/should-you-only-mock-types-you-own

    @Test
    public void testLoadUserByUsername_Mockito() {

        ReflectionTestUtils.setField(authenticationDAO, "userService", userService);

        User user = new User();
        user.setLogin("test_user");
        user.setPassword("password");
        user.setEnabled(true);
        Role userRole = new Role();
        userRole.setRole(RolesEnum.ROLE_USER.toString());
        user.getRoles().add(userRole);
        Mockito.when(userService.findUser("test_user")).thenReturn(user);
        UserDetails springSecurityUser;
        springSecurityUser = authenticationDAO.loadUserByUsername("test_user");
        Assertions.assertThat(user.getLogin()).isEqualTo(springSecurityUser.getUsername());
        Assertions.assertThat(user.getPassword()).isEqualTo(springSecurityUser.getPassword());
        Assertions.assertThat(user.getLastAccessDate()).isNotNull();
        Assertions.assertThat(1).isEqualTo(springSecurityUser.getAuthorities().size());
        Assertions.assertThat(RolesEnum.ROLE_USER.toString()).isEqualTo(springSecurityUser.getAuthorities().iterator().next().getAuthority());

    }
}
