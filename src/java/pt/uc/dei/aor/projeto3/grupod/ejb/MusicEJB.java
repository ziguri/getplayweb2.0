
package pt.uc.dei.aor.projeto3.grupod.ejb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static java.util.Calendar.YEAR;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.Part;
import pt.uc.dei.aor.projeto3.grupod.entities.Music;
import pt.uc.dei.aor.projeto3.grupod.entities.Playlist;
import pt.uc.dei.aor.projeto3.grupod.entities.UserPlay;
import pt.uc.dei.aor.projeto3.grupod.exceptions.MusicInPlaylistException;
import pt.uc.dei.aor.projeto3.grupod.exceptions.NotMp3Exception;
import pt.uc.dei.aor.projeto3.grupod.exceptions.ReleaseYearInvalidException;
import pt.uc.dei.aor.projeto3.grupod.facades.MusicFacade;
import pt.uc.dei.aor.projeto3.grupod.facades.PlaylistFacade;

@Stateless
public class MusicEJB {

    @EJB
    private MusicFacade musicFacade;

    private String path;
    private File destination;
    private Part file;
    private String yearInvalid;
    private String isMP3ErrorMessage;
    
    @EJB
    private PlaylistFacade playlistFacade;
    
    public MusicFacade getMusicFacade() {
        return musicFacade;
    }

    
    public void setMusicFacade(MusicFacade musicFacade) {
        this.musicFacade = musicFacade;
    }

    
    public String getPath() {
        return path;
    }

    
    public void setPath(String path) {
        this.path = path;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
    
    
    public File getDestination() {
        return destination;
    }

    
    public void setDestination(File destination) {
        this.destination = destination;
    }

    /**
     *Creates a new music
     * @param m
     * @return A Music
     * @throws pt.uc.dei.aor.projeto3.grupod.exceptions.ReleaseYearInvalidException
     * @throws pt.uc.dei.aor.projeto3.grupod.exceptions.NotMp3Exception
     * 
     */
    public Music newMusic(Music m) throws ReleaseYearInvalidException, NotMp3Exception {

        try {
            validateYear(m.getReleaseYEAR()+"");
            isMP3();

        } catch (ReleaseYearInvalidException e) {
            throw new ReleaseYearInvalidException();
            
            
        } catch (NotMp3Exception e) {
            throw new NotMp3Exception();
           
        }
        musicFacade.create(m);
        
        return m;

    }
    
    /**
     * method to validate the part file uploaded
     *
     * @throws NotMp3Exception
     *
     */
    public void isMP3() throws NotMp3Exception {
        if (!"audio/mpeg".equals(file.getContentType()) && !"audio/mp3".equals(file.getContentType())) {
            Exception ex = new NotMp3Exception();

            throw new NotMp3Exception();
        }

    }
    

    /**
     * method to get the music destination folder
     * @param u
     * @return the path of the folder where the the music is saved
     */
    public static String getMusicFolder(UserPlay u) {
        String currentDir = new File(".").getAbsolutePath();
        currentDir = currentDir.substring(0, currentDir.length() - 1);
        System.out.println(currentDir);
        return currentDir + "music/" + u.geteMail()+"/";
    }

    /**
     * method to upload the music file to the server
     *
     * @param file
     * @param m
     * @param u
     * @return the path of the uploaded file
     */
    public String upload(Part file, Music m, UserPlay u) {

        String fileName = m.getTitle() + "_" + m.getArtist() + "_" + 
                m.getAlbum() + "_" + m.getUser().getUserID() + ".mp3";

        destination = new File(getMusicFolder(u) + fileName);

        try (InputStream inputStream = file.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(destination)) {

            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while (true) {
                bytesRead = inputStream.read(buffer);
                if (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                } else {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MusicEJB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        path = fileName;
      

        return path;

    }

    /**
     * method to validate the releaseYear of the music the user is uploading
     *
     * @param year
     * @return the integer year
     * @throws ReleaseYearInvalidException
     */
    public int validateYear(String year) throws ReleaseYearInvalidException {
        if (year.isEmpty()) {
            throw new ReleaseYearInvalidException();
        }
        int y = -1;
        try {
            y = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            throw new ReleaseYearInvalidException(e);

        }

        int thisYear = new GregorianCalendar().get(YEAR);
        if (y < 1900 || y > thisYear) {
            throw new ReleaseYearInvalidException();
        }

        return y;

    }
    
    
    /**
     * method to get all the music from one user
     * @param user
     * @return The list of music
     */

    public List<Music> myMusicsList(UserPlay user) {

        return musicFacade.findAllMusicByUserID(user);

    }
    
    /**
     * method to get all the music
     * @return The list of music
     */
    
    public List<Music> allMusicsList(){
        return musicFacade.findAll();
    }
    
    /**
     * method to delete one music
     * @param music
     * @param user
     * @return The String to a xhtml page
     * @throws Exception 
     */
    
    public String deleteSong(Music music, UserPlay user) throws Exception {
        musicFacade.removeMusic(music, user);
        return "myMusics";
    }
    
    /**
     * method to edit one music
     * @param music
     * @return The String to a xhtml page
     * @throws ReleaseYearInvalidException 
     */
    
    public String editMusic(Music music) throws ReleaseYearInvalidException{
        
        try {
            validateYear(music.getReleaseYEAR()+"");
            
        } catch (ReleaseYearInvalidException e) {
            throw new ReleaseYearInvalidException();
          
        }
        this.musicFacade.edit(music);
        return "myMusics";
    }
    
    /**
     * method to insert a music in a playlist 
     * @param m
     * @param name
     * @param user
     * @throws MusicInPlaylistException 
     */
    
    public void copyToplaylist(Music m, String name, 
            UserPlay user) throws MusicInPlaylistException {
        
        UserPlay u = user;
  
        Playlist p;
        
        List<Playlist> userListPlaylist = playlistFacade.findAllPlaylistsByUserID(u);
        
        for (int i = 0; i < userListPlaylist.size(); i++) {
            p = userListPlaylist.get(i);
            if (p.getName().equals(name)&&p.getUser().getUserID()==u.getUserID()) {
                try {
                    playlistFacade.editPlaylist(p, m);
                   
                    
                } catch (MusicInPlaylistException ex) {
                    Logger.getLogger(MusicEJB.class.getName()).log(Level.SEVERE, null, ex);
                 
                    throw new MusicInPlaylistException();
                    
                }
            }
        }
    }
    
    /**
     * Search music by title and by artist
     * @param search
     * @return A list of music
     */
    
    public List<Music> searchMusic(String search){
        
        List<String> tags = new ArrayList<>();
        
        StringTokenizer words = new StringTokenizer(search, " ");
        while(words.hasMoreTokens()){
            tags.add(words.nextToken());
        }
        
        List<Music> allMusic = musicFacade.findAll();
        
        
        List<Music> result = new ArrayList<>();
        
        for (int j = 0; j < tags.size(); j++) {
             for (int k = 0; k < allMusic.size(); k++) {
                 
                 String title = allMusic.get(k).getTitle().toLowerCase();
                 String artist = allMusic.get(k).getArtist().toLowerCase();
                 
                 
                 if((title.contains(tags.get(j).toLowerCase()) || 
                          artist.contains(tags.get(j).toLowerCase())) &&
                          !result.contains(allMusic.get(k))){
                      result.add(allMusic.get(k));
                  }
             }
        }
        
        return  result;
    }
    
}
