package com.hpe.tslabs.java.basics.jshell;

import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * (C) Copyright 2021 Hewlett Packard Enterprise Development LP
 *
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
