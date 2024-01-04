import { shallowMount } from "@vue/test-utils";
import ProjectsOverview from "@/components/ProjectsOverview.vue";
import { ProjectAdaptor } from "@/service/projectAdaptor";

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
describe("ProjectsOverview", () => {
  let wrapper;

  beforeEach(() => {
    // Mock the project adaptor.
    ProjectAdaptor.mockImplementation(() => {
      return {
        getAll: jest.fn().mockResolvedValue(mockProjects),
      };
    });

    // Mount the component.
    wrapper = shallowMount(ProjectsOverview, {
      data() {
        return {
          projectService: new ProjectAdaptor(),
        };
      },
    });
  });

  it("renders the component correctly", () => {
    expect(wrapper.exists()).toBe(true);
  });

  /**
   * Data formatting tests.
   */

  it("formats project correctly for table", () => {
    const project = {
      id: 1,
      projectName: "Project 1",
      client: "Client 1",
      dueDate: "2023-12-31",
      team: { team: "Team 1" },
      status: "In Progress",
    };

    const formattedProject = wrapper.vm.formatProjectForTable(project);

    expect(formattedProject).toEqual({
      id: 1,
      name: "Project 1",
      client: "Client 1",
      dueDate: "Dec 31, 2023",
      team: "Team 1",
      status: "In Progress",
    });
  });

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
        { product_id: 1, quantity: 500 },
        { product_id: 2, quantity: 300 },
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
        { product_id: 1, quantity: 500 },
        { product_id: 2, quantity: 300 },
      ],
    };

    const formattedProject = wrapper.vm.formatProjectForRequest(project);

    expect(formattedProject).toEqual({
      project: {
        id: 1,
        projectName: "Project 1",
        team: { id: 1 },
        client: "Client 1",
        dueDate: "2022-12-31",
        description: "Project Description",
        status: "IN_PROGRESS",
      },
      resources: [
        { product: { id: 1 }, quantity: 500 },
        { product: { id: 2 }, quantity: 300 },
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
        { product_id: 1, quantity: 500 },
        { product_id: "", quantity: 300 },
      ],
    };

    const formattedProject = wrapper.vm.formatProjectForRequest(project);

    expect(formattedProject).toEqual({
      project: {
        id: 1,
        projectName: "Project 1",
        team: { id: 1 },
        client: "Client 1",
        dueDate: "2022-12-31",
        description: "Project Description",
        status: "IN_PROGRESS",
      },
      resources: [{ product: { id: 1 }, quantity: 500 }],
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

  it("should update a project when updateProject method is called", async () => {
    // Mock the projectService
    const mockProjectService = {
      update: jest
        .fn()
        .mockResolvedValue({ id: 1, projectName: "Updated Project" }),
    };
    wrapper.vm.projectService = mockProjectService;

    // Set up the initial project data
    const initialProject = {
      id: 1,
      projectName: "Project",
      client: "Client",
      dueDate: "2023-12-31",
      team: "Team",
      status: "UPCOMING",
    };
    wrapper.setData({ projects: [initialProject] });

    // Call the updateProject method.
    await wrapper.vm.updateProject(initialProject);

    // Wait for Vue to update the DOM.
    await wrapper.vm.$nextTick();

    // Check if the projectService update method was called with the correct arguments.
    expect(mockProjectService.update).toHaveBeenCalledWith({
      project: {
        id: 1,
        projectName: "Project",
        team: { id: 1 },
        client: "Client",
        dueDate: "2023-12-31",
        description: undefined,
        status: "UPCOMING",
      },
      resources: [],
    });

    // Check if the project was updated in the projects array.
    expect(wrapper.vm.projects[0].projectName).toBe("Updated Project");
  });

  it("should show an error toast when updateProject fails", async () => {
    // Mock the projectService to throw an error.
    const mockProjectService = {
      update: jest
        .fn()
        .mockRejectedValue(new Error("Failed to update project")),
    };
    wrapper.vm.projectService = mockProjectService;

    // Set up the initial project data
    const initialProject = {
      id: 1,
      projectName: "Project",
      client: "Client",
      dueDate: "2023-12-31",
      team: "Team",
      status: "UPCOMING",
    };
    wrapper.setData({ projects: [initialProject] });

    // Call the updateProject method
    await wrapper.vm.updateProject(initialProject);

    // Check if the projectService update method was called with the correct arguments
    expect(mockProjectService.update).toHaveBeenCalledWith({
      project: {
        id: 1,
        projectName: "Project",
        team: { id: "Team" },
        client: "Client",
        dueDate: "2023-12-31",
        description: undefined,
        status: "UPCOMING",
      },
      resources: [],
    });

    // Check if the error toast is shown
    expect(wrapper.vm.showToast).toBe(true);
    expect(wrapper.vm.toastTitle).toBe("Failed to update");
    expect(wrapper.vm.toastMessage).toBe("Failed to update project");
  });
});
