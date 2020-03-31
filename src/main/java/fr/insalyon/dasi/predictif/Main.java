/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.predictif;

import metier.modele.Client;
import dao.ClientDao;
import dao.JpaUtil;

/**
 *
 * @author Florian
 */
public class Main {
    
    public static void testInscriptionClient() {
        Client c1 = new Client("6 rue des zobs", "Monsieur", "Pastissier", "Michel", "pastis@jaune.fr", "0651515151", "jaune");
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
    
    public static void main(String[] args) {
        JpaUtil.init();
        testInscriptionClient();
    }
    
    
}
