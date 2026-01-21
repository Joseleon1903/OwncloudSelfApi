package com.owncloud.self.api.repository;

import com.owncloud.self.api.domain.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {


}
