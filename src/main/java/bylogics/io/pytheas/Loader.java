package bylogics.io.pytheas;

import bylogics.io.pytheas.db.Planet;
import bylogics.io.pytheas.db.Route;
import bylogics.io.pytheas.repository.PlanetRepository;
import bylogics.io.pytheas.repository.RouteRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class Loader implements ApplicationListener<ApplicationReadyEvent> {
    private final PlanetRepository planetRepository;
    private final RouteEngine routeEngine;
    @Value("${pytheas.record.limit:0}")
    int records;


    @Autowired
    public Loader(PlanetRepository planetRepository, RouteRepository routeRepository, ResourceLoader rl, RouteEngine re) {
        this.planetRepository = planetRepository;
        this.routeRepository = routeRepository;
        this.rl = rl;
        this.routeEngine = re;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        Workbook routeInfo = null;

        Resource rs = rl.getResource("classpath:routes.xlsx");
        try {
            routeInfo = WorkbookFactory.create(rs.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        Sheet planets = routeInfo.getSheet("Planets");
        Sheet routes = routeInfo.getSheet("Routes");
        Sheet traffic = routeInfo.getSheet("Traffic");


        List<Planet> seedPlanets = new ArrayList<>();
        List<Route> seedRoutes = new ArrayList<>();
        int processing = records == 0 ? planets.getLastRowNum() : records;
        for (int i = 1; i <= processing; i++) {
            Planet item = new Planet();
            item.setNode(planets.getRow(i).getCell(0).getStringCellValue());
            item.setName(planets.getRow(i).getCell(1).getStringCellValue());
            seedPlanets.add(item);
        }
        processing = records == 0 ? routes.getLastRowNum() : records;
        for (int i = 1; i <= processing; i++) {
            Route route = new Route();
            route.setFrom(routes.getRow(i).getCell(1).getStringCellValue());
            route.setTo(routes.getRow(i).getCell(2).getStringCellValue());
            route.setDistance(routes.getRow(i).getCell(3).getNumericCellValue());

            seedRoutes.add(route);

        }
        addPlanets(seedPlanets);
        addRoutes(seedRoutes);

        System.out.println("Initialization done..");
        System.out.println("Triggering Route Engine..");
        insertTrafficInfo(traffic);
        routeEngine.processRoutes();


    }

    public void insertTrafficInfo(Sheet traffic) {
        int processing = records == 0 ? traffic.getLastRowNum() : records;
        for (int i = 1; i <= processing; i++) {
            double time = traffic.getRow(i).getCell(3).getNumericCellValue();
            String from = traffic.getRow(i).getCell(1).getStringCellValue();
            String to = traffic.getRow(i).getCell(2).getStringCellValue();
            if (time > 0.0) routeRepository.updateRoute(time, from, to);
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> ke) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(ke.apply(t), Boolean.TRUE) == null;
    }

    public void addPlanets(List<Planet> planets) {
        // only adding planets with unique code.
        planets.stream().filter(distinctByKey(Planet::getNode)).forEach(planetRepository::save);

    }

    public void addRoutes(List<Route> routes) {
        // ignoring all negative weighted edges.
        routes.stream().filter(r -> r.getDistance() > 0).forEach(routeRepository::save);


    }

    private final RouteRepository routeRepository;
    private final ResourceLoader rl;
}
