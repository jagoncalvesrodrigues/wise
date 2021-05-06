package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SudaderaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sudadera.class);
        Sudadera sudadera1 = new Sudadera();
        sudadera1.setId(1L);
        Sudadera sudadera2 = new Sudadera();
        sudadera2.setId(sudadera1.getId());
        assertThat(sudadera1).isEqualTo(sudadera2);
        sudadera2.setId(2L);
        assertThat(sudadera1).isNotEqualTo(sudadera2);
        sudadera1.setId(null);
        assertThat(sudadera1).isNotEqualTo(sudadera2);
    }
}
