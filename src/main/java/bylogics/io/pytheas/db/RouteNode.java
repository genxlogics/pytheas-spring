package bylogics.io.pytheas.db;


import com.fasterxml.jackson.annotation.JsonProperty;

public class RouteNode {
    @JsonProperty("prevPlanet")
    private String prevPlanet;
    @JsonProperty("distanceFromEarth")
    private double weight;

    public RouteNode(String prevPlanet, double weight) {
        this.prevPlanet = prevPlanet;
        this.weight = weight;
    }

    public RouteNode() {
    }

    public String getPrevPlanet() {
        return prevPlanet;
    }

    public void setPrevPlanet(String prevPlanet) {
        this.prevPlanet = prevPlanet;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "RouteNode{" +
                "prevPlanet='" + prevPlanet + '\'' +
                ", weight=" + weight +
                '}';
    }
}
