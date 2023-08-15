package com.stockexchangeapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
  @NotNull(message = "api.validation.constraints.email.message")
  @NotBlank(message = "api.validation.constraints.email.message")
  private String email;
  @NotNull(message = "api.validation.constraints.password.message")
  @NotBlank(message = "api.validation.constraints.password.message")
  private String password;
}
