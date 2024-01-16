import { shallowMount } from "@vue/test-utils";
import ProjectsOverview from "@/components/ProjectsOverview.vue";
import { ProjectAdaptor } from "@/service/projectAdaptor";
import { SessionSbService } from "@/service/SessionSbService";

// Mock the project adaptor.
jest.mock("@/service/projectAdaptor");

const mockProjects = [
  {
    id: 1,
    projectName: "Project 55",
    team: {
      id: 1,
      team: "Team 307",
    },
    client: "Client 13",
    dueDate: "2025-02-18",
    description:
      "Solar Sedum is proud to announce the successful completion of Project GreenSky, where we transformed urban rooftops into vibrant, eco-friendly spaces.",
    status: "IN_PROGRESS",
  },
  {
    id: 2,
    projectName: "Project 50",
    team: {
      id: 5,
      team: "Team 377",
    },
    client: "Client 3",
    dueDate: "2023-08-02",
    description:
      "Solar Sedum is proud to announce the successful completion of Project GreenSky, where we transformed urban rooftops into vibrant, eco-friendly spaces.",
    status: "COMPLETED",
  },
  {
    id: 3,
    projectName: "Project 23",
    team: {
      id: 6,
      team: "Team 984",
    },
    client: "Client 19",
    dueDate: "2025-03-12",
    description:
      "Superzon is a project that aims to provide solar energy to the entire city of Amsterdam. We are proud to announce that we have successfully completed the first phase of this project.",
    status: "UPCOMING",
  },
];

/**
 * Unit tests for ProjectsOverview component.
 *
 * @test {ProjectsOverview}
 * @group unit/components
 * @groupname projectsOverview
 * @author Tim Knops
 */
describe("ProjectsOverview",  () => {
  let wrapper;
  let mockProjectService;

  beforeEach(async () => {
    mockProjectService = {
      delete: jest.fn(),
      add: jest.fn(),
      update: jest.fn(),
    };

    // Mock the router.
    const $router = {
      push: jest.fn(),
    };

    const sessionSbService = new SessionSbService();
    sessionSbService.saveTokenInBrowserStorage('token', {
      id: 1,
      name: 'admin',
      type: 'admin'
    })

    // Mount the component.
    wrapper = shallowMount(ProjectsOverview, {
      global: {
        provide: {
          projectService: mockProjectService,
          sessionService: sessionSbService,
        },
        mocks: {
          $router,
        },
      },
    });

    await wrapper.vm.$nextTick();
  });

  it("renders the component correctly", () => {
    expect(wrapper.exists()).toBe(true);
  });

  /**
   * Data formatting tests.
   */

  it("should format the project for the table", () => {
    const project = {
      id: 1,
      projectName: "Project 1",
      team: {
        id: 1,
        team: "Team 1",
      },
      client: "Client 1",
      dueDate: "2023-12-31",
      description: "Project Description",
      status: "IN_PROGRESS",
      products: [
        {product_id: 1, quantity: 500},
        {product_id: 2, quantity: 300},
      ],
    };

    const formattedProject = wrapper.vm.formatProjectForTable(project);

    expect(formattedProject).toEqual({
      id: 1,
      name: "Project 1",
      client: "Client 1",
      dueDate: "Dec 31, 2023",
      team: "Team 1",
      status: "IN_PROGRESS",
    });
  });

  it("should format the project for the request", () => {
    const project = {
      id: 1,
      projectName: "Project 1",
      team: 1,
      client: "Client 1",
      dueDate: "2022-12-31",
      description: "Project Description",
      status: "In Progress",
      products: [
        {product_id: 1, quantity: 500},
        {product_id: 2, quantity: 300},
      ],
    };

    const formattedProject = wrapper.vm.formatProjectForRequest(project);

    expect(formattedProject).toEqual({
      project: {
        id: 1,
        projectName: "Project 1",
        team: {id: 1},
        client: "Client 1",
        dueDate: "2022-12-31",
        description: "Project Description",
        status: "IN_PROGRESS",
      },
      resources: [
        {product: {id: 1}, quantity: 500},
        {product: {id: 2}, quantity: 300},
      ],
    });
  });

  it("should filter out products with empty product_id", () => {
    const project = {
      id: 1,
      projectName: "Project 1",
      team: 1,
      client: "Client 1",
      dueDate: "2022-12-31",
      description: "Project Description",
      status: "In Progress",
      products: [
        {product_id: 1, quantity: 500},
        {product_id: "", quantity: 300},
      ],
    };

    const formattedProject = wrapper.vm.formatProjectForRequest(project);

    expect(formattedProject).toEqual({
      project: {
        id: 1,
        projectName: "Project 1",
        team: {id: 1},
        client: "Client 1",
        dueDate: "2022-12-31",
        description: "Project Description",
        status: "IN_PROGRESS",
      },
      resources: [{product: {id: 1}, quantity: 500}],
    });
  });

  it("should format the date", () => {
    const date = new Date("2022-06-01");
    const formattedDate = wrapper.vm.formatDate(date);

    expect(formattedDate).toBe("Jun 1, 2022");
  });

  it("should return empty table data", () => {
    const emptyTableData = wrapper.vm.formatEmptyTableData();

    expect(emptyTableData).toEqual({
      id: "",
      name: "",
      client: "",
      dueDate: "",
      team: "",
      status: "",
    });
  });


  /**
   * CRUD operation tests.
   */

  it("should delete a project successfully", async () => {
    const project = { id: 1 };
    await wrapper.vm.deleteProject(project);
    expect(mockProjectService.delete).toHaveBeenCalledWith(project.id);
    expect(wrapper.vm.projects).not.toContain(project);
    expect(wrapper.vm.showToast).toBe(true);
    expect(wrapper.vm.toastTitle).toBe("Deleted successfully");
    expect(wrapper.vm.toastMessage).toBe("Project deleted.");
  });

  it("should handle error when deleting a project", async () => {
    const project = { id: 1 };
    mockProjectService.delete.mockRejectedValueOnce({
      code: 500,
      message: "Server error",
    });
    await wrapper.vm.deleteProject(project);
    expect(wrapper.vm.showToast).toBe(true);
    expect(wrapper.vm.toastTitle).toBe("Failed to delete");
    expect(wrapper.vm.toastMessage).toBe("Server error");
  });

  /**
   * Toast test.
   */

  it("shows the toast and hides it after a certain duration when showTimedToast method is called", () => {
    jest.useFakeTimers(); // Mock the timers.
    const title = "Success";
    const message = "Project added.";

    // Call the method.
    wrapper.vm.showTimedToast(title, message, 4000);

    // Check if the toast is shown and the title and message are correct.
    expect(wrapper.vm.toastTitle).toBe(title);
    expect(wrapper.vm.toastMessage).toBe(message);
    expect(wrapper.vm.showToast).toBe(true);

    // Advance the timers by 4 seconds.
    jest.advanceTimersByTime(4000);

    // Check if the toast is hidden.
    expect(wrapper.vm.showToast).toBe(false);

    // Restore the timers.
    jest.useRealTimers();
  });

  /**
   * Navigation test.
   */

  it("navigates to specific view when showSpecificView method is called", () => {
    const project = { id: 1 };
    const pushSpy = jest.spyOn(wrapper.vm.$router, "push");

    // Call the method.
    wrapper.vm.showSpecificView(project);

    // Check if the router was called with the correct path.
    expect(pushSpy).toHaveBeenCalledWith(`/projects/${project.id}`);

    // Restore the spy.
    pushSpy.mockRestore();
  });
});
