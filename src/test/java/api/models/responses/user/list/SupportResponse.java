package api.models.responses.user.list;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents support information in the List Users API response from reqres.in.
 */
public class SupportResponse {
    @JsonProperty("url")
    private String url;
    @JsonProperty("text")
    private String text;

    // Default constructor for Jackson
    public SupportResponse() {}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}