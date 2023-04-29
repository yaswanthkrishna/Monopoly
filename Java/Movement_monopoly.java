package com.example.demo;

public class Movement_monopoly {
    private int row;
    private int col;
    private PlayerRepository playerRepository;
    private String playerName; // Add this instance variable

    public Movement_monopoly(String playerName, PlayerRepository playerRepository) {
        this.playerName = playerName; // Store playerName
        this.playerRepository = playerRepository;
        Player currentPlayer = playerRepository.findByName(playerName);
        this.row = currentPlayer.getRow();
        this.col = currentPlayer.getCol();
    }

    public void move(int steps) {
        // Movement logic
        if (row == 0 && col > 0) {
            col = Math.max(col - steps, 0);
        } else if (col == 0 && row < 10) {
            row = Math.min(row + steps, 10);
        } else if (row == 10 && col < 10) {
            col = Math.min(col + steps, 10);
        } else if (col == 10 && row > 0) {
            if (row == 10 && steps > 0) {
                steps -= 1;
                row -= 1;
            }
            row = Math.max(row - steps, 0);
        }
        
        
        // Update player position in the database
        playerRepository.updatePosition(playerRepository.findByName(playerName).getName(), row, col);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
