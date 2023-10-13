package br.com.manoelduran.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.manoelduran.todolist.user.IUserRepository;
import br.com.manoelduran.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var now = LocalDateTime.now();
        System.out.println("Chegou no controller" + request.getAttribute("userId"));
        var userId = request.getAttribute("userId");
        taskModel.setUserId((UUID) userId);
        var foundUser = this.userRepository.findById(taskModel.getUserId());

        if (foundUser == null) {
            return ResponseEntity.status(404).body("User Not Found!");
        }
        var foundTask = this.taskRepository.findByTitle(taskModel.getTitle());
        if (foundTask != null) {
            return ResponseEntity.status(400).body("Task Already Exists!");
        }
        if (now.isAfter(taskModel.getStartedAt()) || now.isAfter(taskModel.getFinishedAt())
                || taskModel.getStartedAt().isAfter(taskModel.getFinishedAt())) {
            return ResponseEntity.status(400).body("Please, add a valid start date or finish date!");
        }
        var newTask = this.taskRepository.save(taskModel);
        return ResponseEntity.status(201).body(newTask);
    }

    @GetMapping("/")
    public ResponseEntity<List<TaskModel>> index(HttpServletRequest request) {
        var userId = (UUID) request.getAttribute("userId");
        var tasks = this.taskRepository.findAllByUserId(userId);
        return ResponseEntity.status(200).body(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {

        var foundTask = this.taskRepository.findById(id).orElse(null);
   if (foundTask == null) {
            return ResponseEntity.status(404).body("Task not found!");
        }
        var userId = request.getAttribute("userId");

        if (!foundTask.getUserId().equals(userId)) {
            return ResponseEntity.status(400).body("You only can edit your tasks!");
        }

        Utils.copyNonNullProperties(taskModel, foundTask);

        var updatedTask = this.taskRepository.save(foundTask);

        return ResponseEntity.status(200).body(updatedTask);
    }
}
