package springbook.user.service;

import org.junit.jupiter.api.Test;

import springbook.user.domain.User;
import springbook.user.service.UserService;

public class TestUserService extends UserService {
    private String id;

    public TestUserService(String id) {
        this.id = id;
    }

    @Override
    protected void upgradeLevel(User user) {
        if (user.getId().equals(this.id)) {
            throw new TestUserServiceException();
        }
        super.upgradeLevel(user);
    }
}
