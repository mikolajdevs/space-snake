package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Utils;
import org.example.model.Player;
import org.example.repository.PlayerRepository;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static org.example.Utils.MENU;

public class HighScoresController {

    @FXML
    TableView<Player> tableView;
    @FXML
    TableColumn<Player, Double> tableColumnScore;
    @FXML
    TableColumn<Player, String> tableColumnName;
    @FXML
    Button menu;

    private final PlayerRepository playerRepository;

    public HighScoresController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void initialize() {
        List<Player> players = playerRepository.getAllPlayers();

        Comparator<Player> compareByScore = Comparator.comparingDouble(Player::getScore);
        players.sort(compareByScore.reversed());

        ObservableList<Player> list = FXCollections.observableArrayList(players);

        tableColumnScore.setReorderable(false);
        tableColumnName.setReorderable(false);
        tableColumnScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableView.setItems(list);
    }

    public void backToMenu() throws IOException {
        Utils.changeScene(MENU);
    }
}
