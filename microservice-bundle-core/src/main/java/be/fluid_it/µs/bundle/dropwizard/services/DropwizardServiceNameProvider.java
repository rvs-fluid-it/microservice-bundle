package be.fluid_it.µs.bundle.dropwizard.services;

import be.fluid_it.µs.bundle.dropwizard.µService;

public class DropwizardServiceNameProvider implements ServiceNameProvider {
    public String getName() {
        return µService.µServiceClass.getSimpleName();
    }
}
