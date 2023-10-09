export const generateTeams = (Team, Warehouse, Type) => ({
    Team,
    Warehouse,
    Type,
});

export const teams = [
    generateTeams("Team 1", "Solar Sedum", "Internal"),
    generateTeams("Team 2", "Solar Sedum", "Internal"),
    generateTeams("Team 2", "EHES", "External"),
    generateTeams("Team 2", "Superzon", "External"),
    generateTeams("Team 3", "Solar Sedum", "Internal"),
    generateTeams("Team 3", "Superzon", "External"),
    generateTeams("Team 4", "EHES", "External"),
    generateTeams("Team 4", "The switch", "External"),
    generateTeams("Team 4", "The switch", "External"),
];