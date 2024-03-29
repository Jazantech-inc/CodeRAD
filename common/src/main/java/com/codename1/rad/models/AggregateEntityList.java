/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.rad.models;

/**
 * An entity list that automatically adds its items to its aggregate root.
 * @author shannah
 */
public class AggregateEntityList<T extends Entity> extends EntityList<T> implements Iterable<T> {
    
    public AggregateEntityList(int maxLen) {
        super(maxLen);
    }
    
    public AggregateEntityList() {
        this(-1);
    }

    @Override
    protected T beforeAdd(T link) {
        if (link.getEntity().getAggregate() != getAggregate()) {
            link.getEntity().getAggregate().remove(link);
            getAggregate().add(link);
        }
        return link;
    }

    @Override
    protected T beforeRemove(T link) {
        getAggregate().remove(link);
        return link;
    }
    
    
    
    
    
}
