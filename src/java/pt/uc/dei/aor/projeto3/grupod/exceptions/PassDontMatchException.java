

package pt.uc.dei.aor.projeto3.grupod.exceptions;


public class PassDontMatchException extends Exception {
    
    public PassDontMatchException(){
        super("The passwords dont match");
    }
    
}
