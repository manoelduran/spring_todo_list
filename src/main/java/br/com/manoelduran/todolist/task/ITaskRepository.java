package br.com.manoelduran.todolist.task;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    TaskModel findByTitle(String title);
    List<TaskModel> findAllByUserId(UUID userId);

}