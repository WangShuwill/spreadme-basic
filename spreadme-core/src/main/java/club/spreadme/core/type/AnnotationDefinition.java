/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package club.spreadme.core.type;


import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * the definition of annotation
 *
 * @author wswei
 * @see club.spreadme.core.utils.ReflectionUtil
 * @since 2018
 */
public class AnnotationDefinition {

    private Class<? extends Annotation> type;
    private Map<String, Object> attributes;

    public AnnotationDefinition() {

    }

    public AnnotationDefinition(Class<? extends Annotation> type, Map<String, Object> attributes) {
        this.type = type;
        this.attributes = attributes;
    }

    public Class<? extends Annotation> getType() {
        return type;
    }

    public void setType(Class<? extends Annotation> type) {
        this.type = type;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
