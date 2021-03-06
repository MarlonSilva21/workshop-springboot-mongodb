package com.marlonsilva.workshopmongo.resources;

import com.marlonsilva.workshopmongo.domain.Post;
import com.marlonsilva.workshopmongo.domain.User;
import com.marlonsilva.workshopmongo.dto.UserDTO;
import com.marlonsilva.workshopmongo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {

        List<User> listUsers = userService.findAll();
        List<UserDTO> listUsersDTO = listUsers.stream().map(UserDTO::new).collect(Collectors.toList());

        return ResponseEntity.ok().body(listUsersDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable String id) {

        User user = userService.findById(id);
        UserDTO userDTO = new UserDTO(user);

        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping
    public ResponseEntity<Void> insertUser(@RequestBody UserDTO userDTO) {

        User user = userService.fromDTO(userDTO);
        user = userService.insert(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).build();

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO, @PathVariable String id) {

        User user = userService.fromDTO(userDTO);
        user.setId(id);
        user = userService.updateUser(user);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/posts")
    public ResponseEntity<List<Post>> findPosts(@PathVariable String id) {

        User user = userService.findById(id);

        return ResponseEntity.ok().body(user.getPosts());
    }
}
