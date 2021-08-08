package com.example.webflux.controller;

import com.example.webflux.domain.User;
import com.example.webflux.repository.UserRepository;
import com.example.webflux.util.UserValidationUtil;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/")
  public Flux<User> get() {
    return this.userRepository.findAll();
  }

  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<User> streamGet() {
    return this.userRepository.findAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<User>> getById(@PathVariable("id") String id) {
    return this.userRepository.findById(id).map(usr -> new ResponseEntity<>(usr, HttpStatus.OK))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping(value = "/stream/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Mono<User> streamGet(@PathVariable("id") String id) {
    return this.userRepository.findById(id);
  }

  @PostMapping("/")
  public Mono<User> create(@Valid @RequestBody User user) {
    UserValidationUtil.check(user);
    return this.userRepository.save(user);
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
    return this.userRepository.findById(id)
        .flatMap(user -> this.userRepository.delete(user)
            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<User>> update(@PathVariable("id") String id,
      @Valid @RequestBody User user) {
    UserValidationUtil.check(user);
    return this.userRepository.findById(id)
        .flatMap(usr -> {
          usr.setName(user.getName());
          usr.setAge(user.getAge());
          return this.userRepository.save(usr);
        })
        .map(usr -> new ResponseEntity<>(usr, HttpStatus.OK))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/age/{start}/{end}")
  public Flux<User> getByAge(@PathVariable("start") Integer start,
      @PathVariable("end") Integer end) {
    return this.userRepository.findByAgeBetween(start, end);
  }

  @GetMapping(value = "/stream/age/{start}/{end}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<User> streamGetByAge(@PathVariable("start") Integer start,
      @PathVariable("end") Integer end) {
    return this.userRepository.findByAgeBetween(start, end);
  }
}
