/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.predictif;

import java.util.List;
import java.util.Date;
import javafx.util.Pair;

import metier.modele.Client;
import metier.modele.Consultation;
import metier.modele.Employe;
import dao.ClientDao;
import dao.ConsultationDao;
import dao.EmployeDao;
import dao.JpaUtil;
import dao.MediumDao;
import metier.modele.Astrologue;
import metier.modele.Cartomancien;
import metier.modele.Medium;
import metier.modele.Spirite;
import metier.service.Service;

/**
 *
 * @author Florian
 */
public class Main {

    public static void testerInscriptionClient() {
        Client c1 = new Client("105 avenue Roger Salengro", "Monsieur", "Huchot", "Pierre", "pierre.huchot@insa-lyon.fr", "0651515151",
                "123456", new Date());
        Client c2 = new Client("6 rue Germain David", "Monsieur", "Aymard", "Florian", "florian.aymard@insa-lyon.fr", "0643434434",
                "654321", new Date());
        Client c3 = new Client("20 avenue Albert Einstein", "Monsieur", "Fotiadu", "Frederic", "frederic.fotiadu@insa-lyon.fr", "0659736482", "viveinsa", new Date());

        Client c4 = new Client("20 avenue Albert Einstein", "Monsieur", "Fotiadu", "Frederic", "frederic.fotiadu@insa-lyon.fr", "0659736482", "viveinsa", new Date());
        // System.out.println(c1);
        Service mesServices = new Service();
        boolean test = mesServices.inscrireClient(c1);
        test = mesServices.inscrireClient(c2);
        test = mesServices.inscrireClient(c3);
        test = mesServices.inscrireClient(c4);
        /*if (test) {
            System.out.println("Inscription réussie");
        } else {
            System.out.println("Inscription non réussie");
        }
        if (test) {
            System.out.println("Inscription réussie");
        } else {
            System.out.println("Inscription non réussie");
        }*/

    }

    public static void testerAuthentificationClient() {

        Service mesServices = new Service();

        Long pasnull = mesServices.authentifierUtilisateur("pierre.huchot@insa-lyon.fr", "123456");
        System.out.println(pasnull);

        Long doitetrenull = mesServices.authentifierUtilisateur("pierre.huchot@insa-lyon.fr", "654321");
        System.out.println(doitetrenull);

        doitetrenull = mesServices.authentifierUtilisateur("pierre.huchot@insa-lyon.com", "123456");
        System.out.println(doitetrenull);

    }

    public static void insererEmployes() {
        JpaUtil.creerContextePersistance();

        EmployeDao daoE = new EmployeDao();

        Employe[] te = new Employe[3];

        te[0] = new Employe('M', "CEURLOT", "Florian", "florian.ceurlot@hotmail.fr", "0948039309", "1234");
        te[1] = new Employe('M', "PROOTE", "Thiebaud", "thiebaud.proote@free.fr", "0847090862", "5678");
        te[2] = new Employe('F', "BREDU", "Sara", "sara.bredu@laposte.net", "0203896476", "91011");

        try {
            JpaUtil.ouvrirTransaction();
            for (Employe e : te) {
                daoE.creer(e);
            }
            JpaUtil.validerTransaction();
        } catch (Exception ex) {
            JpaUtil.annulerTransaction();
        }

        JpaUtil.fermerContextePersistance();
    }

    public static void insererMediums() {

        Medium[] tm = new Medium[3];

        tm[0] = new Astrologue("École Normale Supérieure d’Astrologie", 2006, "Serena", 'F',
                "Basée  à  Champigny-sur-Marne, Serena vousrévèlera votre  avenir  pour éclairer  votre passé.");
        tm[1] = new Cartomancien("M. Irma", 'M', "Comprenez votre entourage grâce à mes cartes! Résultats rapides");
        tm[2] = new Spirite("Boule de cristal", "Gwenaëlle", 'F',
                "Spécialiste des grandes conversations au-delà de TOUTES les frontières");
        

        JpaUtil.creerContextePersistance();

        MediumDao daoM = new MediumDao();

        try {
            JpaUtil.ouvrirTransaction();

            for (Medium m : tm) {
                daoM.creer(m);
            }

            JpaUtil.validerTransaction();
        } catch (Exception ex) {
            JpaUtil.annulerTransaction();
        }

        JpaUtil.fermerContextePersistance();
    }

    public static void testerTypeMediums() {

        JpaUtil.creerContextePersistance();
        MediumDao daoM = new MediumDao();
        List<Medium> liste = daoM.listerMediums();

        for (Medium m : liste) {
            System.out.println(m instanceof Astrologue);
        }
        JpaUtil.fermerContextePersistance();
    }

    public static void testerCreerConsultation() {

        JpaUtil.creerContextePersistance();
        MediumDao daoM = new MediumDao();
        ClientDao daoC = new ClientDao();

        Client c = daoC.chercherParMail("pierre.huchot@insa-lyon.fr");
        Medium m1 = daoM.chercherParId(new Long(1));
        Medium m2 = daoM.chercherParId(new Long(3));

        Service mesServices = new Service();
        boolean test = mesServices.creerConsultation(c, m1);
        System.out.println("création première consultation : " + test);
        test = mesServices.creerConsultation(c, m2);
        System.out.println("création deuxième consultation : " + test);
        
        JpaUtil.creerContextePersistance();
        Client c2 = daoC.chercherParMail("florian.aymard@insa-lyon.fr");
        test = mesServices.creerConsultation(c2, m2);
        System.out.println(test);

        ConsultationDao daoCo = new ConsultationDao();
        JpaUtil.creerContextePersistance();
        List<Consultation> toutes = daoCo.listerConsultations();
        JpaUtil.fermerContextePersistance();

        for (Consultation co : toutes) {

            System.out.println(co.toString());

        }

    }

    public static void testerProcessusConsultation() {
        Service mesServices = new Service();
        ConsultationDao daoCo = new ConsultationDao();
        JpaUtil.creerContextePersistance();
        List<Consultation> toutes = daoCo.listerConsultations();
        JpaUtil.fermerContextePersistance();

        for (Consultation co : toutes) {

            System.out.println(co.toString());
            mesServices.debutConsultation(co.getId());
            System.out.println("Consultation démarrée");
            
            mesServices.finConsultation(co.getId(), "stylé");
            System.out.println("Consultation terminée");

        }
    
    }
    
    public static void testerConsultationsParEmploye() {
        Service mesServices = new Service();

        List<Pair<String, Long>> liste = mesServices.obtenirCompteConsultationsParEmploye();

        for(Pair<String, Long> pair : liste) {
            System.out.println(pair.getKey() + " : " + pair.getValue());
        }
    }

    public static void testerConsultationsParMedium() {
        Service mesServices = new Service();

        List<Pair<String, Long>> liste = mesServices.obtenirCompteConsultationsParMedium();

        for(Pair<String, Long> pair : liste) {
            System.out.println(pair.getKey() + " : " + pair.getValue());
        }
    }

    public static void testerTop5Mediums() {
        Service mesServices = new Service();

        List<String> liste = mesServices.obtenirTop5Mediums();

        for(String medium : liste) {
            System.out.println(medium);   
        }
    }

    public static void main(String[] args) {
        JpaUtil.init();
        testerInscriptionClient();
        testerAuthentificationClient();
        insererEmployes();
        insererMediums();
        //testerTypeMediums();
        testerCreerConsultation();
        //testerCommentaireConsultation(id);
        testerProcessusConsultation();
        
        testerConsultationsParEmploye();
        testerConsultationsParMedium();
        testerTop5Mediums();
    }

}
