package com.hpe.tslabs.java.basics.inheritance;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * (C) Copyright 2021 Hewlett Packard Enterprise Development LP
 *
 * @author Gabriel Inäbnit - 2021-01-21
 */
public class SubSuperClassTest
{

   private static Double doubleValue;

   @BeforeClass
   public static void init()
   {
      doubleValue = Math.random();
   }

   @AfterClass
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

   @Test(expected = java.lang.ClassCastException.class)
   public void sub_super_impl_class()
   {
      ILocalValue v;
      // demonstration of compile time validation of  sub/super class compatibility
      // uncomment next line to see IDE/compiler complaining
      //v = new Value(doubleValue);

      // demonstration of runtime error of the above
      v = (ILocalValue) new Value(doubleValue);

      fail("Casting should have caused an exception");
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
