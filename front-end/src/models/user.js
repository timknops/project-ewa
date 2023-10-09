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
    generateUser("Dijkstra", "van der Berg", "********", "Admin", "Team 2", "Warehouse 2"),
    generateUser("Hassan", "Bahara", "********", "Employee", "Team 2", "Warehouse 2"),
    generateUser("Ethan", "Bennett", "********", "Employee", "Team 2", "Warehouse 3"),
    generateUser("Jada ", "Salinas", "********", "Employee", "Team 3", "Warehouse 4"),
    generateUser("Shannon ", "Mccall", "********", "Admin", "Team 3", "Warehouse 4"),
    generateUser("Tiago", "Roman", "********", "Employee", "Team 4", "Warehouse 5"),
    generateUser("Hugh ", "Key", "********", "Admin", "Team 4", "Warehouse 5"),
    generateUser("Walter", "White", "********", "Employee", "Team 4", "Warehouse 5"),
];