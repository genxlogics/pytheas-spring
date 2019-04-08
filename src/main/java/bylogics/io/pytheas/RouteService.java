package bylogics.io.pytheas;

import bylogics.io.pytheas.db.Route;
import bylogics.io.pytheas.domain.TrafficRoute;
import bylogics.io.pytheas.repository.PlanetRepository;
import bylogics.io.pytheas.repository.RouteRepository;
import bylogics.io.pytheas.repository.TrafficRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
    private final RouteRepository routeRepository;
    private final RouteEngine routeEngine;
    private final PlanetRepository planetRepository;
    private final TrafficRepository trafficRepository;
    @Autowired
    public RouteService(RouteRepository routeRepository, RouteEngine routeEngine, PlanetRepository planetRepository, TrafficRepository trafficRepository) {
        this.routeRepository = routeRepository;
        this.routeEngine = routeEngine;
        this.planetRepository = planetRepository;
        this.trafficRepository = trafficRepository;
    }

    public Route updateRoute(Route input) {

        Route returned=routeRepository.save(input);
        routeEngine.recalculate();
        return returned;
    }
    public Route createRoute(Route input) {
        Route returned=routeRepository.save(input);
        routeEngine.recalculate();
        return returned;
    }
    public void deleteRoute(Long routeId){
        routeRepository.deleteById(routeId);
        routeEngine.recalculate();
    }
    public List<TrafficRoute> findRoute(Long planetId,boolean trafficInabled){
        String node=planetRepository.getOne(planetId).getNode();
        return trafficRepository.getRouteInfo(trafficInabled,node);
    }

}
