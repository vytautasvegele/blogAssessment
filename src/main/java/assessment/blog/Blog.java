package assessment.blog;


import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class Blog {

    private @Id @GeneratedValue Long id;
    private String title;
    private String content;
    private String owner;


    Blog() {}

    Blog(String name, String role, String owner) {
        this.title = name;
        this.content = role;
        this.owner = owner;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Blog))
            return false;
        Blog employee = (Blog) o;
        return Objects.equals(this.id, employee.id) && Objects.equals(this.title, employee.title)
                && Objects.equals(this.content, employee.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.content);
    }

    @Override
    public String toString() {
        return "Blog{" + "id=" + this.id + ", title='" + this.title +
                '\'' + ", content='" + this.content  + ", owner='" + this.owner + '\'' + '}';
    }
}
