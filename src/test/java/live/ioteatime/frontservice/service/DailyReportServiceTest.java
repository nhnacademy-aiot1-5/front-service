package live.ioteatime.frontservice.service;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DailyReportServiceTest {
    @Mock
    private UserAdaptor userAdaptor;

    @Mock
    Model model;

    @InjectMocks
    private DailyReportService dailyReportService;

    @BeforeEach
    void setup() {
        dailyReportService = new DailyReportService(userAdaptor);
    }

    @Test
    @DisplayName("일별 리포트 서비스 첫 화면 요청시 place가 null일때")
    void initDailyReport() {
        OrganizationResponse organizationResponse =
                new OrganizationResponse(1, "nhn", 3000L, "1588");

        Mockito.when(userAdaptor.getOrganization()).thenReturn(ResponseEntity.ok(organizationResponse));
        Mockito.when(userAdaptor.getPlacesByOrganizationId(organizationResponse.getId())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> dailyReportService.initDailyReport(model));
    }
}
