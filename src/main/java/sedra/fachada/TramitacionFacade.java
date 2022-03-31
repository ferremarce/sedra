/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.fachada;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sedra.modelo.Tramitacion;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Stateless
public class TramitacionFacade extends AbstractFacade<Tramitacion> {

    @PersistenceContext(unitName = "sedra-3.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TramitacionFacade() {
        super(Tramitacion.class);
    }

    public List<Tramitacion> getAllTramitacionPendientes(String criterio, Integer estado) {
        Query q = em.createQuery("SELECT a FROM Tramitacion a WHERE a.idRol.idRol=:xIdRol AND a.idEstado.idEstado=:xEstado AND a.idDocumento.cerrado=:xCerrado AND (UPPER(a.idDocumento.asunto) LIKE :xCriterio OR a.idDocumento.numeroExpediente=:xNroExpe) ORDER BY a.idPrioridad.orden, a.idTramitacion");
        q.setParameter("xIdRol", JSFutil.getRolSesion().getIdRol());
        q.setParameter("xCerrado", Boolean.FALSE);
        q.setParameter("xEstado", estado);
        //q.setMaxResults(100);
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
        } else {
            q.setParameter("xCriterio", "123456");
        }
        Integer nroExpe = -1;
        try {
            nroExpe = Integer.parseInt(criterio);
        } catch (NumberFormatException ex) {

        }
        q.setParameter("xNroExpe", nroExpe);
        List<Tramitacion> tr = q.getResultList();
        return tr;

    }

    public List<Tramitacion> getAllTramitacion(Integer idRol, Integer idEstado, Date fdesde, Date fhasta) {
        Query q;
        if (idEstado.compareTo(100) == 0) { //Terminado
            q = em.createQuery("SELECT a FROM Tramitacion a WHERE a.idRol.idRol=:xIdRol AND a.idEstado.idEstado=:xIdEstado AND a.fechaSalida BETWEEN :xFdesde AND :xFhasta  ORDER BY a.idTramitacion");
        } else { //Pendiente
            q = em.createQuery("SELECT a FROM Tramitacion a WHERE a.idRol.idRol=:xIdRol AND a.idEstado.idEstado=:xIdEstado AND a.fechaRegistro BETWEEN :xFdesde AND :xFhasta ORDER BY a.idTramitacion");
        }
        q.setParameter("xFdesde", fdesde);
        q.setParameter("xFhasta", fhasta);
        q.setParameter("xIdRol", idRol);
        q.setParameter("xIdEstado", idEstado);
        List<Tramitacion> tr = q.getResultList();
        return tr;
    }

    public Tramitacion findFirstTramitacion(Integer idDocumento) {
        Query q = em.createQuery("SELECT a FROM Tramitacion a WHERE a.idTramitacionPadre IS NULL AND a.idDocumento.idDocumento=:xIdDoc");
        q.setParameter("xIdDoc", idDocumento);
        List<Tramitacion> tr = q.getResultList();
        if (!tr.isEmpty()) {
            return tr.get(0);
        } else {
            return null;
        }
    }
}
