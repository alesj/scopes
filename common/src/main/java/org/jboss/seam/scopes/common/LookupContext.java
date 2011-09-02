/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.seam.scopes.common;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;

/**
 * Abstract lookup context.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class LookupContext implements Context {
    private final BeanManager manager;

    protected LookupContext(BeanManager manager) {
        if (manager == null)
            throw new IllegalArgumentException("Null manager!");
        this.manager = manager;
    }

    /**
     * Lookup bean by type.
     *
     * @param clazz the type
     * @return bean of type clazz
     */
    protected <T> T lookup(Class<T> clazz) {
        InjectionTarget<T> it = manager.createInjectionTarget(manager.createAnnotatedType(clazz));
        CreationalContext<T> cc = manager.createCreationalContext(null);
        T result = it.produce(cc);
        it.inject(result, cc);
        return result;
    }

    public <T> T get(Contextual<T> contextual) {
        return get(contextual, null);
    }

    public boolean isActive() {
        return true;
    }
}
