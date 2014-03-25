package pt.uc.dei.aor.projeto3.grupod.managedBeans;

import java.io.File;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import pt.uc.dei.aor.projeto3.grupod.ejb.UserLogedEJB;
import pt.uc.dei.aor.projeto3.grupod.entities.UserPlay;
import pt.uc.dei.aor.projeto3.grupod.exceptions.DuplicateEmailException;
import pt.uc.dei.aor.projeto3.grupod.exceptions.PassDontMatchException;
import pt.uc.dei.aor.projeto3.grupod.exceptions.PasswordNotCorrectException;
import pt.uc.dei.aor.projeto3.grupod.exceptions.UserNotFoundException;
import pt.uc.dei.aor.projeto3.grupod.facades.UserPlayFacade;

@Named
@RequestScoped
public class UserController {

    @Inject
    private UserLogedEJB userLogedEJB;

    @Inject
    private UserPlayFacade userPlayFacade;

    private UserPlay user;
    private String loginEmail;
    private String loginPassword;
    private String password1;
    private String password2;
    private String emailExists;
    private String passwordMatch;
    private String userExists;
    private String passwordCorrect;
    private String userName;
    private UIForm login;
    private UIForm newUser;
    private String search;
    private String numberOfResults;
   

    public UserController() {

    }

    @PostConstruct
    public void init() {

        if (userLogedEJB.getUser() == null) {
            this.user = new UserPlay();
        } else {
            this.user = userLogedEJB.getUser();
            userName = user.getName();
        }
        System.out.println("UserControllerInit");
        
        
    }

    public UIForm getLogin() {
        return login;
    }

    public void setLogin(UIForm login) {
        this.login = login;
    }

    public UIForm getNewUser() {
        return newUser;
    }

    public void setNewUser(UIForm newUser) {
        this.newUser = newUser;
    }

    
    
    public String getUserName() {
        return userLogedEJB.getUser().getName();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserExists() {
        return userExists;
    }

    public void setUserExists(String userExists) {
        this.userExists = userExists;
    }

    public String getPasswordCorrect() {
        return passwordCorrect;
    }

    public void setPasswordCorrect(String passwordCorrect) {
        this.passwordCorrect = passwordCorrect;
    }

    public UserLogedEJB getUserLogedEJB() {
        return userLogedEJB;
    }

    public void setUserLogedEJB(UserLogedEJB userLogedEJB) {
        this.userLogedEJB = userLogedEJB;
    }

    public UserPlay getUser() {
        return user;
    }

    public void setUser(UserPlay user) {
        this.user = user;
    }

    public String getLoginEmail() {
        return this.loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getEmailExists() {
        return emailExists;
    }

    public void setEmailExists(String emailExists) {
        this.emailExists = emailExists;
    }

    public String getPasswordMatch() {
        return passwordMatch;
    }

    public void setPasswordMatch(String passwordMatch) {
        this.passwordMatch = passwordMatch;
    }

    /**
     * @return the result of the login method
     */
    public String makeLogin() {

        UserPlay result;
                
        try{
        result = userPlayFacade.login(loginEmail, loginPassword);
        }
        catch (UserNotFoundException e){
            userExists = e.getMessage();
            return "login";
        }
        
        catch(PasswordNotCorrectException ex){
        
            passwordCorrect = ex.getMessage();
            return "login";
            
        }
        userLogedEJB.setUser(result);
        return "myPlaylists";
    }

    /**
     * @return The result of the logout method
     */
    public String makeLogout() {
        return userPlayFacade.logout();
    }
    
    /**
     * Verifies if there is a user logged
     */

    public void verifyUser() {
        
        FacesContext fc = FacesContext.getCurrentInstance();
        ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler)fc.getApplication().getNavigationHandler();
        if (userLogedEJB.getUser() == null){
            nav.performNavigation("login");
        }
        
    }
    
    public static String getMusicFolder(UserPlay u) {
        String currentDir = new File(".").getAbsolutePath();
        currentDir = currentDir.substring(0, currentDir.length() - 1);
        return currentDir + "music/" + u.geteMail()+"/";
    }

    /**
     * Calls the newUser method and sets the logedUser
     *
     * @return The String that leads to a XHTML window
     */
    
    public String makeNewUser() {
        
        try{

            UserPlay userN = userPlayFacade.newUser(user, password2);
            userLogedEJB.setUser(userN);
            File f = new File(getMusicFolder(userN));
            f.mkdir();
        }
        
        catch(DuplicateEmailException e){
            emailExists = e.getMessage();
            java.util.logging.Logger.getLogger(UserController.class.getName()).
                    log(Level.SEVERE, null, e);
        }
        
        catch(PassDontMatchException ex){
            passwordMatch = ex.getMessage();
            java.util.logging.Logger.getLogger(UserController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        
        
        return "myPlaylists";

    }

    /**
     * Calls the updateUser method and sets the logedUser
     *
     * @return The String that leads to a XHTML window
     */
    public String makeUpdateUser() {

        UserPlay updatedUser = userPlayFacade.updateUser(userLogedEJB.getUser(),
                user, password1, password2);

        if (updatedUser != null) {
            userLogedEJB.setUser(updatedUser);
            return "myPlaylists";
        } else {
            passwordMatch = userPlayFacade.getPassMissmatch();
            emailExists = userPlayFacade.getEmailExists();
            return "profile";

        }

    }

    /**
     * Deletes the user and invalidate the session
     *
     * @return The String that leads to a XHTML window
     */
    public String makeDeleteUser() {
        userPlayFacade.removeUser(userLogedEJB.getUser());

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();

        return "login";

    }
    
    /**
     * Shows the new user form
     */
    public void newRegistration(){
        newUser.setRendered(true);
        login.setRendered(false);
        
    }
    
    /**
     * Cancel the new user form
     */
    public void cancelNewUser(){
        login.setRendered(true);
        newUser.setRendered(false);
        makeLogout();
        
    }
    
    
    
    

}
