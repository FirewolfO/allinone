package com.firewolf.rule.engine.core.conflict.resolver;

import com.firewolf.rule.engine.core.EntityMetaInfo;

import java.util.List;

public class DefaultConflictResolver extends AbstractConflictResolver {
    @Override
    public List beforeSub(EntityMetaInfo mainMetaInfo, EntityMetaInfo subMetaInfo, List data) throws Exception {
        return data;
    }

    @Override
    public void afterSub(EntityMetaInfo mainMetaInfo, EntityMetaInfo subMetaInfo) throws Exception {

    }
}
