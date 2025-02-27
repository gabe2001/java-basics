package el.dude.java.basics.jshell;

import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Gabriel Inäbnit - 2021-01-21
 */
public class JShellTest
{

   @Test
   public void sub_super_impl_class()
   {
      final JShell jShell = JShell.builder().out(System.out).err(System.out).build();
      eval(jShell, "int i = 0");
      assertTrue(true);
   }



   private void eval(final JShell jShell, final String snippet)
   {
      List<SnippetEvent> events = jShell.eval(snippet);
      events.forEach(event -> {
         System.out.print("Evaluation status: " + event.status());
         System.out.println(" | Evaluation result: " + event.value());
      });
   }
}
