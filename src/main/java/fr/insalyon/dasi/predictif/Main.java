/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.predictif;

import java.util.List;
import java.util.Date;

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
        Client c1 = new Client("6 rue des zobs", "Monsieur", "Pastissier", "Michel", "pastis@jaune.fr", "0651515151",
                "jaune", new Date());
        Client c2 = new Client("6 rue des zobs", "Monsieur", "Pastissier", "Patrick", "pastis@jaune.fr", "0643434434",
                "jaune", new Date());
        // System.out.println(c1);
        Service mesServices = new Service();
        boolean test = mesServices.inscrireClient(c1);
        test = mesServices.inscrireClient(c2);
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

        Long pasnull = mesServices.authentifierUtilisateur("pastis@jaune.fr", "jaune");
        System.out.println(pasnull);

        Long doitetrenull = mesServices.authentifierUtilisateur("pastis@jaune.fr", "pas jaune");
        System.out.println(doitetrenull);

        doitetrenull = mesServices.authentifierUtilisateur("pastis@pasjaune.fr", "pas jaune");
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
        tm[1] = new Cartomancien("Mme.Irma", 'F', "Comprenez votre entourage grâce à mes cartes! Résultats rapides");
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

    public static Long testerCreerConsultation() {

        JpaUtil.creerContextePersistance();
        MediumDao daoM = new MediumDao();
        ClientDao daoC = new ClientDao();

        Client c = daoC.chercherParMail("pastis@jaune.fr");
        Medium m = daoM.chercherParId(new Long(1));

        Service mesServices = new Service();
        boolean test = mesServices.creerConsultation(c, m);
        System.out.println(test);

        ConsultationDao daoCo = new ConsultationDao();
        JpaUtil.creerContextePersistance();
        List<Consultation> toutes = daoCo.listerConsultations();
        JpaUtil.fermerContextePersistance();

        for (Consultation co : toutes) {

            System.out.println(co.toString());

        }

        return toutes.get(0).getId();

    }

    public static void testerCommentaireConsultation(Long id) {
        Service mesServices = new Service();
        mesServices.ajouterCommentaireSurConsultation(id, "sah quel plaisir");
    }

    public static void main(String[] args) {
        JpaUtil.init();
        testerInscriptionClient();
        //testerAuthentificationClient();
        //insererEmployes();
        //insererMediums();
        // testerTypeMediums();
        //Long id = testerCreerConsultation();
        //testerCommentaireConsultation(id);
    }

}
