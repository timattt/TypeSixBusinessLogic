package org.shlimtech.typesixdatabasecommon.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shlimtech.typesixdatabasecommon.dto.UserDTO;
import org.shlimtech.typesixdatabasecommon.model.User;
import org.shlimtech.typesixdatabasecommon.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    protected final UserRepository userRepository;
    protected final ModelMapper modelMapper;

    private void complement(User user, UserDTO userDTO) {
        if (userDTO.getGithubLink() != null) {
            user.setGithubLink(userDTO.getGithubLink());
        }
        if (userDTO.getVkLink() != null) {
            user.setVkLink(userDTO.getVkLink());
        }

        // TODO more fields to complement
    }

    @Transactional
    public boolean containsUser(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Transactional
    public UserDTO createOrComplementUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());

        if (user == null) {
            user = modelMapper.map(userDTO, User.class);
        }

        complement(user, userDTO);
        userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
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

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public void setBio(int id, String bio) {
        userRepository.getReferenceById(id).setBiography(bio);
    }

}
