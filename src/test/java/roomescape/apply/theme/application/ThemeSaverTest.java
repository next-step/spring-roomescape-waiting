package roomescape.apply.theme.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import roomescape.apply.theme.domain.InMemoryThemeRepository;
import roomescape.apply.theme.domain.repository.ThemeRepository;
import roomescape.apply.theme.ui.dto.ThemeRequest;
import roomescape.apply.theme.ui.dto.ThemeResponse;
import roomescape.support.BaseTestService;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.ReservationsFixture.themeRequest;

class ThemeSaverTest extends BaseTestService {
    private ThemeSaver themeSaver;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        themeRepository = new InMemoryThemeRepository();
        themeSaver = new ThemeSaver(themeRepository);
    }

    @AfterEach
    void clear() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("새로운 테마를 저장할 수 있다.")
    void recordReservationBy() {
        // given
        assertThat(themeRepository.findAll()).isEmpty();
        ThemeRequest request = themeRequest();
        // when
        ThemeResponse response = themeSaver.saveThemeBy(request);
        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotZero();
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(request);
    }

}
