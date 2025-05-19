package api.models.responses.user.list;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the List Users API response from reqres.in, including pagination and user data.
 */
public class UserListResponse {
    @JsonProperty("page")
    private int page;
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("total")
    private int total;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("data")
    private List<UserDataResponse> data;
    @JsonProperty("support")
    private SupportResponse support;

    // Default constructor for Jackson
    public UserListResponse() {}

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<UserDataResponse> getData() {
        return data;
    }

    public void setData(List<UserDataResponse> data) {
        this.data = data;
    }

    public SupportResponse getSupport() {
        return support;
    }

    public void setSupport(SupportResponse support) {
        this.support = support;
    }
}
