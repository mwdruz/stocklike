package org.test.stocklike.domain.state;

public enum State {
    WAIT_FOR_QUERY,
    WAIT_FOR_CATEGORIES,
    PROCESS_QUERY,
    PROCESS_CATEGORIES,
    PROCESS_PARAMS,
    DISPLAY_HGRAM_AND_WAIT,
    DISPLAY_ERROR_AND_WAIT,
    INVALID_STATE // testing only!
}
