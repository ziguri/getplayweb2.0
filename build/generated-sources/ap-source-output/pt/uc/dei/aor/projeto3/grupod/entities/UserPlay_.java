package pt.uc.dei.aor.projeto3.grupod.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import pt.uc.dei.aor.projeto3.grupod.entities.Music;
import pt.uc.dei.aor.projeto3.grupod.entities.Playlist;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-03-24T16:02:15")
@StaticMetamodel(UserPlay.class)
public class UserPlay_ { 

    public static volatile SingularAttribute<UserPlay, Long> userID;
    public static volatile SingularAttribute<UserPlay, String> name;
    public static volatile ListAttribute<UserPlay, Music> songs;
    public static volatile SingularAttribute<UserPlay, String> eMail;
    public static volatile ListAttribute<UserPlay, Playlist> playlists;
    public static volatile SingularAttribute<UserPlay, String> password;

}