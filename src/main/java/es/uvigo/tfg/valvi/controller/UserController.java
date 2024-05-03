package es.uvigo.tfg.valvi.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import es.uvigo.tfg.valvi.dto.UserDto;
import es.uvigo.tfg.valvi.entity.User;
import es.uvigo.tfg.valvi.service.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static es.uvigo.tfg.valvi.utils.PasswordToKeyConverter.convertPasswordToKey;
import static es.uvigo.tfg.valvi.utils.PasswordToKeyConverter.generateSalt;

/**
 * The type User controller.
 */
@Validated
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/users")
public class UserController {
  
  @Autowired
  private UserService userService;

  /**
   * Authenticate boolean.
   *
   * @param userDto the user dto
   * @return the boolean
   */
  @PostMapping("/authenticate")
  @ResponseStatus(HttpStatus.OK)
  public Map<String, String> authenticate(@RequestBody @NonNull UserDto userDto) {
    return this.userService.authenticate(userDto.getUsername(), userDto.getPassword());
  }

  /**
   * Find user user dto.
   *
   * @param username the username
   * @return the user dto
   */
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public UserDto findUser(@NonNull String username){
    return this.userService.findUser(username);
  }

  /**
   * Upsert user user dto.
   *
   * @param userDto the user dto
   * @return the user dto
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserDto upsertUser(@RequestBody @NonNull UserDto userDto) throws NoSuchAlgorithmException {
    byte[] salt = generateSalt();
    byte[] key = convertPasswordToKey(userDto.getPassword(), salt);
    userDto.setPassword(new String(key));
    
    return this.userService.upsertUser(userDto);
  }

  /**
   * Delete user string.
   *
   * @param username the username
   * @return the string
   */
  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  public String deleteUser(String username){
    return this.userService.deleteUser(username);
  }
  
}
