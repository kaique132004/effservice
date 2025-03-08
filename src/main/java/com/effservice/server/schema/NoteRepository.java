package com.effservice.server.schema;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<NoteEntity, String> {
}