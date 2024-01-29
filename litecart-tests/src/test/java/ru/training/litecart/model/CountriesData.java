package ru.training.litecart.model;

// модельный класс структуры таблицы стран
public record CountriesData(String countryName, String countryLink, int zonesCount) {
    public CountriesData() {
        this("", "", 0);
    }

    public CountriesData setCountryName(String countryName) {
        return new CountriesData(countryName, this.countryLink, this.zonesCount);
    }

    public CountriesData setCountryLink(String countryLink) {
        return new CountriesData(this.countryName, countryLink, this.zonesCount);
    }

    public CountriesData setZonesCount(int zonesCount) {
        return new CountriesData(this.countryName, this.countryLink, zonesCount);
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryLink() {
        return countryLink;
    }

    public int getZonesCount() {
        return zonesCount;
    }

}