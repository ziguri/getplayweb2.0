/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.ejb.embeddable.EJBContainer;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.uc.dei.aor.projeto3.grupod.entities.UserPlay;
import pt.uc.dei.aor.projeto3.grupod.exceptions.UserNotFoundException;
import pt.uc.dei.aor.projeto3.grupod.facades.UserPlayFacade;

/**
 *
 * @author Mar
 */
public class ApUserFacadeTest {

    public ApUserFacadeTest() {
    }

    static EJBContainer container;
    
    static UserPlayFacade facade;

    @BeforeClass
    public static void setUpClass() throws Exception {
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        facade = (UserPlayFacade) container.getContext().lookup("java:global/classes/ApUserFacade");
    }

    @AfterClass
    public static void tearDownClass() {
        container.close();
    }

    @Test
    public void testUserCreate() throws UserNotFoundException{
        System.out.println("testUserCreate");

        UserPlay u = new UserPlay("teste", "teste@teste.com", "testeste");

        facade.create(u);
        UserPlay result = facade.findUserByMail(u.geteMail());

        assertEquals(u.geteMail(), result.geteMail());
        assertEquals(u.getName(), result.getName());

        facade.removeUser(u);
    }

    @Test
    public void testGetUserByEmail() throws UserNotFoundException{
        System.out.println("testGetUserByEmail");

        String expectedEmail = "teste@teste.com";
        UserPlay u = new UserPlay("teste", expectedEmail, "testeste");

        facade.create(u);

        UserPlay result = facade.findUserByMail(expectedEmail);
        assertEquals(u, result);

        facade.removeUser(u);
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetNonExistantUserByEmail() throws UserNotFoundException{
        System.out.println("testGetNonExistantUserByEmail");

        String email = "testesssssss@teste.com";

        UserPlay result = facade.findUserByMail(email);
        assertNull(result);
    }

//    @Test
//    public void testValidUserPassword() throws InvalidAuthException, UserNotFoundException {
//        System.out.println("validUserPassword");
//
//        String email = "teste@teste.com";
//        ApUser u = new ApUser("teste", email, "testeste");
//
//        facade.create(u);
//        ApUser result = facade.validAuthentication(email, "testeste");
//        assertNotNull(result);
//        facade.remove(u);
//    }

//    @Test(expected = InvalidAuthException.class)
//    public void testInvalidUserPassword() throws InvalidAuthException, UserNotFoundException {
//        System.out.println("invalidUserPassword");
//
//        String email = "teste@teste.com";
//        ApUser u = new ApUser("teste", email, "testeste");
//
//        facade.create(u);
//        ApUser result = facade.validAuthentication(email, "teste2");
//        assertNull(result);
//        facade.remove(u);
//    }

//    @Test
//    public void testValidEmail() {
//        System.out.println("validEmail");
//
//        String email = "teste@teste.com";
//        ApUser u = new ApUser("teste", email, "testeste");
//
//        facade.create(u);
//        boolean result = facade.emailExists(email);
//        assertEquals(false, result);
//
//        result = facade.emailExists("1231231232131232@gmail.com");
//        assertEquals(true, result);
//        facade.remove(u);
//    }

}
