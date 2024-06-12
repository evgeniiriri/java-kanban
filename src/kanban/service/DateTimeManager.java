package kanban.service;
/*
* Класс для работы со временем и вычислениями связанными со временем.
*/

import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DateTimeManager {

    public Subtask getMinDateTime(List<Subtask> subtasks) {
        //возвращает самую раннюю дату.
        Optional<Subtask> s = subtasks.stream().min(Comparator.comparing(Subtask::getStartTime));
        return s.get();

    }

//    private LocalDateTime getMaxDateTime(List<Subtask> subtasks) {
//        //Возвращает самую позднюю дату.
//    }
//
//    private Duration getDuration(List<Subtask> subtasks) {
//        //Возвращает продолжительность всех задач.
//    }
//
//    public Epic calculateTime(List<Subtask> subtasks, Epic epic) {
//
//    }
}
