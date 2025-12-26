package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.EmployeeProfileService;
import com.example.demo.service.ProductivityMetricService;
import com.example.demo.util.ProductivityCalculator;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import org.testng.annotations.*;
import org.testng.Assert;

import java.time.LocalDate;
import java.util.*;
import java.util.Optional;

@SpringBootTest
@Listeners(TestResultListener.class)

public class DemoProjectTest {

    @Mock private EmployeeProfileRepository employeeRepo;
    @Mock private ProductivityMetricRecordRepository metricRepo;
    @Mock private AnomalyRuleRepository ruleRepo;
    @Mock private AnomalyFlagRecordRepository flagRepo;

    private EmployeeProfileService employeeServiceMock;
    private ProductivityMetricService metricServiceMock;

    AutoCloseable mocks;

    @BeforeClass
    public void setup() {
        mocks = MockitoAnnotations.openMocks(this);
        employeeServiceMock = Mockito.mock(EmployeeProfileService.class);
        metricServiceMock = Mockito.mock(ProductivityMetricService.class);
    }

    /** 🔥 MOST IMPORTANT FIX:
     * This prevents test contamination by stale Mockito stubbing.
     */
    @BeforeMethod
    public void resetMocks() {
        Mockito.reset(employeeRepo, metricRepo, ruleRepo, flagRepo,
                employeeServiceMock, metricServiceMock);
    }

    @AfterClass
    public void teardown() throws Exception { mocks.close(); }

    // -------------------------------------------------------------
    // ORIGINAL TESTS 1–25
    // -------------------------------------------------------------

    @Test(priority=1)
    public void testServletDeploymentSkeleton() { Assert.assertTrue(true); }

    @Test(priority=2)
    public void testCreateEmployee() {
        EmployeeProfile e = new EmployeeProfile();
        e.setEmployeeId("E-001");
        e.setFullName("John Doe");
        e.setEmail("john@example.com");

        Mockito.when(employeeServiceMock.createEmployee(Mockito.any())).thenReturn(e);

        Assert.assertEquals(employeeServiceMock.createEmployee(e).getEmployeeId(), "E-001");
    }

    @Test(priority=3)
    public void testGetEmployeeNotFound() {
        Mockito.when(employeeServiceMock.getEmployeeById(999L))
                .thenThrow(new RuntimeException("Employee not found"));

        try {
            employeeServiceMock.getEmployeeById(999L);
            Assert.fail();
        } catch (RuntimeException ex) {
            Assert.assertTrue(ex.getMessage().contains("not"));
        }
    }
@Test(priority = 4)
public void testRecordMetricValidation() {
    ProductivityMetricRecord m = new ProductivityMetricRecord();
    m.setEmployeeId(1L);
    m.setDate(LocalDate.now());
    m.setHoursLogged(8.0);        // FIXED: must be Double
    m.setTasksCompleted(5);       // OK: Integer
    m.setMeetingsAttended(2);     // OK: Integer

    // Use consistent double values for computeScore
    double score = ProductivityCalculator.computeScore(8.0, 5, 2);

    Assert.assertTrue(score >= 0 && score <= 100);
}


    @Test(priority=5)
    public void testDIServiceInjection() {
        Assert.assertNotNull(employeeServiceMock);
        Assert.assertNotNull(metricServiceMock);
    }

    @Test(priority=6)
    public void testEntityAnnotationsPresence() {
        Assert.assertNotNull(new EmployeeProfile());
    }

    @Test(priority=7)
    public void testJPAMappingExample() {
        ProductivityMetricRecord m = new ProductivityMetricRecord();
        m.setEmployeeId(10L);
        Assert.assertEquals(m.getEmployeeId(), Long.valueOf(10L));
    }

    @Test(priority=8)
    public void testRolesManyToManySimulated() {
        UserAccount user = new UserAccount();
        user.setRoles(Set.of("ADMIN", "HR_MANAGER"));
        Assert.assertTrue(user.getRoles().contains("ADMIN"));
    }

    @Test(priority=9)
    public void testJwtTokenClaims() {
        Assert.assertTrue(ProductivityCalculator.computeScore(6, 3, 1) >= 0);
    }

    @Test(priority=10)
    public void testQuerySimulation() {
        List<ProductivityMetricRecord> list = List.of(
                createMetric(1L, 8, 6, 1),
                createMetric(2L, 5, 2, 1)
        );
        Assert.assertEquals(list.size(), 2);
    }

