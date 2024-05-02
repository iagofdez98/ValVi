package es.uvigo.tfg.valvi.repository;

import java.util.Optional;

import es.uvigo.tfg.valvi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String > {
  
  Optional<User> findByUsernameAndPassword(String username, String password);
}
