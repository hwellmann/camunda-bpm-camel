package org.camunda.bpm.camel.cdi.itest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;

import org.apache.camel.spi.Registry;
import org.apache.camel.util.ObjectHelper;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CdiBeanRegistry implements Registry {

    private static final Logger LOG = LoggerFactory.getLogger(CdiBeanRegistry.class);

    @Override
    public Object lookupByName(final String name) {
        ObjectHelper.notEmpty(name, "name");
        LOG.trace("Looking up bean with name {}", name);

        return BeanProvider.getContextualReference(name, true);
    }

    @Override
    public <T> T lookupByNameAndType(final String name, final Class<T> type) {
        ObjectHelper.notEmpty(name, "name");
        ObjectHelper.notNull(type, "type");

        LOG.trace("Looking up bean with name {} of type {}", name, type);
        return BeanProvider.getContextualReference(name, true, type);
    }

    @Override
    public <T> Map<String, T> findByTypeWithName(final Class<T> type) {
        ObjectHelper.notNull(type, "type");

        LOG.trace("Lookups based of type {}", type);
        Map<String, T> beans = new HashMap<String, T>();
        Set<Bean<T>> definitions = BeanProvider.getBeanDefinitions(type, true, true);

        if (definitions == null) {
            return beans;
        }
        for (Bean<T> bean : definitions) {
            if (bean.getName() != null) {
                beans.put(bean.getName(), BeanProvider.getContextualReference(type, bean));
            }
        }
        return beans;
    }

    @Override
    public <T> Set<T> findByType(Class<T> type) {
        ObjectHelper.notNull(type, "type");

        LOG.trace("Lookups based of type {}", type);
        Set<T> beans = new HashSet<T>();
        Set<Bean<T>> definitions = BeanProvider.getBeanDefinitions(type, true, true);

        if (definitions == null) {
            return beans;
        }
        for (Bean<T> bean : definitions) {
            if (bean.getName() != null) {
                beans.add(BeanProvider.getContextualReference(type, bean));
            }
        }
        return beans;
    }

    @Override
    public Object lookup(String name) {
        return lookupByName(name);
    }

    @Override
    public <T> T lookup(String name, Class<T> type) {
        return lookupByNameAndType(name, type);
    }

    @Override
    public <T> Map<String, T> lookupByType(Class<T> type) {
        return findByTypeWithName(type);
    }

    @Override
    public String toString() {
        return "CdiRegistry[" + System.identityHashCode(this) + "]";
    }
}
