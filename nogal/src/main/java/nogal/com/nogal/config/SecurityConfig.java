/*
 * package nogal.com.nogal.config;
 * 
 * import org.springframework.context.annotation.Configuration;
 * import org.springframework.context.annotation.Bean;
 * import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity;
 * import org.springframework.security.web.SecurityFilterChain;
 * import org.springframework.security.config.annotation.web.configurers.
 * AbstractHttpConfigurer;
 * import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 * import static org.springframework.security.config.Customizer.withDefaults;
 * 
 * @Configuration
 * 
 * @EnableWebSecurity
 * public class SecurityConfig {
 * 
 * // 1. Bean para BCryptPasswordEncoder (necesario para UsuarioService)
 * 
 * @Bean
 * public BCryptPasswordEncoder passwordEncoder() {
 * return new BCryptPasswordEncoder();
 * }
 * 
 * // 2. Filtro de Seguridad para permitir /registro y /login
 * 
 * @Bean
 * public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
 * Exception {
 * http
 * .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF para peticiones
 * desde Angular
 * .authorizeHttpRequests((auth) -> auth
 * // Rutas públicas
 * .requestMatchers("/api/usuarios/registro", "/api/usuarios/login",
 * "/api/productos/**")
 * .permitAll()
 * // Asegúrate de permitir las rutas de Producto si las tienes
 * // Otras rutas requieren autenticación
 * .anyRequest().authenticated())
 * .httpBasic(withDefaults());
 * 
 * return http.build();
 * }
 * }
 */