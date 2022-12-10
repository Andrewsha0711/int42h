package com.andrewsha.int42h.domain.user;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andrewsha.int42h.config.Config;
import com.andrewsha.int42h.domain.user.request.CreateUserForm;
import com.andrewsha.int42h.domain.user.request.PatchUserForm;
import com.andrewsha.int42h.exception.UserServiceException;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUser(UUID id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserServiceException(
                        "user with id " + id + " does not exists"));
    }

    @Override
    @Cacheable(key = "#username", value = "loadByUsernameCache")
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        if (Pattern.matches(Config.emailRegexp, username)) {
            return this.userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserServiceException("wrong email"));
        } else {
            if (Pattern.matches(Config.phoneNumberRegexp, username)) {
                return this.userRepository.findByPhoneNumber(username).orElseThrow(
                        () -> new UserServiceException("wrong phone number"));
            } else {
                throw new UserServiceException(
                        "username cannot be resolved to email or phone number");
            }
        }
    }

    public Page<User> getUsers(int page, int size) {
        return this.userRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public User createUser(CreateUserForm userDetails) {
        // TODO to a separate function
        if (userDetails.getEmail() != null) {
            Optional<User> userByEmail =
                    this.userRepository.findByEmail(userDetails.getEmail());
            if (userByEmail.isPresent()) {
                throw new UserServiceException("email taken");
            }
        }
        if (userDetails.getPhoneNumber() != null) {
            Optional<User> userByPhoneNumber =
                    this.userRepository.findByPhoneNumber(userDetails.getPhoneNumber());
            if (userByPhoneNumber.isPresent()) {
                throw new UserServiceException("phone number taken");
            }
        }

        User user = new User();
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setDob(userDetails.getDob());
        user.setProfileIcon(userDetails.getProfileIcon());
        user.setPassword(this.passwordEncoder.encode(userDetails.getPassword()));

        return this.userRepository.save(user);
    }

    @Transactional
    public User patchUser(UUID id, PatchUserForm userDetails) {
        User user = this.getUser(id);
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getName() != null) {
            user.setEmail(userDetails.getName());
        }
        if (userDetails.getPassword() != null) {
            user.setEmail(userDetails.getPassword());
        }
        if (userDetails.getPhoneNumber() != null) {
            user.setEmail(userDetails.getPhoneNumber());
        }
        if (userDetails.getProfileIcon() != null) {
            user.setEmail(userDetails.getProfileIcon());
        }
        return user;
    }

    @Transactional
    public User putUser(UUID id, User userDetails) {
        User user = this.getUser(id);
        user.setEmail(userDetails.getEmail());

        return user;
        // TODO complete
        // Почитать про батчинг сеттеров
        // Доделать про телефон, валидация пароля и почты по регулярным выражениям
    }

    public void deleteUser(UUID id) {
        if (!(this.userRepository.existsById(id))) {
            throw new NoSuchElementException("user with id " + id + "does not exists");
        }
        this.userRepository.deleteById(id);
    }

    public User getAuthenticatedUser(Authentication authentication) {
        if (authentication != null) {
            return (User) authentication.getPrincipal();
        } else {
            throw new UserServiceException("not authenticated");
        }
    }
}
