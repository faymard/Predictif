/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import metier.modele.Consultation;
import metier.modele.Client;
import metier.modele.Medium;
import metier.modele.Employe;

/**
 *
 * @author Pierre
 */
public class ConsultationDao {
    public void creer(Consultation consultation) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(consultation);
    }
    
    public Consultation chercherParId(Long consultationId) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Consultation.class, consultationId); // renvoie null si l'identifiant n'existe pas
    }
    
    public List<Consultation> listerConsultations() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Consultation> query = em.createQuery("SELECT c FROM Consultation c", Consultation.class);
        return query.getResultList();
    }
    
    public List<Consultation> listerConsultationParClient(Client client){
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Consultation> query = em.createQuery("SELECT c FROM Consultation c WHERE c.clientId = :id", Consultation.class);
        query.setParameter("id", client.getId());
        return query.getResultList();
    }

    public List<Consultation> listerConsultationParMedium(Medium m){
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Consultation> query = em.createQuery("SELECT c FROM Consultation c WHERE c.mediumId = :id", Consultation.class);
        query.setParameter("id", m.getId());
        return query.getResultList();
    }

    public List<Consultation> listerConsultationParEmploye(Employe e){
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Consultation> query = em.createQuery("SELECT c FROM Consultation c WHERE c.employeId = :id", Consultation.class);
        query.setParameter("id", e.getId());
        return query.getResultList();
    }

}
