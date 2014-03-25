/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto3.grupod.entities;

import java.io.Serializable;
import static java.util.Calendar.YEAR;
import java.util.GregorianCalendar;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import pt.uc.dei.aor.projeto3.grupod.ejb.MusicEJB;

/**
 *
 * @author UserPlay
 */
@Entity
@Table(name = "MUSIC")
@NamedQueries({
    @NamedQuery(name = "Music.findAll", query = "SELECT m FROM Music m"),
    @NamedQuery(name = "Music.findBySONG_Id", query = "SELECT m FROM Music m WHERE m.songID = :songID"),
    @NamedQuery(name = "Music.findByTitle", query = "SELECT m FROM Music m WHERE m.title = :title"),
    @NamedQuery(name = "Music.findByArtist", query = "SELECT m FROM Music m WHERE m.artist = :artist"),
    @NamedQuery(name = "Music.findByAlbum", query = "SELECT m FROM Music m WHERE m.album = :album"),
    @NamedQuery(name = "Music.findByReleaseYear", query = "SELECT m FROM Music m WHERE m.releaseYEAR = :releaseYEAR"),
    @NamedQuery(name = "Music.findByPath", query = "SELECT m FROM Music m WHERE m.releaseYEAR = :releaseYEAR"),
    @NamedQuery(name = "Music.findByUserID", query = "SELECT m FROM Music m WHERE m.userplay.userID = :userID"),
    @NamedQuery(name = "Music.musicsALLList", query = "SELECT m.title,m.artist,m.album,m.releaseYEAR FROM Music m"),
    @NamedQuery(name = "Music.musicsALLListOfUser", query = "SELECT m FROM Music m WHERE m.userplay.userID = :userID")
    })
public class Music implements Serializable {
    
    
    
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SONG_ID")
    @Basic(optional = false)
    private Long songID;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100, message = "Title not valid")
    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @NotNull
    @Size(min = 1, max = 50, message = "Artist not valid")
    @Column(name = "ARTIST", nullable = false, length = 50)
    private String artist;

    @NotNull
    @Size(min = 1, max = 50, message = "Album not valid")
    @Column(name = "ALBUM", nullable = false, length = 50)
    private String album;

   
    @Min(value=1900, message = "Year not valid")
    @Digits(integer = 4, fraction=0, message = "Year not valid")
    @Basic(optional = false)
    @NotNull(message = "Year not valid")
    @Column(name = "RELEASE_YEAR", nullable = false)
    private int releaseYEAR;

    //@Basic(optional = false)
    //@NotNull
    @Size(min = 1, message = "Path not valid")
    @Column(name = "MUSIC_PATH", nullable = false)
    private String path;


    @JoinColumn(name = "USER_ID")
    @ManyToOne
    private UserPlay userplay;

    

    public Long getSongID() {
        return songID;
    }

    public void setSongID(Long songID) {
        this.songID = songID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getReleaseYEAR() {
        return releaseYEAR;
    }

    public void setReleaseYEAR(int releaseYEAR) {
        this.releaseYEAR = releaseYEAR;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UserPlay getUser() {
        return userplay;
    }

    public void setUser(UserPlay user) {
        this.userplay = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (songID != null ? songID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the songID fields are not set
        if (!(object instanceof Music)) {
            return false;
        }
        Music other = (Music) object;
        if ((this.songID == null && other.songID != null) || (this.songID != null && !this.songID.equals(other.songID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "title=" + title + " "+ ", artist=" + artist + ", album="+album+", releaseYear="+releaseYEAR;
    }
    

}
