package com.khalil.management.user.service;

import com.khalil.management.group.entity.AcademicGroup;
import com.khalil.management.group.repository.AcademicGroupRepository;
import com.khalil.management.user.mapper.UserMapper;
import com.khalil.management.user.dto.UserRequest;
import com.khalil.management.user.dto.UserResponse;
import com.khalil.management.user.entity.User;
import com.khalil.management.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final AcademicGroupRepository groupRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Transactional
    public Integer createUser(UserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("User with email " + request.email() + " already exists.");
        }

        AcademicGroup group = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found with ID: " + request.groupId()));

        var user = mapper.toUser(request);
        user.setGroup(group);
        repository.save(user);

        logger.info("User created with ID: {}", user.getId());
        return user.getId();
    }

    public UserResponse findById(Integer id) {
        logger.info("Fetching user with ID: {}", id);
        return repository.findById(id)
                .map(mapper::toUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }


    public Page<UserResponse> findAll(Pageable pageable) {
        logger.info("Fetching all users with pagination: {}", pageable);
        return repository.findAll(pageable)
                .map(mapper::toUserResponse);
    }

    public List<UserResponse> findByGroupId(Integer groupId) {
        logger.info("Fetching users for group ID: {}", groupId);

        if (!groupRepository.existsById(groupId)) {
            throw new EntityNotFoundException("Group not found with ID: " + groupId);
        }

        return repository.findByGroupId(groupId)
                .stream()
                .map(mapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUser(Integer id, UserRequest request) {
        logger.info("Updating user with ID: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        AcademicGroup group = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found with ID: " + request.groupId()));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setGroup(group);

        repository.save(user);
        logger.info("User with ID: {} updated successfully", id);
    }

    @Transactional
    public void deleteUser(Integer id) {
        logger.info("Deleting user with ID: {}", id);

        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }

        repository.deleteById(id);
        logger.info("User with ID: {} deleted successfully", id);
    }
}
