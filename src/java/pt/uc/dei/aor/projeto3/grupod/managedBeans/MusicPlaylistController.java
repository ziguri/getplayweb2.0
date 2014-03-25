
package pt.uc.dei.aor.projeto3.grupod.managedBeans;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import pt.uc.dei.aor.projeto3.grupod.ejb.MusicEJB;
import pt.uc.dei.aor.projeto3.grupod.ejb.UserLogedEJB;
import pt.uc.dei.aor.projeto3.grupod.entities.Music;
import pt.uc.dei.aor.projeto3.grupod.entities.Playlist;
import pt.uc.dei.aor.projeto3.grupod.entities.UserPlay;
import pt.uc.dei.aor.projeto3.grupod.exceptions.MusicInPlaylistException;
import pt.uc.dei.aor.projeto3.grupod.facades.PlaylistFacade;

@ManagedBean
@ViewScoped
public class MusicPlaylistController implements Serializable, Converter {

    @Inject
    private MusicEJB musicEJB;
    
    @Inject
    private PlaylistFacade playlistFacade;

    @Inject
    private UserLogedEJB userLogedEJB;
    
    private long id;

    private Music currentMusic;
    private Playlist currentPlay;
    private List<Music> searchResults;
    private String search;
    private String numberOfResults;
    private List<Music> targetSearch;
    private List<Playlist> playlistList;
    private UIForm content;
    private Music music;
    private long musicID;
    private String errorCopyingtoPlaylist;
    private String selectedItemSearch;
    private UIForm searchCopyPlay;
    private UIComponent viewPlaylist;
    private UIComponent tablePlaylists;    
    private UIComponent tableSearch;
    private Flash flash;
    private UserPlay user;

    

    public MusicPlaylistController() {
    }

    @PostConstruct
    public void init() {
        if(userLogedEJB.getUser() != null){
            
            playlistList = playlistFacade.findAllPlaylistsByUserID(userLogedEJB.getUser());
        }
        
    }

    public UIForm getContent() {
        return content;
    }

