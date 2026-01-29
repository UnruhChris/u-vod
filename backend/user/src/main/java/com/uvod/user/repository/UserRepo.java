package com.uvod.user.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.uvod.user.model.User;

public interface UserRepo extends CosmosRepository<User, String> {
}
