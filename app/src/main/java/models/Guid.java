package models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by Spaja on 26-Apr-17.
 */

class Guid {

    @Attribute (required = false)
    private String isPermaLink;

    @Element
    private String value;

    public String getValue() {
        return value;
    }

    public String getIsPermaLink() {
        return isPermaLink;
    }
}
