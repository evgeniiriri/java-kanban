package kanban.service;

import com.google.gson.Gson;

import java.io.File;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import kanban.model.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


public class Manager {
//    {
//        "view": false,
//            "status": "NEW",
//            "name": "POST",
//            "id": 1,
//            "description": "POST POST",
//            "duration": 30,
//            "startTime": "2024-07-11 11:30"
//    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getFileBackedManager(File file) {
        return FileBackedTaskManager.loadFromFile(file);
    }
//
//    public static Task taskCollector(HashMap<String, String>) {
//        return Task;
//    }


}
