package nl.solar.app.rest;

import nl.solar.app.models.ForecastItem;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.jpaRepositories.DashboardRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
@RequestMapping("/forecast")
public class DashboardController {
    @Autowired
    DashboardRepositoryJpa dashboardRepo;
    @GetMapping(produces = "application/json")
    public List<ForecastItem> getAll(){
        return this.dashboardRepo.findAll();
    }
}
