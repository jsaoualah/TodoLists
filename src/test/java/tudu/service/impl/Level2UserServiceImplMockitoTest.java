package tudu.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import tudu.domain.User;
import tudu.service.UserAlreadyExistsException;


import tudu.domain.*;


import javax.persistence.EntityManager;



public class Level2UserServiceImplMockitoTest {

    User user = new User();
    Role role = new Role();
    TodoList todoList = new TodoList();
    Todo todo = new Todo();

    @Mock
    EntityManager entityManager;
    @InjectMocks
    UserServiceImpl userService = new UserServiceImpl();

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        user.setLogin("test_user");
        user.setFirstName("First name");
        user.setLastName("Last name");
        role.setRole(String.valueOf(RolesEnum.ROLE_USER));
        todoList.setName("Welcome!");
    }
    /*
    * Vérifier que l on récupère bien uneRuntimeException en sortie de updateUser si la methode Merge de l entityManager leve une RuntimeException
    * Méthode : updateUser
    */
    @Test(expected = RuntimeException.class)
    public void when_an_exception_is_thrown_by_entityManager_then_rethrow() {

        Mockito.when(entityManager.merge(user)).thenThrow(new RuntimeException());
        userService.updateUser(user);
    }

    /*
    LES METHODES SUIVANTES SONT UNIQUEMENT LA POUR APPRENDRE LA SYNTAXE. CES TESTS SONT EXTREMEMENTS FRAGILES, A MANIPULER AVEC PRECAUTIONS !
     */
    /*
    Type : Test Comportement
    Vérifier que l appel a l'entity manager persist a bien été effectué 4 fois
    Méthode : createUser
    */
    @Test
    public void when_creation_of_a_new_user_then_4_calls_to_entityManager_persist() throws UserAlreadyExistsException {

        Mockito.when(entityManager.find(User.class, "test_user")).thenReturn(null);
        Mockito.when(entityManager.find(Role.class, RolesEnum.ROLE_USER.toString())).thenReturn(role);
        userService.createUser(user);
        Mockito.verify(entityManager, Mockito.atLeast(1)).persist(user);
        Mockito.verify(entityManager, Mockito.atLeast(1)).persist(todoList);
        Mockito.verify(entityManager, Mockito.atLeast(1)).persist(todo);
    }


    /*
    Type : Test Comportement
    Vérifier que l appel a l'entity manager persist a bien été effectué au moins 2 fois et au plus 5 fois
    Méthode : createUser
    */

    @Test
    public void when_creation_of_a_new_user_then_between_2_and_5_calls_to_entityManager_persist() throws UserAlreadyExistsException {
        Mockito.when(entityManager.find(User.class, "test_user")).thenReturn(null);
        Mockito.when(entityManager.find(Role.class, RolesEnum.ROLE_USER.toString())).thenReturn(role);
        userService.createUser(user);
        Mockito.verify(entityManager, Mockito.atLeast(2)).persist(todoList);
        }

    /*
    Type : Test Comportement
    Vérifier que si l'utilisateur existe, la methode persiste n'a jamais été appelée
    Méthode : createUser
    */
    @Test(expected = UserAlreadyExistsException.class)
    public void when_the_login_already_exist_then_persist_never_called_1() throws UserAlreadyExistsException {

        Mockito.when(entityManager.find(User.class, "test_user")).thenReturn(user);
        userService.createUser(user);
        Mockito.verify(entityManager, Mockito.never()).persist(user);
    }

    /*
    Type : Test Comportement
    Vérifier que si l'utilisateur existe, la methode persiste n'a jamais été appelée - 2eme alternative avec verifyNoMoreInteractions
    Méthode : createUser
    */

    @Test(expected = UserAlreadyExistsException.class)
    public void when_the_login_already_exist_then_persist_never_called_2() throws UserAlreadyExistsException {

        Mockito.when(entityManager.find(User.class, "test_user")).thenReturn(user);
        userService.createUser(user);
        Mockito.verifyNoMoreInteractions(entityManager);
    }

}



















