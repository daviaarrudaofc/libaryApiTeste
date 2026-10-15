package io.github.daviaarrudaofc.libaryAPI.security;

import io.github.daviaarrudaofc.libaryAPI.model.Usuario;
import io.github.daviaarrudaofc.libaryAPI.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UsuarioService usuarioService;

    public Usuario obterUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String login = userDetails.getUsername();
//        return usuarioService.obterPorLogin(login);
        // faz a propia authetication no CustomAuthentication, agr nao é necessario mais fazer essa "mini  autenticação
        // dentro do Security Service, pois tb nao era nem sua responsabilidade fazer isto

        if(authentication instanceof CustomAuthentication customAuthentication){
            return customAuthentication.getUsuario();
        }
        return null;
    }
}
