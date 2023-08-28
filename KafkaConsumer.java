package com.example.l7;

import com.example.l7.model.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.net.http.HttpRequest;
import java.util.Set;

@Component
public class KafkaConsumer {

    @Autowired
    private LessonRepository lessonRepository;

    @KafkaListener(topics = "lessons-topic", groupId = "1")
    public void saveLesson(Lesson lesson)
    {
        lessonRepository.save(lesson);
    }
}
