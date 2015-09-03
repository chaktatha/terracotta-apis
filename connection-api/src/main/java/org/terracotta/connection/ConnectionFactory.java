/*
 *
 *  The contents of this file are subject to the Terracotta Public License Version
 *  2.0 (the "License"); You may not use this file except in compliance with the
 *  License. You may obtain a copy of the License at
 *
 *  http://terracotta.org/legal/terracotta-public-license.
 *
 *  Software distributed under the License is distributed on an "AS IS" basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 *  the specific language governing rights and limitations under the License.
 *
 *  The Covered Software is Connection API.
 *
 *  The Initial Developer of the Covered Software is
 *  Terracotta, Inc., a Software AG company
 *
 */
package org.terracotta.connection;

import java.net.URI;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 * Factory for generating connections to clusters (or pretend connections to pretend clusters).
 */
public final class ConnectionFactory {
  /**
   * Establish a connection based on the given uri. The method will attempt to look for the first suitable implementation
   * of a {@link org.terracotta.connection.ConnectionService} based on whether or not it handles the given URI.
   *
   * @param uri URI to connect to
   * @param properties any configurations to be applied (implementation specific)
   * @return an established connection
   * @throws ConnectionException if there is an error while attempting to connect
   */
  public static Connection connect(URI uri, Properties properties) throws ConnectionException {
    return connect(uri, null, properties);
  }

  /**
   * Establish a connection based on the given uri. The method will attempt to look for the first suitable implementation
   * of a {@link org.terracotta.connection.ConnectionService} based on whether or not it handles the given URI.
   *
   * @param uri URI to connect to
   * @param disconnectHandler handler for when the connection is irrecoverably lost
   * @param properties any configurations to be applied (implementation specific)  @return an established connection
   * @return established connection
   * @throws ConnectionException if there is an error while attempting to connect
   */
  public static Connection connect(URI uri, DisconnectHandler disconnectHandler, Properties properties) throws ConnectionException {
    ServiceLoader<ConnectionService> serviceLoader = ServiceLoader.load(ConnectionService.class,
        ConnectionFactory.class.getClassLoader());
    for (ConnectionService connectionService : serviceLoader) {
      if (connectionService.handlesURI(uri)) {
        return connectionService.connect(uri, disconnectHandler, properties);
      }
    }
    throw new IllegalArgumentException("Unknown URI " + uri);
  }
}