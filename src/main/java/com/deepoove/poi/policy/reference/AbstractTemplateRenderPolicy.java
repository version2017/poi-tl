/*
 * Copyright 2014-2020 Sayi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.deepoove.poi.policy.reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.template.ElementTemplate;

/**
 * 
 * @author Sayi
 * @version
 */
public abstract class AbstractTemplateRenderPolicy<E, T> implements RenderPolicy {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("unchecked")
    @Override
    public void render(ElementTemplate eleTemplate, Object data, XWPFTemplate template) {
        // type safe
        T model = null;
        try {
            model = (T) data;
        } catch (ClassCastException e) {
            throw new RenderException("Error Render Data format for template: " + eleTemplate.getSource(), e);
        }

        try {
            doRender((E) eleTemplate, model, template);
        } catch (Exception e) {
            throw new RenderException("TemplateRenderPolicy render error", e);
        }

    }

    public abstract void doRender(E eleTemplate, T t, XWPFTemplate template) throws Exception;

}