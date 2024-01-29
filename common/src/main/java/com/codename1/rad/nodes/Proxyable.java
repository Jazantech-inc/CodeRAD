/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.rad.nodes;

/**
 * An interface implemented by nodes that can be proxied.  Proxyable node can be added to multiple branches of the node hierarchy, and, when
 * added, if it already is part of the node hierarchy elsewhere, it will add a proxy node instead, which behaves like the original node, and forwards all
 * attribute calls to the original node, but in fact, is a different node.  This is necessary to preserve the Tree characteristics of the node hierarchy.
 * 
 * You can always find the "original" node for any {@link Node} using the {@link Node#getCanonicalNode() } method.  In most cases this method just returns the 
 * calling {@link Node}, but in Proxyable cases, it may return a different node.
 * 
 * 
 * @author shannah
 */
public interface Proxyable<T> {
    public Node<T> createProxy(Node parent);
}
