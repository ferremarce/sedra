/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sedra3.fachada.ClasificadorFacade;
import sedra3.modelo.Clasificador;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "ClasificadorController")
@SessionScoped
public class ClasificadorController implements Serializable {

    private static final Logger LOG = Logger.getLogger(DocumentoController.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());

    @Inject
    ClasificadorFacade clasificadorFacade;
    @Inject
    CommonController commonController;

    private Clasificador clasificador;
    private List<Clasificador> listaClasificador;
    private String criterio;

    private TreeNode selectedNode;
    private TreeNode root;

    /**
     * Creates a new instance of ClasificadorController
     */
    public ClasificadorController() {
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public ClasificadorFacade getClasificadorFacade() {
        return clasificadorFacade;
    }

    public void setClasificadorFacade(ClasificadorFacade clasificadorFacade) {
        this.clasificadorFacade = clasificadorFacade;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public Clasificador getClasificador() {
        return clasificador;
    }

    public void setClasificador(Clasificador clasificador) {
        this.clasificador = clasificador;
    }

    public String obtenerRutaClasificador(Integer idClasificador) {
        String cadena = "";
        Integer padre;
        Clasificador c = clasificadorFacade.find(idClasificador);
        while (c != null) {
            padre = getPadre(c);
            cadena = c.getDenominacionClasificador() + "/" + cadena;
            if (padre.compareTo(0) == 0) {
                return cadena;
            } else {
                c = clasificadorFacade.find(padre);
            }
        }
        return cadena;
    }

    public Integer getPadre(Clasificador c) {
        for (Clasificador n : clasificadorFacade.findAll()) {
            if (n.getIdClasificador().compareTo(c.getIdClasificador()) == 0) {
                return n.getPadre();
            }
        }
        return 0;
    }

    public String listPlanArchivoSetup() {
        this.selectedNode = null;
        this.cargarTree();
        return "/clasificador/PlanDeArchivo";
    }

    public void cargarTree() {
        this.root = new DefaultTreeNode("Root", null);
        for (Clasificador st : clasificadorFacade.getAllClasificadorPadres()) {
            TreeNode raiz = new DefaultTreeNode(st, root);
            this.buildTree(st, raiz);
        }
    }

    public void buildTree(Clasificador cla, TreeNode raiz) {
        List<Clasificador> listaHijos = clasificadorFacade.getHijos(cla.getIdClasificador());
        for (Clasificador hijo : listaHijos) {
            TreeNode nodeHijo = new DefaultTreeNode(hijo, raiz);
            buildTree(hijo, nodeHijo);
        }
    }

    public void doNuevoForm() {
        this.clasificador = new Clasificador();
        this.clasificador.setPadre(0);
    }

    public void doNuevoHijoForm() {
        this.clasificador = new Clasificador();
        System.out.println((Clasificador) this.selectedNode.getData());
        Integer idPadre = ((Clasificador) this.selectedNode.getData()).getIdClasificador();
        this.clasificador.setPadre(idPadre);
    }

    public void doEditarForm() {
        this.clasificador = (Clasificador) this.selectedNode.getData();
    }

    public void doBorrarNodo() {
        try {
            Clasificador st = (Clasificador) this.selectedNode.getData();
            System.out.println("---------------------" + st);
            clasificadorFacade.remove(st);
            JSFutil.addMessage(this.bundle.getString("UpdateSuccess"), JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
        this.cargarTree();
    }

    public String doGuardarNodo() {
        try {
            if (this.clasificador.getIdClasificador() == null) {
                clasificadorFacade.create(this.clasificador);
            } else {
                clasificadorFacade.edit(this.clasificador);
            }
            JSFutil.addMessage(this.bundle.getString("UpdateSuccess"), JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
        this.clasificador = new Clasificador();
        this.cargarTree();
        return "";
    }

    public List<Clasificador> listaAutocompleteClasificador(String valor) {
        return clasificadorFacade.getAllClasificador(valor);
    }
}
