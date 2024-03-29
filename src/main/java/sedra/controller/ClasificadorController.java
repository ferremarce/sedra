/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sedra.fachada.ClasificadorFacade;
import sedra.modelo.Clasificador;
import sedra.util.JSFutil;

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
    private TreeNode copyNode;

    /**
     * Creates a new instance of ClasificadorController
     */
    public ClasificadorController() {
    }

    public TreeNode getCopyNode() {
        return copyNode;
    }

    public void setCopyNode(TreeNode copyNode) {
        this.copyNode = copyNode;
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
        if (idClasificador == null) {
            return "SIN-CLASIFICADOR";
        }
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
        this.cargarTree(Boolean.FALSE);
        return "/clasificador/PlanDeArchivo";
    }

    public void cargarTree(Boolean forzar) {
        if (forzar) {
            this.root = new DefaultTreeNode("Root", null);
            for (Clasificador st : clasificadorFacade.getAllClasificadorPadres()) {
                TreeNode raiz = new DefaultTreeNode(st, root);
                this.buildTree(st, raiz);
            }
        } else {
            if (this.root == null) {
                this.cargarTree(Boolean.TRUE);
            }
        }
        if (selectedNode != null) {
            expandNode(root, selectedNode);
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
            clasificadorFacade.remove(st);
            this.cargarTree(Boolean.TRUE);
            this.expandNode(root, this.selectedNode.getParent());
            JSFutil.addMessage(this.bundle.getString("UpdateSuccess"), JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }

    }

    public String doGuardarNodo() {
        try {
            if (this.clasificador.getIdClasificador() == null) {
                clasificadorFacade.create(this.clasificador);
            } else {
                clasificadorFacade.edit(this.clasificador);
            }
            this.cargarTree(Boolean.TRUE);
            JSFutil.addMessage(this.bundle.getString("UpdateSuccess"), JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
        this.clasificador = new Clasificador();
        return "";
    }

    public List<Clasificador> listaAutocompleteClasificador(String valor) {
        return clasificadorFacade.getAllClasificador(valor);
    }

    public void onDragDrop(TreeDragDropEvent event) {
        TreeNode nodoDrag = event.getDragNode();
        TreeNode nodoDrop = event.getDropNode();

        Clasificador cDrag = (Clasificador) nodoDrag.getData();
        Clasificador cDrop = (Clasificador) nodoDrop.getData();
        cDrag.setPadre(cDrop.getIdClasificador());
        try {
            clasificadorFacade.edit(cDrag);
            JSFutil.addMessage("Carpeta movida exitosamente.", JSFutil.StatusMessage.INFORMATION);
            this.expandNode(root, nodoDrop);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
    }

    public void copyNode() {
        this.copyNode = this.selectedNode;
        JSFutil.addMessage(this.copyNode + " copiado.", JSFutil.StatusMessage.INFORMATION);
    }

    public void pasteNode() {
        try {
            Clasificador c = (Clasificador) this.selectedNode.getData();
            pegarNodo(this.copyNode, c.getIdClasificador());
            this.cargarTree(Boolean.TRUE);
            JSFutil.addMessage(this.copyNode + " pegado.", JSFutil.StatusMessage.INFORMATION);
            this.copyNode = null;

        } catch (Exception e) {
            this.commonController.doExcepcion(e);
            //JSFutil.addMessage("Error al crear la estructura...", JSFutil.StatusMessage.ERROR);
        }
    }

    public void expandNode() {
        this.expandNode(this.root, this.selectedNode);
    }

    private void expandNode(TreeNode raiz, TreeNode t) {
        for (Object n : raiz.getChildren()) {
            TreeNode tn = (TreeNode) n;
            expandNode(tn, t);
            //System.out.println("--" + n.getData());
            if (tn.getRowKey().compareTo(t.getRowKey()) == 0) {
                System.out.println("--" + tn.getData());
                this.expandToRoot(tn);
            }
        }
    }

    private void expandToRoot(TreeNode t) {
        if (t.getParent() == null) {
            t.setExpanded(true);
        } else {
            expandToRoot(t.getParent());
            t.setExpanded(true);
        }
    }

    private void pegarNodo(TreeNode t, Integer i) {
        for (Object tn : t.getChildren()) {
            TreeNode n = (TreeNode) tn;
            Clasificador c = (Clasificador) n.getData();
            Clasificador newC = new Clasificador();
            newC.setPadre(i);
            newC.setDenominacionClasificador(c.getDenominacionClasificador());
            clasificadorFacade.create(newC);
            pegarNodo(n, newC.getIdClasificador());
        }
    }
}
