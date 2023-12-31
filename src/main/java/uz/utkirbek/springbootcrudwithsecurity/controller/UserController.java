package uz.utkirbek.springbootcrudwithsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.utkirbek.springbootcrudwithsecurity.dto.ApiResponse;
import uz.utkirbek.springbootcrudwithsecurity.dto.UserDto;
import uz.utkirbek.springbootcrudwithsecurity.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: utkirbek.
 * Time: 21:41:58
 * Date: July 04, 2023, Tuesday
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('WORKER','MANAGER','DIRECTOR')")
    public ResponseEntity<?> findAll(@RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "size", required = false) Integer size){
        if (page==null || size==null)
        return ResponseEntity.ok(userService.getAll());
        else return ResponseEntity.ok(userService.getAll(page,size));
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('WORKER','MANAGER','DIRECTOR')")
    public ResponseEntity<?> getOne(@PathVariable Integer id){
        return ResponseEntity.ok(userService.getOne(id));
    }

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('DIRECTOR')")
    public ResponseEntity<?> save(@Valid @RequestBody UserDto user){
        ApiResponse apiResponse = userService.save(user);
       return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('DIRECTOR')")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        ApiResponse apiResponse = userService.delete(id);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PutMapping
    @PreAuthorize(value = "hasAnyRole('DIRECTOR')")
    public ResponseEntity<?> update(@Valid @RequestBody UserDto user){
        ApiResponse apiResponse = userService.update(user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
