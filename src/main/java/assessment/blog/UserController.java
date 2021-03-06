package assessment.blog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
class UserController {

    private  UserRepository userRepository;
    private  BlogRepository blogRepository;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    UserController(UserRepository repository, BlogRepository blogRepository)

    {
        this.userRepository = repository;
        this.blogRepository = blogRepository;
    }

    //GET methods-----------------------------

    @GetMapping("/")
    List<Blog> home() {
        return allBlogs();
    }

    @GetMapping("/users")
    List<User> allUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/blogs")
    List<Blog> allBlogs() {
        log.info("Received get request for blogs");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null)
        {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal!=null)
            {
                return blogRepository.getBlogsByOwner(((UserDetails)principal).getUsername());
            }
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
    User newRegistration(@RequestBody UserForm form) {
        log.info("Received post request for registration");
        if (userRepository.getUserByEmail(form.getEmail()) == null)
        {
            String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
            if (form.getEmail().matches(regex))
            {
                User newuser = new User();
                newuser.setEmail(form.email);
                newuser.setPassword(form.password);
                newuser.setRole("USER");
                newuser.setEnabled(true);
                log.info("Attempting to register user: " + form.email);
                return userRepository.save(newuser);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "email format is wrong");
            }

        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "email already taken");


    }


    @PostMapping("/blogs")
    Blog newBlogEntry(@RequestBody BlogForm form) {
        log.info("Received post request for blog entry posting");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null)
            {
                Blog newBlog = new Blog();
                newBlog.setOwner(((UserDetails) principal).getUsername());
                newBlog.setTitle(form.getTitle());
                newBlog.setContent(form.getContent());

                log.info("Attempting to create blog entry (user: "
                        + ((UserDetails) principal).getUsername() + ";title:"
                        + form.getTitle() + ")");
                return blogRepository.save(newBlog);
            }
        }
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "user was not logged in"
        );
    }

    @PutMapping("/blogs")
    Blog blogUpdate(@PathVariable Long id, @RequestBody BlogForm form) {
        log.info("Received put request for blog entry editing");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null)
            {
                Blog newBlog = blogRepository.findById(id).get();
                if (newBlog != null)
                {
                    if (((UserDetails) principal).getUsername().equals(newBlog.getOwner()))
                    {
                        newBlog.setTitle(form.getTitle());
                        newBlog.setContent(form.getContent());
                        log.info("Attempting to update blog entry (user: "
                                + ((UserDetails) principal).getUsername() + ";title:"
                                + form.getTitle() + ")");
                        return blogRepository.save(newBlog);
                    } else {
                        log.info("Unauthorized access to blog update: " + id);
                        throw new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "no such blog belongs to logged in user"
                        );
                    }
                }
                else //we could assume put is post for a new entry
                    newBlogEntry(form);
            }
        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "user was not logged in"
        );
    }

    //DELETE methods-----------------------------

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        log.warn("Attempted to delete a user (NOT IMPLEMENTED");
        throw new ResponseStatusException(
                HttpStatus.NOT_IMPLEMENTED, "not implemented"
        );
    }

    @DeleteMapping("/blogs/{id}")
    void deleteBlog(@PathVariable Long id) {
        log.info("Received delete request for blog entry removal");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null)
            {
                    if (((UserDetails) principal).getUsername().equals(blogRepository.findById(id).get().getOwner())) {
                        log.info("Atempting blog deletion: " + id);
                        blogRepository.deleteById(id);
                        return;
                    }
                    log.info("Unauthorized access to blog deletion: " + id + " when owner was " + blogRepository.findById(id).get().getOwner());
                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "no such blog belongs to logged in user"
                    );
            }
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
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth != null) {
                        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if (principal != null) {
                                if (((UserDetails) principal).getUsername().equals(blog.getOwner())) {
                                    blog.setTitle(newBlog.getTitle());
                                    blog.setContent(newBlog.getContent());
                                    return blogRepository.save(blog);
                                }
                                log.info("Unauthorized access to blog change: " + id);
                                throw new ResponseStatusException(
                                        HttpStatus.UNAUTHORIZED, "no such blog belongs to logged in user"
                                );
                        }
                    }
                    log.info("Unauthorized access to blog change: " + id);
                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "user was not logged in"
                    );

                })
                .orElseGet(() -> {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth != null) {
                        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if (principal != null) {


                            newBlog.setId(id);
                            newBlog.setOwner(((UserDetails) principal).getUsername());
                            return blogRepository.save(newBlog);
                        }
                    }
                    log.info("Unauthorized access to blog " + id);
                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "user was not logged in"
                    );
                });
    }




}