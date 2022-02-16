/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.server.communication;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class BackLog {
    private List<Object> backlogs;

    public BackLog() {
        backlogs=new ArrayList<>();
    }
    
    
    public boolean isBacklogEmpty(){
        return backlogs.isEmpty();
    }

    public List<Object> getBacklogs() {
        return backlogs;
    }
    
    
}
