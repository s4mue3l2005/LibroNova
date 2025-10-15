package com.codeup.novabook.util;

import com.codeup.novabook.model.Book;
import com.codeup.novabook.model.Loan;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

/**
 * Utility class for exporting data to CSV files
 */
public class CSVExporter {

    private static final Logger logger = Logger.getLogger(CSVExporter.class.getName());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Export all books to CSV file
     */
    public static void exportBooksToCSV(List<Book> books, String filename) throws IOException {
        logger.info("Exporting books to CSV: " + filename);

        try (FileWriter writer = new FileWriter(filename)) {
            // Write header
            writer.append("ID,ISBN,Title,Author,Category,Total Copies,Available Copies,Reference Price,Active,Created At,Updated At\n");

            // Write data
            for (Book book : books) {
                writer.append(String.valueOf(book.getId())).append(",");
                writer.append(escapeCSV(book.getIsbn())).append(",");
                writer.append(escapeCSV(book.getTitle())).append(",");
                writer.append(escapeCSV(book.getAuthor())).append(",");
                writer.append(escapeCSV(book.getCategory())).append(",");
                writer.append(String.valueOf(book.getTotalCopies())).append(",");
                writer.append(String.valueOf(book.getAvailableCopies())).append(",");
                writer.append(book.getReferencePrice().toString()).append(",");
                writer.append(book.isActive() ? "Yes" : "No").append(",");
                writer.append(book.getCreatedAt().format(DATETIME_FORMATTER)).append(",");
                writer.append(book.getUpdatedAt().format(DATETIME_FORMATTER)).append("\n");
            }
        }

        logger.info("Books exported successfully to: " + filename);
    }

    /**
     * Export overdue loans to CSV file
     */
    public static void exportOverdueLoansToCSV(List<Loan> overdueLoans, String filename) throws IOException {
        logger.info("Exporting overdue loans to CSV: " + filename);

        try (FileWriter writer = new FileWriter(filename)) {
            // Write header
            writer.append("Loan ID,Partner Name,Document Number,Book Title,ISBN,Loan Date,Due Date,Days Overdue,Fine Amount,Status\n");

            // Write data
            for (Loan loan : overdueLoans) {
                writer.append(String.valueOf(loan.getId())).append(",");
                writer.append(escapeCSV(loan.getPartnerName())).append(",");
                writer.append(escapeCSV("")).append(","); // Document number not available in current model
                writer.append(escapeCSV(loan.getBookTitle())).append(",");
                writer.append(escapeCSV(loan.getBookIsbn())).append(",");
                writer.append(loan.getLoanDate().format(DATE_FORMATTER)).append(",");
                writer.append(loan.getDueDate().format(DATE_FORMATTER)).append(",");
                writer.append(String.valueOf(loan.getDaysOverdue())).append(",");
                writer.append(loan.getFineAmount().toString()).append(",");
                writer.append(loan.getStatus().name()).append("\n");
            }
        }

        logger.info("Overdue loans exported successfully to: " + filename);
    }

    // Export all loans to CSV file
    public static void exportLoansToCSV(List<Loan> loans, String filename) throws IOException {
        logger.info("Exporting all loans to CSV: " + filename);

        try (FileWriter writer = new FileWriter(filename)) {
            // Write header
            writer.append("Loan ID,Partner Name,Book Title,ISBN,Loan Date,Due Date,Return Date,Fine Amount,Status\n");

            // Write data
            for (Loan loan : loans) {
                writer.append(String.valueOf(loan.getId())).append(",");
                writer.append(escapeCSV(loan.getPartnerName())).append(",");
                writer.append(escapeCSV(loan.getBookTitle())).append(",");
                writer.append(escapeCSV(loan.getBookIsbn())).append(",");
                writer.append(loan.getLoanDate().format(DATE_FORMATTER)).append(",");
                writer.append(loan.getDueDate().format(DATE_FORMATTER)).append(",");

                if (loan.getReturnDate() != null) {
                    writer.append(loan.getReturnDate().format(DATE_FORMATTER));
                } else {
                    writer.append("");
                }

                writer.append(",");
                writer.append(loan.getFineAmount().toString()).append(",");
                writer.append(loan.getStatus().name()).append("\n");
            }
        }

        logger.info("Loans exported successfully to: " + filename);
    }

    //Escape CSV field values
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }

        // If value contains comma, newline, or double quote, wrap in quotes and escape quotes
        if (value.contains(",") || value.contains("\n") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    // Generate timestamped filename
    public static String generateTimestampedFilename(String baseName, String extension) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return baseName + "_" + now.format(formatter) + "." + extension;
    }
}
