package assessment.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("SELECT u FROM Blog u WHERE u.owner = :owner")
    public List<Blog> getBlogsByOwner(@Param("owner") String owner);

}