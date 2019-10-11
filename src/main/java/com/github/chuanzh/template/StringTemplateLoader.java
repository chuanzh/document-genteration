package com.github.chuanzh.template;

import freemarker.cache.TemplateLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author zhangchuan
 */
public class StringTemplateLoader implements TemplateLoader {

    private String template;

    public StringTemplateLoader(String template) {
        this.template = template;
        if (template == null) {
            this.template = "";
        }
    }

    public void closeTemplateSource(Object templateSource) throws IOException {
        ((StringReader) templateSource).close();
    }

    public Object findTemplateSource(String name) throws IOException {
        return new StringReader(template);
    }

    public long getLastModified(Object templateSource) {
        return 0;
    }

    public Reader getReader(Object templateSource, String encoding)
            throws IOException {
        return (Reader) templateSource;
    }

}
