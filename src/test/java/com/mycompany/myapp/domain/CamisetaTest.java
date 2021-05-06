package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CamisetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Camiseta.class);
        Camiseta camiseta1 = new Camiseta();
        camiseta1.setId(1L);
        Camiseta camiseta2 = new Camiseta();
        camiseta2.setId(camiseta1.getId());
        assertThat(camiseta1).isEqualTo(camiseta2);
        camiseta2.setId(2L);
        assertThat(camiseta1).isNotEqualTo(camiseta2);
        camiseta1.setId(null);
        assertThat(camiseta1).isNotEqualTo(camiseta2);
    }
}
