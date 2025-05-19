package api.models.responses.resource.list;

import api.models.responses.user.list.SupportResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the List Resources API response from reqres.in, including pagination and resource data.
 */
@Getter
@Setter
public class ResourceListResponse {
    @JsonProperty("page")
    private int page;
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("total")
    private int total;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("data")
    private List<ResourceDataResponse> data;
    @JsonProperty("support")
    private SupportResponse support;
}
