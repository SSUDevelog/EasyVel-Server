package com.easyvel.server.tag.dto;

import com.easyvel.server.global.entity.Tag;
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

    public static TagList make(Tag source) {
        List<String> tagList = source.getTags();
        List<String> copyList = new ArrayList<>(tagList);

        return new TagList(copyList);
    }
}
