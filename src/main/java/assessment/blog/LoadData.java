package assessment.blog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadData {

    private static final Logger log = LoggerFactory.getLogger(LoadData.class);

    @Bean
    CommandLineRunner initUserDatabase(UserRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new User("user1@example.com", "p", "USER", true)));
            log.info("Preloading " + repository.save(new User("user2@example.com", "p", "USER", true)));
        };
    }

    @Bean
    CommandLineRunner initBlogDatabase(BlogRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Blog("Test", "Text1", "user1@example.com")));
            log.info("Preloading " + repository.save(new Blog("Test2", "Text2", "user2@example.com")));
            log.info("Preloading " + repository.save(new Blog("Test3", "Text3", "user1@example.com")));
        };
    }
}