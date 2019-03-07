package pl.sii.shopsmvc.controller;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(Principal principal) {
        if (hasRole(principal, "ROLE_ADMIN")) {
            return "redirect:/products";
        }
        else {
            return "redirect:/orders";
        }
    }

    private boolean hasRole(Principal principal, String role) {
        return ((AbstractAuthenticationToken)principal).getAuthorities().stream().allMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }
}
