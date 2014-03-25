package pt.uc.dei.aor.projeto3.grupod.facades;

import java.io.File;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import pt.uc.dei.aor.projeto3.grupod.utilities.EncriptMD5;
import pt.uc.dei.aor.projeto3.grupod.entities.Music;
import pt.uc.dei.aor.projeto3.grupod.entities.Playlist;
import pt.uc.dei.aor.projeto3.grupod.entities.UserPlay;
import pt.uc.dei.aor.projeto3.grupod.exceptions.DuplicateEmailException;
import pt.uc.dei.aor.projeto3.grupod.exceptions.PassDontMatchException;
import pt.uc.dei.aor.projeto3.grupod.exceptions.PasswordNotCorrectException;
import pt.uc.dei.aor.projeto3.grupod.exceptions.UserNotFoundException;


@Stateless
public class UserPlayFacade extends AbstractFacade<UserPlay> {

    @PersistenceContext(unitName = "GetPlayWebPU")
    private EntityManager em;

    @Inject
    private MusicFacade musicFacade;
    
    @Inject
    private PlaylistFacade playlistFacade;


    private String userExists;
    private String passwordCorrect;
    private String passMissmatch;
    private String emailUnavailable;
    private String emailExists;

    public UserPlayFacade() {
        super(UserPlay.class);
    }
    
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
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

    public String getPassMissmatch() {
        return passMissmatch;
    }

    public void setPassMissmatch(String passMissmatch) {
        this.passMissmatch = passMissmatch;
    }

    public String getEmailUnavailable() {
        return emailUnavailable;
    }

    public void setEmailUnavailable(String emailUnavailable) {
        this.emailUnavailable = emailUnavailable;
    }

    public String getEmailExists() {
        return emailExists;
    }

    public void setEmailExists(String emailExists) {
        this.emailExists = emailExists;
    }
    
    
    /**
     * Checks if the user exists and if the combination between Email 
     * and Password is correct
     * @param loginEmail
     * @param loginPassword
     * @return The String that leads to a XHTML window
     * @throws pt.uc.dei.aor.projeto3.grupod.exceptions.UserNotFoundException
     * @throws pt.uc.dei.aor.projeto3.grupod.exceptions.PasswordNotCorrectException
     */
    
    public UserPlay login(String loginEmail, String loginPassword) throws UserNotFoundException, PasswordNotCorrectException{
        
        UserPlay  user = findUserByMail(loginEmail);
        
        String pass = EncriptMD5.cryptWithMD5(loginPassword);
        
        if(user!= null && user.getPassword().equals(pass)){
            
           return user;
        }
        else{
            if (user == null){
                throw new UserNotFoundException();
            }
            else if (!user.getPassword().equals(pass)){
                throw new PasswordNotCorrectException();
            }
            return null;
        }
    }
    
    /**
     * Invalidate the session
     * @return The String that leads to a XHTML window
     */

    public String logout(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
        
        return "login";
    }
    
    /**
     * Creates a new user
     * @param user
     * @param password2
     * @return The new user
     * @throws pt.uc.dei.aor.projeto3.grupod.exceptions.DuplicateEmailException
     * @throws pt.uc.dei.aor.projeto3.grupod.exceptions.PassDontMatchException
     */
    
    public UserPlay newUser(UserPlay user, String password2) throws DuplicateEmailException, PassDontMatchException {

        if (user.getPassword().equals(password2)) {

            String pass = EncriptMD5.cryptWithMD5(password2);
            user.setPassword(pass);

            try {
                checkMailCreate(user);
                create(user);
                return user;

            } catch (DuplicateEmailException e) {
                throw new DuplicateEmailException();
            }
        }
        else {
            throw new PassDontMatchException();
        }

    }
    
    /**
     * Make the changes to the user
     * @param logedUser
     * @param user
     * @param password1
     * @param password2
     * @return The new user
     */
    
    public UserPlay updateUser(UserPlay logedUser, UserPlay user, 
            String password1, String password2) {
        
        if(!password1.isEmpty() && password1.equals(password2)){
            
            String pass = EncriptMD5.cryptWithMD5(password1);
            user.setPassword(pass);
            
            try{
                checkMailUpdate(user);
                edit(user);
                
                return user;
            }
            catch(DuplicateEmailException e){

                emailExists = e.getMessage();
                return null;
            }
        }
        else if(password1.isEmpty() && password2.isEmpty()){
            
            try{
                checkMailUpdate(user);
                edit(user);
                return user;
            }
            catch(DuplicateEmailException e){

                emailExists = e.getMessage();
                return null;
            }
        }
        
        else{
           passMissmatch = "The passwords doesn't match";
           return null;
        }
    }
    
    /**
     * finds an user by an email, using a named query
     * @param eMail
     * @return user
     */
    @PermitAll
    public UserPlay findUserByMail(String eMail) {

        Query q = em.createNamedQuery("UserPlay.findByEmail");
        q.setParameter("eMail", eMail);
        try {
            UserPlay user = (UserPlay) q.getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * checks if the user's email is already on another user
     * @param user
     * @throws DuplicateEmailException 
     */
    @PermitAll
    public void checkMailCreate(UserPlay user) throws DuplicateEmailException {
        
            UserPlay u = findUserByMail(user.geteMail());
            if(u!=null){
               throw new DuplicateEmailException();
            }
        
    }

    /**
     * checks if the user's new email is already on another user
     * @param user
     * @throws DuplicateEmailException 
     */
    public void checkMailUpdate(UserPlay user) throws DuplicateEmailException {
        UserPlay u = findUserByMail(user.geteMail());

        if (u != null && !u.geteMail().equals(user.geteMail())) {
            throw new DuplicateEmailException();
        }
    }

    /**
     * removes the user from the database
     * @param user 
     */
    @PermitAll
    public void removeUser(UserPlay user) {
      
        List<Playlist> playlistListOfUser = playlistFacade.findAllPlaylistsByUserID(user);
        List<Music> musicListOfUser = musicFacade.findAllMusicByUserID(user);
        for(Playlist p : playlistListOfUser){
            playlistFacade.remove(p);
        }
        for (Music m : musicListOfUser) {
        
            musicFacade.removeMusic(m, user);
           
        }
        File file = new File(getMusicFolder(user));
        file.delete();
        remove(user);
    }
    
    public static String getMusicFolder(UserPlay u) {
        String currentDir = new File(".").getAbsolutePath();
        currentDir = currentDir.substring(0, currentDir.length() - 1);
        System.out.println(currentDir);
        return currentDir + "music/" + u.geteMail() + "/";
    }

}
