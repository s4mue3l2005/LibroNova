package com.codeup.novabook.service;

import com.codeup.novabook.model.Partner;
import com.codeup.novabook.service.impl.PartnerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PartnerService
 */
public class PartnerServiceTest {
    
    private PartnerService partnerService;
    
    @BeforeEach
    void setUp() {
        partnerService = new PartnerServiceImpl();
    }
    
    @Test
    @DisplayName("Should validate partner creation parameters")
    void testPartnerCreationValidation() {
        // Test null partner
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.createPartner(null);
        });
        
        // Test partner with null document number
        Partner partnerWithNullDocument = new Partner();
        partnerWithNullDocument.setFirstName("John");
        partnerWithNullDocument.setLastName("Doe");
        partnerWithNullDocument.setEmail("john@test.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.createPartner(partnerWithNullDocument);
        });
        
        // Test partner with empty document number
        Partner partnerWithEmptyDocument = new Partner();
        partnerWithEmptyDocument.setDocumentNumber("");
        partnerWithEmptyDocument.setFirstName("John");
        partnerWithEmptyDocument.setLastName("Doe");
        partnerWithEmptyDocument.setEmail("john@test.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.createPartner(partnerWithEmptyDocument);
        });
        
        // Test partner with null first name
        Partner partnerWithNullFirstName = new Partner();
        partnerWithNullFirstName.setDocumentNumber("12345678");
        partnerWithNullFirstName.setLastName("Doe");
        partnerWithNullFirstName.setEmail("john@test.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.createPartner(partnerWithNullFirstName);
        });
        
        // Test partner with empty first name
        Partner partnerWithEmptyFirstName = new Partner();
        partnerWithEmptyFirstName.setDocumentNumber("12345678");
        partnerWithEmptyFirstName.setFirstName("");
        partnerWithEmptyFirstName.setLastName("Doe");
        partnerWithEmptyFirstName.setEmail("john@test.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.createPartner(partnerWithEmptyFirstName);
        });
        
        // Test partner with null last name
        Partner partnerWithNullLastName = new Partner();
        partnerWithNullLastName.setDocumentNumber("12345678");
        partnerWithNullLastName.setFirstName("John");
        partnerWithNullLastName.setEmail("john@test.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.createPartner(partnerWithNullLastName);
        });
        
        // Test partner with empty last name
        Partner partnerWithEmptyLastName = new Partner();
        partnerWithEmptyLastName.setDocumentNumber("12345678");
        partnerWithEmptyLastName.setFirstName("John");
        partnerWithEmptyLastName.setLastName("");
        partnerWithEmptyLastName.setEmail("john@test.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.createPartner(partnerWithEmptyLastName);
        });
        
        // Test partner with null email
        Partner partnerWithNullEmail = new Partner();
        partnerWithNullEmail.setDocumentNumber("12345678");
        partnerWithNullEmail.setFirstName("John");
        partnerWithNullEmail.setLastName("Doe");
        
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.createPartner(partnerWithNullEmail);
        });
        
        // Test partner with empty email
        Partner partnerWithEmptyEmail = new Partner();
        partnerWithEmptyEmail.setDocumentNumber("12345678");
        partnerWithEmptyEmail.setFirstName("John");
        partnerWithEmptyEmail.setLastName("Doe");
        partnerWithEmptyEmail.setEmail("");
        
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.createPartner(partnerWithEmptyEmail);
        });
    }
    
    @Test
    @DisplayName("Should validate partner retrieval parameters")
    void testPartnerRetrievalValidation() {
        // Test invalid partner ID
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.getPartnerById(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.getPartnerById(-1);
        });
        
        // Test null document number
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.getPartnerByDocumentNumber(null);
        });
        
        // Test empty document number
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.getPartnerByDocumentNumber("");
        });
    }
    
    @Test
    @DisplayName("Should validate partner update parameters")
    void testPartnerUpdateValidation() throws Exception {
        // Test null partner
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.updatePartner(null);
        });
        
        // Test partner with invalid ID
        Partner partnerWithInvalidId = new Partner();
        partnerWithInvalidId.setId(0);
        partnerWithInvalidId.setDocumentNumber("12345678");
        partnerWithInvalidId.setFirstName("John");
        partnerWithInvalidId.setLastName("Doe");
        partnerWithInvalidId.setEmail("john@test.com");
        partnerWithInvalidId.setStatus(Partner.PartnerStatus.ACTIVO);
        
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.updatePartner(partnerWithInvalidId);
        });
    }
    
    @Test
    @DisplayName("Should validate partner deletion parameters")
    void testPartnerDeletionValidation() {
        // Test invalid partner ID
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.deletePartner(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            partnerService.deletePartner(-1);
        });
    }
    
    @Test
    @DisplayName("Should validate document number and email availability checks")
    void testDocumentNumberEmailAvailabilityValidation() throws Exception {
        // Test null values
        assertFalse(partnerService.isDocumentNumberAvailable(null));
        assertFalse(partnerService.isDocumentNumberAvailable(""));
        assertFalse(partnerService.isEmailAvailable(null));
        assertFalse(partnerService.isEmailAvailable(""));
        
        // Test with valid values (assuming no existing partners in test DB)
        assertDoesNotThrow(() -> {
            boolean documentAvailable = partnerService.isDocumentNumberAvailable("12345678");
            assertNotNull(Boolean.valueOf(documentAvailable));
        });
        
        assertDoesNotThrow(() -> {
            boolean emailAvailable = partnerService.isEmailAvailable("test@test.com");
            assertNotNull(Boolean.valueOf(emailAvailable));
        });
        
        // Test availability for update with invalid parameters
        assertFalse(partnerService.isDocumentNumberAvailableForUpdate(null, 1));
        assertFalse(partnerService.isDocumentNumberAvailableForUpdate("", 1));
        assertFalse(partnerService.isDocumentNumberAvailableForUpdate("12345678", 0));
        
        assertFalse(partnerService.isEmailAvailableForUpdate(null, 1));
        assertFalse(partnerService.isEmailAvailableForUpdate("", 1));
        assertFalse(partnerService.isEmailAvailableForUpdate("test@test.com", 0));
    }
    
    @Test
    @DisplayName("Should validate partner model properties")
    void testPartnerModelValidation() {
        Partner partner = new Partner("12345678", "John", "Doe", "john@test.com");
        
        // Test partner creation
        assertNotNull(partner);
        assertEquals("12345678", partner.getDocumentNumber());
        assertEquals("John", partner.getFirstName());
        assertEquals("Doe", partner.getLastName());
        assertEquals("john@test.com", partner.getEmail());
        assertEquals(Partner.PartnerStatus.ACTIVO, partner.getStatus());
        assertNotNull(partner.getCreatedAt());
        assertNotNull(partner.getUpdatedAt());
        
        // Test full name method
        assertEquals("John Doe", partner.getFullName());
        
        // Test toString method
        String partnerString = partner.toString();
        assertNotNull(partnerString);
        assertTrue(partnerString.contains("12345678"));
        assertTrue(partnerString.contains("John"));
        assertTrue(partnerString.contains("Doe"));
        assertTrue(partnerString.contains("john@test.com"));
        assertTrue(partnerString.contains("ACTIVO"));
    }
    
    @Test
    @DisplayName("Should validate partner status enum")
    void testPartnerStatusEnumValidation() {
        // Test PartnerStatus enum
        assertEquals("ACTIVO", Partner.PartnerStatus.ACTIVO.name());
        assertEquals("INACTIVO", Partner.PartnerStatus.INACTIVO.name());
        
        // Test enum values
        assertEquals(2, Partner.PartnerStatus.values().length);
    }
    
    @Test
    @DisplayName("Should validate partner creation with decorator pattern")
    void testPartnerCreationWithDecorator() throws Exception {
        // Test that default properties are applied
        Partner partner = new Partner("12345678", "John", "Doe", "john@test.com");
        
        // Verify default status is set
        assertEquals(Partner.PartnerStatus.ACTIVO, partner.getStatus());
        
        // Verify timestamps are set
        assertNotNull(partner.getCreatedAt());
        assertNotNull(partner.getUpdatedAt());
        
        // Verify timestamps are recent (within last minute)
        assertTrue(java.time.Duration.between(partner.getCreatedAt(), java.time.LocalDateTime.now()).toSeconds() < 60);
        assertTrue(java.time.Duration.between(partner.getUpdatedAt(), java.time.LocalDateTime.now()).toSeconds() < 60);
    }
    
    @Test
    @DisplayName("Should validate getAllPartners and getActivePartners methods")
    void testGetPartnersMethods() throws Exception {
        // These tests would require a test database setup
        // For now, we'll test that the methods don't throw exceptions
        assertDoesNotThrow(() -> {
            java.util.List<Partner> partners = partnerService.getAllPartners();
            assertNotNull(partners);
        });
        
        assertDoesNotThrow(() -> {
            java.util.List<Partner> activePartners = partnerService.getActivePartners();
            assertNotNull(activePartners);
        });
    }
}
