/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.aditional;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sedra.modelo.Tramitacion;

/**
 *
 * @author jmferreira
 */
public class TreeTramitacion {

    public TreeNode init(Tramitacion root) {
        if (root != null) {
            return newNodeWithChildren(root, null);
        } else {
            return null;
        }
    }

    /**
     * recursive function that returns a new node with its children
     *
     * @param ttParent
     * @param parent
     * @return
     */
    private TreeNode newNodeWithChildren(Tramitacion ttParent, TreeNode parent) {
        TreeNode newNode = new DefaultTreeNode(ttParent, parent);
        for (Tramitacion tt : ttParent.getTramitacionList()) {
            TreeNode newNode2 = newNodeWithChildren(tt, newNode);
        }
        return newNode;
    }

    public void expandedAll(final TreeNode node, final boolean expanded) {
        for (final Object hijo : node.getChildren()) {
            TreeNode child = (TreeNode) hijo;
            expandedAll(child, expanded);
        }
        node.setExpanded(expanded);
    }

}
