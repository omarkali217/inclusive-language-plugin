import Objects.QuarantineItem;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

import java.io.IOException;
import java.util.List;

public class HelloWorldAction extends AnAction {

    public HelloWorldAction() throws IOException {
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Messages.showInfoMessage("Hello World", "info");
    }

    public QuarantineList listBuilder = new QuarantineList();
    List<QuarantineItem> mylist = listBuilder.buildQuarantineList();



}
