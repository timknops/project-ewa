import { ProjectAdaptor } from "@/service/projectAdaptor";

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
 * Tests for the ProjectAdaptor service.
 *
 * @group unit/service
 * @groupname ProjectAdaptor
 * @author Tim Knops
 */
describe("ProjectAdaptor", () => {
  let projectAdaptor;

  beforeEach(() => {
    projectAdaptor = new ProjectAdaptor();
  });

  it("fetchJSON should fetch JSON data from the specified URL", async () => {
    const url = "https://example.com/api/data";
    const responseData = { id: 1, name: "John Doe" };

    // Mock the fetch function.
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: jest.fn().mockResolvedValue(responseData),
    });

    // Call the fetchJSON function.
    const result = await projectAdaptor.fetchJSON(url);

    // Check if the fetch function was called with the correct arguments.
    expect(fetch).toHaveBeenCalledWith(url, null);
    expect(result).toEqual(responseData);
  });

  it("fetchJSON should throw an error if the fetch request fails", async () => {
    const url = "https://example.com/api/data";
    const errorResponse = { status: 500, message: "Internal Server Error" };

    // Mock the fetch function
    global.fetch = jest.fn().mockResolvedValue({
      ok: false,
      json: jest.fn().mockResolvedValue(errorResponse),
    });

    // Call the fetchJSON function.
    await expect(projectAdaptor.fetchJSON(url)).rejects.toEqual({
      code: errorResponse.status,
      reason: errorResponse.message,
    });
  });

  it("getAll should return all projects", async () => {
    // Mock the fetchJSON function.
    projectAdaptor.fetchJSON = jest.fn().mockResolvedValue(mockProjects);

    // Call the getAll function.
    const result = await projectAdaptor.getAll();

    // Check if the fetchJSON function was called with the correct arguments.
    expect(result, "getAll shouldn't change data").toEqual(mockProjects);
    expect(
      projectAdaptor.fetchJSON,
      "fetchJSON should be called"
    ).toHaveBeenCalledTimes(1);
  });

  it("get should return a project by its ID", async () => {
    // Mock the fetchJSON function.
    projectAdaptor.fetchJSON = jest.fn().mockResolvedValue(mockProjects[0]);

    // Call the get function.
    const result = await projectAdaptor.get(1);

    // Check if the fetchJSON function was called with the correct arguments.
    expect(result, "get shouldn't change data").toEqual(mockProjects[0]);
    expect(
      projectAdaptor.fetchJSON,
      "fetchJSON should be called"
    ).toHaveBeenCalledTimes(1);
  });

  it("add should add a new project", async () => {
    const newProject = {
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
    };

    // Mock the fetchJSON function.
    projectAdaptor.fetchJSON = jest.fn().mockResolvedValue(newProject);

    // Call the add function.
    const result = await projectAdaptor.add(newProject);

    // Check if the fetchJSON function was called with the correct arguments.
    expect(result, "add shouldn't change data").toEqual(newProject);
    expect(
      projectAdaptor.fetchJSON,
      "fetchJSON should be called"
    ).toHaveBeenCalledTimes(1);
  });

  it("add should throw an error if the project is invalid", async () => {
    const newProject = {
      projectName: "Project 55",
      team: {
        id: 1,
        team: "Team 307",
      },
      client: "Client 13",
      dueDate: "2025-02-18",
      description:
        "Solar Sedum is proud to announce the successful completion of Project GreenSky, where we transformed urban rooftops into vibrant, eco-friendly spaces.",
      status: "INVALID_STATUS",
    };

    // Mock the fetchJSON function.
    projectAdaptor.fetchJSON = jest.fn().mockRejectedValue({
      code: 400,
      reason: "Invalid status",
    });

    // Call the add function.
    await expect(projectAdaptor.add(newProject)).rejects.toEqual({
      code: 400,
      reason: "Invalid status",
    });

    // Check if the fetchJSON function was called with the correct arguments.
    expect(
      projectAdaptor.fetchJSON,
      "fetchJSON should be called"
    ).toHaveBeenCalledTimes(1);
  });

  it("delete should delete a project by its ID", async () => {
    // Mock the fetchJSON function.
    projectAdaptor.fetchJSON = jest.fn().mockResolvedValue(mockProjects[0]);

    // Call the delete function.
    const result = await projectAdaptor.delete(1);

    // Check if the fetchJSON function was called with the correct arguments.
    expect(result, "delete shouldn't change data").toEqual(mockProjects[0]);
    expect(
      projectAdaptor.fetchJSON,
      "fetchJSON should be called"
    ).toHaveBeenCalledTimes(1);
  });

  it("delete should throw an error if the project cannot be deleted", async () => {
    // Mock the fetchJSON function.
    projectAdaptor.fetchJSON = jest.fn().mockRejectedValue({
      code: 400,
      reason: "Invalid status",
    });

    // Call the delete function.
    await expect(projectAdaptor.delete(1)).rejects.toEqual({
      code: 400,
      reason: "Invalid status",
    });

    // Check if the fetchJSON function was called with the correct arguments.
    expect(
      projectAdaptor.fetchJSON,
      "fetchJSON should be called"
    ).toHaveBeenCalledTimes(1);
  });

  it("update should update a project", async () => {
    const updatedProject = {
      project: {
        projectName: "Updated Project Name",
        id: 1,
        team: {
          id: 6,
        },
        client: "Updated Client Name",
        dueDate: "2025-12-31",
        description: "Updated Project Description",
        status: "COMPLETED",
      },
      resources: [
        {
          product: {
            id: 6,
            productName: "Enphase Q Relay 3 fase",
            description: "Dummy description",
          },
          quantity: 3333,
        },
      ],
    };

    // Mock the fetchJSON function.
    projectAdaptor.fetchJSON = jest.fn().mockResolvedValue(updatedProject);

    // Call the update function.
    const result = await projectAdaptor.update(updatedProject);

    // Check if the fetchJSON function was called with the correct arguments.
    expect(result, "update shouldn't change data").toEqual(updatedProject);
    expect(
      projectAdaptor.fetchJSON,
      "fetchJSON should be called"
    ).toHaveBeenCalledTimes(1);
  });

  it("update should throw an error if the project cannot be updated", async () => {
    const updatedProject = {
      project: {
        projectName: "Updated Project Name",
        id: 1,
        team: {
          id: 6,
        },
        client: "Updated Client Name",
        dueDate: "2025-12-31",
        description: "Updated Project Description",
        status: "COMPLETED",
      },
      resources: [
        {
          product: {
            id: 6,
            productName: "Enphase Q Relay 3 fase",
            description: "Dummy description",
          },
          quantity: 3333,
        },
      ],
    };

    // Mock the fetchJSON function.
    projectAdaptor.fetchJSON = jest.fn().mockRejectedValue({
      code: 400,
      reason: "Invalid status",
    });

    // Call the update function.
    await expect(projectAdaptor.update(updatedProject)).rejects.toEqual({
      code: 400,
      reason: "Invalid status",
    });

    // Check if the fetchJSON function was called with the correct arguments.
    expect(
      projectAdaptor.fetchJSON,
      "fetchJSON should be called"
    ).toHaveBeenCalledTimes(1);
  });
});
