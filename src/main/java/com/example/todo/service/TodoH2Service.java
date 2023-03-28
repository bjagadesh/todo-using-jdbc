/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 *
 */

// Write your code here

package com.example.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.todo.repository.*;
import com.example.todo.model.*;

@Service
public class TodoH2Service implements TodoRepository{

    @Autowired
    private JdbcTemplate db;

    public ArrayList<Todo> getTodos(){
        List<Todo> todos=db.query("select * from todolist",new TodoRowMapper());
        ArrayList<Todo> todolist=new ArrayList<>(todos);
        return todolist;
    }

    public Todo addTodo(Todo todo){
            db.update("insert into todolist(todo,priority,status) values (?,?,?)",todo.getTodo(),todo.getPriority(),todo.getStatus());
            Todo ntodo=db.queryForObject("select * from todolist where todo=? and priority=? and status=?", new TodoRowMapper(),todo.getTodo(),todo.getPriority(),todo.getStatus());
            return ntodo;
    }

    public Todo getTodolist(int id){
        try{
            Todo todo=db.queryForObject("select * from todolist where id=?", new TodoRowMapper(),id);
            return todo;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Todo updateTodo(int id,Todo todo){
        if(todo.getTodo()!=null){
            db.update("update todolist set todo=?",todo.getTodo());
        }

        if(todo.getPriority()!=null){
            db.update("update todolist set priority=?",todo.getPriority());
        }

        if(todo.getStatus()!=null){
            db.update("update todolist set status=?",todo.getStatus());
        }

        return getTodolist(id);
    }

    public void deleteTodo(int id){
            db.update("delete from todolist where id=?",id);
    }
    
}