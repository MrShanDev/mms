<#if formLayout==3>
-- Apply only to a clean table with at most one record per tenant.
-- Required invariant for generated singleton forms, including concurrent direct writes.
ALTER TABLE `${tableName}` ADD COLUMN singleton_key TINYINT NOT NULL DEFAULT 1,
 ADD UNIQUE KEY uk_singleton_tenant (tenant_id,singleton_key),
 ADD CONSTRAINT ck_singleton_key CHECK (singleton_key = 1);
</#if>
