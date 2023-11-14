package org.example.repository;

import org.example.Utils;
import org.example.model.Player;

import java.io.*;
import java.util.List;

import static org.example.Utils.MENU;

public class FilePlayerRepository implements PlayerRepository {

    @SuppressWarnings("unchecked")
    @Override
    public List<Player> getAllPlayers() {
        String fileName = "players.obj";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<Player>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return List.of();
        }
    }

    @Override
    public void savePlayer(Player player) {
        List<Player> players = getAllPlayers();
        players.add(player);
        try {
            Utils.changeScene(MENU);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("players.obj"));
            oos.writeObject(players);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
