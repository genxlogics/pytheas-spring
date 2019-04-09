package bylogics.io.pytheas.controller;

import bylogics.io.pytheas.db.Route;
import bylogics.io.pytheas.domain.TrafficRoute;
import bylogics.io.pytheas.repository.RouteRepository;
import bylogics.io.pytheas.repository.TrafficRepository;
import bylogics.io.pytheas.service.RouteService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@CrossOrigin("*")
@Api("/pytheas/routes")
public class RouteController {

    private final TrafficRepository trafficRepository;
    private final RouteRepository routeRepository;
    private final RouteService routeService;

    @Autowired
    public RouteController(TrafficRepository trafficRepository, RouteRepository rr, RouteService routeService) {
        this.trafficRepository = trafficRepository;
        this.routeRepository = rr;
        this.routeService = routeService;
    }

    @GetMapping("/discover")
    @ApiOperation(value = "findPath", notes = "Returns the shortest path to the inquired planet with all the intermediate planets included.", response = TrafficRoute[].class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = TrafficRoute[].class)})
    public @ResponseBody
    List<TrafficRoute> findPath(
            @ApiParam(required = true, name = "to", value = "Planet Code of destination planet", defaultValue = "Q")
            @RequestParam(name = "to") String to,
            @ApiParam(name = "traffic", value = "calculate path with/without traffic", defaultValue = "false")
            @RequestParam(name = "traffic", required = false, defaultValue = "false") boolean traffic) {
        System.out.println("Received request for route to " + to + " and traffic usage " + traffic);

        return this.trafficRepository.getRouteInfo(traffic,to);

    }

    @ApiOperation(value = "getRoutes", notes = "Returns all the available routes from planet earth to this planet.", response = Route[].class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Route[].class)})
    @GetMapping("/routes")
    public @ResponseBody
    List<Route> getRoutes(
            @ApiParam(required = true, name = "to", value = "all routes to this planet", defaultValue = "Z")
            @RequestParam(name = "to") String to) {
        return this.routeService.findRoutes(to);

    }

    @PostMapping("/routes")
    public ResponseEntity<Route> addRoute(@Valid @RequestBody Route newRoute) {
        Route returned = routeRepository.save(newRoute);

        return new ResponseEntity<>(returned, HttpStatus.CREATED);
    }

    @PutMapping("/routes/{routeId}")
    public ResponseEntity<Route> updateRoute(@Valid @RequestBody Route updated, @PathVariable("routeId") Long routeId) {
        Route returned = routeService.updateRoute(updated);
        return new ResponseEntity<>(returned, HttpStatus.OK);
    }

    @DeleteMapping("/routes/{routeId}")
    public ResponseEntity<?> removeRoute(@PathVariable("routeId") Long routeId) {
        routeRepository.deleteById(routeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
