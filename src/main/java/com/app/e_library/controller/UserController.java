package com.app.e_library.controller;

import com.app.e_library.persistence.pagination.PageRequest;
import com.app.e_library.persistence.pagination.PageResponse;
import com.app.e_library.persistence.pagination.UserSearchCriteria;
import com.app.e_library.service.UserService;
import com.app.e_library.service.dto.UserDto;
import com.app.e_library.validation.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/parse/csv")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> uploadUsers(@RequestParam("users") MultipartFile users) {

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request!");
        }

        if (!Objects.equals(users.getContentType(), "text/csv")){
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
        
        userService.uploadUsers(users);
        return ResponseEntity.ok().body("Users saved successfully!");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Page<UserDto>> getSllUsers(PageRequest pageRequest) {

        PageResponse<UserDto> userPageResponse = userService.getAllUsers(pageRequest);
        HttpHeaders responseHeaders = new HttpHeaders(userPageResponse.buildHttpHeadersForPages());

        return ResponseEntity.ok().headers(responseHeaders).body(userPageResponse.getPage());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'USER')")
    public ResponseEntity<Page<UserDto>> getUsersPage(UserSearchCriteria userSearchCriteria) {

        PageResponse<UserDto> userPageResponse = userService.searchUsers(userSearchCriteria);
        HttpHeaders responseHeaders = new HttpHeaders(userPageResponse.buildHttpHeadersForPages());

        return ResponseEntity.ok().headers(responseHeaders).body(userPageResponse.getPage());
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserDto> createUserProfile(@RequestBody UserDto user) {
        if (UserValidator.isValid(user)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'USER')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId){
        UserDto foundedUser = userService.getById(userId);
        return ResponseEntity.ok().body(foundedUser);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserDto> updateUserProfile(@PathVariable Long userId, @RequestBody UserDto user){
        if (UserValidator.isValid(user)){
            UserDto updateUser = userService.update(userId, user);
            return ResponseEntity.ok().body(updateUser);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId){
        userService.deleteById(userId);
        return ResponseEntity.ok().build();
    }

}
