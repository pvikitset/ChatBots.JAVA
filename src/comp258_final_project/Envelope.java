/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp258_final_project;
import java.io.Serializable;
/**
 *
 * @author vikitset5527
 */
public class Envelope implements Serializable{
    
    private String name;
    private Object contents;

    public Envelope() {
    }

    public Envelope(String name, Object contents) {
        setName(name);
        setContents(contents);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getContents() {
        return contents;
    }

    public void setContents(Object contents) {
        this.contents = contents;
    }
    
}
