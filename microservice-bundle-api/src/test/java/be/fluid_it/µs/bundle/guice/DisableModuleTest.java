package be.fluid_it.µs.bundle.guice;

import com.google.inject.Module;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DisableModuleTest {
  @Test
  public void testDisableModule() {
    List<Module> modules = Modules.find(Module.class);
    Assert.assertNotNull(modules);
    Assert.assertTrue(modules.size() == 1);
    Assert.assertEquals(AModule.class, modules.get(0).getClass());
    Modules.disable(AModule.class);
    modules = Modules.find(Module.class);
    Assert.assertNotNull(modules);
    Assert.assertTrue(modules.isEmpty());
    Modules.resetDisabled();
    modules = Modules.find(Module.class);
    Assert.assertTrue(modules.size() == 1);
    Assert.assertEquals(AModule.class, modules.get(0).getClass());
  }
}
