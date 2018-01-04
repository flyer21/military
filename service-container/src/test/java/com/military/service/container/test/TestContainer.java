package  com.military.service.container.test;

import com.military.service.container.ModuleApplicationContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestContainer {
    @Autowired
    private ModuleApplicationContainer sc;

    @Test
    void  createContainer(){
        sc.init();
        sc.start();
        sc.stop();

    }
}
