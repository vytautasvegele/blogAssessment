package assessment.blog;

import org.springframework.data.jpa.repository.JpaRepository;

interface BlogData extends JpaRepository<Blog, Long> {

}