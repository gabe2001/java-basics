// (c) Copyright 2021-2022 Hewlett Packard Enterprise Development LP
package com.hpe.tslabs.java.basics.inheritance;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class hierarchy and inheritance
 *
 * @author Gabriel Inäbnit - 2021-01-21
 */
public class SubSuperClassTest
{

   private static Double doubleValue;

   @BeforeAll
   public static void init()
   {
      doubleValue = Math.random();
   }

   @AfterAll
   public static void destroy()
   {
      doubleValue = null;
   }

   @Test
   public void sub_super_class()
   {
      final ILocalValue lv = new LocalValue(doubleValue);
      value(lv);
      assertFalse(lv.getClass().isAssignableFrom(Value.class));
      assertTrue(lv instanceof IValue);
   }

   @Test
   public void super_sub_class()
   {
      final IValue v = new Value(doubleValue);
      value(v);
      assertTrue(v.getClass().isAssignableFrom(LocalValue.class));
      assertFalse(v instanceof ILocalValue);
   }

   @Test
   public void super_sub_impl_class()
   {
      final IValue v = new LocalValue(doubleValue);
      value(v);
      assertTrue(v instanceof IValue);
      assertTrue(v instanceof ILocalValue);
      assertFalse(v.getClass().isAssignableFrom(Value.class));
      assertTrue(v.getClass().isAssignableFrom(LocalValue.class));
   }

   @Test //(expected = java.lang.ClassCastException.class)
   public void sub_super_impl_class()
   {
      final ClassCastException thrown = Assertions.assertThrows(ClassCastException.class, () -> {
         final ILocalValue v;
         // demonstration of compile time validation of  sub/super class compatibility
         // uncomment next line to see IDE/compiler complaining
         //v = new Value(doubleValue);

         // demonstration of runtime error of the above
         v = (ILocalValue) new Value(doubleValue);
      });
   }



   private void value(final IValue v)
   {
      System.out.println("IValue");
      assertTrue(v.getValue() >= 0);
   }

   private void value(final ILocalValue v)
   {
      System.out.println("ILocalValue");
      assertTrue(v.getValue() >= 0);
      assertTrue(v.getHalfValue() >= 0);
   }

}
