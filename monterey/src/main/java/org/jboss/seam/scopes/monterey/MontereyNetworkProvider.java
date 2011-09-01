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
import com.cloudsoftcorp.monterey.servicebean.access.api.MontereyNetworkEndpointImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Monterey network provider.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@ApplicationScoped
public class MontereyNetworkProvider {

    @Produces @ApplicationScoped
    public MontereyNetworkEndpoint createEndpoint() throws Exception {
        Properties p = loadProperties();
		MontereyNetworkEndpointImpl network = new MontereyNetworkEndpointImpl();
		network.setManagementNodeUrl(p.getProperty("managementNodeUrl"));
		network.setUsername(p.getProperty("username"));
		network.setPassword(p.getProperty("password"));
		network.setLocation(p.getProperty("location"));
		network.setHasPrivateIp(Boolean.parseBoolean(p.getProperty("hasPrivateIp")));
		network.start();
        return network;
    }

    public void shutdownEndpoint(@Disposes MontereyNetworkEndpoint endpoint) {
        endpoint.shutdown();
    }

    protected Properties loadProperties() throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream("monterey.properties");
        try {
            Properties properties = new Properties();
            properties.load(is);
            return properties;
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }

}
