package chat.hala.app.entity;

import javax.persistence.*;

@Table(name = "tag")
@Entity
public class Tag {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long   id;

    private  String  category;

    @Column(name = "tag_name")
    private  String  tagName;

    @Column(name="arabic_category")
    private String arabicCategory;

    @Column(name="arabic_tag_name")
    private String arabicTagName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getArabicCategory() {
        return arabicCategory;
    }

    public void setArabicCategory(String arabicCategory) {
        this.arabicCategory = arabicCategory;
    }

    public String getArabicTagName() {
        return arabicTagName;
    }

    public void setArabicTagName(String arabicTagName) {
        this.arabicTagName = arabicTagName;
    }
}

