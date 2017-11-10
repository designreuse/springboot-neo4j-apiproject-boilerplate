package in.techbeat.palapa;

import in.techbeat.palapa.controller.ApiController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class tests whether the spring context is loading as expected.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private ApiController apiController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(apiController).isNotNull();
    }
}