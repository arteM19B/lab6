package Network;

import Collection.Route;

import java.io.Serializable;

public class UpdateArgument implements Serializable {
    private final Long id;
    private final Route<Long> route;

    public UpdateArgument(Long id, Route<Long> route) {
        this.id = id;
        this.route = route;
    }

    public Long getId() {
        return id;
    }

    public Route<Long> getRoute() {
        return route;
    }
}
