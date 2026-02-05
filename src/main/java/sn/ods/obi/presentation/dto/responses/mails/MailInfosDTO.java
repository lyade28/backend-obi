package sn.ods.obi.presentation.dto.responses.mails;

/**
 * @author G2k R&D
 */

public record MailInfosDTO(Long id, String  originalText, String subject, String text, String destinataire) {
}