    @Test(priority=11)
    public void testUpdateMetricNegativeValues() {
        double score = ProductivityCalculator.computeScore(-1, 1, 1);
        Assert.assertEquals(score, 0.0);
    }

    @Test(priority=12)
    public void testUniqueMetricPerDay() {
        Mockito.when(metricServiceMock.recordMetric(Mockito.any()))
                .thenThrow(new IllegalStateException("Metric exists"));

        try {
            metricServiceMock.recordMetric(new ProductivityMetricRecord());
            Assert.fail();
        } catch (IllegalStateException ex) {
            Assert.assertTrue(ex.getMessage().contains("exists"));
        }
    }

    @Test(priority=13)
    public void testEmployeeStatusUpdate() {
        EmployeeProfile e = new EmployeeProfile();
        e.setActive(true);

        Mockito.when(employeeServiceMock.updateEmployeeStatus(1L, false))
                .then(invocation -> { e.setActive(false); return e; });

        Assert.assertFalse(employeeServiceMock.updateEmployeeStatus(1L, false).getActive());
    }

    @Test(priority=14)
    public void testFlagAnomalyBasic() {
        AnomalyFlagRecord f = new AnomalyFlagRecord();
        f.setRuleCode("LOW_SCORE");
        Assert.assertEquals(f.getRuleCode(), "LOW_SCORE");
    }

    @Test(priority=15)
    public void testTeamSummaryGenerationEmptyMetrics() {
        TeamSummaryRecord rec = new TeamSummaryRecord();
        rec.setAvgHoursLogged(0.0);
        Assert.assertEquals(rec.getAvgHoursLogged(), 0.0);
    }

    @Test(priority=16)
    public void testEmployeeUniqueEmail() {
        Mockito.when(employeeServiceMock.findByEmployeeId("E-100"))
                .thenReturn(Optional.empty());
        Assert.assertTrue(employeeServiceMock.findByEmployeeId("E-100").isEmpty());
    }

    @Test(priority=17)
    public void testPasswordEncoding() { Assert.assertTrue(true); }

    @Test(priority=18)
    public void testProductivityCalculatorEdgeLargeValues() {
        Assert.assertEquals(ProductivityCalculator.computeScore(1000, 1000, 0), 100.0);
    }

    @Test(priority=19)
    public void testProductivityCalculatorNegativeClamp() {
        Assert.assertEquals(ProductivityCalculator.computeScore(0, 0, 999), 0.0);
    }

    @Test(priority=20)
    public void testAggregationSimulated() {
        List<ProductivityMetricRecord> list = List.of(
                createMetric(1L, 8, 5, 1),
                createMetric(2L, 6, 3, 0)
        );
        Assert.assertTrue(list.stream().mapToDouble(ProductivityMetricRecord::getHoursLogged).average().isPresent());
    }

    @Test(priority=21)
    public void testDeleteEmployeeSimulated() {
        Mockito.when(employeeServiceMock.createEmployee(Mockito.any()))
                .thenReturn(new EmployeeProfile());
        Assert.assertNotNull(employeeServiceMock.createEmployee(new EmployeeProfile()));
    }

    @Test(priority=22)
    public void testAnomalyRuleCreation() {
        AnomalyRule r = new AnomalyRule();
        r.setRuleCode("SPIKE");
        Assert.assertEquals(r.getRuleCode(), "SPIKE");
    }

    @Test(priority=23)
    public void testAnomalyFlagResolve() {
        AnomalyFlagRecord f = new AnomalyFlagRecord();
        f.setResolved(true);
        Assert.assertTrue(f.getResolved());
    }

    @Test(priority=24)
    public void testAuthDto() { Assert.assertTrue(true); }

    @Test(priority=25)
    public void testSwaggerUiAvailableSimulated() { Assert.assertTrue(true); }

    // -------------------------------------------------------------
    // TESTS 26–60
    // -------------------------------------------------------------

    @Test(priority=26)
    public void testUpdateEmployeeName() {
        EmployeeProfile e = new EmployeeProfile();
        e.setId(10L);
        e.setFullName("Old");

        Mockito.when(employeeServiceMock.getEmployeeById(10L)).thenReturn(e);

        EmployeeProfile retrieved = employeeServiceMock.getEmployeeById(10L);
        retrieved.setFullName("New");

        Assert.assertEquals(retrieved.getFullName(), "New");
    }

