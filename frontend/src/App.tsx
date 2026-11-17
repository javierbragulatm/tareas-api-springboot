
import { useState } from "react";
import type { Task } from "./types/Task";
import TaskCard from "./components/TaskCard";
import "./App.css";

function App() {
  const [tasks, setTasks] = useState<Task[]>([
    {
      id: 1,
      title: "React",
      description: "Aplicación con React",
      completed: false,
      createdAt: "2026-11-17"
    },
    {
      id: 2,
      title: "TypeScript",
      description: "Interfaces y tipos",
      completed: true,
      createdAt: "2026-11-17"
    },
    {
      id: 3,
      title: "Spring Boot",
      description: "Integrar el frontend con mi API",
      completed: false,
      createdAt: "2026-11-17"
    }
  ]);

  const toggleTask = (id: number) => {
    setTasks(currentTasks =>
      currentTasks.map(task =>
        task.id === id
          ? { ...task, completed: !task.completed }
          : task
      )
    );
  };

  return (
    <main className="app">
      <h1>Gestor de tareas</h1>
      <p>Organizacón y control de tareas.</p>

      <section className="task-list">
        {tasks.map(task => (
          <TaskCard
            key={task.id}
            task={task}
            onToggle={toggleTask}
          />
        ))}
      </section>
    </main>
  );
}

export default App;
