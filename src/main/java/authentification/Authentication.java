package authentification;

import authentification.domain.User;
import authentification.utils.BasicAuthDecoder;
import authentification.utils.PasswordHasher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Authentication {
    private static List<User> users = new ArrayList<>();
    public static User currentUser;

    static {
        users.add(new User("rojgar", "ksI14X0x6N2bfZCJeJ84sg=="));
        users.add(new User("wlady", "dvRspKXY3t1wBsUE84O5gg=="));
    }

    public static boolean hasAuthentication(Map<String, String> headers) {
        String authorizationHeader = headers.get("Authorization");
        return (authorizationHeader != null && !authorizationHeader.isEmpty());
    }

    public static boolean isUserAuthenticated (Map<String, String> headers){
        String authHeaderValue = headers.get("Authorization");
        if (authHeaderValue == null || !authHeaderValue.startsWith("Basic ")) {
            return false;
        }

        String userData = BasicAuthDecoder.decodeBasicAuth(authHeaderValue);

        String[] user = userData.split(":");
        if (user.length != 2) {
            return false;
        }

        String login = user[0];
        String password = PasswordHasher.hashPassword(user[1], login);
        currentUser = new User(login, password);

        return checkUser(login, password);
    }

    private static boolean checkUser(String login, String password){
        return users.stream().anyMatch(user -> user.getLogin().equals(login)
                && user.getPassword().equals(password));
    }
}
