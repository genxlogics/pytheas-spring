package bylogics.io.pytheas.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

public class TrafficRoute {

    @JsonProperty("planetId")
    private Long planetId;
    @JsonProperty("planetName")
    private String planetName;
    @JsonProperty("planetCode")
    private String planetCode;

    @JsonProperty("distanceFromEarth")
    private Double distance;
    @JsonProperty("prevPlanetId")
    private String prevPlanetId;

    public String getPlanetCode() {
        return planetCode;
    }

    public void setPlanetCode(String planetCode) {
        this.planetCode = planetCode;
    }

    public Long getPlanetId() {
        return planetId;
    }

    public void setPlanetId(Long planetId) {
        this.planetId = planetId;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getPrevPlanetId() {
        return prevPlanetId;
    }

    public void setPrevPlanetId(String prevPlanetId) {
        this.prevPlanetId = prevPlanetId;
    }
}
