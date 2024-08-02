package el.dude.java.basics.inheritance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Author: Gabriel Inäbnit - 2022-02-03
 */
public class InstanceOfTest
{

   @Test
   void is_long_instance_of_number()
   {
      final Long longNumber = 1000L;
      assertTrue(longNumber instanceof Number);
   }

   @Test
   void is_int_instance_of_number()
   {
      final Integer intNumber = 123;
      assertTrue(intNumber instanceof Number);
   }

}
