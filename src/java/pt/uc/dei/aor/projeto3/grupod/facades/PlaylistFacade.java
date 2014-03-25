
package pt.uc.dei.aor.projeto3.grupod.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pt.uc.dei.aor.projeto3.grupod.entities.Music;
import pt.uc.dei.aor.projeto3.grupod.entities.Playlist;
import pt.uc.dei.aor.projeto3.grupod.entities.UserPlay;
import pt.uc.dei.aor.projeto3.grupod.exceptions.MusicInPlaylistException;
import pt.uc.dei.aor.projeto3.grupod.exceptions.PlaylistNameException;


@Stateless
public class PlaylistFacade extends AbstractFacade<Playlist> {
    
    @PersistenceContext(unitName = "GetPlayWebPU")
    private EntityManager em;
    
    private String messageError;
    private List<Playlist> listMyPlaylists;
    private Playlist playlist;
    
    public PlaylistFacade() {
        super(Playlist.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public Playlist getPlaylist() {
        return playlist;
    }
    
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
    
    public List<Playlist> getListMyPlaylists() {
        
        return listMyPlaylists;
    }
    
    public void setListMyPlaylists(List<Playlist> listMyPlaylists) {
        this.listMyPlaylists = listMyPlaylists;
    }
    
    public String getMessageError() {
        return messageError;
    }
    
    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
    
    /**
     * Creates a new playlist
     * @param p
     * @return the 
     */
    public String newPlaylist(Playlist p) {
        
        try {
            createPlaylist(p);
            return "myPlaylists";
        } catch (PlaylistNameException ex) {
            messageError = ex.getMessage();
            return "myPlaylist";
        }
        
    }
    
    /**
     * Find the playlists of one user
     * @param user
     * @return the list of playlists
     */
    public List<Playlist> listPlaylist(UserPlay user) {
        List<Playlist> resultsSongs = findAllPlaylistsByUserID(user);
        
        return resultsSongs;
    }
    
    public List<Music> seePlaylist(Playlist p) {
        return p.getSongs();
        
    }
    
    /**
     * Find the playlists of one user ID
     * @param user
     * @return List of playlists
     */
    public List<Playlist> findAllPlaylistsByUserID(UserPlay user) {
        Query q = em.createNamedQuery("Playlist.findByUserId");
        q.setParameter("userID", user.getUserID());
        List<Playlist> playlists = q.getResultList();
        return playlists;
        
    }
    
    /**
     * Order the playlist by name
     * @param user
     * @return List of playlists
     */
    public List<Playlist> findAllPlaylistsByUserIDName(int a, UserPlay user) {
        Query q;
        if (a == 1) {
            q = em.createNamedQuery("Playlist.findByUserIdNameAscending");
        } else {
            q = em.createNamedQuery("Playlist.findByUserIdNameDescending");
        }
        q.setParameter("userID", user.getUserID());
        List<Playlist> playlists = q.getResultList();
        return playlists;
        
    }
    
    /**
     * Order the playlist by Date
     * @param a
     * @param user
     * @return List of playlists
     */
    public List<Playlist> findAllPlaylistsByUserIDDate(int a, UserPlay user) {
        Query q;
        if (a == 1) {
            q = em.createNamedQuery("Playlist.findByUserIdDateAscending");
        } else {
            q = em.createNamedQuery("Playlist.findByUserIdDateDescending");
        }
        q.setParameter("userID", user.getUserID());
        List<Playlist> playlists = q.getResultList();
        return playlists;
        
    }
    
    /**
     * Find a playlist by its ID
     * @param id
     * @return A playlist
     */
    public Playlist findPlaylistsByID(long id) {
        Query q;
        
        q = em.createNamedQuery("Playlist.findByPlaylistID");
        
        q.setParameter("playlistID", id);
        
        return (Playlist) q.getSingleResult();
        
    }
    
    /**
     * Search a playlist by bame
     * @param p
     * @return
     * @throws PlaylistNameException 
     */
    public Playlist searchPlaylist(Playlist p) throws PlaylistNameException {
        
        Query q = em.createNamedQuery("Playlist.findByPlaylistName");
        q.setParameter("name", p.getName());
        
        List<Playlist> pl = q.getResultList();
        if (pl == null) {
            return null;
        } else {
            UserPlay playUser = p.getUser();
            for (Playlist play : pl) {
                UserPlay playUser2 = play.getUser();
                if (play.getName().equals(p.getName()) && playUser2.getUserID() == playUser.getUserID()) {
                    throw new PlaylistNameException();
                }
            }
            
            return null;
        }
    }
    
    /**
     * Create a playlist
     * @param p
     * @return The String that leads to a web page
     * @throws PlaylistNameException 
     */
    public String createPlaylist(Playlist p) throws PlaylistNameException {
        
        if (!p.getName().isEmpty()) {
            Playlist pl = this.searchPlaylist(p);
            if (pl == null) {
                super.create(p);
                return "myPlaylists";
            }
            throw new PlaylistNameException();
        } else {
            
            throw new PlaylistNameException();
            
        }
    }
    
    /**
     * Edit a playlist
     * @param p
     * @param m
     * @return The String that leads to a web page
     * @throws MusicInPlaylistException 
     */
    public String editPlaylist(Playlist p, Music m) throws MusicInPlaylistException {
        if (!p.getSongs().contains(m)) {
            p.getSongs().add(m);
            edit(p);
            return "myPlaylists";
        } else {
            throw new MusicInPlaylistException();
            
        }
    }
    
    /**
     * Deletes a playlist
     * @param p 
     */
    public void deletePlaylist(Playlist p) {
        
        p.getSongs().removeAll(p.getSongs());
        edit(p);
        
        remove(p);
    }
}
