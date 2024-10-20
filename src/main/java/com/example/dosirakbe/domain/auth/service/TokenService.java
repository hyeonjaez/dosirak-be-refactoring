package com.example.dosirakbe.domain.auth.service;

import com.example.dosirakbe.domain.auth.dto.response.TokenResponse;

public interface TokenService {

    TokenResponse reissueAccessToken();
}
