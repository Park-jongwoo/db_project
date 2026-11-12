package com.example.db_project.config;

import com.example.db_project.domain.Book;
import com.example.db_project.domain.Category;
import com.example.db_project.domain.Member;
import com.example.db_project.repository.BookRepository;
import com.example.db_project.repository.CategoryRepository;
import com.example.db_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // 수정 포인트: "소설" 카테고리가 이미 있으면 가져오고, 없으면 새로 저장합니다.
        Category novel = categoryRepository.findByName("소설")
            .orElseGet(() -> categoryRepository.save(new Category("소설")));

        // 만약 회원이나 도서도 앱을 켤 때마다 중복으로 쌓이는 게 걱정된다면
        // 아래처럼 데이터가 아예 없을 때만 넣도록 조건문을 걸어줄 수도 있습니다.
        if (memberRepository.count() == 0) {
            for (int i = 1; i <= 5; i++) {
                memberRepository.save(new Member("회원" + i, "member" + i + "@test.com"));
            }
        }

        if (bookRepository.count() == 0) {
            for (int i = 1; i <= 10; i++) {
                Book book = new Book("도서 " + i, "저자 " + i, 10000, 5);
                book.assignCategory(novel);
                bookRepository.save(book);
            }
        }
    }
}