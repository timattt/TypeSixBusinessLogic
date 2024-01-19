package org.shlimtech.typesixdatabasecommon.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    private String version;
    private List<MetadataEntrySet> metadataEntrySets;
    private List<Integer> selectedUsers;

}
