package assessment.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface UserRepository extends JpaRepository<User, Long> {

@Query("SELECT u FROM User u WHERE u.email = :email")
public User getUserByEmail(@Param("email") String email);

}