package com.easyvel.server.tag.dto;

import com.easyvel.server.global.entity.User;
import com.easyvel.server.tag.Tag;
import com.easyvel.server.tag.bridge.UserTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagList {
    List<String> tags;

    public static TagList make(User user) {
        List<String> copyList = new ArrayList<>();

        for (UserTag userTag : user.getUserTagList()) {
            copyList.add(userTag.getTag().getValue());
        }
        return new TagList(copyList);
    }
}
