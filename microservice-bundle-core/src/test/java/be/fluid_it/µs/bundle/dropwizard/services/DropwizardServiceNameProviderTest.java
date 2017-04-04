package be.fluid_it.µs.bundle.dropwizard.services;


import be.fluid_it.µs.bundle.dropwizard.µService;
import org.junit.Assert;
import org.junit.Test;

public class DropwizardServiceNameProviderTest {
    static {
        µService.µServiceClass = µService.class;
    }

    @Test
    public void checkServiceNameProvider() {
        ServiceNameProvider serviceNameProvider = ProviderUtil.getProvider(ServiceNameProvider.class);
        Assert.assertNotNull(serviceNameProvider);
        Assert.assertEquals(µService.class.getSimpleName(), serviceNameProvider.getName());
    }
}
