package com.infilos.mantou.api;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Transfrom domain to view model.
 */
public interface ModelTransfer<D, M> {

    M buildModel(D domain);

    default List<M> buildModel(final Collection<D> domains) {
        if (domains == null) {
            return Collections.emptyList();
        }
        
        return domains.stream()
            .map(this::buildModel)
            .collect(Collectors.toList());
    }
}
