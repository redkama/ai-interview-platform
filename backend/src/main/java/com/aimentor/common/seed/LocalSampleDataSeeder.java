package com.aimentor.common.seed;

import com.aimentor.domain.book.book.entity.Book;
import com.aimentor.domain.book.book.entity.BookCategory;
import com.aimentor.domain.book.book.repository.BookRepository;
import com.aimentor.domain.book.cart.entity.Cart;
import com.aimentor.domain.book.cart.entity.CartItem;
import com.aimentor.domain.book.cart.repository.CartItemRepository;
import com.aimentor.domain.book.cart.repository.CartRepository;
import com.aimentor.domain.book.order.entity.Order;
import com.aimentor.domain.book.order.entity.OrderItem;
import com.aimentor.domain.book.order.entity.OrderStatus;
import com.aimentor.domain.book.order.repository.OrderItemRepository;
import com.aimentor.domain.book.order.repository.OrderRepository;
import com.aimentor.domain.education.english.entity.EnglishLearningHistory;
import com.aimentor.domain.education.english.entity.EnglishLesson;
import com.aimentor.domain.education.english.entity.EnglishLevel;
import com.aimentor.domain.education.english.entity.LevelTestResult;
import com.aimentor.domain.education.english.repository.EnglishLearningHistoryRepository;
import com.aimentor.domain.education.english.repository.EnglishLessonRepository;
import com.aimentor.domain.education.english.repository.LevelTestResultRepository;
import com.aimentor.domain.education.history.entity.HistoryEra;
import com.aimentor.domain.education.history.entity.HistoryLearningHistory;
import com.aimentor.domain.education.history.entity.HistoryLesson;
import com.aimentor.domain.education.history.entity.HistoryQuiz;
import com.aimentor.domain.education.history.repository.HistoryLearningHistoryRepository;
import com.aimentor.domain.education.history.repository.HistoryLessonRepository;
import com.aimentor.domain.education.history.repository.HistoryQuizRepository;
import com.aimentor.domain.interview.entity.InterviewAnswer;
import com.aimentor.domain.interview.entity.InterviewFeedback;
import com.aimentor.domain.interview.entity.InterviewQuestion;
import com.aimentor.domain.interview.entity.InterviewSession;
import com.aimentor.domain.interview.entity.InterviewSessionStatus;
import com.aimentor.domain.interview.repository.InterviewSessionRepository;
import com.aimentor.domain.user.entity.Role;
import com.aimentor.domain.user.entity.User;
import com.aimentor.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("local")
public class LocalSampleDataSeeder implements CommandLineRunner {

    private static final String SAMPLE_USER_EMAIL = "demo@aimentor.dev";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EnglishLessonRepository englishLessonRepository;
    private final EnglishLearningHistoryRepository englishLearningHistoryRepository;
    private final LevelTestResultRepository levelTestResultRepository;
    private final HistoryLessonRepository historyLessonRepository;
    private final HistoryQuizRepository historyQuizRepository;
    private final HistoryLearningHistoryRepository historyLearningHistoryRepository;
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InterviewSessionRepository interviewSessionRepository;

    public LocalSampleDataSeeder(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EnglishLessonRepository englishLessonRepository,
            EnglishLearningHistoryRepository englishLearningHistoryRepository,
            LevelTestResultRepository levelTestResultRepository,
            HistoryLessonRepository historyLessonRepository,
            HistoryQuizRepository historyQuizRepository,
            HistoryLearningHistoryRepository historyLearningHistoryRepository,
            BookRepository bookRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            InterviewSessionRepository interviewSessionRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.englishLessonRepository = englishLessonRepository;
        this.englishLearningHistoryRepository = englishLearningHistoryRepository;
        this.levelTestResultRepository = levelTestResultRepository;
        this.historyLessonRepository = historyLessonRepository;
        this.historyQuizRepository = historyQuizRepository;
        this.historyLearningHistoryRepository = historyLearningHistoryRepository;
        this.bookRepository = bookRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.interviewSessionRepository = interviewSessionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.existsByEmail(SAMPLE_USER_EMAIL)) {
            return;
        }

        User sampleUser = userRepository.save(User.builder()
                .email(SAMPLE_USER_EMAIL)
                .name("Demo User")
                .password(passwordEncoder.encode("password123"))
                .role(Role.USER)
                .build());

