package org.shlimtech.typesixdatabasecommon.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataEntrySet {

    private String name;
    private String message;
    private boolean multiChoice;
    private List<MetadataEntry> entries;

}
