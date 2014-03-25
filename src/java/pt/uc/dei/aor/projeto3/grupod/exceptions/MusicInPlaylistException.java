

package pt.uc.dei.aor.projeto3.grupod.exceptions;


public class MusicInPlaylistException extends Exception{
    
    public MusicInPlaylistException(){
        super("This music is already inside the playlist");
    }
}
