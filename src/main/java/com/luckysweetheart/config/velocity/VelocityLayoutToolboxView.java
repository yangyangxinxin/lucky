package com.luckysweetheart.config.velocity;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolboxFactory;
import org.apache.velocity.tools.config.ToolboxConfiguration;
import org.apache.velocity.tools.config.XmlFactoryConfiguration;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * Created by yangxin on 2017/5/22.
 */
public class VelocityLayoutToolboxView extends VelocityToolboxView {

    private VelocityLayoutToolboxView() {

    }
    @Override
    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ViewToolContext ctx = new ViewToolContext(this.getVelocityEngine(), request, response, this.getServletContext());
        if (this.getToolboxConfigLocation() != null) {
            XmlFactoryConfiguration factory = new XmlFactoryConfiguration();

            factory.read(ResourceUtils.getURL(getToolboxConfigLocation()).openStream());

            ToolboxFactory toolboxFactory = factory.createFactory();
            toolboxFactory.configure(factory);
            Collection<ToolboxConfiguration> toolboxes = factory.getToolboxes();
            for (ToolboxConfiguration tc : toolboxes) {
                ctx.addToolbox(toolboxFactory.createToolbox(tc.getScope()));// 这样操作后就可以用工具里面的东西了。
            }
        }
        if (model != null && !model.isEmpty()) {
            ctx.putAll(model);
        }
        return ctx;
    }

}
