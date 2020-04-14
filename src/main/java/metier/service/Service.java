package metier.service;

import java.util.List;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import metier.modele.*;
import metier.modele.Consultation.EtatConsultation;
import util.AstroTest;
import util.Message;
import dao.*;

public class Service {

    // ---------------------------DAO-----------------------------------
    private ClientDao clientDao = new ClientDao();
    private MediumDao mediumDao = new MediumDao();
    private EmployeDao employeDao = new EmployeDao();
    private ConsultationDao consultationDao = new ConsultationDao();
    private ProfilAstralDao profilAstralDao = new ProfilAstralDao();

    // ---------------------------CONSULTATIONS-----------------------------------

    /**
     * Gère le processus de création de consultation :<br>
     * - recherche d'un employé disponible <br>
     * - notification au client et à l'employé <br>
     * - persistence de la consultation <br>
     * 
     * @param c : le client demandant une consultation
     * @param m : le médium demandé
     * @return status : statut de la demande de consultation (<strong>true</strong>
     *         si réussie, <strong>false</strong> si échouée)
     */
    public boolean creerConsultation(Client c, Medium m) {
        boolean status;
        JpaUtil.creerContextePersistance();
        Employe employeConsultation = null;
        Consultation consultation = null;

        List<Employe> employesDisponibles = employeDao.listerEmployesDisponibleParSexe(m.getGenre());
        List<Consultation> consultationsClient = consultationDao.listerConsultationNonTermineeParClient(c);
        if (employesDisponibles.size() == 0 || consultationsClient.size() != 0) {

            JpaUtil.fermerContextePersistance();
            status = false;
        } else {
            employeConsultation = employesDisponibles.get(0);
            employeConsultation.incrNbConsultations();
            employeConsultation.setDisponibilite(false);
            Date date = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            consultation = new Consultation(date, timeFormat.format(date), c, employeConsultation, m);

            try {
                JpaUtil.ouvrirTransaction();
                consultationDao.creer(consultation);
                JpaUtil.validerTransaction();

                status = true;
            } catch (Exception e) {
                System.out.println(e);
                status = false;
            }
            JpaUtil.fermerContextePersistance();

        }

        if (status) {

            // message à l'employé
            String msgEmploye = "Bonjour " + employeConsultation.getPrenom();
            msgEmploye += ". Consultation requise pour " + c.getCivilite() + " " + c.getPrenom() + " " + c.getNom()
                    + ". ";
            msgEmploye += "Medium à incarner : " + m.getDenomination();
            Message.envoyerNotification(employeConsultation.getTelephone(), msgEmploye);
        } else {

            // message au client
            String msgClient = "Bonjour " + c.getPrenom()
                    + ". Votre demande de réservation a échoué. Merci de réessayer plus tard.";
            Message.envoyerNotification(c.getTelephone(), msgClient);

        }
        return status;
    }

    /**
     *  Cette méthode permet de débuter une consultation en lui assignant l'état EtatConsultation.ENCOURS et d'envoyer un message au client pour lui indiquer
     * @param id: l'id de la consultation à débuter
     */
    public void debutConsultation(Long id){
        JpaUtil.creerContextePersistance();
        Consultation consultation = consultationDao.chercherParId(id);
        Client c = consultation.getClient();
        if (id != null) {
            try {
                JpaUtil.ouvrirTransaction();
                consultation.setEtat(EtatConsultation.ENCOURS);
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String heure_debut = timeFormat.format(new Date());
                consultation.setHeure_debut(heure_debut);
                JpaUtil.validerTransaction();
                // message au client
                String msgClient = "Bonjour " + c.getPrenom() + ". J'ai bien reçu votre demande de consultation du "
                        + consultation.getDate() + ". Vous pouvez dès à présent me contacter au "
                        + consultation.getEmploye().getTelephone() + ". A tout de suite ! Médiumiquement vôtre, "
                        + consultation.getMedium().getDenomination();
                Message.envoyerNotification(c.getTelephone(), msgClient);
            } catch (Exception e) {
                e.printStackTrace();
                JpaUtil.annulerTransaction();
            } finally {
                JpaUtil.fermerContextePersistance();
            }
        }
        
    }

    /**
     *  Cette méthode permet de terminer une consultation en lui assignant l'état EtatConsultation.TERMINEE et d'y ajouter un commentaire
     * @param id: l'id de la consultation à terminer
     * @param commentaire: le commentaire à ajouter à cette consultation
     */
    public void finConsultation(Long id, String commentaire) {
        JpaUtil.creerContextePersistance();
        Consultation c = consultationDao.chercherParId(id);
        Employe employe = c.getEmploye();
        if (id != null) {
            try {
                JpaUtil.ouvrirTransaction();
                c.setEtat(EtatConsultation.TERMINEE);
                c.setCommentaire(commentaire);
                
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String heure_fin = timeFormat.format(new Date());
                c.setHeure_fin(heure_fin);
                employe.setDisponibilite(true);
                JpaUtil.validerTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                JpaUtil.annulerTransaction();
            } finally {
                JpaUtil.fermerContextePersistance();
            }
        }
    }

