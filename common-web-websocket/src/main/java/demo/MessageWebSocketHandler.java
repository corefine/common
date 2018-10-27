package demo;

import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class MessageWebSocketHandler extends SessionWebSocketHandler {
    private Logger logger = LoggerFactory.getLogger("socket.message");

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String data = message.getPayload();
        if (logger.isDebugEnabled()) {
            logger.debug("receive data: {}", data);
        }


        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .failFast( true )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        validator.validate(this);
    }
}