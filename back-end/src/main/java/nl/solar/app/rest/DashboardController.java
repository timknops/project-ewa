package nl.solar.app.rest;

import nl.solar.app.DTO.DashboardDTO;
import nl.solar.app.repositories.jpaRepositories.DashboardRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard-items")
public class DashboardController {

    private final DashboardRepositoryJpa dashboardRepo;

    @Autowired
    public DashboardController(DashboardRepositoryJpa dashboardRepo) {
        this.dashboardRepo = dashboardRepo;
    }

    @GetMapping("/inventory")
    public List<DashboardDTO> getDashboardItems() {
        return dashboardRepo.getDashboardItems();
    }

    @GetMapping("/project")
    public List<DashboardDTO> getProjectDashboardItems() {
        return dashboardRepo.getProjectDashboardItems();
    }

}
