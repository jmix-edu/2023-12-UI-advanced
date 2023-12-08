package com.company.jmixpm.screen.sandbox;

import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.JavaScriptComponent;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Sandbox")
@UiDescriptor("sandbox.xml")
public class Sandbox extends Screen {
    @Autowired
    private JavaScriptComponent quill;
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onInit(final InitEvent event) {
        QuillState state = new QuillState();
        state.theme = "snow";
        state.placeholder = "Some placeholder string";

        quill.setState(state);

        quill.addFunction("valueChanged", javaScriptCallbackEvent -> {
            String value = javaScriptCallbackEvent.getArguments().getString(0);
            notifications.create()
                    .withCaption(value)
                    .withType(Notifications.NotificationType.TRAY)
                    .withPosition(Notifications.Position.BOTTOM_RIGHT)
                    .show();
        });
        
    }

    @Subscribe("clearQuillBtn")
    public void onClearQuillBtnClick(final Button.ClickEvent event) {
        quill.callFunction("deleteText");

    }
    
    class QuillState {
        public String theme;
        public String placeholder;
    }
    
}