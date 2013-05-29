package org.jboss.forge.arquillian.extension.drone;

import java.util.Arrays;
import java.util.List;

import org.jboss.forge.arquillian.extension.Extension;
import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyBuilder;

public class DroneExtension implements Extension
{

   @Override
   public DependencyBuilder getBom()
   {
      return DependencyBuilder.create("org.jboss.arquillian.extension:arquillian-drone-bom::import:pom");
   }

   @Override
   public List<Dependency> listDependencies()
   {
      DependencyBuilder seleniumServer = DependencyBuilder.create("org.seleniumhq.selenium:selenium-server::test");
      seleniumServer.getExcludedDependencies().add(DependencyBuilder.create("org.mortbay.jetty:servlet-api-2.5"));
      return Arrays.asList((Dependency) DependencyBuilder.create("org.jboss.arquillian.extension:arquillian-drone-impl::test"),
               (Dependency) DependencyBuilder.create("org.jboss.arquillian.extension:arquillian-drone-selenium::test"),
               (Dependency) DependencyBuilder.create("org.jboss.arquillian.extension:arquillian-drone-selenium-server::test"),
               (Dependency) DependencyBuilder.create("org.seleniumhq.selenium:selenium-java::test"),
               (Dependency) seleniumServer, 
               (Dependency) DependencyBuilder.create("org.slf4j:slf4j-simple:1.6.4:test"));
   }

}
