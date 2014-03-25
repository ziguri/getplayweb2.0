package pt.uc.dei.aor.projeto3.grupod.managedBeans;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIForm;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import pt.uc.dei.aor.projeto3.grupod.ejb.UserLogedEJB;
import pt.uc.dei.aor.projeto3.grupod.ejb.MusicEJB;
import pt.uc.dei.aor.projeto3.grupod.entities.Music;
import pt.uc.dei.aor.projeto3.grupod.entities.UserPlay;
import pt.uc.dei.aor.projeto3.grupod.exceptions.MusicInPlaylistException;
import pt.uc.dei.aor.projeto3.grupod.exceptions.ReleaseYearInvalidException;
import pt.uc.dei.aor.projeto3.grupod.exceptions.NotMp3Exception;

@Named
@RequestScoped
public class MusicController {

    @Inject
    private UserLogedEJB logedUserEJB;

    @Inject
    private MusicEJB musicEJB;

    private Music music;
    private Part file;
    private String isMP3ErrorMessage;
    private String year;
    private String yearInvalid;
    private String selectedItem;
    private String errorCopyingtoPlaylist;
    private List<Music> allMusic;
    private List<Music> musicResultsOfTheUserLoged;
    private UIForm addToPlaylistAll;
    private long musicID;
    private UIForm addMusicMy;
    private UIForm addMusicAll;
    private UIForm addToPlaylistMy;
    private UIForm editMusic;
    private Flash flash;

    /**
     * Creates a new instance of MusicController
     */
    public MusicController() {
    }

    @PostConstruct
    public void init() {
        
        music = new Music();
        
        if(logedUserEJB.getUser()!= null){

            musicResultsOfTheUserLoged = musicEJB.myMusicsList(logedUserEJB.getUser());}
    }

    
    public MusicEJB getMusicEJB() {
        return musicEJB;
    }

    public void setMusicEJB(MusicEJB musicEJB) {
        this.musicEJB = musicEJB;
    }

    public UIForm getAddMusicAll() {
        return addMusicAll;
    }

    public UIForm getAddToPlaylistMy() {
        return addToPlaylistMy;
    }

    public void setAddToPlaylistMy(UIForm addToPlaylistMy) {
        this.addToPlaylistMy = addToPlaylistMy;
    }

    public UIForm getEditMusic() {
        return editMusic;
    }

    public void setEditMusic(UIForm editMusic) {
        this.editMusic = editMusic;
    }

    public void setAddMusicAll(UIForm addMusicAll) {
        this.addMusicAll = addMusicAll;
    }

    public UIForm getAddMusicMy() {
        return addMusicMy;
    }

    public void setAddMusicMy(UIForm addMusicMy) {
        this.addMusicMy = addMusicMy;
    }

    public List<Music> getAllMusic() {
        return musicEJB.allMusicsList();
    }

    public void setAllMusic(List<Music> allMusic) {
        this.allMusic = allMusic;
    }

    public UIForm getAddToPlaylistAll() {
        return addToPlaylistAll;
    }

    public void setAddToPlaylistAll(UIForm addToPlaylistAll) {
        this.addToPlaylistAll = addToPlaylistAll;
    }

    public long getMusicID() {
        return musicID;
    }

    public void setMusicID(long musicID) {
        this.musicID = musicID;
    }

    public String getErrorCopyingtoPlaylist() {
        return errorCopyingtoPlaylist;
    }

