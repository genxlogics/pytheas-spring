package bylogics.io.pytheas.db;

import javax.persistence.*;

@Entity

@Table(name="planets")
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long planetId;

    @Column(name = "node")
    private String node;

    @Column(name = "name")
    private String name;

    public Long getPlanetId() {
        return planetId;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Planet{" +
                "planetId=" + planetId +
                ", node='" + node + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
