package chat.hala.app.restful.wrapper;

import chat.hala.app.entity.Comment;
import com.vividsolutions.jts.geom.Coordinate;

public class CommentWithLocate {

    private Comment comment;

    private Coordinate locate;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Coordinate getLocate() {
        return locate;
    }

    public void setLocate(Coordinate locate) {
        this.locate = locate;
    }
}

