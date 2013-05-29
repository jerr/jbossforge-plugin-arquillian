package org.jboss.forge.arquillian.extension;

import java.util.Arrays;

import org.jboss.forge.shell.completer.SimpleTokenCompleter;

/**
 * @author Jérémie Lagarde
 *
 */
public class ExtensionCommandCompleter extends SimpleTokenCompleter{

   @Override
   public Iterable<?> getCompletionTokens() {
       return Arrays.asList(ExtensionType.values());
   }
}
