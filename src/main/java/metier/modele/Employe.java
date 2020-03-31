/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.io.Serializable;
import javax.persistence.Entity;


/**
 *
 * @author Pierre
 */
@Entity
public class Employe extends Utilisateur implements Serializable {

    private char genre;

    public Employe() {
    }

    public Employe(char genre, String nom, String prenom, String mail, String telephone, String motDePasse) {
        super(nom, prenom, mail, telephone, motDePasse);
        this.genre = genre;
    }

    
    
    public char getGenre() {
        return genre;
    }

    public void setGenre(char genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Employe{" + "genre=" + genre + '}';
    }

}
