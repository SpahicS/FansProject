package models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by Spaja on 26-Apr-17.
 */

public class ResponseData {

    @Element (name = "channel")
    private Channel channel;

    @Attribute (required = false)
    private String version;

    public Channel getChannel() {
        return channel;
    }

    public String getVersion() {
        return version;
    }
}
