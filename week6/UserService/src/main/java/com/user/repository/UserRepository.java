package com.user.repository;

//Importing the User entity class to be managed by the repository
import com.user.model.User;

//Importing Spring Data JPA’s base interface for CRUD operations
import org.springframework.data.jpa.repository.JpaRepository;

//Importing Optional for safe return of possibly-null values
import java.util.Optional;

//This interface acts as a repository for User entities.
//It extends JpaRepository, which provides built-in CRUD methods.
public interface UserRepository extends JpaRepository<User, Integer> {

 // Custom query method to find a User by their username.
 // Spring Data JPA will automatically generate the query based on method name.
 Optional<User> findByUsername(String username);
}
