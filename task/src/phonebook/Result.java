package phonebook;

public class Result {
    private Integer entryQuantity;
    private Integer foundQuantity;
    private Long searchingTime;
    private Long sortingTime;
    private Boolean excededTime;

    public Integer getEntryQuantity() {
        return entryQuantity;
    }

    public void setEntryQuantity(Integer entryQuantity) {
        this.entryQuantity = entryQuantity;
    }

    public Integer getFoundQuantity() {
        return foundQuantity;
    }

    public void setFoundQuantity(Integer foundQuantity) {
        this.foundQuantity = foundQuantity;
    }

    public Long getSearchingTime() {
        return searchingTime;
    }

    public void setSearchingTime(Long searchingTime) {
        this.searchingTime = searchingTime;
    }

    public Long getSortingTime() {
        return sortingTime;
    }

    public void setSortingTime(Long sortingTime) {
        this.sortingTime = sortingTime;
    }

    public Boolean getExcededTime() {
        return excededTime;
    }

    public void setExcededTime(Boolean excededTime) {
        this.excededTime = excededTime;
    }
}
