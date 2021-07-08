package org.test.stocklike.domain.state;

public enum States {
    WAIT_FOR_QUERY,
    PROCESS_QUERY,
    WAIT_FOR_REFINE,
    PROCESS_REFINE,
    WAIT_FOR_PARAMETERS_OR_QUERY
}
