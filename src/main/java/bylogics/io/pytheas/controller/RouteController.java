package bylogics.io.pytheas.controller;

import bylogics.io.pytheas.db.Route;
import bylogics.io.pytheas.domain.TrafficRoute;
import bylogics.io.pytheas.repository.RouteRepository;
import bylogics.io.pytheas.repository.TrafficRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class RouteController {

    private final TrafficRepository trafficRepository;
    private final RouteRepository routeRepository;
    @Autowired
    public RouteController(TrafficRepository trafficRepository, RouteRepository rr) {
        this.trafficRepository = trafficRepository;
        this.routeRepository=rr;
    }

    @GetMapping("/routes")
    public @ResponseBody
    List<TrafficRoute> getRoutes(@RequestParam(name = "to")String to,
                                 @RequestParam(name = "traffic",required = false,defaultValue = "false")boolean traffic) {
        return this.trafficRepository.getRouteInfo(traffic,to);

    }
    @PostMapping("/routes")
    public ResponseEntity<Route> addRoute(@RequestBody Route newRoute) {
        Route returned=routeRepository.save(newRoute);

        return new ResponseEntity<>(returned, HttpStatus.CREATED);
    }
    @PutMapping("/routes/{routeId}")
    public ResponseEntity<Route> updateRoute(@RequestBody Route updated,@PathVariable("routeId")Long routeId) {
        Route returned=routeRepository.save(updated);
        return new ResponseEntity<>(returned,HttpStatus.OK);
    }
    @DeleteMapping("/routes/{routeID}")
    public ResponseEntity<?> removeRoute(@PathVariable("routeId")Long routeId) {
        routeRepository.deleteById(routeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
