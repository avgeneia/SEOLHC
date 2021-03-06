package com.mhms.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mhms.dto.CodeDto;
import com.mhms.dto.UserListDto;
import com.mhms.dto.UserPopupDto;
import com.mhms.security.UserContext;
import com.mhms.service.CodeService;
import com.mhms.service.UserService;
import com.mhms.util.CommUtil;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CodeService codeService;
	
	/*
	 * 조회 단건
	 */
	@RequestMapping("/selectUser")
	@ResponseBody
	public UserListDto selectUser(@RequestParam(value = "uid") int uid) throws JsonProcessingException {

		List<UserListDto> result = userService.selectUser(uid);
		
		return result.get(0);
	}
	
	/*
	 * 조회 목록(MAIN)
	 */
	@RequestMapping("/userList")
	public String userList(Model model, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "사용자 관리");
		
		//관리자 권한을 확인해서 전체를 조회하게
		boolean auth = CommUtil.isRole(user, "ROLE_ADMIN");
				
		//사용자 목록
		List<UserListDto> userList = userService.userList(user);
		List<CodeDto> codeList = codeService.getCode("ROLE", false, 0);
		
		//관리자의 경우 모든 사용자의 수정이 가능.		
		for(int i = 0; i < userList.size(); i++) {
			
			/*
			 * L1 : 시스템 관리자는 모든 수정권한이 있음.
			 * L2 : 매니저의 경우 해당 관리건물의 사용자만 수정가능.
			 * */
			if( auth
			|| CommUtil.isRole(user, "ROLE_MANAGER")) {
				userList.get(i).setIsUpdate(1);
			}
		}
		
		//관리자권한 체크
		for(int i = 0; i < codeList.size(); i++) {
			
			if(!auth && codeList.get(i).getCd().equals("ROLE_ADMIN")) {
				codeList.remove(i);
			}
		}
		
		UserPopupDto dto = new UserPopupDto(-1, "", "", "-1");
		
		model.addAttribute("userPopVO", dto);
		model.addAttribute("initRoleVO", codeList);
		model.addAttribute("userVO", userList);
		model.addAttribute("pageInfo", "userList");
		
		return "userList";
		
	}
		
	/*
	 * 추가
	 */
	@RequestMapping("/insertUser")
	@ResponseBody
	public Map<String, String> insertUser(HttpServletRequest request) throws JsonProcessingException {

		Map<String, String> map = new HashMap<String, String>();

		try {
			int result = userService.insertUser(request.getParameterMap());
			
			if(result == 0) {
				map.put("CODE", "19");
				map.put("MSG", "이미 등록된 사용자 입니다.");
			} else {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}
		
		return map;
	}
	
	/*
	 * 수정
	 */
	@RequestMapping("/updateUser")
	@ResponseBody
	public Map<String, String> updateUser(HttpServletRequest request) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			long result = userService.updateUser(request.getParameterMap());
			
			if(result == 1) {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			} else {
				map.put("CODE", "19");
				map.put("MSG", "이미 등록된 사용자 입니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}
		
		return map;
	}
	
	/*
	 * 수정 : password 변경
	 */
	@RequestMapping("/changePassword")
	@ResponseBody
	public Map<String, String> changePassword(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			long result = userService.changePassword(request.getParameterMap(), user);
			
			if(result == 1) {
				map.put("CODE", "0");
				map.put("MSG", "변경되었습니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}
		
		return map;
	}
	
	/*
	 * 수정 : password 초기화
	 */
	@RequestMapping("/resetUser")
	@ResponseBody
	public Map<String, String> resetUser(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			long result = userService.resetUser(request.getParameterMap(), user);
			
			if(result == 1) {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}
		
		return map;
	}
	
	/*
	 * 수정 : 사용유무
	 */
	@RequestMapping("/updateUserUseYn")
	@ResponseBody
	public Map<String, String> updateUserUseYn(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			
			long result = userService.updateUserUseyn(request.getParameterMap());
			
			if(result == 1) {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}
		
		return map;
	}
	
	/*
	 * 삭제
	 */
	@RequestMapping("/deleteUser")
	@ResponseBody
	public Map<String, String> deleteUser(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<String, String>();
		try {
			long result = userService.deleteUser(request.getParameterMap());
			
			if(result == 1) {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}
		return map;
	}
}