    /**
     * Permet d'obtenir la liste des consultations d'un client
     * @param c: le client dont on veut obtenir l'historique des consultations
     * @return ret : la liste des consultation
     */
    public List<Consultation> obtenirListeConsultationParClient(Client c)
    {
        List<Consultation> ret = new ArrayList<Consultation>();

        JpaUtil.creerContextePersistance();

        try {
            ret = consultationDao.listerConsultationParClient(c);
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return ret;
    }


    // ---------------------------CLIENT-----------------------------------

    /**
     * Cette méthode permet de faire persister un profil astral lié à un client.
     * 
     * @param p : l'objet profil astral à faire persister
     */
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

    /**
     * Cette méthode permet d'inscrire un client, c'est-à-dire de le faire
     * persister. La notification au client est également gérée par la méthode.
     * 
     * @param c : l'objet métier modèle associé au client qui demande à s'inscrire.
     * @return status : statut de l'inscription (<strong>true</strong> si réussie,
     *         <strong>false</strong> si échouée)
     */
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
            ProfilAstral profil = new ProfilAstral(listeProfil.get(0), listeProfil.get(1), listeProfil.get(2),
                    listeProfil.get(3));
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

    /**
     * Cette méthode permet d'authentifier un utilisateur (employé ou client). Elle
     * retourne l'id de l'utilisateur.
     * 
     * @param mail : l'adresse e-mail de l'utilisateur
     * @param mdp  : le mot de passe de l'utilisateur
     * @return clientId : l'id de l'utilisateur connecté. Vaut <strong>null</strong>
     *         si authentification échouée.
     */
    public Long authentifierUtilisateur(String mail, String mdp) {

        Long clientId = null;
        JpaUtil.creerContextePersistance();

        try {
            Client c = clientDao.chercherParMail(mail);
            // si il existe un client avec cette adresse e-mail
            if (c != null) {
                if (c.getMotDePasse().equals(mdp)) {
                    clientId = c.getId();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return clientId;

    }

    // ---------------------------MEDIUMS-----------------------------------

    /**
     * Cette méthode permet d'obtenir la liste des médiums proposés par
     * l'application, au moment où l'utilisateur cherche à demander une réservation.
     * 
     * @return ret : la liste des médiums. Si elle n'est pas trouvable, la valeur
     *         vaut <strong>null</strong>
     */
    public List<Medium> obtenirListeMediums() {
        List<Medium> ret = new ArrayList<Medium>();

        JpaUtil.creerContextePersistance();

        try {
            ret = mediumDao.listerMediums();
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return ret;
    }
    
    

   


    // ---------------------------EMPLOYES-----------------------------------

    /**
     * Cette méthode permet d'obtenir des prédictions en fonction d'un client et de 3 notes.
     * Les notes amours sante et travail peut varier <strong>EXCLUSIVEMENT</strong> de 0 à 5.
     * @param c: le client pour lequel il faut faire une prédiction
     * @param amour: la note d'amour
     * @param sante: la note de sante
     * @param travil: la note de travail
     * @return Les prédictions sous forme de texte. Si les prédictions sont indisponibles renvoit un message d'erreur.
     */
    public String obtenirPredictions(Client c, int amour, int sante, int travail)
    {

        String predictions = "";
        ProfilAstral profil = c.getProfil();
        try{

            AstroTest astroNet = new AstroTest();
            List<String> listePredictions = astroNet.getPredictions(profil.getCouleur(), profil.getAnimal(), amour, sante, travail);

            predictions += "Note prédite d'amour : " + listePredictions.get(0) + ".";
            predictions += "Note prédite de santé : " + listePredictions.get(1) + ".";
            predictions += "Note prédite de travail : " + listePredictions.get(2) + ".";
        
        }
        catch(Exception e){

            predictions += "Erreur lors de l'obtention des prédictions, veuillez réessayer plus tard.";

        }

        return predictions;

    }

    // ---------------------------STATISTIQUES-----------------------------------

    /**
     * Permet d'obtenir la liste du nombre de consultations effectuées par
     * chaque employé.
     * @return ret : liste de paires <Nom employé, Nombre de consultations enregistrées>
     */
    public List<Pair<String, Long>> obtenirCompteConsultationsParEmploye() {
        
        List<Pair<String, Long>> ret = new ArrayList<Pair<String,Long>>();
        JpaUtil.creerContextePersistance();
        try {
            List<Employe> listeEmployes = employeDao.listerEmployes();
            for(Employe emp : listeEmployes) {
                Long count = consultationDao.obtenirCompteConsultations(emp);
                Pair<String, Long> pair = new Pair<>(emp.getPrenom() + " " + emp.getNom(), count);
                ret.add(pair);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return ret;
    }

    /**
    * Permet d'obtenir le top 5 des mediums en nombre de consultations.
    * @return ret : liste des 5 premiers noms par ordre du nombre de consultations
    */
    public List<String> obtenirTop5Mediums() {
        
        List<String> ret = new ArrayList<String>();
        JpaUtil.creerContextePersistance();
        try {
            ret = consultationDao.obtenirTop5();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return ret;
    }

    /**
     * Permet d'obtenir la liste du nombre de consultations effectuées par
     * chaque medium.
     * @return ret : liste de paires <Nom medium, Nombre de consultations enregistrées>
     */

    public List<Pair<String, Long>> obtenirCompteConsultationsParMedium() {
        
        List<Pair<String, Long>> ret = new ArrayList<Pair<String,Long>>();
        JpaUtil.creerContextePersistance();
        try {
            List<Medium> listeMediums = mediumDao.listerMediums();
            for(Medium med : listeMediums) {
                Long count = consultationDao.obtenirCompteConsultations(med);
                Pair<String, Long> pair = new Pair<>(med.getDenomination(), count);
                ret.add(pair);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return ret;
    }
}
