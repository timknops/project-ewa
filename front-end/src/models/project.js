/**
 * Represents a project that is either in anticipation, in progress, or completed.
 *
 * @author Tim Knops
 */
export class Project {
  static status = {
    UPCOMING: "upcoming",
    IN_PROGRESS: "in progress",
    COMPLETED: "completed",
  };

  /**
   * Constructor of a project.
   *
   * @param id       id of the project
   * @param team     team that is assigned to the project
   * @param name     name of the project
   * @param client   client of the project
   * @param dueDate  due date of the project
   * @param status   status of the project
   */
  constructor(id, team, name, client, dueDate, status) {
    this.id = id;
    this.team = team;
    this.name = name;
    this.client = client;
    this.dueDate = dueDate;
    this.status = status;
  }

  /**
   * Generates a random date between two dates.
   * @param start     start date
   * @param end       end date
   * @returns {Date}  random date between start and end
   * @link https://stackoverflow.com/questions/9035627/elegant-method-to-generate-array-of-random-dates-within-two-dates
   */
  static randomDate(start, end) {
    return new Date(
      start.getTime() + Math.random() * (end.getTime() - start.getTime())
    );
  }

  /**
   * Creates a dummy project.
   *
   * @returns {Project}  a dummy project
   */
  static createDummyProject() {
    const randomId = Math.floor(Math.random() * 1000);
    const randomTeam = "Team " + Math.floor(Math.random() * 10);
    const randomName = "Project " + Math.floor(Math.random() * 100);
    const randomClient = "Client " + Math.floor(Math.random() * 100);

    // Generates a random date between 2012-01-01 and 2024-02-29.
    const randomDueDate = this.randomDate(
      new Date(2022, 0, 1),
      new Date(2026, 1, 0)
    );

    let randomStatus;
    if (randomDueDate < new Date()) {
      // If the due date is in the past, the project is completed.

      randomStatus = Project.status.COMPLETED;
    } else {
      // If the due date is in the future, the project is either upcoming or in progress.

      const random = Math.random();
      if (random < 0.4) {
        // 40% chance of being upcoming.
        randomStatus = Project.status.UPCOMING;
      } else {
        randomStatus = Project.status.IN_PROGRESS;
      }
    }

    return new Project(
      randomId,
      randomTeam,
      randomName,
      randomClient,
      randomDueDate.toISOString().split("T")[0], // Convert date to string in format YYYY-MM-DD.
      randomStatus
    );
  }
}
