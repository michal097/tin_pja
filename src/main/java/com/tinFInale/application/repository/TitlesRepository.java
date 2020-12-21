package com.tinFInale.application.repository;


import com.tinFInale.application.model.Titles;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TitlesRepository extends CrudRepository<Titles,Long> {
    List<Titles> findAll();
}
