package bylogics.io.pytheas.controller;

import bylogics.io.pytheas.db.Planet;
import bylogics.io.pytheas.repository.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlanetController {
    private final PlanetRepository planetRepository;

    @Autowired
    public PlanetController(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @GetMapping("/planets")
    public @ResponseBody
    List<Planet> getPlanets() {
        return planetRepository.findAll();
    }
}
