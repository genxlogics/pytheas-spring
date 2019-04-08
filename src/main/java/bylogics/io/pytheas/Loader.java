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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@Component
public class Loader implements ApplicationListener<ApplicationReadyEvent> {
    private final PlanetRepository planetRepository;
    private final RouteEngine routeEngine;
    @Value("${pytheas.record.limit:0}")
    int records;

    @Autowired
    public Loader(PlanetRepository planetRepository, RouteRepository routeRepository, ResourceLoader rl,RouteEngine re) {
        this.planetRepository = planetRepository;
        this.routeRepository = routeRepository;
        this.rl = rl;
        this.routeEngine=re;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        //DataFormatter df=new DataFormatter();

        Workbook routeInfo = null;

        //System.out.println("class loader : "+rl.getClassLoader().);
        Resource rs = rl.getResource("classpath:routes.xlsx");
        //System.out.println(rs.getFilename());
//
        try {
            routeInfo = WorkbookFactory.create(rs.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        //System.out.println("No of sheets : " + routeInfo.getNumberOfSheets());
        Sheet planets = routeInfo.getSheet("Planets");
        Sheet routes=routeInfo.getSheet("Routes");
        Sheet traffic=routeInfo.getSheet("Traffic");

        System.out.println("Processing only "+records+" records..");
        //System.out.println("Planets are being loaded");

        List<Planet> seedPlanets = new ArrayList<>();
        List<Route> seedRoutes=new ArrayList<>();
        int processing=records==0?planets.getLastRowNum():records;
        for (int i = 1; i <= processing; i++) {
            Planet item = new Planet();
            item.setNode(planets.getRow(i).getCell(0).getStringCellValue());
            item.setName(planets.getRow(i).getCell(1).getStringCellValue());
            seedPlanets.add(item);
            //System.out.println("Adding a planet.."+item.toString());
            //planetRepository.save(item);

        }
        processing=records==0?routes.getLastRowNum():records;
       // System.out.println("Total planets are "+planets.getPhysicalNumberOfRows());
        for(int i=1;i<=processing;i++){
            Route route=new Route();
            route.setFrom(routes.getRow(i).getCell(1).getStringCellValue());
            route.setTo(routes.getRow(i).getCell(2).getStringCellValue());
            route.setDistance(routes.getRow(i).getCell(3).getNumericCellValue());

            seedRoutes.add(route);

        }

        //System.out.println("total routes : "+routes.getPhysicalNumberOfRows());




        addPlanets(seedPlanets);
        addRoutes(seedRoutes);

        System.out.println("Initialization done..");
        System.out.println("Triggering Route Engine..");
        insertTrafficInfo(traffic);
        routeEngine.processRoutes();


    }
    public void insertTrafficInfo(Sheet traffic){
        int processing=records==0?traffic.getLastRowNum():records;
        for (int i=1;i<=processing;i++){
            double time=traffic.getRow(i).getCell(3).getNumericCellValue();
            String from=traffic.getRow(i).getCell(1).getStringCellValue();
            String to=traffic.getRow(i).getCell(2).getStringCellValue();
            routeRepository.updateRoute(time,from,to);
            //System.out.println("added traffic info for "+from.concat(" "+to));
        }
    }

    public void addPlanets(List<Planet> planets) {
        planets.forEach(planetRepository::save);
        //System.out.println("Total planets in db: "+planetRepository.count());
    }

    public void addRoutes(List<Route> routes) {
        routes.forEach(routeRepository::save);
        System.out.println("Routes Added..");

        //System.out.println("total routes in db : "+routeRepository.count());
        //routeRepository.findRouteByFrom("I").forEach(System.out::print);

    }

    private final RouteRepository routeRepository;
    private final ResourceLoader rl;
}
