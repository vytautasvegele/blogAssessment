package assessment.blog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

@RestController
class UserController {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    UserController(UserRepository repository, BlogRepository blogRepository)

    {
        this.userRepository = repository;
        this.blogRepository = blogRepository;
    }

    //GET methods-----------------------------

    @GetMapping("/")
    List<User> home() {
        return userRepository.findAll();
    }

    @GetMapping("/users")
    List<User> allUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/blogs")
    List<Blog> allBlogs() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal!=null)
        {
            return blogRepository.getBlogsByOwner(((UserDetails)principal).getUsername());
        }
        log.info("Unauthorized access to blogs");
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "user was not logged in"
        );

    }

    //POST methods-----------------------------


    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @PostMapping("/register")
    User newUser(@RequestBody String email, @RequestBody String password) {
        User newuser = new User();
        newuser.setEmail(email);
        newuser.setPassword(password);
        return userRepository.save(newuser);
    }


    @PostMapping("/blogs")
    Blog newBlog(@RequestBody Blog newBlog) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal!=null)
        {
            newBlog.setOwner(((UserDetails)principal).getUsername());
            return blogRepository.save(newBlog);
        }
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "user was not logged in"
        );

    }

    //DELETE methods-----------------------------

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        throw new ResponseStatusException(
                HttpStatus.NOT_IMPLEMENTED, "not implemented"
        );
    }

    @DeleteMapping("/blogs/{id}")
    void deleteBlog(@PathVariable Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal!=null)
        {
            if (((UserDetails) principal).getUsername().equals(blogRepository.findById(id).get().getOwner()))
            {
                blogRepository.deleteById(id);
            }
            log.info("Unauthorized access to blog deletion: " + id);
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "no such blog belongs to logged in user"
            );
        }
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "user was not logged in"
        );


    }

    //PUT methods-----------------------------


    @PutMapping("/blogs/{id}")
    Blog replaceBlog(@RequestBody Blog newBlog, @PathVariable Long id) {

        return blogRepository.findById(id)
                .map(blog -> {
                    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    if (principal!=null)
                    {
                        if (((UserDetails) principal).getUsername().equals(blog.getOwner()))
                        {
                            blog.setTitle(newBlog.getTitle());
                            blog.setContent(newBlog.getContent());
                            return blogRepository.save(blog);
                        }
                        log.info("Unauthorized access to blog change: " + id);
                        throw new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "no such blog belongs to logged in user"
                        );
                    }
                    log.info("Unauthorized access to blog change: " + id);
                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "user was not logged in"
                    );

                })
                .orElseGet(() -> {
                    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    if (principal!=null)
                    {
                        newBlog.setId(id);
                        newBlog.setOwner(((UserDetails) principal).getUsername());
                        return blogRepository.save(newBlog);
                    }
                    log.info("Unauthorized access to blog " + id);
                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "user was not logged in"
                    );
                });
    }



    // Single item

//    @GetMapping("/employees/{id}")
//    User one(@PathVariable Long id) throws Exception {
//
//        return userRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundException(id));
//    }

//    @PutMapping("/employees/{id}")
//    User replaceEmployee(@RequestBody User newUser, @PathVariable Long id) {
//
//        return userRepository.findById(id)
//                .map(user -> {
//                    user.setUsername(newUser.getUsername());
//                    user.setRole(newUser.getRole());
//                    return userRepository.save(user);
//                })
//                .orElseGet(() -> {
//                    newUser.setId(id);
//                    return userRepository.save(newUser);
//                });
//    }




}