package com.epam.learn.service.impl;

import com.epam.learn.dao.TrainingTypeDao;
import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.TrainingType;
import com.epam.learn.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of the TrainingTypeService interface.
 * This service provides operations for creating, retrieving, and managing training types.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;

    @Override
    @Transactional
    public TrainingType create(TrainingType trainingType) {
        LOGGER.info("create:: creating training type");
        if (trainingType.getName() == null || trainingType.getName().isEmpty()) {
            throw new IllegalArgumentException("Training type name cannot be empty");
        }
        
        trainingTypeDao.save(trainingType);
        return trainingType;
    }

    @Override
    public TrainingType getById(UUID id) {
        return trainingTypeDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TRAINING_TYPE, id));
    }

    @Override
    public PagedResponse<TrainingType> getAll(int page, int size) {
        return trainingTypeDao.findAll(page, size);
    }

    @Override
    @Transactional
    public void update(UUID id, TrainingType trainingType) {
        LOGGER.info("update:: updating training type with id: '{}'", id);
        if (trainingType.getName() == null || trainingType.getName().isEmpty()) {
            throw new IllegalArgumentException("Training type name cannot be empty");
        }
        
        trainingTypeDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TRAINING_TYPE, id));
        
        trainingType.setId(id);
        trainingTypeDao.update(id, trainingType);
    }

    @Override
    public TrainingType getByName(String name) {
        LOGGER.info("getByName:: getting training type by name: '{}'", name);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Training type name cannot be empty");
        }
        
        return trainingTypeDao.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    @Transactional
    public TrainingType findOrCreate(String name) {
        LOGGER.info("findOrCreate:: finding or creating training type with name: '{}'", name);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Training type name cannot be empty");
        }
        
        // Try to find existing training type by name (case-insensitive)
        TrainingType existingType = getByName(name);
        
        // If not found, create a new one
        if (existingType == null) {
            LOGGER.info("findOrCreate:: creating new training type with name: '{}'", name);
            TrainingType newType = new TrainingType();
            newType.setName(name);
            return create(newType);
        }
        
        return existingType;
    }
}