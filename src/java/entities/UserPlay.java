/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author brunocosta
 */
@Entity
@Table(name = "USERS")
@NamedQueries({
    @NamedQuery(name = "UserPlay.findByAll", query = "SELECT u FROM UserPlay u"),
    @NamedQuery(name = "UserPlay.findByUSERID", query = "SELECT u FROM UserPlay u WHERE u.userID = :userID"),
    @NamedQuery(name = "UserPlay.findByPlaylistName", query = "SELECT u FROM UserPlay u WHERE u.eMail = :email"),
})
public class UserPlay implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true,name = "USER_ID", nullable = false)
    private Long userID;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = 50, message = "The title must have less than 100 characters")
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = 50, message = "The title must have less than 100 characters")
    @Column(name = "E_MAIL", nullable = false, length = 100) 
    @Email
    private String eMail;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "PASSWORD", nullable = false, length = 20)
    @Size(max = 20, message = "The title must have less than 20 characters")
    private String password;
    
    
    
    @OneToMany(mappedBy = "userplay")
    private List<Music> songs;
    
    
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userplay")
    private List<Playlist> playlists;

     
    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userID != null ? userID.hashCode() : 0);
        return hash;
    }
    
    public List<Music> getSongs() {
        return songs;
    }

    public void setSongs(List<Music> songs) {
        this.songs = songs;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the userID fields are not set
        if (!(object instanceof UserPlay)) {
            return false;
        }
        UserPlay other = (UserPlay) object;
        if ((this.userID == null && other.userID != null) || (this.userID != null && !this.userID.equals(other.userID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.User[ id=" + userID + " ]";
    }
    
}
