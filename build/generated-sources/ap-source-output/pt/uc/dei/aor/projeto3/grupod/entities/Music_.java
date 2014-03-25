package pt.uc.dei.aor.projeto3.grupod.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import pt.uc.dei.aor.projeto3.grupod.entities.UserPlay;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-03-24T16:02:15")
@StaticMetamodel(Music.class)
public class Music_ { 

    public static volatile SingularAttribute<Music, UserPlay> userplay;
    public static volatile SingularAttribute<Music, Long> songID;
    public static volatile SingularAttribute<Music, String> title;
    public static volatile SingularAttribute<Music, String> album;
    public static volatile SingularAttribute<Music, String> path;
    public static volatile SingularAttribute<Music, String> artist;
    public static volatile SingularAttribute<Music, Integer> releaseYEAR;

}