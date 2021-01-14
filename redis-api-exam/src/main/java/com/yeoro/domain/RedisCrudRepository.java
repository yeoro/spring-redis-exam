package com.yeoro.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisCrudRepository extends CrudRepository<RedisCrud, Long>{

}
