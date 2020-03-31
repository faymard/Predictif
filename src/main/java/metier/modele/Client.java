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
 * @author Florian
 */
@Entity
public class Client extends Utilisateur implements Serializable {
    
    private String address;
    private String civilite;
    
    public Client() {
        
    }

    public Client(String address, String civilite, String nom, String prenom, String mail, String telephone, String motDePasse) {
        super(nom, prenom, mail, telephone, motDePasse);
        this.address = address;
        this.civilite = civilite;
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

    @Override
    public String toString() {
       
        return super.toString()+ "Client{" + "address=" + address + ", civilite=" + civilite + '}';
    }
    
    
}
