package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccesorioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Accesorio.class);
        Accesorio accesorio1 = new Accesorio();
        accesorio1.setId(1L);
        Accesorio accesorio2 = new Accesorio();
        accesorio2.setId(accesorio1.getId());
        assertThat(accesorio1).isEqualTo(accesorio2);
        accesorio2.setId(2L);
        assertThat(accesorio1).isNotEqualTo(accesorio2);
        accesorio1.setId(null);
        assertThat(accesorio1).isNotEqualTo(accesorio2);
    }
}
