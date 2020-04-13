package dao;

import metier.modele.ProfilAstral;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Pierre Huchot
 */
public class ProfilAstralDao {
    
    public void creer(ProfilAstral profil) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(profil);
    }
    
    public ProfilAstral chercherParId(Long profilId) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(ProfilAstral.class, profilId); // renvoie null si l'identifiant n'existe pas
    }
    
   
}
 