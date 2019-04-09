package bylogics.io.pytheas.controller;

import bylogics.io.pytheas.db.Planet;
import bylogics.io.pytheas.error.RecordNotFoundException;
import bylogics.io.pytheas.repository.PlanetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@Api("/pytheas/planets")
public class PlanetController {
    private final PlanetRepository planetRepository;

    @Autowired
    public PlanetController(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @GetMapping("/planets")
    @ApiOperation(value = "getPlanets", notes = "Retrieving the list of discovered planets", response = Planet[].class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Planet[].class)})
    public @ResponseBody
    List<Planet> getPlanets() {
        System.out.println("Received request for planet list...");
        return planetRepository.findAll();

    }


    @ApiOperation(value = "getPlanetById", notes = "Retrieve planet detail", response = Planet.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Planet.class)})
    @GetMapping("/planets/{planetId}")
    public @ResponseBody
    Planet getPlanetInfo(@ApiParam(required = true, name = "planetId", value = "ID of planet", defaultValue = "A")
                         @PathVariable("planetId") Long planetId) {
        return planetRepository.findById(planetId).orElseThrow(() -> new RecordNotFoundException("No Such Planet"));
    }
}
