package assessment.blog;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserController {

    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]

    @GetMapping("/")
    List<User> home() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()!=null)
        {
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        }
        return repository.findAll();
    }
    // end::get-aggregate-root[]


    @GetMapping("/employees")
    List<User> all() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()!=null)
        {
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        }
        return repository.findAll();
    }
    // end::get-aggregate-root[]




    @PostMapping("/employees")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item

    @GetMapping("/employees/{id}")
    User one(@PathVariable Long id) throws Exception {

        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/employees/{id}")
    User replaceEmployee(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setRole(newUser.getRole());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/employees/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }






}