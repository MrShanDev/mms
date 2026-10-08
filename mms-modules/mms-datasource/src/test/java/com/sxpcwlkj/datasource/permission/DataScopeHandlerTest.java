package com.sxpcwlkj.datasource.permission;
import com.sxpcwlkj.common.annotation.DataScope;
import net.sf.jsqlparser.schema.Table;
import org.springframework.beans.factory.ObjectProvider;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class DataScopeHandlerTest {
    @DataScope(table="test_rows") interface ScopedMapper { void list(); }
    @Test void missingPolicyFailsClosedAndUnrelatedTableIsUnchanged() {
        ObjectProvider<DataScopeProvider> provider=mock(ObjectProvider.class);
        var handler=new DataScopeHandler(provider);
        assertEquals("1 = 0",handler.getSqlSegment(new Table("test_rows"),null,ScopedMapper.class.getName()+".list").toString());
        assertNull(handler.getSqlSegment(new Table("another"),null,ScopedMapper.class.getName()+".list"));
    }
}
