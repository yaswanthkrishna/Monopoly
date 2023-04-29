// PlayerRepository.java
package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByName(String name);
    
    List<Player> findByMoneyGreaterThan(int amount);

    @Modifying
    @Transactional
    @Query("UPDATE com.example.demo.Player p SET p.row = :row, p.col = :col WHERE p.name = :name")
    void updatePosition(String name, int row, int col);
    @Query("SELECT p.money FROM Player p WHERE p.id = ?1")
    int findMoneyById(Long id);
    
    List<Property> findByOwner(Player player);
    
    List<Property> findByType(String type);

    Property findByRowAndCol(int newRow, int newCol);
	
    List<Property> findByColor(String color);
}
