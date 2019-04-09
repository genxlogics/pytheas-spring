package bylogics.io.pytheas.controller;

import bylogics.io.pytheas.db.Planet;
import bylogics.io.pytheas.error.RecordNotFoundException;
import bylogics.io.pytheas.repository.PlanetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class PlanetController {
    private final PlanetRepository planetRepository;

    @Autowired
    public PlanetController(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @GetMapping("/planets")
    public @ResponseBody
    List<Planet> getPlanets() {
        System.out.println("Received request for planet list...");
        List<Planet> returned=planetRepository.findAll();
        try {
            System.out.println(new ObjectMapper().writeValueAsString(returned));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return returned;
    }
    @GetMapping("/planets/{planetId}")
    public @ResponseBody Planet getPlanetInfo(@PathVariable("planetId") Long planetId){
        return planetRepository.findById(planetId).orElseThrow(()->new RecordNotFoundException("No Such Planet"));
    }
}
