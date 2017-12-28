package  com.military.service.container.test;

import com.military.service.container.ModuleContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestContainer {
    @Autowired
    private ApplicationContext ctx;

    @Test
    void  createContainer(){
        ModuleContainer sc = new ModuleContainer(ctx);
        sc.init();
        sc.start();
        sc.stop();

    }
}
