package pt.uc.dei.aor.projeto3.grupod.managedBeans;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIForm;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.aor.projeto3.grupod.ejb.UserLogedEJB;
import pt.uc.dei.aor.projeto3.grupod.entities.Playlist;
import pt.uc.dei.aor.projeto3.grupod.exceptions.PlaylistNameException;
import pt.uc.dei.aor.projeto3.grupod.facades.PlaylistFacade;

@Named
@RequestScoped
public class PlaylistController implements Serializable {

    @Inject
    private UserLogedEJB userLogedEJB;

    @Inject
    private PlaylistFacade playlistFacade;

    private List<Playlist> listOfPlaylists;
    private String messageError;

    private Playlist activePlaylist;
    private Playlist selectedPlaylist;
    private Playlist thePlaylistByID;

    private long playlistID;

    private long musicID;
    private UIForm addPlay;
    private UIForm editPlay;
    

    public PlaylistController() {
    }

    @PostConstruct
    public void init() {
        if (activePlaylist == null) {
            
            activePlaylist = new Playlist();
        }
    }

    public Playlist getThePlaylistByID() {

        thePlaylistByID = playlistFacade.findPlaylistsByID(playlistID);

        return thePlaylistByID;
    }

    public void setThePlaylistByID(Playlist thePlaylistByID) {
        this.thePlaylistByID = thePlaylistByID;
    }

    public List<Playlist> getListOfPlaylists() {
        return listOfPlaylists;
    }

    public void setListOfPlaylists(List<Playlist> listOfPlaylists) {
        this.listOfPlaylists = listOfPlaylists;
    }

    public UIForm getAddPlay() {
        return addPlay;
    }

    public void setAddPlay(UIForm addPlay) {
        this.addPlay = addPlay;
    }

    public UIForm getEditPlay() {
        return editPlay;
    }

    public void setEditPlay(UIForm editPlay) {
        this.editPlay = editPlay;
    }
    
    

    public long getMusicID() {
        return musicID;
    }

    public void setMusicID(long musicID) {
        this.musicID = musicID;
    }

    public PlaylistFacade getPlaylistFacade() {

        return playlistFacade;
    }

    public void setPlaylistFacade(PlaylistFacade playlistFacade) {
        this.playlistFacade = playlistFacade;
    }

    public UserLogedEJB getUserLogedEJB() {
        return userLogedEJB;
    }

    public void setUserLogedEJB(UserLogedEJB userLogedEJB) {
        this.userLogedEJB = userLogedEJB;
    }

    public Playlist getActivePlaylist() {

        return activePlaylist;
    }

    public void setActivePlaylist(Playlist activePlaylist) {
        this.activePlaylist = activePlaylist;
    }

    public Playlist getSelectedPlaylist() {
        return selectedPlaylist;
    }

    public void setSelectedPlaylist(Playlist selectedPlaylist) {
        this.selectedPlaylist = selectedPlaylist;
    }

    public long getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(long playlistID) {
        this.playlistID = playlistID;
    }

   
    public String getMessageError() {
        return messageError = playlistFacade.getMessageError();
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    /**
     * Creates a new Playlist
     *
     * @return The String that leads to a XHTML window
     */
    public String newPlaylist() {

        GregorianCalendar gc = new GregorianCalendar();
        activePlaylist.setCreationDate(gc.getTime());
        activePlaylist.setUser(userLogedEJB.getUser());

        String result = playlistFacade.newPlaylist(activePlaylist);
        return result;
    }
    
    /**
     * Open the edit playlist form
     * @param p The selected playlist
     */
    public void goToEditPlaylist(Playlist p) {

        activePlaylist = p;
        playlistID = p.getPlaylistID();
        addPlay.setRendered(false);
        editPlay.setRendered(true);

    }
    
    /**
     * Edits a playlist
     * @return The string that leads to the web page
     */
    public String edit() {

        try {
            playlistFacade.searchPlaylist(activePlaylist);
        } catch (PlaylistNameException ex) {
            Logger.getLogger(PlaylistController.class.getName()).log(Level.SEVERE, null, ex);
            messageError = ex.getMessage();
        }
        Playlist play = playlistFacade.findPlaylistsByID(playlistID);
        play.setName(activePlaylist.getName());
        playlistFacade.edit(play);

        return "myPlaylists";
    }
    
    /**
     * Shows the add playlist form
     */
    public void addPlaylist(){
        addPlay.setRendered(true);
        editPlay.setRendered(false);
    }
    
    /**
     * Cancel the add playlist form
     */
    public void cancelNewPlay(){
        addPlay.setRendered(false);
    }
    
    /**
     * Cancel the edit playlist
     */
    public void cancelEditPlay(){
        editPlay.setRendered(false);
    }



}
