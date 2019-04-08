package bylogics.io.pytheas.repository;

import bylogics.io.pytheas.db.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface RouteRepository extends JpaRepository<Route,Long> {
    List<Route> findRouteByFrom(String starting);
    @Modifying
    @Query("update Route r set r.time = :time where r.from = :from_node and r.to = :to_node")
    @Transactional
    int updateRoute(@Param("time") double time, @Param("from_node")String source,@Param("to_node")String destination);
}
