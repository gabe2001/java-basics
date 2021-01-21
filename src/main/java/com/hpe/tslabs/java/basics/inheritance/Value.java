/**
 * (C) Copyright 2021 Hewlett Packard Enterprise Development LP
 *
 * @author Gabriel Inäbnit - 2021-01-21
 */

package com.hpe.tslabs.java.basics.inheritance;

public class Value implements IValue
{

   private final double val;

   public Value(final double val)
   {
      this.val = val;
   }

   @Override
   public double getValue()
   {
      return val;
   }
}
