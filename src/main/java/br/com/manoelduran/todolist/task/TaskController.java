package br.com.manoelduran.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.manoelduran.todolist.user.IUserRepository;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel) {
        var foundUser = this.userRepository.findById(taskModel.getUser_id());

        if (foundUser == null) {
            return ResponseEntity.status(404).body("User Not Found!");
        }
        var foundTask = this.taskRepository.findByTitle(taskModel.getTitle());
        if (foundTask != null) {
            return ResponseEntity.status(400).body("Task Already Exists!");
        }
        var newTask = this.taskRepository.save(taskModel);
        return ResponseEntity.status(201).body(newTask);
    }
}
