package com.codeup.novabook.util;

import com.codeup.novabook.model.Book;
import com.codeup.novabook.model.Loan;
import com.codeup.novabook.model.Partner;
import com.codeup.novabook.model.User;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for formatting data into tables for JOptionPane display
 */
public class TableFormatter {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * Format books into a table string
     */
    public static String formatBooksTable(List<Book> books) {
        if (books.isEmpty()) {
            return "No books found.";
        }
        
        StringBuilder table = new StringBuilder();
        table.append("┌─────┬─────────────┬─────────────────────────────────┬─────────────────────┬────────────┬─────────┬─────────────┬─────────┬─────────┐\n");
        table.append("│ ID  │ ISBN        │ Title                           │ Author              │ Category   │ Copies  │ Available   │ Price   │ Status  │\n");
        table.append("├─────┼─────────────┼─────────────────────────────────┼─────────────────────┼────────────┼─────────┼─────────────┼─────────┼─────────┤\n");
        
        for (Book book : books) {
            String id = String.format("%-4s", String.valueOf(book.getId()));
            String isbn = String.format("%-12s", truncate(book.getIsbn(), 12));
            String title = String.format("%-32s", truncate(book.getTitle(), 32));
            String author = String.format("%-18s", truncate(book.getAuthor(), 18));
            String category = String.format("%-11s", truncate(book.getCategory(), 11));
            String copies = String.format("%-8s", book.getTotalCopies() + "/" + book.getAvailableCopies());
            String available = String.format("%-12s", book.getAvailableCopies());
            String price = String.format("%-8s", "$" + book.getReferencePrice().toString());
            String status = String.format("%-8s", book.isActive() ? "[ACTIVO]" : "[INACTIVO]");
            
            table.append("│ ").append(id).append(" │ ").append(isbn).append(" │ ").append(title)
                 .append(" │ ").append(author).append(" │ ").append(category)
                 .append(" │ ").append(copies).append(" │ ").append(available)
                 .append(" │ ").append(price).append(" │ ").append(status).append(" │\n");
        }
        
        table.append("└─────┴─────────────┴─────────────────────────────────┴─────────────────────┴────────────┴─────────┴─────────────┴─────────┴─────────┘\n");
        
        return table.toString();
    }
    
    /**
     * Format partners into a table string
     */
    public static String formatPartnersTable(List<Partner> partners) {
        if (partners.isEmpty()) {
            return "No partners found.";
        }
        
        StringBuilder table = new StringBuilder();
        table.append("┌─────┬─────────────┬─────────────────────────────┬─────────────────────┬─────────────────────┬─────────┐\n");
        table.append("│ ID  │ Document    │ Full Name                    │ Email               │ Phone               │ Status  │\n");
        table.append("├─────┼─────────────┼─────────────────────────────┼─────────────────────┼─────────────────────┼─────────┤\n");
        
        for (Partner partner : partners) {
            String id = String.format("%-4s", String.valueOf(partner.getId()));
            String document = String.format("%-12s", truncate(partner.getDocumentNumber(), 12));
            String fullName = String.format("%-28s", truncate(partner.getFullName(), 28));
            String email = String.format("%-19s", truncate(partner.getEmail(), 19));
            String phone = String.format("%-19s", truncate(partner.getPhone() != null ? partner.getPhone() : "", 19));
            String status = String.format("%-8s", partner.getStatus() == Partner.PartnerStatus.ACTIVO ? "[ACTIVO]" : "[INACTIVO]");
            
            table.append("│ ").append(id).append(" │ ").append(document).append(" │ ").append(fullName)
                 .append(" │ ").append(email).append(" │ ").append(phone)
                 .append(" │ ").append(status).append(" │\n");
        }
        
        table.append("└─────┴─────────────┴─────────────────────────────┴─────────────────────┴─────────────────────┴─────────┘\n");
        
        return table.toString();
    }
    
    /**
     * Format users into a table string
     */
    public static String formatUsersTable(List<User> users) {
        if (users.isEmpty()) {
            return "No users found.";
        }
        
        StringBuilder table = new StringBuilder();
        table.append("┌─────┬─────────────────────┬─────────────────────┬─────────────────────┬─────────┬─────────┐\n");
        table.append("│ ID  │ Username             │ Email               │ Role                │ Status  │ Created │\n");
        table.append("├─────┼─────────────────────┼─────────────────────┼─────────────────────┼─────────┼─────────┤\n");
        
        for (User user : users) {
            String id = String.format("%-4s", String.valueOf(user.getId()));
            String username = String.format("%-20s", truncate(user.getUsername(), 20));
            String email = String.format("%-19s", truncate(user.getEmail(), 19));
            String role = String.format("%-19s", truncate(user.getRole().name(), 19));
            String status = String.format("%-8s", user.getStatus() == User.UserStatus.ACTIVO ? "[ACTIVO]" : "[INACTIVO]");
            String created = String.format("%-8s", user.getCreatedAt().format(DATE_FORMATTER));
            
            table.append("│ ").append(id).append(" │ ").append(username).append(" │ ").append(email)
                 .append(" │ ").append(role).append(" │ ").append(status)
                 .append(" │ ").append(created).append(" │\n");
        }
        
        table.append("└─────┴─────────────────────┴─────────────────────┴─────────────────────┴─────────┴─────────┘\n");
        
        return table.toString();
    }
    
    /**
     * Format loans into a table string
     */
    public static String formatLoansTable(List<Loan> loans) {
        if (loans.isEmpty()) {
            return "No loans found.";
        }
        
        StringBuilder table = new StringBuilder();
        table.append("┌─────┬─────────────────────────────┬─────────────────────────────────┬────────────┬────────────┬────────────┬─────────┬─────────┐\n");
        table.append("│ ID  │ Partner                     │ Book                            │ Loan Date  │ Due Date   │ Return Date│ Fine    │ Status  │\n");
        table.append("├─────┼─────────────────────────────┼─────────────────────────────────┼────────────┼────────────┼────────────┼─────────┼─────────┤\n");
        
        for (Loan loan : loans) {
            String id = String.format("%-4s", String.valueOf(loan.getId()));
            String partnerName = String.format("%-28s", truncate(loan.getPartnerName() != null ? loan.getPartnerName() : "N/A", 28));
            String bookTitle = String.format("%-32s", truncate(loan.getBookTitle() != null ? loan.getBookTitle() : "N/A", 32));
            String loanDate = String.format("%-11s", loan.getLoanDate().format(DATE_FORMATTER));
            String dueDate = String.format("%-11s", loan.getDueDate().format(DATE_FORMATTER));
            String returnDate = String.format("%-11s", loan.getReturnDate() != null ? loan.getReturnDate().format(DATE_FORMATTER) : "N/A");
            String fine = String.format("%-8s", "$" + loan.getFineAmount().toString());
            String status = String.format("%-8s", loan.getStatus().name());
            
            table.append("│ ").append(id).append(" │ ").append(partnerName).append(" │ ").append(bookTitle)
                 .append(" │ ").append(loanDate).append(" │ ").append(dueDate)
                 .append(" │ ").append(returnDate).append(" │ ").append(fine)
                 .append(" │ ").append(status).append(" │\n");
        }
        
        table.append("└─────┴─────────────────────────────┴─────────────────────────────────┴────────────┴────────────┴────────────┴─────────┴─────────┘\n");
        
        return table.toString();
    }
    
    /**
     * Truncate string to specified length
     */
    public static String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() > maxLength) {
            return str.substring(0, maxLength - 3) + "...";
        }
        return str;
    }
}
