package it.twinsbrain.dojos;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDisplay implements Display{

    private final List<String> messages = new ArrayList<>();
    @Override
    public void show(String message) {
        messages.add(message);
    }

    public List<String> messages(){
        return messages;
    }
}
