package com.sslexplorer.tasks.forms;

import com.sslexplorer.core.forms.CoreForm;
import com.sslexplorer.tasks.Task;

public class TaskProgressForm extends CoreForm {

    private Task task;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    
    
}
