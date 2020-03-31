/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
/**
 *
 * @author Florian
 */
@Entity
public class Client extends Utilisateur implements Serializable {
    
    private String address;
    private String civilite;
    private Date dateDeNaissance;
    
    @OneToOne
    private ProfilAstral profil = null;
    
    public Client() {
        
    }

    public Client(String address, String civilite, String nom, String prenom, String mail, String telephone, String motDePasse, Date dateDeNaissance) {
        super(nom, prenom, mail, telephone, motDePasse);
        this.address = address;
        this.civilite = civilite;
        this.dateDeNaissance = dateDeNaissance;
    }
    
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public ProfilAstral getProfil() {
        return profil;
    }

    public void setProfil(ProfilAstral profil) {
        this.profil = profil;
    }

    public Date getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(Date dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }
    
    

    @Override
    public String toString() {
       
        return super.toString()+ "Client{" + "address=" + address + ", civilite=" + civilite + ", dateDeNaissance=" + dateDeNaissance + '}';
    }
    
    
}
