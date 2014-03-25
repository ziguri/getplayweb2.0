package pt.uc.dei.aor.projeto3.grupod.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import pt.uc.dei.aor.projeto3.grupod.entities.Music;
import pt.uc.dei.aor.projeto3.grupod.entities.UserPlay;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-03-24T16:02:15")
@StaticMetamodel(Playlist.class)
public class Playlist_ { 

    public static volatile SingularAttribute<Playlist, UserPlay> userplay;
    public static volatile SingularAttribute<Playlist, Date> creationDate;
    public static volatile SingularAttribute<Playlist, String> name;
    public static volatile ListAttribute<Playlist, Music> songs;
    public static volatile SingularAttribute<Playlist, Long> playlistID;

}