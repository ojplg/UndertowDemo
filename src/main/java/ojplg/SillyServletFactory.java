package ojplg;

import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import io.undertow.servlet.util.ImmediateInstanceHandle;

public class SillyServletFactory implements InstanceFactory<MySillyServlet> {

    private final String specialSetting;

    public SillyServletFactory(String setting){
        specialSetting = setting;
    }

    @Override
    public InstanceHandle<MySillyServlet> createInstance() throws InstantiationException {
        MySillyServlet servlet = new MySillyServlet(specialSetting);
        return new ImmediateInstanceHandle<>(servlet);
    }
}
