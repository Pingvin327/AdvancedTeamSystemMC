package org.pingvin.eggCaptureEvent;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;




public class TeamSystem {
    MinecraftTeamManager minecraftTeamManager;
    ArrayList<Team> teamList;
    ArrayList<String> usedNames;
    SaveManager saveManager;
    int playerAccessLevel = 0;

    public TeamSystem(EggCaptureEvent plugin) {
        minecraftTeamManager = new MinecraftTeamManager(plugin);
        this.teamList = new ArrayList<>();
        this.usedNames = new ArrayList<>();
        this.saveManager = new SaveManager();
    }


    private class SaveManager {

        public boolean loadTeams() {
            File pluginDirectory = new File("plugins/EggCaptureEvent");
            pluginDirectory.mkdir();

            File saveTeamFile = new File("plugins/EggCaptureEvent/team-save.txt");

            try (Scanner scanner = new Scanner(saveTeamFile)) {
                while (scanner.hasNextLine()) {
                    String teamString = scanner.nextLine();
                    addTeam(teamString);
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        public boolean saveTeamsFull() {
            File pluginDirectory = new File("plugins/EggCaptureEvent");
            pluginDirectory.mkdir();

            try (FileWriter writer = new FileWriter("plugins/EggCaptureEvent/team-save.txt")) {
                for (Team team:teamList) {
                    writer.write(team.exportToString());
                    writer.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

    }

    public boolean saveTeams() {
        return saveManager.saveTeamsFull();
    }

    public boolean loadTeams() {
        return saveManager.loadTeams();
    }


    public boolean addTeam(String saveTeamString) {
        Team team = new Team(this);
        if (!team.loadFromString(saveTeamString)) { return false; };
        if (team.isDisplay) {
            minecraftTeamManager.createTeamMC(team.displayName, team.color, true, true);
        }
        return teamList.add(team);
    }
    public boolean addTeam(String name, String color, boolean isDisplay) {
        Team team = new Team(this);
        if (usedNames.contains(name)) { return false; };
        team.setDisplayName(name);
        team.setColor(color);
        team.setIsDisplay(isDisplay);
        if (isDisplay) {
            minecraftTeamManager.createTeamMC(name, color, true, true);
        }
        return teamList.add(team);
    }

    public Team getTeam(String name) {
        for (Team team:teamList){
            if (team.displayName.equals(name)){
                return team;
            }
        }
        return null;
    }

}
