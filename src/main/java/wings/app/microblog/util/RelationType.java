package wings.app.microblog.util;

public enum RelationType {
        none(0),
        following(1),
        followed(2),
        friend(3),
        blocking(4),
        blocked(5),
        hater(6);

        private Integer id;

        public Integer getId() {
                return id;
        }

        RelationType(Integer id) {
                this.id = id;
        }
}

