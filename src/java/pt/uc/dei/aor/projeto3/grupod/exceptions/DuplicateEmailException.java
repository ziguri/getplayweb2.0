

package pt.uc.dei.aor.projeto3.grupod.exceptions;


public class DuplicateEmailException extends Exception {
    
    public DuplicateEmailException(){
        super("E-mail already taken");
    }
}
