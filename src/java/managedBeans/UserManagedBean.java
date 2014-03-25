/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedBeans;

import beans.UserFacade;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author brunocosta
 */
@ManagedBean
@SessionScoped
public class UserManagedBean implements Serializable {
    
    private UserFacade userFacade;
    
    private String newUserName;
    private String newUserEmail;
    private String newUserPassword1;
    private String newUserPassword2;
    private String loginEmail;
    private String loginPassword;
    
    
    public UserManagedBean() {
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }

    public String getNewUserEmail() {
        return newUserEmail;
    }

    public void setNewUserEmail(String newUserEMail) {
        this.newUserEmail = newUserEMail;
    }

    public String getNewUserPassword1() {
        return newUserPassword1;
    }

    public void setNewUserPassword1(String newUserPassword1) {
        this.newUserPassword1 = newUserPassword1;
    }

    public String getNewUserPassword2() {
        return newUserPassword2;
    }

    public void setNewUserPassword2(String newUserPassword2) {
        this.newUserPassword2 = newUserPassword2;
    }

    public String getLoginEmail() {
        return loginEmail;
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

    

    
    
    
    
    public String loginUser(){
        
        return "newUser";
    }
    
}
