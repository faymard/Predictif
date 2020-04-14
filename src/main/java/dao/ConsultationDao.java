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
import metier.modele.Consultation.EtatConsultation;
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
        TypedQuery<Consultation> query = em.createQuery("SELECT c FROM Consultation c WHERE c.client = :cli", Consultation.class);
        query.setParameter("cli", client);
        return query.getResultList();
    }

    public List<Consultation> listerConsultationNonTermineeParClient(Client client){
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Consultation> query = em.createQuery("SELECT c FROM Consultation c WHERE c.client = :cli AND c.etat = :state", Consultation.class);
        query.setParameter("cli", client);
        query.setParameter("state", EtatConsultation.OUVERTE);
        return query.getResultList();
    }

    public List<Consultation> listerConsultationParMedium(Medium m){
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Consultation> query = em.createQuery("SELECT c FROM Consultation c WHERE c.medium = :m", Consultation.class);
        query.setParameter("m", m);
        return query.getResultList();
    }

    public List<Consultation> listerConsultationParEmploye(Employe e){
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Consultation> query = em.createQuery("SELECT c FROM Consultation c WHERE c.employe = :e", Consultation.class);
        query.setParameter("e", e);
        return query.getResultList();
    }

    public Long obtenirCompteConsultations(Employe emp) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Long> query = em.createQuery("SELECT count(c) FROM Consultation c WHERE c.employe = :e", Long.class);
        query.setParameter("e", emp);
        Long ret = query.getSingleResult();
        return ret;
    }

    public Long obtenirCompteConsultations(Medium med) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Long> query = em.createQuery("SELECT count(c) FROM Consultation c WHERE c.medium = :m", Long.class);
        query.setParameter("m", med);
        Long ret = query.getSingleResult();
        return ret;
    }

    public List<String> obtenirTop5() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<String> query = em.createQuery("SELECT c.medium.denomination FROM Consultation c GROUP BY c.medium ORDER BY count(c) ASC", String.class);
        return query.setMaxResults(5).getResultList();
    }

}
