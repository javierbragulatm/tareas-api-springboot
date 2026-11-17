
import type { Task } from "../types/Task";

interface TaskCardProps {
  task: Task;
  onToggle: (id: number) => void;
}

function TaskCard({ task, onToggle }: TaskCardProps) {
  return (
    <div className="task-card">
      <div>
        <h3>{task.title}</h3>
        <p>{task.description}</p>
      </div>

      <button onClick={() => onToggle(task.id)}>
        {task.completed ? "Completada" : "Pendiente"}
      </button>
    </div>
  );
}

export default TaskCard;
