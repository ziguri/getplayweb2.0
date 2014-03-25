

package pt.uc.dei.aor.projeto3.grupod.exceptions;


public class ReleaseYearInvalidException extends Exception {
    
    private NumberFormatException e;
    
    public ReleaseYearInvalidException(){
        super("Year not valid");
    }

    public ReleaseYearInvalidException(NumberFormatException e) {
        super("Year is not valid");
        this.e = e;
    }

    public NumberFormatException getE() {
        return e;
    }

    public void setE(NumberFormatException e) {
        this.e = e;
    }
    
    
}
