/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto3.grupod.entities;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author brunocosta
 */
@Entity
@Table(name = "PLAYLISTS")
@NamedQueries({
    @NamedQuery(name = "Playlist.findByAll", query = "SELECT p FROM Playlist p"),
    @NamedQuery(name = "Playlist.findByPlaylistID", query = "SELECT p FROM Playlist p WHERE p.playlistID = :playlistID"),
    @NamedQuery(name = "Playlist.findByPlaylistName", query = "SELECT p FROM Playlist p WHERE p.name = :name"),
    @NamedQuery(name = "Playlist.findByCreationDate", query = "SELECT p FROM Playlist p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "Playlist.findByUserId", query = "SELECT p FROM Playlist p WHERE p.userplay.userID = :userID"),
    @NamedQuery(name = "Playlist.findByUserIdNameAscending", query = "SELECT p FROM Playlist p WHERE p.userplay.userID = :userID ORDER BY p.name ASC"),
    @NamedQuery(name = "Playlist.findByUserIdNameDescending", query = "SELECT p FROM Playlist p WHERE p.userplay.userID = :userID ORDER BY p.name DESC"),
    @NamedQuery(name = "Playlist.findByUserIdDateAscending", query = "SELECT p FROM Playlist p WHERE p.userplay.userID = :userID ORDER BY p.creationDate ASC"),
    @NamedQuery(name = "Playlist.findByUserIdDateDescending", query = "SELECT p FROM Playlist p WHERE p.userplay.userID = :userID ORDER BY p.creationDate DESC")
   
})

public class Playlist implements Serializable, Comparable<Playlist> {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "PLAYLIST_ID")
    private Long playlistID;

    @NotNull
    @Basic(optional = false)
    @Size(max = 30, min = 1, message = "Invalid name for the Playlist")
    @Column(name = "NAME", nullable = false, length = 30)
    private String name;

    @NotNull
    @Basic(optional = false)
    @Column(name = "CREATION_DATE", nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date creationDate;

    @JoinColumn(name = "USERPLAY_USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private UserPlay userplay;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "BELONGS", joinColumns = @JoinColumn(name = "PLAYLIST_ID"),
            inverseJoinColumns = @JoinColumn(name = "SONG_ID"))
    private List<Music> songs;

    public Long getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(Long playlistID) {
        this.playlistID = playlistID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public UserPlay getUser() {
        return userplay;
    }

    public void setUser(UserPlay user) {
        this.userplay = user;
    }

    public List<Music> getSongs() {
        return songs;
    }

    public void setSongs(List<Music> songs) {
        this.songs = songs;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (playlistID != null ? playlistID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the playlistID fields are not set
        if (!(object instanceof Playlist)) {
            return false;
        }
        Playlist other = (Playlist) object;
        if ((this.playlistID == null && other.playlistID != null) || (this.playlistID != null && !this.playlistID.equals(other.playlistID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
    
    @Override
    public int compareTo(Playlist o) {
        return this.getSongs().size()-o.getSongs().size();
    }
    
    public static class OrderBySizeDesc implements Comparator<Playlist>{

        @Override
        public int compare(Playlist o1, Playlist o2) {
            return o2.getSongs().size()-o1.getSongs().size();
        }
        
    }

}
