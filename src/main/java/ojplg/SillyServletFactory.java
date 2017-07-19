package ojplg;

import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import io.undertow.servlet.util.ImmediateInstanceHandle;

public class SillyServletFactory implements InstanceFactory<MySillyServlet> {

    @Override
    public InstanceHandle<MySillyServlet> createInstance() throws InstantiationException {
        MySillyServlet servlet = new MySillyServlet();
        return new ImmediateInstanceHandle<>(servlet);
    }
}
