package nl.solar.app.repositories;

import nl.solar.app.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryMock implements EntityRepository<User> {
    //TODO add comments
    private List<User> userArrayList = new ArrayList<>();
    //amount of users being created
    private final int START_USERS_AMOUNT = 6;
    //starting ids of the teams and user
    private final long START_USER_ID = 500;

    private final long START_TEAM_ID = 500;
    private long nextUserId = START_USER_ID;
    private long nextTeamId = START_TEAM_ID;


    public UserRepositoryMock() {
        for (int i = 0; i < START_USERS_AMOUNT; i++) {
            userArrayList.add(User.creatyDummyUser(randomUserId(), randomTeamId()));
        }
    }

    public long randomUserId(){
        nextUserId = (long) (nextUserId + Math.floor(Math.random() * 3) + 1);
        return nextUserId;
    }

    public long randomTeamId(){
        nextTeamId = (long) (nextTeamId + Math.floor(Math.random() * 3) + 1);
        return nextTeamId;
    }


    @Override
    public List<User> findALL() {
        return userArrayList;
    }

    @Override
    public User findById(long id) {
        return userArrayList.stream().filter(userArrayList -> userArrayList.getId() == id)
                .findFirst().orElse(null);
    }

    @Override
    public User delete(long id) {
        User deletedUser = this.findById(id);
        userArrayList.removeIf(userArrayList -> userArrayList.getId() == id);
        return deletedUser;
    }

    @Override
    public User save(User item) {
        int index = userArrayList.indexOf(item);
        if (index != -1){
            userArrayList.set(index, item);
        } else {
            if (item.getId() == 0){
                item.setId(randomUserId());
                item.setTeamId(randomTeamId());
            }
            userArrayList.add(item);
        }
        return item;
    }
}
