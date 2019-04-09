package bylogics.io.pytheas.repository;

import bylogics.io.pytheas.db.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;
import java.util.Optional;

public interface PlanetRepository extends JpaRepository<Planet,Long> {
    Planet getPlanetByNode(String nodeId);

}
