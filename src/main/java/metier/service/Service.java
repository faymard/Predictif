package metier.service;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import metier.modele.*;
import util.AstroTest;
import util.Message;
import dao.*;

public class Service {

    private ClientDao clientDao = new ClientDao();
    private MediumDao mediumDao = new MediumDao();
    private EmployeDao employeDao = new EmployeDao();
    private ConsultationDao consultationDao = new ConsultationDao();
    private ProfilAstralDao profilAstralDao = new ProfilAstralDao();

    public boolean creerConsultation(Client c, Medium m) {

        JpaUtil.creerContextePersistance();
        List<Employe> employesDisponibles = employeDao.listerEmployesDisponibleParSexe(m.getGenre());
        if (employesDisponibles.size() == 0) {

            JpaUtil.fermerContextePersistance();
            return false;
        } else {
            Employe employeConsultation = employesDisponibles.get(0);
            employeConsultation.incrNbConsultations();
            Date date = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            Consultation consultation = new Consultation(date, timeFormat.format(date), c, employeConsultation, m);

            try {
                JpaUtil.ouvrirTransaction();
                consultationDao.creer(consultation);
                JpaUtil.validerTransaction();
                
                // message au client
                String msgClient = "Bonjour " + c.getPrenom() + ". J'ai bien reçu votre demande de consultation du "
                + consultation.getDate() + ". Vous pouvez dès à présent me contacter au " + employeConsultation.getTelephone() + ". A tout de suite ! Médiumiquement vôtre, " + m.getDenomination();
                Message.envoyerNotification(c.getTelephone(), msgClient);
                
                // message à l'employé
                String msgEmploye = "Bonjour " + employeConsultation.getPrenom();
                msgEmploye += ". Consultation requise pour " + c.getCivilite() + " " + c.getPrenom() + " " +c.getNom() + ". ";
                msgEmploye += "Medium à incarner : " + m.getDenomination();
                Message.envoyerNotification(employeConsultation.getTelephone(), msgEmploye);

            } catch (Exception e) {
                System.out.println(e);
            }
            JpaUtil.fermerContextePersistance();
            return true;
        }
    }

    public void ajouterCommentaireSurConsultation(Long id, String commentaire) {
        JpaUtil.creerContextePersistance();
        Consultation c = consultationDao.chercherParId(id);
        if(id != null) {
            try {
                JpaUtil.ouvrirTransaction();
                c.setCommentaire(commentaire);
                //consultationDao.creer(c);
                JpaUtil.validerTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                JpaUtil.annulerTransaction();
            } finally {
                JpaUtil.fermerContextePersistance();
            }
        }
    }

    private void persisterProfil(ProfilAstral p) {
        try {
            JpaUtil.ouvrirTransaction();
            profilAstralDao.creer(p);
            JpaUtil.validerTransaction();
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace();
        }
    }


    public boolean inscrireClient(Client c) {
        boolean status = false;
        String message = "Bonjour " + c.getPrenom() + ", ";
        String objet = "";
        JpaUtil.creerContextePersistance();
        try {
            JpaUtil.ouvrirTransaction();
            clientDao.creer(c);
            JpaUtil.validerTransaction();
            
            c = clientDao.chercherParMail(c.getMail());

            AstroTest astroNet = new AstroTest();
            List<String> listeProfil = astroNet.getProfil(c.getPrenom(), c.getDateDeNaissance());
            ProfilAstral profil = new ProfilAstral(listeProfil.get(0), listeProfil.get(1), listeProfil.get(2), listeProfil.get(3));
            persisterProfil(profil);

            JpaUtil.ouvrirTransaction();
            c.setProfil(profil);
            JpaUtil.validerTransaction();
            status = true;

            message += "nous vous confirmons votre inscription au service PREDICT’IF.Rendez-vous"  
                + " vite  sur  notre  site  pour  consulter  votre profil  astrologique  et  profiter  des  dons incroyables de nos mediums";
            objet = "Bienvenue chez PREDICT'IF";
        } catch (Exception e) {
            JpaUtil.annulerTransaction();

            status = false;
            message += "votre inscription au service PREDICT’IF a malencontreusement échoué... Merci de recommencer ultérieurement.";
            objet = "Echec de l'inscription chez PREDICT'IF";
        } finally {
            JpaUtil.fermerContextePersistance();
            // notifier le client du statut de son inscription
            Message.envoyerMail("admin@predictif.fr", c.getMail(), objet, message);

        }

        return status;
    }

    public Long authentifierUtilisateur(String mail, String mdp)
    {
        
        Long clientId = null;
        JpaUtil.creerContextePersistance();

        try {
            Client c = clientDao.chercherParMail(mail);
            // si il existe un client avec cette adresse e-mail
            if(c != null) {
                if(c.getMotDePasse().equals(mdp)){
                    clientId = c.getId();
                }
            }

        } catch(Exception e) {
            e.printStackTrace();

        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return clientId;

    }
}