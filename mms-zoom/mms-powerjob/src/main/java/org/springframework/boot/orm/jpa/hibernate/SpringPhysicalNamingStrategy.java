package org.springframework.boot.orm.jpa.hibernate;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;

/**
 * Compatibility shim for libraries expecting the Spring Boot 2.x class.
 * Delegates to Hibernate's CamelCaseToUnderscoresNamingStrategy.
 */
public class SpringPhysicalNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {
}
