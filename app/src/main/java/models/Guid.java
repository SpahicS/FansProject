package models;

import org.simpleframework.xml.Attribute;

/**
 * Created by Spaja on 26-Apr-17.
 */

class Guid {

    @Attribute (required = false)
    private String isPermaLink;

    public String getIsPermaLink() {
        return isPermaLink;
    }
}
