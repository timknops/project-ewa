import { shallowMount } from "@vue/test-utils";
import ProjectsOverview from "@/components/ProjectsOverview.vue";
import { ProjectAdaptor } from "@/service/projectAdaptor";

jest.mock("@/service/projectAdaptor");

describe("ProjectsOverview", () => {
  let wrapper;

  beforeEach(() => {
    // Mock the project adaptor.
    ProjectAdaptor.mockImplementation(() => {
      return {
        getAll: jest.fn().mockResolvedValue([
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
        ]),
      };
    });

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

  it("initializes data correctly", () => {
    expect(wrapper.vm.projects).toEqual([]);
    expect(wrapper.vm.projectsAreLoading).toBe(true);
    expect(wrapper.vm.showModal).toBe(false);
    expect(wrapper.vm.modalTitle).toBe("");
    expect(wrapper.vm.modalBodyComponent).toBe("");
    expect(wrapper.vm.modalProject).toEqual({
      id: Number,
      projectName: String,
      client: String,
      dueDate: Date,
      team: String,
      status: String,
    });
    expect(wrapper.vm.okBtnText).toBe("");
    expect(wrapper.vm.showToast).toBe(false);
    expect(wrapper.vm.toastTitle).toBe("");
    expect(wrapper.vm.toastMessage).toBe("");
  });

  it("fetches projects on created lifecycle hook", async () => {
    const mockProjectService = {
      getAll: jest.fn().mockResolvedValue([]),
    };
    wrapper.setData({ projectService: mockProjectService });

    await wrapper.vm.created();

    expect(mockProjectService.getAll).toHaveBeenCalled();
    expect(wrapper.vm.projects).toEqual([wrapper.vm.formatEmptyTableData()]);
    expect(wrapper.vm.projectsAreLoading).toBe(false);
  });

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
      status: "IN_PROGRESS",
    });
  });

  it("formats project correctly for request", () => {
    const project = {
      id: 1,
      projectName: "Project 1",
      team: 1,
      client: "Client 1",
      dueDate: "2023-12-31",
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
        dueDate: "2023-12-31",
        description: "Project Description",
        status: "IN_PROGRESS",
      },
      resources: [
        { product: { id: 1 }, quantity: 500 },
        { product: { id: 2 }, quantity: 300 },
      ],
    });
  });
});
