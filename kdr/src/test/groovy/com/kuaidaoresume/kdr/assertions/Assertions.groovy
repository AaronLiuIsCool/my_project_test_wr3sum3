package com.kuaidaoresume.kdr.assertions

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.springframework.http.ResponseEntity

class Assertions {
    static DestinationAssert assertThat(WireMockRule... destinations) {
        return new DestinationAssert(destinations)
    }

    static ResponseAssert assertThat(ResponseEntity response) {
        return new ResponseAssert(response)
    }
}
