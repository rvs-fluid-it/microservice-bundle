package be.fluid_it.µs.bundle.dropwizard.services;

import java.util.ServiceLoader;

public class ProviderUtil {
    public static <P> P getProvider(Class<P> providerClazz) {
        ServiceLoader<P> loader = ServiceLoader.load(providerClazz);
        return loader != null && loader.iterator().hasNext() ? loader.iterator().next() : null;
    }
}
