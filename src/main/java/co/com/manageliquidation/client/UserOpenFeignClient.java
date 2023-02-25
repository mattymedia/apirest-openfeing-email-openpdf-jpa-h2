package co.com.manageliquidation.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import co.com.manageliquidation.Entity.User;

@FeignClient(name = "users", url = "6356a20d9243cf412f89d1f9.mockapi.io/api")
public interface UserOpenFeignClient {
	
	@GetMapping("/users")
	public List<User> getAllUsers();

}
