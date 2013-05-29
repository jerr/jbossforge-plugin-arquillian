package org.jboss.forge.arquillian.extension;

import java.util.List;

import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyBuilder;

/**
 * @author Jérémie Lagarde
 * 
 */
public interface Extension
{

   /**
    * Get the bom to configure this extension.
    */
   DependencyBuilder  getBom();

   /**
    * List any dependencies required by this extension.
    */
   List<Dependency> listDependencies();

}
