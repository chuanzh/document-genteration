package com.github.chuanzh.engine;

import com.github.chuanzh.config.GlobalConfig;
import com.github.chuanzh.config.TemplateConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class FreemarkerTemplateEngine extends AbstractTemplateEngine {

    private static final Logger logger = LoggerFactory.getLogger(FreemarkerTemplateEngine.class);

    private static Configuration cfg = new Configuration();
    private Template temp = null;
    private static String encode = "UTF-8";
    private GlobalConfig globalConfig;

    public FreemarkerTemplateEngine() {
        init();
    }

    @Override
    public AbstractTemplateEngine init() {
        cfg.setClassForTemplateLoading(FreemarkerTemplateEngine.class,"/");
        cfg.setDefaultEncoding(encode);
        try {
            this.temp = cfg.getTemplate(TemplateConfig.MARK_DOWN_TEMPLATE);
        } catch (IOException e) {
            logger.error("初始化模板失败", e);
        }
        return this;
    }

    @Override
    public AbstractTemplateEngine process(Map<String, Object> param) {
        StringWriter sw = new StringWriter();
        try {
            this.temp.process(param, sw);
        } catch (Exception e) {
            logger.error("处理模板信息失败",e);
        }
        writeToFile(sw, param.get("path").toString());
        return this;
    }

    private void writeToFile(StringWriter sw, String filePath) {
        FileWriter fw = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            fw.write(sw.toString());
        } catch (IOException e) {
            logger.error("写入文件失败",e);
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                logger.error("关闭文件流失败",e);
            }
        }
    }

}
