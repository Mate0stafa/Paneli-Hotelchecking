package com.example.paneli.Services;

import com.example.paneli.Models.ToDo;
import com.example.paneli.Repositories.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ToDoService {

    @Autowired
    private ToDoRepository todorepo;


    public List<ToDo> getToDoByProperty(Long propertyId) {
        return todorepo.findByPropertyId(propertyId);
    }
    public ToDo getToDoById(Long id){
        return todorepo.getById(id);
    }

    public ToDo addToDo(ToDo toDo) {
        return todorepo.save(toDo);
    }

    public void deleteToDo(Long id) {
        todorepo.deleteById(id);
    }

    public ToDo updateToDo(ToDo toDo) {
        return todorepo.save(toDo);
    }
}
