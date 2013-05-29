package org.jboss.forge.arquillian.extension;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.arquillian.DependencyUtil;
import org.jboss.forge.arquillian.ProfileBuilder;
import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.shell.Shell;

/**
 * @Author Jérémie Lagarde
 */
public class ExtensionInstaller
{
   @Inject
   ProfileBuilder profileBuilder;

   @Inject
   Shell shell;

   @Inject
   Project project;

   public void installExtension(Extension extension)
   {
      installExtensionBom(extension);
      installExtensionDependencies(extension);

   }

   private void installExtensionBom(Extension extension)
   {
      DependencyBuilder extensionBom = extension.getBom();
      if (extensionBom != null)
      {
         DependencyFacet dependencyFacet = project.getFacet(DependencyFacet.class);

         String extensionVersionProperty = "version." + extensionBom.getArtifactId().replaceAll("-", ".");
         String extensionVersion = dependencyFacet.getProperty(extensionVersionProperty);
         if (extensionVersion == null)
         {
            List<Dependency> versions = dependencyFacet.resolveAvailableVersions(extensionBom);
            Dependency dependency = shell.promptChoiceTyped("What version of " + extensionBom.getArtifactId()
                     + " do you want to use?", versions, DependencyUtil.getLatestNonSnapshotVersion(versions));
            extensionVersion = dependency.getVersion();
            dependencyFacet.setProperty(extensionVersionProperty, extensionVersion);
         }

         // need to set version after resolve is done, else nothing will resolve.
         if (!dependencyFacet.hasDirectManagedDependency(extensionBom))
         {
            extensionBom.setVersion("${" + extensionVersionProperty + "}");
            dependencyFacet.addDirectManagedDependency(extensionBom);
         }
      }
   }

   private void installExtensionDependencies(Extension extension)
   {
      List<Dependency> dependencies = extension.listDependencies();
      DependencyFacet dependencyFacet = project.getFacet(DependencyFacet.class);

      for (Dependency dependency : dependencies)
      {
         if (!dependencyFacet.hasEffectiveDependency(dependency))
         {
            if (!dependencyFacet.hasEffectiveManagedDependency(dependency))
            {
               String versionProperty = "version." + dependency.getArtifactId().replaceAll("-", ".");
               List<Dependency> versions = dependencyFacet.resolveAvailableVersions(dependency);
               Dependency selectedDependency = shell.promptChoiceTyped("What version of " + dependency.getArtifactId()
                        + " do you want to use?", versions, DependencyUtil.getLatestNonSnapshotVersion(versions));

               dependencyFacet.setProperty(versionProperty, selectedDependency.getVersion());
               dependencyFacet.addDirectDependency(DependencyBuilder.create(dependency).setVersion(
                        "${" + versionProperty + "}"));
            }
            else
            {
               dependencyFacet.addDirectDependency(DependencyBuilder.create(dependency).setVersion(null));
            }
         }

         if (!dependencyFacet.hasEffectiveDependency(dependency))
         {
            dependencyFacet.addDirectDependency(dependency);
         }
      }
   }
}
