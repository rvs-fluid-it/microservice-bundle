package be.fluid_it.µs.bundle.guice;

import com.google.inject.Module;

import java.lang.Class;import java.lang.Object;import java.lang.String;import java.lang.System;import java.util.*;import java.util.Iterator;import java.util.LinkedList;import java.util.List;import java.util.Map;import java.util.ServiceLoader;

public class Modules {
  private static final String DISABLED = "disabled";

  public static List<Module> find(Class<? extends Module> moduleClass) {
    List<Module> modules = new LinkedList<Module>();
    Iterator<? extends Module> moduleIterator = ServiceLoader.load(moduleClass).iterator();
    while (moduleIterator.hasNext()) {
      Module module = moduleIterator.next();
      if (!isDisabled(module.getClass())) {
        modules.add(module);
      }
    }
    return modules;
  }

  public static boolean isDisabled(Class<? extends Module> moduleClass) {
    String moduleKey = moduleClass.getName();
    if (System.getProperties().containsKey(moduleKey)) {
      String moduleValue = System.getProperty(moduleKey);
      if (moduleValue != null) {
        return DISABLED.equals(moduleValue);
      }
    }
    return false;
  }

  public static void disable(Class<? extends Module> moduleClass) {
    if (moduleClass != null) {
      System.setProperty(moduleClass.getName(), DISABLED);
    }
  }

  public static void resetDisabled() {
    List<String> keysToClear = new LinkedList<String>();
    for (Map.Entry<Object, Object> entry  : System.getProperties().entrySet()) {
      Object value = entry.getValue();
      if (value != null && DISABLED.equals(value)) {
        keysToClear.add(entry.getKey().toString());
      }
    }
    for (String keyToClear : keysToClear) {
      System.clearProperty(keyToClear);
    }
  }
}
