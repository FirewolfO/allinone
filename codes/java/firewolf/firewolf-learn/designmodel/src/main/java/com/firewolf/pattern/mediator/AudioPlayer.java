package com.firewolf.pattern.mediator;

public class AudioPlayer extends EventHandler {
    public AudioPlayer(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public void handle(Object message) {
        System.out.println("播放音效[" + message.toString() + "]");
    }
}