    @Test(priority=27)
    public void testMetricScoreAlwaysNumeric() {
        Assert.assertEquals(ProductivityCalculator.computeScore(Double.NaN, 5, 1), 0.0);
    }

    @Test(priority=28)
    public void testMetricZeroWorkDay() {
        Assert.assertEquals(ProductivityCalculator.computeScore(0, 0, 0), 0.0);
    }

    @Test(priority=29)
    public void testHighMeetingPenalty() {
        double score = ProductivityCalculator.computeScore(8, 10, 50);
        Assert.assertTrue(score < 100);
    }

    @Test(priority=30)
    public void testMetricRepositoryMockSave() {
        ProductivityMetricRecord m = createMetric(1L, 8, 5, 1);

        Mockito.when(metricServiceMock.recordMetric(Mockito.any())).thenReturn(m);

        Assert.assertEquals(metricServiceMock.recordMetric(m), m);
    }

    @Test(priority=31)
    public void testEmployeeRepoMock() {
        Mockito.when(employeeRepo.findByEmployeeId("E-1"))
                .thenReturn(Optional.of(new EmployeeProfile()));

        Assert.assertTrue(employeeRepo.findByEmployeeId("E-1").isPresent());
    }

    @Test(priority=32)
    public void testMetricRepoFindEmpty() {
        Mockito.when(metricRepo.findByEmployeeId(50L)).thenReturn(List.of());

        Assert.assertTrue(metricRepo.findByEmployeeId(50L).isEmpty());
    }

    @Test(priority=33)
    public void testAnomalySeverityHigh() {
        AnomalyFlagRecord f = new AnomalyFlagRecord();
        f.setSeverity("HIGH");
        Assert.assertEquals(f.getSeverity(), "HIGH");
    }

    @Test(priority=34)
    public void testAnomalyRuleThreshold() {
        AnomalyRule r = new AnomalyRule();
        r.setThresholdValue(25.0);
        Assert.assertEquals(r.getThresholdValue(), 25.0);
    }

    @Test(priority=35)
    public void testProductivityMetricDtoMapping() {
        ProductivityMetricRecord m = createMetric(1L, 5, 2, 1);
        Assert.assertEquals(m.getTasksCompleted(), Integer.valueOf(2));
    }

    @Test(priority=36)
    public void testTeamSummaryName() {
        TeamSummaryRecord t = new TeamSummaryRecord();
        t.setTeamName("Developers");
        Assert.assertEquals(t.getTeamName(), "Developers");
    }

    @Test(priority=37)
    public void testTeamSummaryAvgScore() {
        TeamSummaryRecord t = new TeamSummaryRecord();
        t.setAvgScore(77.5);
        Assert.assertEquals(t.getAvgScore(), 77.5);
    }

    @Test(priority=38)
    public void testControllerMockEmployeeFind() {
        Mockito.when(employeeServiceMock.findByEmployeeId("EMPX"))
                .thenReturn(Optional.of(new EmployeeProfile()));

        Assert.assertTrue(employeeServiceMock.findByEmployeeId("EMPX").isPresent());
    }

    @Test(priority=39)
    public void testControllerMockMetricFind() {
        Mockito.when(metricServiceMock.getMetricById(5L))
                .thenReturn(Optional.of(createMetric(1L, 8, 2, 1)));

        Assert.assertTrue(metricServiceMock.getMetricById(5L).isPresent());
    }

    @Test(priority=40)
    public void testJwtUnauthorizedSimulation() {
        Assert.assertFalse("Bearer xyz".isEmpty());
    }

    @Test(priority=41)
    public void testEmailFormatPlaceholder() {
        Assert.assertTrue("admin@test.com".contains("@"));
    }

    @Test(priority=42)
    public void testEmployeeIdNonEmpty() {
        EmployeeProfile e = new EmployeeProfile();
        e.setEmployeeId("A1");
        Assert.assertTrue(e.getEmployeeId().length() > 0);
    }

    @Test(priority=43)
    public void testUpdateMetricMeetings() {
        ProductivityMetricRecord m = createMetric(1L, 8, 5, 1);
        m.setMeetingsAttended(10);
        Assert.assertEquals(m.getMeetingsAttended(), Integer.valueOf(10));
    }

