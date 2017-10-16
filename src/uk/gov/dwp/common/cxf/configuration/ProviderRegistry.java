package uk.gov.dwp.common.cxf.configuration;

import java.util.ArrayList;
import java.util.List;

public class ProviderRegistry {

    private final List<Object> providers = new ArrayList<>();

    public <T> T add(T provider) {
        providers.add(provider);
        return provider;
    }

    public List<Object> getProviders() {
        return providers;
    }
}
