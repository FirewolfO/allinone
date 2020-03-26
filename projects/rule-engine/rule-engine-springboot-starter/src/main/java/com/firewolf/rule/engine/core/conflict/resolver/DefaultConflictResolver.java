package com.firewolf.rule.engine.core.conflict.resolver;

import com.firewolf.rule.engine.entity.EntityMetaInfo;

import java.util.List;

public class DefaultConflictResolver extends AbstractConflictResolver {
    @Override
    public List beforeSub(EntityMetaInfo mainEntityMetaInfo, EntityMetaInfo subMetaInfo, List data, List conflictItem, List notConflictItem) throws Exception {
        return data;
    }

    @Override
    public void afterSub(EntityMetaInfo mainMetaInfo, EntityMetaInfo subMetaInfo) throws Exception {

    }
}
