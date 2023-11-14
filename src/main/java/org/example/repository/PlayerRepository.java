package org.example.repository;

import org.example.model.Player;

import java.util.List;

public interface PlayerRepository {
    List<Player> getAllPlayers();

    void savePlayer(Player player);
}