    public void setErrorCopyingtoPlaylist(String errorCopyingtoPlaylist) {
        this.errorCopyingtoPlaylist = errorCopyingtoPlaylist;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public Music getMusic() {

        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public UserLogedEJB getActiveObjectsEJB() {
        return logedUserEJB;
    }

    public void setActiveObjectsEJB(UserLogedEJB activeObjectsEJB) {
        this.logedUserEJB = activeObjectsEJB;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getIsMP3ErrorMessage() {
        return isMP3ErrorMessage;
    }

    public void setIsMP3ErrorMessage(String isMP3ErrorMessage) {
        this.isMP3ErrorMessage = isMP3ErrorMessage;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYearInvalid() {
        return yearInvalid;
    }

    public void setYearInvalid(String yearInvalid) {
        this.yearInvalid = yearInvalid;
    }

    public List<Music> getMusicResultsOfTheUserLoged() {

        return musicEJB.myMusicsList(logedUserEJB.getUser());
    }

    public void setMusicResultsOfTheUserLoged(List<Music> musicResultsOfTheUserLoged) {
        this.musicResultsOfTheUserLoged = musicResultsOfTheUserLoged;
    }

    /**
     * method for validating the releaseYear of the new music.
     *
     * @throws ReleaseYearInvalidException
     *
     */
    public int verifyYear() throws ReleaseYearInvalidException {

        try {

            return musicEJB.validateYear(year);

        } catch (ReleaseYearInvalidException e) {

            throw new ReleaseYearInvalidException();

        }

    }

    /**
     * persists the new music to the database
     *
     */
    public void newMusic() {
        musicEJB.setFile(file);
        UserPlay u = logedUserEJB.getUser();
        music.setUser(u);
        music.setPath(musicEJB.upload(file, music, u));

        try {
            musicEJB.newMusic(music);
            if (addMusicAll != null) {
                addMusicAll.setRendered(false);
            } else if (addMusicMy != null) {
                addMusicMy.setRendered(false);
            }
        } catch (ReleaseYearInvalidException e) {
            yearInvalid = e.getLocalizedMessage();
            java.util.logging.Logger.getLogger(MusicController.class.getName()).log(Level.SEVERE, null, e);

        } catch (NotMp3Exception e) {
            isMP3ErrorMessage = e.getMessage();
            java.util.logging.Logger.getLogger(MusicController.class.getName()).log(Level.SEVERE, null, e);

        }

    }

    /**
     * deletes a song from the database
     *
     * @param song
     * @return
     */
    public String deleteSong(Music song) {
        String result;

        try {
            return musicEJB.deleteSong(song, logedUserEJB.getUser());
        } catch (Exception ex) {

            Logger.getLogger(MusicController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     * Selects the Music to edit
     *
     * @param song
     * @return
     */
    public void gotToeditMusic(Music song) {

        music = song;
        musicID = music.getSongID();

        addToPlaylistMy.setRendered(false);
        editMusic.setRendered(true);
        addMusicMy.setRendered(false);

    }

    /**
     * Edit the music
     *
     * @return
     */
    public String edit() {

        String result = "";
        Music musicEdit = musicEJB.getMusicFacade().findMusicBySongID(musicID);
        musicEdit.setTitle(music.getTitle());
        musicEdit.setArtist(music.getArtist());
        musicEdit.setAlbum(music.getAlbum());
        musicEdit.setReleaseYEAR(music.getReleaseYEAR());

        try {
            result = musicEJB.editMusic(musicEdit);
        } catch (ReleaseYearInvalidException e) {
            yearInvalid = e.getLocalizedMessage();
            java.util.logging.Logger.getLogger(MusicController.class.getName()).log(Level.SEVERE, null, e);
            return "editMusic";

        }

        return result;

    }

    /**
     * Cancel the edition
     *
     * @return
     */
    public void cancel() {

        editMusic.setRendered(false);
    }

    /**
     * Insert a music in a Playlist
     */
    public void copyToplaylist() {
        Music musicToCopy = musicEJB.getMusicFacade().findMusicBySongID(musicID);
        try {
            musicEJB.copyToplaylist(musicToCopy, selectedItem, logedUserEJB.getUser());
            if (addToPlaylistMy != null) {
                addToPlaylistMy.setRendered(false);
            } else if (addToPlaylistAll != null) {
                addToPlaylistAll.setRendered(false);
            }
        } catch (MusicInPlaylistException ex) {
            errorCopyingtoPlaylist = ex.getMessage();
            Logger.getLogger(MusicController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    

    

    /**
     * Presents the list of Playlist to where the user can copy a music
     *
     * @param m
     */
    public void showListAll(Music m) {
        music = m;
        musicID = music.getSongID();
        addToPlaylistAll.setRendered(true);

        addMusicAll.setRendered(false);

    }
    
    /**
     * Presents the list of Playlist to where the user can copy a music
     *
     * @param m
     */
    public void showListMy(Music m) {
        music = m;
        musicID = music.getSongID();
        addToPlaylistMy.setRendered(true);

        addMusicMy.setRendered(false);

    }

    

   

    /**
     * Opens the add music form in the all music list
     */
    public void goToAddMusicAll() {
        addMusicAll.setRendered(true);
        addToPlaylistAll.setRendered(false);

    }

    /**
     * Cancel the add music in all music list
     */
    public void cancelMusicAll() {
        addMusicAll.setRendered(false);

    }

    /**
     * Cancel the copy music to playlist
     */
    public void cancelCopyToPlaylistAll() {
        addToPlaylistAll.setRendered(false);
    }

    /**
     * Opens the add music form in the my music list
     */
    public void goToAddMusicMy() {
        addMusicMy.setRendered(true);
        addToPlaylistMy.setRendered(false);
        editMusic.setRendered(false);

    }

    /**
     * Cancel the add music in my music list
     */
    public void cancelMusicMy() {
        addMusicMy.setRendered(false);

    }

    /**
     * Cancel the add music to playlist in my music list
     */
    public void cancelCopyToPlaylistMy() {
        addToPlaylistMy.setRendered(false);
    }

    /**
     * Cancel the add music in all music list
     */
    public void cancelMusic() {
        addMusicAll.setRendered(false);
    }


}
