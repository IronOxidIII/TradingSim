package com.tradingsim.client.api.portfolios;


public class GetPortfoliosRequest {

    private Integer id;
    private Integer startSum;
    private Double totalSumLess;
    private Double totalSumMore;

    public GetPortfoliosRequest(
            Integer id,
            Integer startSum,
            Double totalSumLess,
            Double totalSumMore
    ) {
        this.id = id;
        this.startSum = startSum;
        this.totalSumLess = totalSumLess;
        this.totalSumMore = totalSumMore;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStartSum() {
        return startSum;
    }

    public void setStartSum(Integer startSum) {
        this.startSum = startSum;
    }

    public Double getTotalSumLess() {
        return totalSumLess;
    }

    public void setTotalSumLess(Double totalSumLess) {
        this.totalSumLess = totalSumLess;
    }

    public Double getTotalSumMore() {
        return totalSumMore;
    }

    public void setTotalSumMore(Double totalSumMore) {
        this.totalSumMore = totalSumMore;
    }

    public static class Builder {
        private Integer id;
        private Integer startSum;
        private Double totalSumLess;
        private Double totalSumMore;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder startSum(Integer startSum) {
            this.startSum = startSum;
            return this;
        }

        public Builder totalSumLess(Double totalSumLess) {
            this.totalSumLess = totalSumLess;
            return this;
        }

        public Builder totalSumMore(Double totalSumMore) {
            this.totalSumMore = totalSumMore;
            return this;
        }

        public GetPortfoliosRequest build(){
            return new GetPortfoliosRequest(
                    id,
                    startSum,
                    totalSumLess,
                    totalSumMore
            );
        }
    }
}