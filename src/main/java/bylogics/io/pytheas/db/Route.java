package bylogics.io.pytheas.db;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("routeId")
    @Column(name = "id")
    private Long routeId;

    @Column(name = "from_node")
    @JsonProperty("fromNode")
    private String from;
    @Column(name = "to_node")
    @JsonProperty("toNode")
    private String to;

    @Column(name = "distance")
    @JsonProperty("distance")
    private Double distance;
    @JsonProperty("time")
    @Column(name = "time")
    private double time;

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeId=" + routeId +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", distance=" + distance +
                '}';
    }
}
