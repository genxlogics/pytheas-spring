package bylogics.io.pytheas;

import bylogics.io.pytheas.db.Planet;
import bylogics.io.pytheas.db.Route;
import bylogics.io.pytheas.db.RouteNode;
import bylogics.io.pytheas.repository.PlanetRepository;
import bylogics.io.pytheas.repository.RouteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class RouteEngine {

    private final RouteRepository routeRepository;
    private final PlanetRepository planetRepository;
    @Value("${pytheas.home}")
    private String home;
    @Value("${pytheas.traffic}")
    private String allowTraffic;
    ObjectMapper mapper = new ObjectMapper();

    private Map<String, RouteNode> shortestPathRoute = new HashMap<>();
    private Map<String, RouteNode> shortestPathRouteWithTraffic = new HashMap<>();

    @Autowired
    public RouteEngine(RouteRepository routeRepository, PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
        this.routeRepository = routeRepository;
    }

    public void processRoutes() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, List<Route>> routeList = this.adjacencyList();
        try {
            shortestPathRoute = calculate(routeList, false);
            shortestPathRouteWithTraffic = calculate(routeList, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        try {
//            System.out.println(mapper.writeValueAsString(shortestPathRoute));
//            System.out.println(mapper.writeValueAsString(shortestPathRouteWithTraffic));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }


    }

    public void recalculate() {
        CompletableFuture.runAsync(this::processRoutes);
    }

    public Map<String, RouteNode> getTraceRoute(boolean withTraffic) {
        return withTraffic ? shortestPathRouteWithTraffic : shortestPathRoute;

    }

    private Map<String, RouteNode> calculate(Map<String, List<Route>> neighbourList, boolean considerTraffic) throws Exception {
        Map<String, RouteNode> routeTable = new HashMap<>();
        neighbourList.keySet().forEach(planet -> {
            routeTable.put(planet, new RouteNode("", Integer.MAX_VALUE));
        });
        routeTable.replace("A", new RouteNode("A", 0));

        int vertices = neighbourList.size();
        Set<String> processed = new HashSet<>();

        while (processed.size() != vertices) {
            Map.Entry<String, RouteNode> current = routeTable.entrySet().stream().filter(entry -> !processed.contains(entry.getKey())).min(Comparator.comparingDouble(entry -> entry.getValue().getWeight())).get();
            double covered = current.getValue().getWeight();
            processed.add(current.getKey());
            List<Route> neighbours = neighbourList.get(current.getKey());

            if (null != neighbours) {
                neighbours.forEach(edge -> {
                    double edgeDist = considerTraffic ? edge.getDistance() + edge.getTime() : edge.getDistance();
                    double next = routeTable.get(edge.getTo()).getWeight();
                    if (edgeDist + covered < next) {
                        routeTable.get(edge.getTo()).setWeight(covered + edgeDist);
                        routeTable.get(edge.getTo()).setPrevPlanet(current.getKey());
                    }
                });
            } else {
                System.out.println("No Path found for planet " + current.getKey());
            }

        }
        return routeTable;

    }

    private Map<String, List<Route>> adjacencyList() {
        Map<String, List<Route>> populated = new HashMap<>();
        List<Planet> planets = planetRepository.findAll();
        planets.sort(Comparator.comparingLong(Planet::getPlanetId));
        planets.forEach(planet -> populated.put(planet.getNode(), routeRepository.findRouteByFrom(planet.getNode())));
        return populated;
    }

}
