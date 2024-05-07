package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.AdminAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.response.BudgetHistoryResponse;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 어드민 권한을 가진 유저만 사용할 수 있는 컨트롤러이며 주로 어드민 페이지와 관련된 컨트롤러입니다.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    private final AdminAdaptor adminAdaptor;
    private final UserAdaptor userAdaptor;

    /**
     * 어드민 페이지에 처음 들어왔을때 기본으로 로딩되는 페이지로 소속된 조직에서 Guest 권한을 가지고 있는 유저들의 리스트를 확인할 수 있는
     * 페이지를 불러옵니다.
     * @param model 결과 페이지에 전달할 파라미터입니다. 해당 컨트롤러는 사이드바 로딩에 필요한 유저 정보와 조직에 속한 GUEST 유저 권한을
     *              가진 유저들의 리스트를 프론트로 전달합니다.
     * @return 해당하는 경로의 html 파일을 불러오며 GUEST권한을 가진 유저의 리스트와 유저의 권한을 관리할 수 있는 페이지를 반환합니다.
     */
    @GetMapping
    public String adminPage(Model model) {
        //Guest 유저 리스트 불러오기
        List<GetUserResponse> userList = adminAdaptor.requestGuestUsers().getBody();
        model.addAttribute("userList", userList);

        return "/admin/admin-authority";
    }

    /**
     * 게스트 유저 관리 버튼을 눌렀을 때 작동하는 컨트롤러로 소속된 조직에서 Guest 권한을 가지고 있는 유저들의 리스트를 불러옵니다.
     * @param model 결과 페이지에 전달할 파라미터입니다.
     * @return 어드민 페이지에 처음 들어왔을 때 기본으로 로딩되는 페이지와 같기에 adminPage 메서드를 재사용합니다.
     */
    @GetMapping("/roleup")
    public String roleUpPage(Model model) {
        return adminPage(model);
    }

    /**
     * 유저 관리 버튼을 눌렀을 때 작동하는 컨트롤러로 조직에 소속된 모든 유저의 리스트를 확인할 수있는 페이지를 불러옵니다.
     * @param model 결과 페이지에 전달할 파라미터입니다. 해당 컨트롤러는 사이드바 로딩에 필요한 유저 정보와 조직에 속한 모든 유저들의 리스트를
     *              프론트로 전달합니다.
     * @return 해당하는 경로의 html 파일을 불러오며 모든 유저를 확인하는 페이지를 반환합니다.
     */
    @GetMapping("/users")
    public String userListPage(Model model) {
        //유저 정보
        List<GetUserResponse> userList = adminAdaptor.requestUsers().getBody();
        model.addAttribute("userList", userList);

        return "/admin/admin-user";
    }

    /**
     * 요금 설정 버튼을 눌렀을 때 작동하는 컨트롤러로 admin이 속한 조직의 예산을 설정하는 페이지를 불러옵니다.
     * @param model 결과 페이지에 전달할 파라미터입니다. 해당 컨트롤러는 사이드바 로딩에 필요한 유저 정보와 조직이 현재 설정한 예산을 프론트로
     *              전달합니다.
     * @return 해당하는 경로의 html 파일을 불러오며 조직의 현재 예산을 확인하고 수정하는 페이지를 반환합니다.
     */
    @GetMapping("/budget")
    public String budgetPage(Model model) {
        //조직 예산
        OrganizationResponse budget = userAdaptor.requestBudget().getBody();
        model.addAttribute("budget", budget);

        return "/admin/admin-budget";
    }

    /**
     * 요금 변경 내역 버튼을 눌렀을 때 작동하는 컨트롤러로 admin이 변경했던 조직의 예산 변경 내역 리스트를 불러옵니다.
     * @param model 결과 페이지에 전달할 파라미터입니다. 해당 컨트롤러는 사이드바 로딩에 필요한 유저 정보와 해당하는 조직의 예산 변경 내역
     *              리스트를 프론트로 전달합니다.
     * @return 해당하는 경로의 html 파일을 불러오며 해당하는 조직의 예산 변경 내역 리스트를 확인하는 페이지를 반환합니다.
     */
    @GetMapping("/budgethistory")
    public String budgetHistoryPage(Model model) {
        //예산 내역
        List<BudgetHistoryResponse> budgetHistory = adminAdaptor.requestBudgetHistory().getBody();
        model.addAttribute("budgetHistory", budgetHistory);

        return "/admin/admin-budget-history";
    }

    /**
     * 조직 정보 관리 버튼을 눌렀을 때 작동하는 컨트롤러로 admin이 속한 조직의 이름과 조직코드를 불러옵니다.
     * @param model 결과 페이지에 전달할 파라미터입니다. 해당 컨트롤러는 사이드바 로딩에 필요한 유저 정보와 조직의 이름과 조직 코드를 프론트로
     *              전달합니다.
     * @return 해당하는 경로의 html 파일을 불러오며 해당하는 조직 정보를 확인하는 페이지를 반환합니다.
     */
    @GetMapping("/organization")
    public String organizationPage(Model model) {
        //조직 내용
        OrganizationResponse organization = adminAdaptor.requestOrganization().getBody();
        model.addAttribute("organization", organization);

        return "/admin/admin-organization";
    }

    @GetMapping("/checkcode")
    public ResponseEntity<Boolean> codeCheck(@RequestParam String code) {
        boolean existCheck = adminAdaptor.requestCheckcode(code).getBody();
        return ResponseEntity.ok(existCheck);
    }

    /**
     * 게스트 유저관리 화면에서 해당하는 아이디의 권한 변경 버튼을 눌렀을 때 작동하는 컨트롤러로 GUEST 유저의 USER로의 권한을 올립니다.
     * @param userId 권한을 바꿀 유저의 userId 파라미터입니다.
     * @return 어드민 페이지 컨트롤러로 리다이렉션합니다.
     */
    @PutMapping("/roles/{userId}")
    public String upgradeFromGuestToMember(@PathVariable("userId") String userId) {
        //유저정보 변경
        adminAdaptor.requestRole(userId);
        return "redirect:/admin";
    }

    /**
     * 요금 설정 화면에서 목표 요금을 입력하고 저장버튼을 눌렀을 때 작동하는 컨트롤러로 입력한 숫자로 설정 요금을 수정합니다.
     * @param budget 입력받은 요금의 값입니다.
     * @return 요금 설정 페이지 컨트롤러로 리다이렉션합니다.
     */
    @PutMapping("/budget")
    public String updateBudget(@RequestParam Long budget) {
        //목표금액 변경
        adminAdaptor.updateBudget(budget);
        return "redirect:/admin/budget";
    }

    @PutMapping("/organizationname")
    public String updateOrganizationName(@RequestParam String name){
        //조직 이름 변경
        adminAdaptor.updateOrganizationName(name);
        return "redirect:/admin/organization";
    }

    @PutMapping("/organizationcode")
    public String updateOrganizationCode(@RequestParam String code){
        //조직 코드 변경
        adminAdaptor.updateOrganizationCode(code);
        return "redirect:/admin/organization";
    }
}
