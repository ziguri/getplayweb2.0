package pt.uc.dei.aor.projeto3.grupod.facades;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pt.uc.dei.aor.projeto3.grupod.entities.Music;
import pt.uc.dei.aor.projeto3.grupod.entities.Playlist;
import pt.uc.dei.aor.projeto3.grupod.entities.UserPlay;

@Stateless
public class MusicFacade extends AbstractFacade<Music> {

    @PersistenceContext(unitName = "GetPlayWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @EJB
    private PlaylistFacade playlistFacade;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public MusicFacade() {
        super(Music.class);
    }

    /**
     * Finds a music by the ID
     *
     * @param songID
     * @return A music
     */
    public Music findMusicBySongID(long songID) {
        Query q = em.createNamedQuery("Music.findBySONG_Id");
        q.setParameter("songID", songID);

        return (Music) q.getSingleResult();

    }

    /**
     * Finds a music by title
     *
     * @param title
     * @return a music
     */
    public List<Music> findMusicByTitle(String title) {
        Query q = em.createNamedQuery("Music.findByTitle");
        q.setParameter("title", title);
        try {
            List<Music> musics = q.getResultList();
            return musics;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a music by artist
     *
     * @param artist
     * @return A music
     */
    public List<Music> findMusicByArtist(String artist) {
        Query q = em.createNamedQuery("Music.findByArtist");
        q.setParameter("artist", artist);
        try {
            List<Music> musics = q.getResultList();
            return musics;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds the music of one user
     *
     * @param userplay
     * @return A list of music
     */
    public List<Music> findAllMusicByUserID(UserPlay userplay) {
        Query q = em.createNamedQuery("Music.musicsALLListOfUser");
        q.setParameter("userID", userplay.getUserID());
        try {
            List<Music> musics = q.getResultList();
            return musics;
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * Deletes a music
     *
     * @param m
     * @param u
     */
    public void removeMusic(Music m, UserPlay u) {
        List<Playlist> resultsPlaylists = playlistFacade.findAll();
        if (m.getUser().getUserID() == u.getUserID()) {
            for (Playlist p : resultsPlaylists) {
                if (p.getSongs().contains(m)) {
                    p.getSongs().remove(m);
                    playlistFacade.edit(p);
                }
            }
            
            File f = new File(getMusicFolder(u)+m.getPath());
            f.delete();
            remove(m);

        }
    }

    public static String getMusicFolder(UserPlay u) {
        String currentDir = new File(".").getAbsolutePath();
        currentDir = currentDir.substring(0, currentDir.length() - 1);
        System.out.println(currentDir);
        return currentDir + "music/" + u.geteMail() + "/";
    }
}
