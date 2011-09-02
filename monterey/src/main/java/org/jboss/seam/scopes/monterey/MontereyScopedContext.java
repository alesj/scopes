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

package org.jboss.seam.scopes.monterey;

import com.cloudsoftcorp.monterey.servicebean.access.api.MontereyNetworkEndpoint;
import org.jboss.seam.scopes.common.LookupContext;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;

/**
 * Monterey scoped context.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Singleton
public class MontereyScopedContext extends LookupContext {
    private MontereyNetworkEndpoint endpoint;
    private long timeout;

    @Inject
    public MontereyScopedContext(BeanManager manager) {
        super(manager);
    }

    protected MontereyNetworkEndpoint getEndpoint() {
        if (endpoint == null)
            endpoint = lookup(MontereyNetworkEndpoint.class);
        return endpoint;
    }

    public long getTimeout() {
        if (timeout <= 0)
            timeout = MontereyProvider.getTimeout();
        return timeout;
    }

    public Class<? extends Annotation> getScope() {
        return MontereyScoped.class;
    }

    @SuppressWarnings({"unchecked"})
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        if (contextual instanceof Bean<?>) {
            Bean<T> bean = (Bean<T>) contextual;
            String segment = ""; // TODO
            return (T) getEndpoint().getService(bean.getBeanClass(), segment, getTimeout());
        } else {
            throw new IllegalArgumentException("Can only handle beans: " + contextual);
        }
    }
}
