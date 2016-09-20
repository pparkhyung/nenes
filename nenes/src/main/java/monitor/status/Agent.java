package monitor.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import io.netty.channel.Channel;
import server.boot.BootServer;

@Controller
public class Agent {

    //@Resource(name = "bootServer")
    @Autowired
    BootServer bootServer;

    @RequestMapping("/agent")
    public String retrieveAgent(Model model){

        if (bootServer != null) {
            Channel b = bootServer.getChannel();
            model.addAttribute("serverBootstrap", b.toString());
        } else {
            System.out.println("bootServeris null");
        }

        return "Agent";

    }

    public void setBootServer(BootServer bootServer){
        this.bootServer = bootServer;
    }

    /*    @RequestMapping("/AgentStatus")
    public String retrieveStatus(Model model){
        
        System.out.println("model2");
        
        return "x";
    
    }*/

}