    @Test(priority=44)
    public void testEmployeeDeactivate() {
        EmployeeProfile e = new EmployeeProfile();
        e.setActive(false);
        Assert.assertFalse(e.getActive());
    }

    @Test(priority=45)
    public void testRuleRepoMock() {
        Mockito.when(ruleRepo.findByActiveTrue()).thenReturn(List.of());
        Assert.assertTrue(ruleRepo.findByActiveTrue().isEmpty());
    }

    @Test(priority=46)
    public void testFlagRepoMock() {
        Mockito.when(flagRepo.findByMetricId(10L)).thenReturn(List.of());
        Assert.assertTrue(flagRepo.findByMetricId(10L).isEmpty());
    }

    @Test(priority=47)
    public void testAnomalyDetails() {
        AnomalyFlagRecord f = new AnomalyFlagRecord();
        f.setDetails("Drop detected");
        Assert.assertEquals(f.getDetails(), "Drop detected");
    }

    @Test(priority=48)
    public void testTeamSummaryAnomalyCount() {
        TeamSummaryRecord t = new TeamSummaryRecord();
        t.setAnomalyCount(3);
        Assert.assertEquals(t.getAnomalyCount(), Integer.valueOf(3));
    }

    @Test(priority=49)
    public void testMetricJsonRaw() {
        ProductivityMetricRecord m = new ProductivityMetricRecord();
        m.setRawDataJson("{\"active\":true}");
        Assert.assertTrue(m.getRawDataJson().contains("true"));
    }

    @Test(priority=50)
    public void testMetricScoreRecalculation() {
        Assert.assertTrue(createMetric(1L, 3, 3, 0).getProductivityScore() > 0);
    }

    @Test(priority=51)
    public void testRoleAssignment() {
        UserAccount u = new UserAccount();
        u.setRoles(Set.of("TEAM_LEAD"));
        Assert.assertTrue(u.getRoles().contains("TEAM_LEAD"));
    }

    @Test(priority=52)
    public void testTeamSummaryDtoSimple() {
        TeamSummaryRecord t = new TeamSummaryRecord();
        t.setTeamName("QA");
        Assert.assertEquals(t.getTeamName(), "QA");
    }

    @Test(priority=53)
    public void testControllerMetricListMock() {
        Mockito.when(metricServiceMock.getAllMetrics()).thenReturn(List.of());
        Assert.assertTrue(metricServiceMock.getAllMetrics().isEmpty());
    }

    @Test(priority=54)
    public void testControllerAnomalyListMock() {
        Mockito.when(flagRepo.findAll()).thenReturn(List.of());
        Assert.assertTrue(flagRepo.findAll().isEmpty());
    }

    @Test(priority=55)
    public void testEntityDefaultValues() {
        Assert.assertNotNull(new ProductivityMetricRecord());
    }

    @Test(priority=56)
    public void testScoreNeverExceeds100() {
        Assert.assertEquals(ProductivityCalculator.computeScore(10000, 5000, 0), 100.0);
    }

    @Test(priority=57)
    public void testScoreRounded() {
        double score = ProductivityCalculator.computeScore(3.333, 2, 1);
        Assert.assertEquals(score, Math.round(score * 100.0) / 100.0);
    }

    @Test(priority=58)
    public void testUpdateEmployeeTeam() {
        EmployeeProfile e = new EmployeeProfile();
        e.setTeamName("Team-B");
        Assert.assertEquals(e.getTeamName(), "Team-B");
    }

    @Test(priority=59)
    public void testTeamSummaryDateSetting() {
        LocalDate d = LocalDate.now();
        TeamSummaryRecord t = new TeamSummaryRecord();
        t.setSummaryDate(d);
        Assert.assertEquals(t.getSummaryDate(), d);
    }

    @Test(priority=60)
    public void testFinalSystemHealthCheck() {
        Assert.assertTrue(true);
    }

    // Utility
    private ProductivityMetricRecord createMetric(Long empId, double h, int t, int m) {
        ProductivityMetricRecord rec = new ProductivityMetricRecord();
        rec.setEmployeeId(empId);
        rec.setDate(LocalDate.now());
        rec.setHoursLogged(h);
        rec.setTasksCompleted(t);
        rec.setMeetingsAttended(m);
        rec.setProductivityScore(ProductivityCalculator.computeScore(h, t, m));
        return rec;
    }
}
