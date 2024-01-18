package org.shlimtech.typesixdatabasecommon.metadata;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetadataEntry {

    private String name;
    private boolean flag;

    public MetadataEntry(String name) {
        this.name = name;
        this.flag = false;
    }

}
