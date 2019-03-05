package pl.sii.shopsmvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SiiWebController {

    @ModelAttribute("my_message")
    public String myMessage() {
        return "#1: Hi Sii";
    }

    @RequestMapping("/hi")
    public String home(Model model){
        model.addAttribute("my_message2", "#2: hi Sii");
        return "homePage";
    }

    @RequestMapping("/hi2")
    public ModelAndView hi() {
        ModelAndView modelAndView = new ModelAndView("homePage");
        modelAndView.addObject("my_message2", "#2: Hi All");
        return modelAndView;
    }
}
