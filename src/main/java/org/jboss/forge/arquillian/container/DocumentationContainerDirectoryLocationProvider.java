package org.jboss.forge.arquillian.container;

import javax.enterprise.inject.Alternative;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author Paul Bakker - paul.bakker.nl@gmail.com
 */
@Alternative
public class DocumentationContainerDirectoryLocationProvider implements ContainerDirectoryLocationProvider{
    @Override
    public URL getUrl() {
        try {
            return new URL("https://raw.github.com/gist/1324966/afe53313a2ed345585188a5c1f3d43fdb0c667d6/containers.json");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
