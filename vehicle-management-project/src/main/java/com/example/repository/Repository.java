package com.example.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    T save(T t) throws Exception;
    T update(T t) throws Exception;
    void delete(Long id) throws Exception;
    Optional<T> findById(Long id) throws Exception;
    List<T> findAll() throws Exception;
}
