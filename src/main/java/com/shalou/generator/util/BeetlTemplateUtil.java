package com.shalou.generator.util;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.IOException;

/**
 * beetl获取模板的工具类
 *
 * @author sunyu on 2016/12/6.
 */
public class BeetlTemplateUtil {

    /**
     * 根据名称获取
     * @param templateName
     * @return
     */
    public static Template getByName(String templateName) {
        try {
            ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("/template/");
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            return gt.getTemplate(templateName);
        } catch (IOException e) {
            throw new RuntimeException("获取模板异常");
        }
    }
}
