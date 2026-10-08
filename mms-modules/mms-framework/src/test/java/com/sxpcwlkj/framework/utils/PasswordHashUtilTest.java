package com.sxpcwlkj.framework.utils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class PasswordHashUtilTest {
    @Test void saltedHashMatchesOnlyCorrectPassword() {
        String a=PasswordHashUtil.hash("Mms@123456"), b=PasswordHashUtil.hash("Mms@123456");
        assertNotEquals(a,b); assertTrue(PasswordHashUtil.matches("Mms@123456",a,null));
        assertFalse(PasswordHashUtil.matches("wrongpass",a,null));
    }
    @Test void legacyShortPasswordMigratesWithoutWeakeningNewPasswordPolicy() {
        assertTrue(PasswordHashUtil.matches("old123", PasswordHashUtil.migrateVerifiedLegacy("old123"), null));
        assertThrows(RuntimeException.class, () -> PasswordHashUtil.hash("old123"));
    }
    @Test void legacyPasswordCanMigrateAndMalformedHashesFailClosed() {
        String old=SignUtil.pressWord("Mms@123456","legacy");
        assertTrue(PasswordHashUtil.matches("Mms@123456",old,"legacy"));
        assertTrue(PasswordHashUtil.isLegacy(old));
        assertFalse(PasswordHashUtil.matches("Mms@123456","pbkdf2-sha256$999999999$bad$bad",null));
        assertFalse(PasswordHashUtil.matches("Mms@123456","invalid",null));
        assertThrows(RuntimeException.class,()->PasswordHashUtil.hash("short"));
    }
}
