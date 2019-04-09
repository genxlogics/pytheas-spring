package bylogics.io.pytheas.service;

import bylogics.io.pytheas.RouteEngine;
import bylogics.io.pytheas.db.Planet;
import bylogics.io.pytheas.db.Route;
import bylogics.io.pytheas.domain.TrafficRoute;
import bylogics.io.pytheas.error.DataIntigrityException;
import bylogics.io.pytheas.error.RecordNotFoundException;
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
        if (routeRepository.existsRouteByFromAndTo(input.getFrom(), input.getTo())) {
            Route returned = routeRepository.save(input);
            routeEngine.recalculate();
            return returned;
        } else throw new RecordNotFoundException("No Route exists between planets");
    }

    public Route createRoute(Route input) {
        if (routeRepository.existsRouteByFromAndTo(input.getFrom(), input.getTo()))
            throw new DataIntigrityException("route already exists. use update to update this route.");
        Route returned = routeRepository.save(input);
        routeEngine.recalculate();
        return returned;
    }

    public void deleteRoute(Long routeId) {
        routeRepository.deleteById(routeId);
        routeEngine.recalculate();
    }
    public List<Route> findRoutes(String to){
        Planet dest=planetRepository.getPlanetByNode(to);
        if (dest==null)
            throw new RecordNotFoundException("Planet does not exist.");
        return routeRepository.findRoutesByTo(dest.getNode());
    }

    public List<TrafficRoute> findRoute(Long planetId, boolean trafficInabled) {
        String node = planetRepository.getOne(planetId).getNode();
        return trafficRepository.getRouteInfo(trafficInabled, node);
    }

}
