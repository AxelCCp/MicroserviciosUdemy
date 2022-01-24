package com.springboot.app.oauth.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.springboot.app.commons.usuarios.models.entity.Usuario;
import com.springboot.app.oauth.services.IUsuarioService;

//ESTA CLASE ES PARA AGREGAR NUEVA INFORMACIÓN AL TOKEN

@Component
public class InfoAdicionalToken implements TokenEnhancer{
	
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		//1.-EN UN MAP SE GUARDA LA INFORMACIÓN <NOMBRE DE LA INFORMACIÓN A GUARDAR, VALOR DEL LA INFORMACIÓN Q PUEDE SER DE CUALQUIER TIPO>
		Map<String,Object>info = new HashMap<String,Object>();
		//2.-SE ALMACENA EL USUARIO Y SE CONSIGUE A TRAVÉS DE authentication
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		info.put("nombre", usuario.getNombre());
		info.put("apellido", usuario.getApellido());
		info.put("correo", usuario.getEmail());
		//3.-ASOCIAR EL "info" AL "accessToken"
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		return accessToken;
	}
	
	
	@Autowired 
	private IUsuarioService usuarioService;
}
