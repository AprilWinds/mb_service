package chat.hala.app.restful.wrapper;

import chat.hala.app.entity.Dynamic;
import com.vividsolutions.jts.geom.Coordinate;

public class DynamicWithLocate {

    private Dynamic dynamic;

    private Coordinate locate;

    public Dynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }

    public Coordinate getLocate() {
        return locate;
    }

    public void setLocate(Coordinate locate) {
        this.locate = locate;
    }
}

