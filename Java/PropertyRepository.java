package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
	
	List<Property> findByOwner(Player player);
    
    List<Property> findByType(String type);

	Property findByRowAndCol(int newRow, int newCol);
	
    List<Property> findByColor(String color);

	static void save(Property property, Long id) {
		// TODO Auto-generated method stub
		
	}

	
}
