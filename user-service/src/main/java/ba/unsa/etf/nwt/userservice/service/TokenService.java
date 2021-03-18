package ba.unsa.etf.nwt.userservice.service;

import ba.unsa.etf.nwt.userservice.exception.base.NotFoundException;
import ba.unsa.etf.nwt.userservice.exception.base.UnprocessableEntityException;
import ba.unsa.etf.nwt.userservice.model.Token;
import ba.unsa.etf.nwt.userservice.model.User;
import ba.unsa.etf.nwt.userservice.repository.TokenRepository;
import ba.unsa.etf.nwt.userservice.repository.UserRepository;
import ba.unsa.etf.nwt.userservice.request.user.ConfirmEmailRequest;
import ba.unsa.etf.nwt.userservice.utility.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenUtils tokenUtils;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public Token generateToken(User user, Token.TokenType type) {
        tokenRepository.invalidateTokenByType(user.getId(), type.name());
        Token token = createToken(user, type);
        token = tokenRepository.save(token);
        return token;
    }

    public void confirmActivation(ConfirmEmailRequest request) {
        Token token = checkTokenValidity(request.getToken(), Token.TokenType.ACTIVATE_ACCOUNT);
        User user = token.getUser();
        if (user.getEnabled()) throw new UnprocessableEntityException("User already confirmed email");
        activateUserAccount(user);
    }

    private Token checkTokenValidity(String tokenPayload, Token.TokenType type) {
        Token token = tokenRepository.findTokenByTokenAndType(tokenPayload, type)
                .orElseThrow(() -> new NotFoundException("Token doesn't exist"));
        if (token.isExpired()) throw new UnprocessableEntityException("Expired token");
        return token;
    }

    private void activateUserAccount(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    private Token createToken(User user, Token.TokenType type) {
        Integer duration = (type == Token.TokenType.ACTIVATE_ACCOUNT)
                ? tokenUtils.getActivationTokenDuration() : tokenUtils.getResetPasswordTokenDuration();
        Token token = new Token();
        token.setType(type);
        token.setDuration(duration);
        token.setUser(user);
        token.setToken(tokenUtils.generateHashString());
        return token;
    }
}
