package com.tradingsim.client.api.assets;

public class GetAssetsRequest {
    private Integer id;
    private String name;
    private Integer page;
    private Integer perPage;

    public GetAssetsRequest(
            Integer id,
            String name,
            Integer page,
            Integer perPage
    ) {
        this.id = id;
        this.name = name;
        this.page = page;
        this.perPage = perPage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Integer page;
        private Integer perPage;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }

        public GetAssetsRequest build(){
            return new GetAssetsRequest(
                    id,
                    name,
                    page,
                    perPage
            );
        }
    }
}
