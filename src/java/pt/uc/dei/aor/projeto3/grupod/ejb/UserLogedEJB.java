
package pt.uc.dei.aor.projeto3.grupod.ejb;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import pt.uc.dei.aor.projeto3.grupod.entities.UserPlay;


@Stateful
@SessionScoped
public class UserLogedEJB {

    private UserPlay user;
   
    public UserPlay getUser() {
        return user;
    }

    public void setUser(UserPlay user) {
        this.user = user;
    }

    

   
    
    
    
    
    
}
