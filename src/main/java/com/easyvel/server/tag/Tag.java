package com.easyvel.server.tag;

import com.easyvel.server.tag.bridge.UserTag;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    private String value;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTag> userTagList = new ArrayList<>();

    public Tag(String value) {
        this.value = value;
    }
}
