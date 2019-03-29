package club.spreadme.domain;

import club.spreadme.database.annotation.Table;

import java.io.Serializable;
import java.util.Date;

@Table(name = "actors", primarykey = "id")
public class Actors implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String primaryname;
    private Short birthyear;
    private Short deathYear;
    private String primaryprofession;
    private String knownformovies;

    public Actors(){

    }

    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getPrimaryname(){
        return this.primaryname;
    }
    public void setPrimaryname(String primaryname){
        this.primaryname = primaryname;
    }
    public Short getBirthyear(){
        return this.birthyear;
    }
    public void setBirthyear(Short birthyear){
        this.birthyear = birthyear;
    }
    public Short getDeathYear(){
        return this.deathYear;
    }
    public void setDeathYear(Short deathYear){
        this.deathYear = deathYear;
    }
    public String getPrimaryprofession(){
        return this.primaryprofession;
    }
    public void setPrimaryprofession(String primaryprofession){
        this.primaryprofession = primaryprofession;
    }
    public String getKnownformovies(){
        return this.knownformovies;
    }
    public void setKnownformovies(String knownformovies){
        this.knownformovies = knownformovies;
    }

}