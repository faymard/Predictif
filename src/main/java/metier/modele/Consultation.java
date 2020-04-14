package metier.modele;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;




@Entity
public class Consultation implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentaire;

    @Temporal(TemporalType.DATE)
    private Date date;
    private String heure_demande;
    private String heure_debut;
    private String heure_fin;

    @OneToOne
    private Client client;
    @OneToOne
    private Employe employe;
    @OneToOne
    private Medium medium;
  
    public enum EtatConsultation{

        OUVERTE,
        ENCOURS,
        TERMINEE
    
    }

    private EtatConsultation etat;

    

    public Consultation(Date date, String heure_demande,
            Client client, Employe employe, Medium medium) {
        this.date = date;
        this.heure_demande = heure_demande;
        this.client = client;
        this.employe = employe;
        this.medium = medium;
        this.etat = EtatConsultation.OUVERTE;
    }

    public Consultation() {

    }

    

    public Long getId() {
        return id;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHeure_demande() {
        return heure_demande;
    }

    public void setHeure_demande(String heure_demande) {
        this.heure_demande = heure_demande;
    }

    public String getHeure_debut() {
        return heure_debut;
    }

    public void setHeure_debut(String heure_debut) {
        this.heure_debut = heure_debut;
    }

    public String getHeure_fin() {
        return heure_fin;
    }

    public void setHeure_fin(String heure_fin) {
        this.heure_fin = heure_fin;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public EtatConsultation getEtat() {
        return etat;
    }

    public void setEtat(EtatConsultation etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Consultation [client=" + client + ", commentaire=" + commentaire + ", date=" + date + ", employe="
                + employe + ", etat=" + etat + ", heure_debut=" + heure_debut + ", heure_demande=" + heure_demande
                + ", heure_fin=" + heure_fin + ", id=" + id + ", medium=" + medium + "]";
    }
    

    

}

