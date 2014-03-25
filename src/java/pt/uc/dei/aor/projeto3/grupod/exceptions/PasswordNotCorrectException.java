

package pt.uc.dei.aor.projeto3.grupod.exceptions;


public class PasswordNotCorrectException extends Exception {
    
    
    public PasswordNotCorrectException(){
        super("The password is not correct");
    }
}
