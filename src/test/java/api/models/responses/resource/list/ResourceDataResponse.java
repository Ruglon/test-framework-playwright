package api.models.responses.resource.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an individual resource in the List Resources API response from reqres.in.
 */
@Getter
@Setter
public class ResourceDataResponse {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("year")
    private int year;
    @JsonProperty("color")
    private String color;
    @JsonProperty("pantone_value")
    private String pantoneValue;
}
