/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.util;

import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sedra3.modelo.Clasificador;

/**
 *
 * @author jmferreira
 */
public class MyTree {

    private List<Clasificador> subList2;
    private List<Clasificador> listaPadres;
    private List<Clasificador> listaCompleta;
    private TreeNode root;
    private TreeNode hijos;

    public MyTree() {
    }

    public TreeNode crearArbol(List<Clasificador> listaClasif) {
        this.listaCompleta = listaClasif;
        root = new DefaultTreeNode("Raiz", null);
        hijos = new DefaultTreeNode(null, root);
        recursive(listaClasif, 0, hijos);
        hijos.setExpanded(true);
        return root;
    }

    public void recursive(List<Clasificador> listaClasif, Integer id, TreeNode node) {
        subList2 = new ArrayList<>();
        subList2 = getRegistroByPadre(id);

        for (Clasificador k : subList2) {
            TreeNode childNode = new DefaultTreeNode(k, node);

            recursive(subList2, k.getIdClasificador(), childNode);
        }
    }

    public List<Clasificador> getRegistroByPadre(Integer i) {
        listaPadres = new ArrayList<>();
        try {
            for (Clasificador k : this.listaCompleta) {
                if (k.getPadre().compareTo(i) == 0) {
                    listaPadres.add(k);
                }
            }
        } catch (Exception e) {
            System.out.println();
        }
        return listaPadres;
    }
}
