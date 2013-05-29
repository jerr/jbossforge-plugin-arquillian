package org.jboss.forge.arquillian.extension;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.forge.shell.util.BeanManagerUtils;
import org.jboss.forge.arquillian.extension.drone.DroneExtension;
import org.jboss.forge.arquillian.extension.graphene.GrapheneExtension;

/**
 * Represents the list of known/supported Arquillian extension.
 * 
 * @author Jérémie Lagarde
 * 
 */
public enum ExtensionType
{
   DRONE(DroneExtension.class),
   GRAPHENE(GrapheneExtension.class);

   private Class<? extends Extension> extension;

   private ExtensionType(final Class<? extends Extension> extension)
   {
      this.extension = extension;
   }

   public Extension getExtension(final BeanManager manager)
   {
      return BeanManagerUtils.getContextualInstance(manager, extension);
   }
}
