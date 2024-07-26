package springbook.user.service;

import springbook.user.domain.User;

public class TestUserServiceImpl extends UserServiceImpl {
    private String id;

    public TestUserServiceImpl(String id) {
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
