package com.github.chuanzh.engine;

import java.util.Map;

public abstract class AbstractTemplateEngine {

    public abstract AbstractTemplateEngine init();

    public abstract AbstractTemplateEngine process(Map<String,Object> params);

}