        seedEnglishEducation(sampleUser);
        seedHistoryEducation(sampleUser);
        List<Book> books = seedBooks();
        seedCommerce(sampleUser, books);
        seedInterviews(sampleUser);
    }

    private void seedEnglishEducation(User sampleUser) {
        List<EnglishLesson> lessons = englishLessonRepository.saveAll(List.of(
                lesson("English Basics for Introductions", "Speaking", EnglishLevel.BEGINNER, "Build short self-introductions for interview openings.", 1),
                lesson("Daily Confidence Expressions", "Listening", EnglishLevel.BEGINNER, "Understand and repeat common confidence-building expressions.", 2),
                lesson("Simple Achievement Statements", "Writing", EnglishLevel.BEGINNER, "Turn small experiences into simple result-focused statements.", 3),
                lesson("Project Explanation Framework", "Speaking", EnglishLevel.INTERMEDIATE, "Explain context, action, and result without losing structure.", 1),
                lesson("Behavioral Interview English", "Speaking", EnglishLevel.INTERMEDIATE, "Answer teamwork and conflict questions with STAR patterns.", 2),
                lesson("Clarifying Follow-up Questions", "Listening", EnglishLevel.INTERMEDIATE, "Recognize follow-up prompts and respond calmly.", 3),
                lesson("Leadership Storytelling", "Speaking", EnglishLevel.ADVANCED, "Present leadership stories with tradeoffs and measurable impact.", 1),
                lesson("Executive Summary Answers", "Writing", EnglishLevel.ADVANCED, "Compress complex projects into concise business language.", 2),
                lesson("Opinion and Tradeoff Discussion", "Speaking", EnglishLevel.ADVANCED, "Discuss architectural and process tradeoffs with nuance.", 3)
        ));

        levelTestResultRepository.saveAll(List.of(
                LevelTestResult.builder().user(sampleUser).level(EnglishLevel.BEGINNER).testScore(38).testedAt(LocalDateTime.now().minusDays(21)).build(),
                LevelTestResult.builder().user(sampleUser).level(EnglishLevel.INTERMEDIATE).testScore(64).testedAt(LocalDateTime.now().minusDays(12)).build(),
                LevelTestResult.builder().user(sampleUser).level(EnglishLevel.ADVANCED).testScore(87).testedAt(LocalDateTime.now().minusDays(2)).build()
        ));

        englishLearningHistoryRepository.saveAll(List.of(
                EnglishLearningHistory.builder().user(sampleUser).lesson(lessons.get(0)).score(72).completedAt(LocalDateTime.now().minusDays(10)).build(),
                EnglishLearningHistory.builder().user(sampleUser).lesson(lessons.get(3)).score(81).completedAt(LocalDateTime.now().minusDays(6)).build(),
                EnglishLearningHistory.builder().user(sampleUser).lesson(lessons.get(6)).score(89).completedAt(LocalDateTime.now().minusDays(1)).build()
        ));
    }

    private void seedHistoryEducation(User sampleUser) {
        List<HistoryLesson> lessons = new ArrayList<>();
        lessons.addAll(createHistoryLessons(HistoryEra.ANCIENT, "Ancient State Formation", "Myth, federation, and early political order.", "Ancient Society and Culture", "Social structure, belief, and daily life."));
        lessons.addAll(createHistoryLessons(HistoryEra.THREE_KINGDOMS, "Three Kingdoms Competition", "Expansion, diplomacy, and military rivalry.", "Culture Across the Three Kingdoms", "Buddhism, exchange, and regional identity."));
        lessons.addAll(createHistoryLessons(HistoryEra.GORYEO, "Goryeo Central Government", "Aristocracy, bureaucracy, and military rule.", "Goryeo Religion and Trade", "Buddhism, printing, and maritime trade."));
        lessons.addAll(createHistoryLessons(HistoryEra.JOSEON, "Joseon Reform and Governance", "Confucian order, law, and institutional reform.", "Joseon Economy and Society", "Agriculture, local administration, and social hierarchy."));
        lessons.addAll(createHistoryLessons(HistoryEra.MODERN, "Opening Ports and Reform", "External pressure and reform attempts.", "Resistance and Enlightenment Movement", "New ideas, newspapers, and patriotic reform."));
        lessons.addAll(createHistoryLessons(HistoryEra.CONTEMPORARY, "Industrialization and Growth", "Economic development and state-led modernization.", "Democratization Movement", "Civil society, protest, and constitutional change."));

        historyLessonRepository.saveAll(lessons);

        List<HistoryQuiz> quizzes = new ArrayList<>();
        for (HistoryLesson lesson : lessons) {
            quizzes.add(historyQuiz(lesson, lesson.getTitle() + " Key Figure", "[\"Representative leader\",\"Religious founder\",\"Foreign envoy\",\"Local merchant\"]", "Representative leader", "This lesson emphasizes the main political or social actor tied to the topic."));
            quizzes.add(historyQuiz(lesson, lesson.getTitle() + " Core Theme", "[\"Institutional change\",\"Navigation only\",\"Sports culture\",\"Random migration\"]", "Institutional change", "The topic is mainly studied through how institutions or systems changed."));
            quizzes.add(historyQuiz(lesson, lesson.getTitle() + " Historical Impact", "[\"Long-term influence\",\"No influence\",\"Fictional effect\",\"Purely local rumor\"]", "Long-term influence", "The lesson should be connected to broader historical consequences."));
        }
        historyQuizRepository.saveAll(quizzes);

        historyLearningHistoryRepository.saveAll(List.of(
                HistoryLearningHistory.builder().user(sampleUser).lesson(lessons.get(6)).quizScore(83).completedAt(LocalDateTime.now().minusDays(8)).build(),
                HistoryLearningHistory.builder().user(sampleUser).lesson(lessons.get(7)).quizScore(92).completedAt(LocalDateTime.now().minusDays(3)).build(),
                HistoryLearningHistory.builder().user(sampleUser).lesson(lessons.get(11)).quizScore(88).completedAt(LocalDateTime.now().minusDays(1)).build()
        ));
    }

    private List<Book> seedBooks() {
        return bookRepository.saveAll(List.of(
                book("Interview Strategy Playbook", "A. Mentor", "AI Mentor Press", 22000, 18, "Structured mock interview preparation with sample answers and reflection prompts.", "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=900&q=80", BookCategory.INTERVIEW),
                book("Backend Interview Patterns", "Kim Dev", "Code Shelf", 19800, 11, "Frequently asked backend interview patterns and architecture answers.", "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?auto=format&fit=crop&w=900&q=80", BookCategory.INTERVIEW),
                book("Behavioral Answers That Land", "S. Park", "Hiring Note", 17600, 14, "Behavioral interview stories organized by leadership, conflict, and ownership.", "https://images.unsplash.com/photo-1516979187457-637abb4f9353?auto=format&fit=crop&w=900&q=80", BookCategory.INTERVIEW),
                book("One Week Mock Interview Sprint", "J. Choi", "Growth Deck", 15400, 20, "A compact seven-day mock interview sprint plan for final review.", "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?auto=format&fit=crop&w=900&q=80", BookCategory.INTERVIEW),

                book("English Answers for Job Seekers", "Claire Park", "AI Mentor Press", 18000, 10, "English answer structures for self-introduction, projects, and follow-up questions.", "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=900&q=80", BookCategory.ENGLISH),
                book("Business English Warmup", "Daniel Lee", "Language Forge", 16800, 17, "Short business English drills to reduce hesitation in interviews.", "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?auto=format&fit=crop&w=900&q=80", BookCategory.ENGLISH),
                book("Fluent Project Explanations", "Nina Seo", "Speech Lab", 19200, 9, "Explain projects and tradeoffs naturally in interview English.", "https://images.unsplash.com/photo-1516979187457-637abb4f9353?auto=format&fit=crop&w=900&q=80", BookCategory.ENGLISH),
                book("Practical Listening for Interviews", "James Han", "Language Forge", 17400, 16, "Listening practice for recruiter and interviewer follow-up patterns.", "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?auto=format&fit=crop&w=900&q=80", BookCategory.ENGLISH),

                book("Core Korean History Notes", "Min Lee", "History Lab", 19500, 13, "A compact review of Korean history organized by era and exam theme.", "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=900&q=80", BookCategory.KOREAN_HISTORY),
                book("Joseon Reform Essentials", "Y. Jung", "History Table", 18200, 8, "Institutional reform, law, and social order in Joseon.", "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?auto=format&fit=crop&w=900&q=80", BookCategory.KOREAN_HISTORY),
                book("Modern Korea Timeline", "H. Song", "History Lab", 16900, 12, "A visual timeline for reform, occupation, liberation, and democratization.", "https://images.unsplash.com/photo-1516979187457-637abb4f9353?auto=format&fit=crop&w=900&q=80", BookCategory.KOREAN_HISTORY),
                book("Three Kingdoms Story Map", "B. Lim", "History Table", 15800, 15, "The political and cultural flow of Goguryeo, Baekje, and Silla.", "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?auto=format&fit=crop&w=900&q=80", BookCategory.KOREAN_HISTORY),

                book("Self Development for Consistent Practice", "J. Han", "Growth Note", 16800, 22, "Build a repeatable learning system and avoid last-minute preparation.", "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=900&q=80", BookCategory.SELF_DEVELOPMENT),
                book("Deep Work for Candidates", "R. Kwon", "Focus Press", 17300, 19, "Focus routines for resumes, study blocks, and interview rehearsal.", "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?auto=format&fit=crop&w=900&q=80", BookCategory.SELF_DEVELOPMENT),
                book("Weekly Reflection Notebook", "M. Cho", "Growth Note", 14900, 25, "Reflection prompts for strengths, gaps, and next actions every week.", "https://images.unsplash.com/photo-1516979187457-637abb4f9353?auto=format&fit=crop&w=900&q=80", BookCategory.SELF_DEVELOPMENT),
                book("Confidence Through Repetition", "E. Yoo", "Better Habits", 16200, 14, "How to improve speaking confidence by short, repeated practice.", "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?auto=format&fit=crop&w=900&q=80", BookCategory.SELF_DEVELOPMENT),

                book("Creative Problem Solving Cards", "Studio Grid", "Other Shelf", 13800, 18, "A card-style prompt collection for creative thinking and discussion.", "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=900&q=80", BookCategory.OTHER),
                book("Whiteboard Practice Sheets", "Tool Maker", "Other Shelf", 12600, 30, "Reusable whiteboard challenge sheets for technical explanation drills.", "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?auto=format&fit=crop&w=900&q=80", BookCategory.OTHER),
                book("Career Conversation Prompts", "L. Han", "Other Shelf", 14100, 21, "Conversation starters for networking, mentorship, and growth reviews.", "https://images.unsplash.com/photo-1516979187457-637abb4f9353?auto=format&fit=crop&w=900&q=80", BookCategory.OTHER),
                book("Presentation Warmup Deck", "P. Kim", "Stage Note", 15200, 17, "Short exercises for clearer speaking before demos and interviews.", "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?auto=format&fit=crop&w=900&q=80", BookCategory.OTHER)
        ));
    }

    private void seedCommerce(User sampleUser, List<Book> books) {
        Cart cart = cartRepository.save(Cart.builder().user(sampleUser).build());
        cartItemRepository.saveAll(List.of(
                CartItem.builder().cart(cart).book(books.get(1)).quantity(1).build(),
                CartItem.builder().cart(cart).book(books.get(5)).quantity(2).build()
        ));

        Order deliveredOrder = orderRepository.save(Order.builder()
                .user(sampleUser)
                .totalPrice(39600)
                .status(OrderStatus.DELIVERED)
                .orderedAt(LocalDateTime.now().minusDays(15))
                .build());
        Order paidOrder = orderRepository.save(Order.builder()
                .user(sampleUser)
                .totalPrice(36400)
                .status(OrderStatus.PAID)
                .orderedAt(LocalDateTime.now().minusDays(7))
                .build());
        Order shippedOrder = orderRepository.save(Order.builder()
                .user(sampleUser)
                .totalPrice(33200)
                .status(OrderStatus.SHIPPED)
                .orderedAt(LocalDateTime.now().minusDays(2))
                .build());

        orderItemRepository.saveAll(List.of(
                OrderItem.builder().order(deliveredOrder).book(books.get(0)).quantity(1).priceAtOrder(22000).build(),
                OrderItem.builder().order(deliveredOrder).book(books.get(4)).quantity(1).priceAtOrder(17600).build(),
                OrderItem.builder().order(paidOrder).book(books.get(8)).quantity(1).priceAtOrder(19500).build(),
                OrderItem.builder().order(paidOrder).book(books.get(12)).quantity(1).priceAtOrder(16900).build(),
                OrderItem.builder().order(shippedOrder).book(books.get(2)).quantity(1).priceAtOrder(17600).build(),
                OrderItem.builder().order(shippedOrder).book(books.get(16)).quantity(1).priceAtOrder(15600).build()
        ));
    }

    private void seedInterviews(User sampleUser) {
        InterviewSession firstSession = InterviewSession.builder()
                .user(sampleUser)
                .title("Backend Platform Mock Interview")
                .positionTitle("Backend Engineer")
                .status(InterviewSessionStatus.IN_PROGRESS)
                .startedAt(LocalDateTime.now().minusDays(9))
                .resumeSnapshot("Spring Boot, JPA, MySQL, and REST API development experience.")
                .coverLetterSnapshot("Focused on reliable backend systems and practical product delivery.")
                .jobPostingSnapshot("Build scalable platform APIs and collaborate with frontend teams.")
                .build();
        addQuestionWithAnswer(firstSession, 1, "Tell me about a backend project with measurable impact.", "I improved an internal API flow and reduced average response time by 32 percent by restructuring query patterns.");
        addQuestionWithAnswer(firstSession, 2, "How do you approach debugging production issues?", "I narrow the issue by timeline, logs, and reproduction scope, then communicate mitigation steps before the root-cause fix.");
        addQuestionWithAnswer(firstSession, 3, "Describe a conflict with another team and how you resolved it.", "I aligned the API contract with the frontend team using examples, version rules, and a small rollout checklist.");
        firstSession.end();
        firstSession.assignFeedback(InterviewFeedback.builder()
                .interviewSession(firstSession)
                .relevanceScore(86)
                .logicScore(84)
                .specificityScore(82)
                .overallScore(84)
                .summary("Strong structure and practical examples. More quantified impact would make the answers even stronger.")
                .build());

        InterviewSession secondSession = InterviewSession.builder()
                .user(sampleUser)
                .title("Service Reliability Interview")
                .positionTitle("Platform Backend Engineer")
                .status(InterviewSessionStatus.IN_PROGRESS)
                .startedAt(LocalDateTime.now().minusDays(4))
                .resumeSnapshot("Designed APIs, improved logging, and worked on release processes.")
                .coverLetterSnapshot("Interested in reliability, ownership, and strong cross-team collaboration.")
                .jobPostingSnapshot("Own backend features, observability, and service operations.")
                .build();
        addQuestionWithAnswer(secondSession, 1, "How do you design APIs that are easy for clients to use?", "I start with the client workflow, define stable resources, and document clear validation and error responses.");
        addQuestionWithAnswer(secondSession, 2, "What is your process for handling incident communication?", "I share current impact, short-term mitigation, and next checkpoints so stakeholders know what changed and what is still unknown.");
        addQuestionWithAnswer(secondSession, 3, "What would you improve in your last project if given more time?", "I would strengthen observability dashboards and create more integration tests around failure scenarios.");
        secondSession.end();
        secondSession.assignFeedback(InterviewFeedback.builder()
                .interviewSession(secondSession)
                .relevanceScore(90)
                .logicScore(88)
                .specificityScore(85)
                .overallScore(88)
                .summary("Clear and job-relevant answers with strong ownership signals. Add one deeper technical tradeoff example for maximum impact.")
                .build());

        interviewSessionRepository.saveAll(List.of(firstSession, secondSession));
    }

    private EnglishLesson lesson(String title, String part, EnglishLevel level, String content, int orderNum) {
        return EnglishLesson.builder()
                .title(title)
                .part(part)
                .level(level)
                .content(content)
                .orderNum(orderNum)
                .build();
    }

    private List<HistoryLesson> createHistoryLessons(
            HistoryEra era,
            String firstTitle,
            String firstContent,
            String secondTitle,
            String secondContent
    ) {
        return List.of(
                HistoryLesson.builder().title(firstTitle).era(era).content(firstContent).orderNum(1).build(),
                HistoryLesson.builder().title(secondTitle).era(era).content(secondContent).orderNum(2).build()
        );
    }

    private HistoryQuiz historyQuiz(HistoryLesson lesson, String question, String options, String answer, String explanation) {
        return HistoryQuiz.builder()
                .lesson(lesson)
                .question(question)
                .options(options)
                .answer(answer)
                .explanation(explanation)
                .build();
    }

    private Book book(
            String title,
            String author,
            String publisher,
            int price,
            int stock,
            String description,
            String imageUrl,
            BookCategory category
    ) {
        return Book.builder()
                .title(title)
                .author(author)
                .publisher(publisher)
                .price(price)
                .stock(stock)
                .description(description)
                .imageUrl(imageUrl)
                .category(category)
                .build();
    }

    private void addQuestionWithAnswer(InterviewSession session, int sequenceNumber, String questionText, String answerText) {
        InterviewQuestion question = InterviewQuestion.builder()
                .interviewSession(session)
                .sequenceNumber(sequenceNumber)
                .questionText(questionText)
                .build();
        InterviewAnswer answer = InterviewAnswer.builder()
                .interviewQuestion(question)
                .answerText(answerText)
                .audioUrl("https://cdn.aimentor.dev/sample-audio/interview-" + sequenceNumber + ".wav")
                .build();
        question.assignAnswer(answer);
        session.addQuestion(question);
    }
}
