package org.jboss.forge.arquillian.extension.graphene;

import java.util.Arrays;
import java.util.List;

import org.jboss.forge.arquillian.extension.Extension;
import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyBuilder;

public class GrapheneExtension implements Extension
{

   @Override
   public DependencyBuilder getBom()
   {
      return null;
   }

   @Override
   public List<Dependency> listDependencies()
   {

      return Arrays.asList((Dependency) DependencyBuilder
               .create("org.jboss.arquillian.graphene:arquillian-graphene::test:pom"));
   }
}
