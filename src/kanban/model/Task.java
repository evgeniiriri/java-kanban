package kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements TaskInterface, Cloneable {
    protected boolean view;
    protected Status status;
    protected String name;
    protected int id;
    protected String description;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.view = false;
    }

    public DateTimeFormatter getTaskDateTimeFormatter() {
        return formatter;
    }

    protected String getStringDateTime() {
        if (startTime == null) {
            return "null";
        }
        return startTime.format(formatter);
    }

    protected String getStringDurationMinutes() {
        if (duration == null) {
            return "null";
        }
        return String.valueOf(duration.toMinutes());
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return LocalDateTime.MAX;
        }
        return startTime.plus(duration);
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        return (Task) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && status == task.status && name.equals(task.name) && description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, name, id, description);
    }

    @Override
    public boolean isView() {
        return view;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean isEqualsStatus(Status status) {
        return this.status.equals(status);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("TASK;%s;%s;%s;%s;%s;%s",
                id, name, status, description, getStringDateTime(), getStringDurationMinutes());
    }
}
