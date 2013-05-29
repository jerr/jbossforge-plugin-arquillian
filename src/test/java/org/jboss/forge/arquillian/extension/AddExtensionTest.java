package org.jboss.forge.arquillian.extension;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.Arrays;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.forge.Root;
import org.jboss.forge.arquillian.ArquillianPlugin;
import org.jboss.forge.arquillian.container.Container;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.project.Project;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.seam.render.RenderRoot;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.solder.SolderRoot;
import org.junit.Test;

public class AddExtensionTest extends AbstractShellTest
{
   @Deployment
   public static JavaArchive getDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
               .addPackages(true, Root.class.getPackage())
               .addPackages(true, RenderRoot.class.getPackage())
               .addPackages(true, SolderRoot.class.getPackage())
               .addPackages(true, ArquillianPlugin.class.getPackage(), Exception.class.getPackage())
               .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }
   

   private Project addExtension(final String extension, final List<DependencyMatcher> dependencies) throws Exception
   {
      Project project = initializeJavaProject();

      MavenCoreFacet coreFacet = project.getFacet(MavenCoreFacet.class);

      List<Profile> profiles = coreFacet.getPOM().getProfiles();
      for (Profile profile : profiles)
      {
         System.out.println(profile.getId());
      }
      assertThat(profiles.size(), is(0));

      queueInputLines(extension, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
      getShell().execute("arquillian add-extension");

      assertThat(coreFacet.getPOM().getProfiles().size(), is(1));
      Profile profile = coreFacet.getPOM().getProfiles().get(0);

      for (DependencyMatcher dependency : dependencies)
      {
         assertThat(profile.getDependencies(), hasItem(dependency));
      }

      Model pom = coreFacet.getPOM();
      DependencyMatcher arqBom = new DependencyMatcher("arquillian-bom");

      assertThat("Verify arquillian:bom was added to DependencyManagement ",
               pom.getDependencyManagement().getDependencies(), hasItem(arqBom));

      assertNotNull("Verify that the plugin use a version property for arquillian core",
               pom.getProperties().get(ArquillianPlugin.ARQ_CORE_VERSION_PROP_NAME));

      assertNotNull("Verify that the plugin use a version property for junit",
               pom.getProperties().get(ArquillianPlugin.JUNIT_VERSION_PROP_NAME));

      return project;
   }

   @Test
   public void addDroneExtension() throws Exception
   {
      addExtension("DRONE",
               Arrays.asList(
                        new DependencyMatcher("arquillian-openejb-embedded-3.1"),
                        new DependencyMatcher("openejb-core")));
   }

   class DependencyMatcher extends BaseMatcher<Dependency>
   {
      private final String artifactId;

      public DependencyMatcher(final String artifactId)
      {
         this.artifactId = artifactId;
      }

      @Override
      public boolean matches(final Object o)
      {
         Dependency d = (Dependency) o;
         return d.getArtifactId().equals(artifactId);
      }

      @Override
      public void describeTo(final Description description)
      {
      }
   }
}
