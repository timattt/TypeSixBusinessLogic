package org.shlimtech.typesixdatabasecommon.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shlimtech.typesixdatabasecommon.dto.UserDTO;
import org.shlimtech.typesixdatabasecommon.model.User;
import org.shlimtech.typesixdatabasecommon.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private void complement(User user, UserDTO userDTO) {
        if (userDTO.getGithubLink() != null) {
            user.setGithubLink(userDTO.getGithubLink());
        }
        if (userDTO.getVkLink() != null) {
            user.setVkLink(userDTO.getVkLink());
        }

        // TODO more fields to complement
    }

    public void createOrComplementUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());

        if (user == null) {
            user = modelMapper.map(userDTO, User.class);
        }

        complement(user, userDTO);
        userRepository.save(user);
    }

    @Transactional
    public UserDTO loadUser(String email) {
        User user = userRepository.findByEmail(email);
        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional
    public UserDTO loadUser(int id) {
        User user = userRepository.getReferenceById(id);
        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional
    public UserDTO getRandomUser() {
        List<User> users = userRepository.findAll();
        UserDTO userDTO = null;

        if (!users.isEmpty()) {
            userDTO = modelMapper.map(users.get((int) (users.size() * Math.random())), UserDTO.class);
        }

        return userDTO;
    }

}
