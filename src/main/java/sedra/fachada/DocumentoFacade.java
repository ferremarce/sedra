/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.fachada;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sedra.modelo.Documento;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Stateless
public class DocumentoFacade extends AbstractFacade<Documento> {

    @PersistenceContext(unitName = "sedra-3.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentoFacade() {
        super(Documento.class);
    }

    public List<Documento> getAllDocumento(String criterio) {
        Query q = em.createQuery("SELECT a FROM Documento a "
                + "WHERE (UPPER(a.asunto) LIKE :xCriterio OR a.numeroExpediente=:xNroExpe) "
                //+ "OR a.idDocumento IN (SELECT d.idDocumento.idDocumento FROM DetalleNotaSalida d WHERE UPPER(d.idNota.numeroSalida) LIKE :xCriterio OR UPPER(d.idNota.numeroStr) LIKE :xCriterio) "
                + "AND a.idUsuario.idRol.idRol=:xIdRol "
                + "ORDER BY a.idDocumento DESC");
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
        } else {
            q.setParameter("xCriterio", "123456");
        }
        q.setParameter("xIdRol", JSFutil.getRolSesion().getIdRol());
        
        Integer nroExpe = -1;
        try {
            nroExpe = Integer.parseInt(criterio);
        } catch (NumberFormatException ex) {

        }
        q.setParameter("xNroExpe", nroExpe);
        List<Documento> tr = q.getResultList();
        this.metaDatabase();
        return tr;

    }

    private String metaDatabase() {
        try {
            Connection con = this.getEntityManager().unwrap(Connection.class);
            System.out.println(con.getMetaData().getDatabaseProductName() + " " + con.getMetaData().getDatabaseProductVersion() + " " + con.getMetaData().getDriverVersion());
            return "";
        } catch (SQLException ex) {
            Logger.getLogger(DocumentoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public List<Documento> getAllDocumentoParaSeguimiento(String criterio) {
        Query q = em.createQuery("SELECT a FROM Documento a "
                + "WHERE UPPER(a.asunto) LIKE :xCriterio OR a.numeroExpediente=:xNroExpe "
                + "OR a.idDocumento IN (SELECT d.idDocumento.idDocumento FROM DetalleNotaSalida d WHERE UPPER(d.idNota.numeroSalida) LIKE :xCriterio OR UPPER(d.idNota.numeroStr) LIKE :xCriterio) "
                + "ORDER BY a.idDocumento");
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

        List<Documento> tr = q.getResultList();
        return tr;

    }

    public Documento getDocumentoByNroEntradaAnho(Integer nroExpediente, Integer anho) {
        Query q = em.createQuery("SELECT a FROM Documento a "
                + "WHERE a.numeroExpediente=:xNroExpe "
                + "AND a.anho=:xAnho");
        q.setParameter("xNroExpe", nroExpediente);
        q.setParameter("xAnho", anho);

        List<Documento> tr = q.getResultList();
        if (tr.isEmpty()) {
            return null;
        } else {
            return tr.get(0);
        }
    }

    public List<Documento> getAllDocumentoPlanArchivo(Integer clasificador) {
        Query q = em.createQuery("SELECT a FROM Documento a WHERE a.idClasificador.idClasificador=:xCriterio ORDER BY a.numeroExpediente DESC, a.fechaDocumento DESC");
        q.setParameter("xCriterio", clasificador);
        List<Documento> tr = q.getResultList();
        return tr;

    }

    public List<Documento> getAllDocumentoByExpediente(String criterio) {
        Query q = em.createQuery("SELECT a FROM Documento a "
                + "WHERE a.numeroExpediente=:xNroExpe "
                + "ORDER BY a.idDocumento");
        Integer nroExpe = -1;
        try {
            nroExpe = Integer.parseInt(criterio);
        } catch (NumberFormatException ex) {

        }
        q.setParameter("xNroExpe", nroExpe);

        List<Documento> tr = q.getResultList();
        return tr;

    }

    public List<Documento> getDocumentoByNroEntrada(String criterio) {
        Query q = em.createQuery("SELECT a FROM Documento a WHERE UPPER(a.nroEntrada) LIKE :xCriterio ORDER BY a.idDocumento");
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", criterio.toUpperCase());
        } else {
            q.setParameter("xCriterio", "123456");
        }
        List<Documento> tr = q.getResultList();

        return tr;
    }

    public List<Documento> getAllDocumento(String criterio, String campo, Date f1, Date f2) {
        String select = "SELECT d FROM Documento d ";
        String where = "";
        String consultaSQL;
        switch (campo.charAt(0)) {
            case 'd':
                where += " WHERE UPPER(CONCAT(" + campo + ",'')) LIKE :xCriterio ";
                break;
            case 'b':
                where += " WHERE d.idDocumento IN (SELECT b.idDocumento.idDocumento FROM DetalleNotaSalida b WHERE UPPER(" + campo + ") LIKE :xCriterio) ";
                break;
            default:
                where += " WHERE d.idDocumento IN (SELECT t.idDocumento.idDocumento FROM Tramitacion t WHERE UPPER(" + campo + ") LIKE :xCriterio) ";
                break;
        }
        String orderby = " AND d.fechaDocumento BETWEEN :xF1 AND :xF2 ORDER BY d.idDocumento ";
        consultaSQL = select + where + orderby;
        Query q = em.createQuery(consultaSQL);
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
        } else {
            q.setParameter("xCriterio", "123456");
        }
        q.setParameter("xF1", f1);
        q.setParameter("xF2", f2);
        List<Documento> tr = q.getResultList();
        return tr;
    }

    public List<Documento> getAllDocumentoSinNota(String criterio) {
        Query q = em.createQuery("SELECT a FROM Documento a "
                + "WHERE (UPPER(a.asunto) LIKE :xCriterio OR UPPER(a.nroEntrada) LIKE :xCriterio) "
                + "AND a.idDocumento NOT IN (SELECT DISTINCT d.idDocumento.idDocumento FROM DetalleNotaSalida d) "
                + "ORDER BY a.idDocumento");
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
        } else {
            q.setParameter("xCriterio", "123456");
        }
        List<Documento> tr = q.getResultList();
        return tr;
    }

    public Integer findNextNroExpediente() {
        Query q = em.createQuery("SELECT a FROM Documento a WHERE a.numeroExpediente IS NOT NULL AND a.anho=:xAnho ORDER BY a.numeroExpediente DESC");
        q.setMaxResults(1);
        Calendar cal = JSFutil.getCalendar();
        q.setParameter("xAnho", cal.get(Calendar.YEAR));
        List<Documento> tr = q.getResultList();
        if (!tr.isEmpty()) {
            return (tr.get(0).getNumeroExpediente() + 1);
        } else {
            return 1;
        }
    }

    public List<Documento> findAllRegistroAutomatico() {
        Query q = em.createQuery("SELECT a FROM Documento a WHERE a.idClasificador IS NULL AND a.numeroExpediente IS NOT NULL ORDER BY a.numeroExpediente DESC");
        List<Documento> tr = q.getResultList();
        return tr;
    }

    public List<Documento> findAllDocumentoAutocomplete(String query) {
        Query q = em.createQuery("SELECT a FROM Documento a WHERE a.numeroExpediente=:xNroExpe ORDER BY a.anho DESC");
        Integer nroExpe = -1;
        try {
            nroExpe = Integer.parseInt(query);
        } catch (NumberFormatException ex) {

        }
        q.setParameter("xNroExpe", nroExpe);
        List<Documento> tr = q.getResultList();
        return tr;
    }
}
