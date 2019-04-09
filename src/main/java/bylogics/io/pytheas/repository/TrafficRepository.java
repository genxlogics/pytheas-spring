package bylogics.io.pytheas.repository;

import bylogics.io.pytheas.RouteEngine;
import bylogics.io.pytheas.db.Planet;
import bylogics.io.pytheas.db.RouteNode;
import bylogics.io.pytheas.domain.TrafficRoute;
import bylogics.io.pytheas.error.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TrafficRepository {
    private final RouteEngine routeEngine;
    private final PlanetRepository planetRepository;

    @Autowired
    public TrafficRepository(RouteEngine re,PlanetRepository pr){
        this.routeEngine=re;
        this.planetRepository=pr;
    }
    public List<TrafficRoute> getRouteInfo(boolean withTraffic,String destinationId){

        Map<String,Planet> lookup=planetRepository.findAll().stream().collect(Collectors.toMap(
           Planet::getNode,
                planet->planet
        ));

        List<TrafficRoute> routes=new ArrayList<>();
        Map<String,RouteNode> cachedMap=routeEngine.getTraceRoute(withTraffic);
        if (lookup.get(destinationId)==null)
            throw new RecordNotFoundException("Planet is not discovered yet ");
        if (cachedMap==null)
            throw new RecordNotFoundException("No path exists from Earth to "+lookup.get(destinationId).getName());

        TrafficRoute tr;
        String current=destinationId;
        do {
            tr=new TrafficRoute();
            tr.setPlanetCode(current);
            tr.setPlanetId(lookup.get(current).getPlanetId());
            tr.setDistance(cachedMap.get(current).getWeight());
            tr.setPrevPlanetId(cachedMap.get(current).getPrevPlanet());
            tr.setPlanetName(lookup.get(current).getName());
            current=cachedMap.get(current).getPrevPlanet();
            routes.add(tr);

        }while (!current.equalsIgnoreCase("A"));

        Collections.reverse(routes);
        return routes;
    }

}
