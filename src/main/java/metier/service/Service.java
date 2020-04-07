package metier.service;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import metier.modele.*;
import dao.*;

public class Service {

    private ClientDao clientDao = new ClientDao();
    private MediumDao mediumDao = new MediumDao();
    private EmployeDao employeDao = new EmployeDao();
    private ConsultationDao consultationDao = new ConsultationDao();

    
    public boolean creerConsultation(Client c, Medium m) {

        JpaUtil.creerContextePersistance();
        List<Employe> employesDisponibles = employeDao.listerEmployesDisponibleParSexe(m.getGenre());
        if(employesDisponibles.size() == 0) {
            
            JpaUtil.fermerContextePersistance();
            return false;
        }
        else {
            Employe employeConsultation = employesDisponibles.get(0);
            employeConsultation.incrNbConsultations();
            Date date = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            Consultation consultation = new Consultation(date, timeFormat.format(date), c, employeConsultation, m);

            try {
                JpaUtil.ouvrirTransaction();
                consultationDao.creer(consultation);
                JpaUtil.validerTransaction();

            } catch(Exception e) {
                System.out.println(e);
            }
            JpaUtil.fermerContextePersistance();
            return true;
        }
    } 
}