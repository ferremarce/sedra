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
import sedra.util.Codigo;
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
        Query q = em.createQuery("SELECT a FROM Tramitacion a WHERE a.idRol.idRol=:xIdRol AND a.idEstado.idEstado=:xEstado "
                + "AND (UPPER(a.idDocumento.asunto) LIKE :xCriterio OR CONCAT(a.idDocumento.numeroExpediente,'-',a.idDocumento.anho) LIKE :xCriterio) ORDER BY a.idPrioridad.orden, a.idTramitacion DESC");
        q.setParameter("xIdRol", JSFutil.getRolSesion().getIdRol());
        //q.setParameter("xCerrado", Boolean.FALSE);
        q.setParameter("xEstado", estado);
        //q.setMaxResults(100);
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
        } else {
            q.setParameter("xCriterio", "123456");
        }
        List<Tramitacion> tr = q.getResultList();
        return tr;

    }

    public List<Tramitacion> getAllTramitacionPendientesNoLeidos(String criterio, Integer estado) {
        Query q = em.createQuery("SELECT a FROM Tramitacion a WHERE a.leido=:xLeido AND a.idRol.idRol=:xIdRol AND a.idEstado.idEstado=:xEstado "
                + "AND a.idDocumento.cerrado=:xCerrado AND (UPPER(a.idDocumento.asunto) LIKE :xCriterio OR CONCAT(a.idDocumento.numeroExpediente,'-',a.idDocumento.anho) LIKE :xCriterio) ORDER BY a.idPrioridad.orden, a.idTramitacion DESC");
        q.setParameter("xIdRol", JSFutil.getRolSesion().getIdRol());
        q.setParameter("xCerrado", Boolean.FALSE);
        q.setParameter("xLeido", Boolean.FALSE);
        q.setParameter("xEstado", estado);
        //q.setMaxResults(100);
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
        } else {
            q.setParameter("xCriterio", "123456");
        }
        List<Tramitacion> tr = q.getResultList();
        return tr;

    }

    public List<Tramitacion> getAllTramitacion(Integer idRol, Integer idEstado, Date fdesde, Date fhasta) {
        String where;
        if (idRol != null) {
            where = "a.idRol.idRol=:xIdRol";
        } else {
            where = "a.idRol IS NOT NULL";
        }
        String fechaConsulta = "a.fechaDerivacion";
        switch (idEstado) {
            case 1://Pendiente
                break;
            case 2: //Rechazado
                fechaConsulta = "a.fechaSalida";
                break;
            case 3://Recibido
                fechaConsulta = "a.fechaConfirmacion";
                break;
            case 4://Derivado
                where = "a.idTramitacionPadre.idRol.idRol=:xIdRol"; 
                break;
            case 5://Ingresado
                break;
            case 100://Archivado
                fechaConsulta = "a.fechaSalida";
                break;
        }
        Query q = em.createQuery("SELECT a FROM Tramitacion a WHERE " + where + " AND a.idEstado.idEstado=:xIdEstado AND " + fechaConsulta + " BETWEEN :xFdesde AND :xFhasta  ORDER BY a.idTramitacion");
        q.setParameter("xFdesde", fdesde);
        q.setParameter("xFhasta", fhasta);
        if (idRol != null) {
            q.setParameter("xIdRol", idRol);
        }
        q.setParameter("xIdEstado", idEstado);
        List<Tramitacion> tr = q.getResultList();
        return tr;
    }

    public Tramitacion findFirstTramitacion(Integer idDocumento) {
        Query q = em.createQuery("SELECT a FROM Tramitacion a WHERE a.idTramitacionPadre IS NULL AND a.idDocumento.idDocumento=:xIdDoc ORDER BY a.idTramitacion");
        q.setParameter("xIdDoc", idDocumento);
        List<Tramitacion> tr = q.getResultList();
        if (!tr.isEmpty()) {
            return tr.get(0);
        } else {
            return null;
        }
    }
}
