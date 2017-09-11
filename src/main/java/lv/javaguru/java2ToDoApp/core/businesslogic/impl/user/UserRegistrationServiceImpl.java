package lv.javaguru.java2ToDoApp.core.businesslogic.impl.user;

import lv.javaguru.java2ToDoApp.core.businesslogic.api.user.RegistrationFormValidator;
import lv.javaguru.java2ToDoApp.core.businesslogic.api.user.UserRegistrationService;
import lv.javaguru.java2ToDoApp.common.Error;
import lv.javaguru.java2ToDoApp.common.Response;
import lv.javaguru.java2ToDoApp.common.form.RegistrationForm;
import lv.javaguru.java2ToDoApp.core.database.api.UserDAO;
import lv.javaguru.java2ToDoApp.core.domain.User;
import lv.javaguru.java2ToDoApp.core.domain.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class UserRegistrationServiceImpl implements UserRegistrationService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RegistrationFormValidator registrationFormValidator;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public Response register(RegistrationForm registrationForm) {

        Map<String, Error> validationErrors = registrationFormValidator.validate(registrationForm);
        if (!validationErrors.isEmpty()) {
            return Response.createFailResponse(validationErrors);
        }

        User user = UserBuilder.createUser()
                .withFullName(registrationForm.getFullName())
                .withLogin(registrationForm.getLogin())
                .withPassword(bCryptPasswordEncoder.encode(registrationForm.getPassword()))
                .build();

        user = userDAO.save(user);

        return Response.createSuccessResponse(user);
    }
}
