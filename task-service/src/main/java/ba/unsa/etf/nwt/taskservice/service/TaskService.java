package ba.unsa.etf.nwt.taskservice.service;

import ba.unsa.etf.nwt.taskservice.model.Priority;
import ba.unsa.etf.nwt.taskservice.model.Status;
import ba.unsa.etf.nwt.taskservice.model.Task;
import ba.unsa.etf.nwt.taskservice.model.Type;
import ba.unsa.etf.nwt.taskservice.repository.TaskRepository;
import ba.unsa.etf.nwt.taskservice.request.CreateTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final PriorityService priorityService;
    private final StatusService statusService;
    private final TypeService typeService;

    public Task create(final CreateTaskRequest request) {
        Task task = createTaskFromRequest(request);
        return taskRepository.save(task);
    }

    private Task createTaskFromRequest(final CreateTaskRequest request) {
        Priority priority =  priorityService.findById(request.getPriorityId());
        Status status = statusService.findByStatusType(Status.StatusType.OPEN);
        Type type = typeService.findById(request.getTypeId());

        Task task = new Task();
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setUserId(request.getUserId());
        task.setProjectId(request.getProjectId());
        task.setPriority(priority);
        task.setStatus(status);
        task.setType(type);
        return task;
    }
}