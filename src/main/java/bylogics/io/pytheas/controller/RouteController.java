package bylogics.io.pytheas.controller;

import bylogics.io.pytheas.db.Route;
import bylogics.io.pytheas.domain.TrafficRoute;
import bylogics.io.pytheas.repository.RouteRepository;
import bylogics.io.pytheas.repository.TrafficRepository;
import bylogics.io.pytheas.service.RouteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@CrossOrigin("*")
public class RouteController {

    private final TrafficRepository trafficRepository;
    private final RouteRepository routeRepository;
    private final RouteService routeService;
    @Autowired
    public RouteController(TrafficRepository trafficRepository, RouteRepository rr, RouteService routeService) {
        this.trafficRepository = trafficRepository;
        this.routeRepository=rr;
        this.routeService = routeService;
    }

    @GetMapping("/discover")
    public @ResponseBody
    List<TrafficRoute> findPath(@RequestParam(name = "to")String to,
                                 @RequestParam(name = "traffic",required = false,defaultValue = "false")boolean traffic) {
        System.out.println("Received request for route to "+to+" and traffic usage "+traffic);
        List<TrafficRoute> routes=trafficRepository.getRouteInfo(traffic,to);
        try {
            System.out.println(new ObjectMapper().writeValueAsString(routes));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return routes;
        //return this.trafficRepository.getRouteInfo(traffic,to);

    }
    @GetMapping("/routes")
    public @ResponseBody
    List<Route> getRoutes(@RequestParam(name = "to")String to) {
        return this.routeService.findRoutes(to);

    }
    @PostMapping("/routes")
    public ResponseEntity<Route> addRoute(@Valid @RequestBody Route newRoute) {
        Route returned=routeRepository.save(newRoute);

        return new ResponseEntity<>(returned, HttpStatus.CREATED);
    }
    @PutMapping("/routes/{routeId}")
    public ResponseEntity<Route> updateRoute(@Valid @RequestBody Route updated,@PathVariable("routeId")Long routeId) {
        Route returned=routeService.updateRoute(updated);
        return new ResponseEntity<>(returned,HttpStatus.OK);
    }
    @DeleteMapping("/routes/{routeID}")
    public ResponseEntity<?> removeRoute(@PathVariable("routeId")Long routeId) {
        routeRepository.deleteById(routeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
