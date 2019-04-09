package bylogics.io.pytheas.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;


public class Route {
    @JsonProperty("routeId")
    private Long routeId;


    @JsonProperty("fromNode")
    private String from;

    @JsonProperty("toNode")
    private String to;


    @JsonProperty("distance")
    private Double distance;
    @JsonProperty("time")

    private double time;

}
