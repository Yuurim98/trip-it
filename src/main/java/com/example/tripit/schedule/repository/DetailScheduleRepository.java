package com.example.tripit.schedule.repository;

import com.example.tripit.schedule.entity.DetailScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetailScheduleRepository extends JpaRepository<DetailScheduleEntity, Long> {

    List<DetailScheduleEntity> findByScheduleId(Long scheduleId);
}