    public void setContent(UIForm content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public long getMusicID() {
        return musicID;
    }

    public void setMusicID(long musicID) {
        this.musicID = musicID;
    }
    
    
    
    public UIForm getSearchCopyPlay() {
        return searchCopyPlay;
    }

    public void setSearchCopyPlay(UIForm searchCopyPlay) {
        this.searchCopyPlay = searchCopyPlay;
    }

    public List<Music> getSearchResults() {
        makeSearch();
        return searchResults;
    }

    public void setSearchResults(List<Music> searchResults) {
        this.searchResults = searchResults;
    }

    public UIComponent getTableSearch() {
        return tableSearch;
    }

    public void setTableSearch(UIComponent tableSearch) {
        this.tableSearch = tableSearch;
    }

    public Flash getFlash() {
        return flash;
    }

    public void setFlash(Flash flash) {
        this.flash = flash;
    }

    
    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(String numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public List<Music> getTargetSearch() {
        return targetSearch;
    }

    public void setTargetSearch(List<Music> targetSearch) {
        this.targetSearch = targetSearch;
    }
    
    

    public UIComponent getTablePlaylists() {
        return tablePlaylists;
    }

    public void setTablePlaylists(UIComponent tablePlaylists) {
        this.tablePlaylists = tablePlaylists;
    }

    

    public String getSelectedItemSearch() {
        return selectedItemSearch;
    }

    public void setSelectedItemSearch(String selectedItemSearch) {
        this.selectedItemSearch = selectedItemSearch;
    }
    
    

    public List<Playlist> getPlaylistList() {

        return playlistList;
    }

    public void setPlaylistList(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }

    public String getErrorCopyingtoPlaylist() {
        return errorCopyingtoPlaylist;
    }

    public void setErrorCopyingtoPlaylist(String errorCopyingtoPlaylist) {
        this.errorCopyingtoPlaylist = errorCopyingtoPlaylist;
    }
    
    

    public PlaylistFacade getPlaylistFacade() {
        return playlistFacade;
    }

    public void setPlaylistFacade(PlaylistFacade playlistFacade) {
        this.playlistFacade = playlistFacade;
    }

    public UIComponent getViewPlaylist() {
        return viewPlaylist;
    }

    public void setViewPlaylist(UIComponent viewPlaylist) {
        this.viewPlaylist = viewPlaylist;
    }

    public MusicEJB getMusicEJB() {
        return musicEJB;
    }

    public void setMusicEJB(MusicEJB musicEJB) {
        this.musicEJB = musicEJB;
    }

    public UserLogedEJB getUserLogedEJB() {
        return userLogedEJB;
    }

    public void setUserLogedEJB(UserLogedEJB userLogedEJB) {
        this.userLogedEJB = userLogedEJB;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public void setCurrentMusic(Music currentMusic) {
        this.currentMusic = currentMusic;
    }

    public Playlist getCurrentPlay() {
        return currentPlay;
    }

    public void setCurrentPlay(Playlist currentPlay) {
        this.currentPlay = currentPlay;
    }

    public String removeMusic(Music music) {

        currentPlay = playlistFacade.find(id);

        currentPlay.getSongs().remove(music);
        playlistFacade.edit(currentPlay);
        return null;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        if (!value.matches("\\d+")) {
            throw new ConverterException("The value is not a valid playlist ID: " + value);
        }

        Long id = Long.parseLong(value);
        return playlistFacade.find(id);

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }

        if (!(value instanceof Playlist)) {
            throw new ConverterException("The value is not a Playlist: " + value);
        }

        String id = ((Playlist) value).getPlaylistID().toString();
        return (id != null) ? id.toString() : null;
    }
    
    /**
     * Open a playlist
     * @param p The playlist 
     */
    public void openPlaylist(Playlist p) {
        id = p.getPlaylistID();
        currentPlay = p;
        tablePlaylists.setRendered(false);
        viewPlaylist.setRendered(true);
    }
    
    /**
     * Delete a playlist
     * @param p
     * @return 
     */
    public String delete(Playlist p) {
        playlistFacade.deletePlaylist(p);
        return "myPlaylists";
    }
    
    /**
     * Order a playlist by name
     * @param a 
     */
    public void sortByOrderName(int a) {

        playlistList = playlistFacade.findAllPlaylistsByUserIDName(a, userLogedEJB.getUser());

    }
    
    /**
     * Order a playlist by date
     * @param a 
     */
    public void sortByOrderDate(int a) {

        playlistList = playlistFacade.findAllPlaylistsByUserIDDate(a, userLogedEJB.getUser());

    }
    
    /**
     * Order a playlist by size
     * @param a 
     */
    public void sortByOrderSize(int a) {
        playlistList = playlistFacade.findAllPlaylistsByUserID(userLogedEJB.getUser());
        if (a == 1) {
            Collections.sort(playlistList);
        } else {
            Collections.sort(playlistList, new Playlist.OrderBySizeDesc());
        }

    }
    
    /** Search music by Title and by Artist
     *
     * @return
     */
    public void makeSearch() {

        searchResults = musicEJB.searchMusic(search);
        int n = searchResults.size();
        numberOfResults = "We found " + n + " results for your search";
        
        tableSearch.setRendered(true);
        
        flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("search", search);
        flash.put("searchResults", searchResults);
        flash.put("numberOfResults", numberOfResults);

    }
    
    /**
     * Presents the search result
     * @param m 
     */
    
    public void showListSearch(Music m) {
        music = m;
        musicID = music.getSongID();
        searchCopyPlay.setRendered(true);

    }
    
    /**
     * Insert a music in a Playlist
     */
    public void copyToplaylistSearch() {
        Music musicToCopy = musicEJB.getMusicFacade().findMusicBySongID(musicID);
        try {
            musicEJB.copyToplaylist(musicToCopy, selectedItemSearch, userLogedEJB.getUser());
            
        } catch (MusicInPlaylistException ex) {
            errorCopyingtoPlaylist = ex.getMessage();
            Logger.getLogger(MusicController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     * Cancel the copy of a music in the search table to a playlist
     */
    
    public void cancelCopyToPlaylistSearch(){
        searchCopyPlay.setRendered(false);
        errorCopyingtoPlaylist = "";
    }
    

}
