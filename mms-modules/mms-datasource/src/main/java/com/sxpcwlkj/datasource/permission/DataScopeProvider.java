package com.sxpcwlkj.datasource.permission;

import com.sxpcwlkj.common.annotation.DataScope;

/** Domain supplies authorization without coupling datasource to system entities. */
public interface DataScopeProvider {
    String condition(DataScope scope, String qualifier);
}
