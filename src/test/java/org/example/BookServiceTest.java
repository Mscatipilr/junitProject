package org.example;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private BookService bookService;
    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        user = Mockito.mock(User.class);
        book = new Book("1984", "George Orwell", "Dystopian", 9.99);
        bookService.addBook(book);
        Mockito.when(user.getPurchasedBooks()).thenReturn(List.of(book));
    }

    @Test
    void testSearchBook_Success() {
        List<Book> books = bookService.searchBook("1984");
        assertFalse(books.isEmpty(), "Search should return a list with the matching book.");
        assertEquals("1984", books.get(0).getTitle(), "The book title should match the search keyword.");
    }

    @Test
    void testSearchBook_NoMatch() {
        List<Book> books = bookService.searchBook("NonExistentBook");
        assertTrue(books.isEmpty(), "Search should return an empty list if no book matches the keyword.");
    }

    @Test
    void testSearchBook_PartialMatch() {
        List<Book> books = bookService.searchBook("George");
        assertFalse(books.isEmpty(), "Search should return books with matching author names.");
    }

    @Test
    void testPurchaseBook_Success() {
        Mockito.when(user.getPurchasedBooks()).thenReturn(new ArrayList<>());
        boolean result = bookService.purchaseBook(user, book);
        assertTrue(result, "Purchase should succeed if the book exists in the database.");
    }

    @Test
    void testPurchaseBook_BookNotAvailable() {
        Book nonExistentBook = new Book("NonExistent", "Unknown", "Unknown", 0.0);
        boolean result = bookService.purchaseBook(user, nonExistentBook);
        assertFalse(result, "Purchase should fail if the book is not available.");
    }

    @Test
    void testPurchaseBook_EmptyBook() {
        Book emptyBook = new Book("", "", "", 0.0);
        boolean result = bookService.purchaseBook(user, emptyBook);
        assertFalse(result, "Purchase should fail if the book details are empty.");
    }

    @Test
    void testAddBookReview_NotPurchased() {
        Mockito.when(user.getPurchasedBooks()).thenReturn(new ArrayList<>());
        boolean result = bookService.addBookReview(user, book, "Great book!");
        assertFalse(result, "Review should not be added if the user has not purchased the book.");
    }

    @Test
    void testAddBookReview_EmptyReview() {
        Mockito.when(user.getPurchasedBooks()).thenReturn(List.of(book));
        boolean result = bookService.addBookReview(user, book, "");
        assertTrue(result, "Review should be added even if the review text is empty.");
    }
    @Test
    void testAddBookReview_Success() {
        boolean result = bookService.addBookReview(user, book, "Amazing book!");
        assertTrue(result, "Review should be added successfully if the user has purchased the book.");
        assertEquals(1, book.getReviews().size(), "The book should have one review.");
        assertEquals("Amazing book!", book.getReviews().get(0), "The review text should match the input.");
    }

    @Test
    void testAddBookReview_UserNotPurchasedBook() {
        Mockito.when(user.getPurchasedBooks()).thenReturn(List.of());
        boolean result = bookService.addBookReview(user, book, "Great book!");
        assertFalse(result, "Review should not be added if the user has not purchased the book.");
    }


}
