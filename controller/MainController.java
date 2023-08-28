package com.example.l7.controller;

import com.example.l7.LessonRepository;
import com.example.l7.form.LessonForm;
import com.example.l7.model.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class MainController {

    @Autowired
    private LessonRepository lessonRepository;
    // Переменная для генерации ID занятия
    private static final AtomicInteger LESSON_ID_HOLDER = new AtomicInteger();

    @Autowired
    private KafkaTemplate<String, Lesson> kafkaTemplate;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = { "/list" }, method = RequestMethod.GET)
    public String lessonList(Model model) {
        model.addAttribute("lessons", lessonRepository.findAll());
        return "list";
    }

    @RequestMapping(value = { "/add" }, method = RequestMethod.GET)
    public String showAddLessonPage(Model model) {
        LessonForm lessonForm = new LessonForm();
        model.addAttribute("lessonForm", lessonForm);
        return "add";
    }

    @RequestMapping(value = { "/delete" }, method = RequestMethod.GET)
    public String showDeleteLessonPage(Model model) {
        LessonForm lessonForm = new LessonForm();
        model.addAttribute("lessonForm", lessonForm);
        return "delete";
    }

    @RequestMapping(value = { "/add" }, method = RequestMethod.POST)
    public String saveLesson(Model model, //
                             @ModelAttribute("lessonForm") LessonForm lessonForm) {

        final int clientId = LESSON_ID_HOLDER.incrementAndGet();
        String title = lessonForm.getTitle();
        String time = lessonForm.getTime();
        String teacher = lessonForm.getTeacher();

        if (title != null && title.length() > 0 //
                && time != null && time.length() > 0 //
                && teacher != null && teacher.length() > 0) {
            Lesson newLesson = new Lesson(clientId, title, time, teacher);
            lessonRepository.save(newLesson);
            kafkaTemplate.send("lessons-topic", newLesson);
            return "redirect:/list";
        }

        return "add";
    }

    @RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
    public String deleteLesson(Model model,@ModelAttribute("lessonForm") LessonForm lessonForm) {
        Integer id = lessonForm.getId();
        if (lessonRepository.findById(id).isPresent())
        {
            lessonRepository.deleteById(id);
        }
        return "redirect:/list";
    }
}
