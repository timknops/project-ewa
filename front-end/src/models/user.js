export const generateUser = (Firstname, Surname, Password, Role, Team, Warehouse) => ({
    Firstname,
    Surname,
    Password,
    Role,
    Team,
    Warehouse,
});

export const users = [
    generateUser("Ben", "van Weg", "********", "Employee", "Team 1", "Solar Sedum"),
    generateUser("Dijkstra", "van der Berg", "********", "Admin", "Team 2", "Solar Sedum"),
    generateUser("Hassan", "Bahara", "********", "Employee", "Team 2", "EHES"),
    generateUser("Ethan", "Bennett", "********", "Employee", "Team 2", "Superzon"),
    generateUser("Jada ", "Salinas", "********", "Employee", "Team 3", "Solar Sedum"),
    generateUser("Shannon ", "Mccall", "********", "Admin", "Team 3", "Superzon"),
    generateUser("Tiago", "Roman", "********", "Employee", "Team 4", "EHES"),
    generateUser("Hugh ", "Key", "********", "Admin", "Team 4", "The switch"),
    generateUser("Walter", "White", "********", "Employee", "Team 4", "The switch"),
];