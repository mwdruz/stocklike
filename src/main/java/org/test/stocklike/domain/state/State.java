package org.test.stocklike.domain.state;

public enum State {
    WAIT_FOR_REQUEST,
    WAIT_FOR_CATEGORIES,
    PROCESS_REQUEST,
    PROCESS_REFINED_CATEGORIES,
    PROCESS_REFINED_PARAMS,
    DISPLAY_HGRAM_AND_WAIT,
    DISPLAY_ERROR_AND_WAIT,
    INVALID_STATE
}
