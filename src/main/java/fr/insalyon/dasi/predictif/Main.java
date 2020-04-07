/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.predictif;

import java.util.List;

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

    public static void testInscriptionClient() {
        Client c1 = new Client("6 rue des zobs", "Monsieur", "Pastissier", "Michel", "pastis@jaune.fr", "0651515151", "jaune", null);
        //System.out.println(c1);
        ClientDao daoC = new ClientDao();
        JpaUtil.creerContextePersistance();
        Long resultat = null;
        try {
            JpaUtil.ouvrirTransaction();
            daoC.creer(c1);
            JpaUtil.validerTransaction();
            resultat = c1.getId();
        } catch (Exception ex) {
            JpaUtil.annulerTransaction();
            resultat = null;
        } finally {
        }
        System.out.println(resultat);
        Client c_test = daoC.chercherParMail(c1.getMail());
        JpaUtil.fermerContextePersistance();
        System.out.println(c_test);
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

        tm[0] = new Astrologue("École Normale Supérieure d’Astrologie", 2006, "Serena", 'F', "Basée  à  Champigny-sur-Marne, Serena vousrévèlera votre  avenir  pour éclairer  votre passé.");
        tm[1] = new Cartomancien("Mme.Irma", 'F', "Comprenez votre entourage grâce à mes cartes! Résultats rapides");
        tm[2] = new Spirite("Boule de cristal", "Gwenaëlle", 'F', "Spécialiste des grandes conversations au-delà de TOUTES les frontières");

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

        for(Medium m : liste) {
            System.out.println(m instanceof Astrologue);
        }
        JpaUtil.fermerContextePersistance();
    }

    public static void testerCreerConsultation() {
        
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

        for(Consultation co : toutes)
        {

            System.out.println(co.toString());
            
        }

    }
    public static void main(String[] args) {
        JpaUtil.init();
        testInscriptionClient();
        insererEmployes();
        insererMediums();
        //testerTypeMediums();
        testerCreerConsultation();
    }

}
