package com.kh.spring.board.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.member.model.vo.Member;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("/board/*")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	/*
	 * @RequestParam : 파라미터를 전달 받는 역할을 하는 어노테이션 

		속성 없이 사용하는 방법 
		@RequestParam("input태그 name 속성값") 자료형 변수명 


		속성
		value : 전달 받은 input 태그의 name 속성 값 
		required : 파라미터 필수 여부 지정(true(기본값)/false) 
		defaultValue : (선행 조건 : required 속성 값이 false)
		전달받은 파라미터가 존재하지 않을 때 기본값을 지정 

	 * 
	 * */
	
	// @PathVariable: URL 경로에 있는 값을 파라미터로 사용할 수 있게 하는 어노테이션
	@RequestMapping("list/{type}")
	public String boardList( @PathVariable int type, 
							@RequestParam(value="cp", required=false, defaultValue="1") int cp   , 
							Model model) {
		System.out.println("type : " + type  );
		
		
		
		
		// 1) Pagination 처리에 사용될 클래스 PageInfo 작성 bean 등록
			
		// 2) pageInfo 초기세팅
			// -> 전체 게시글 수 조회 + 현재 페이지(current Page)
			// --> start, end, max 계산
		PageInfo pInfo = boardService.pagination(type, cp);
		
		System.out.println(pInfo);
		
		// 3) 게시글 목록 조회 
		List<Board> boardList = boardService.selectList(pInfo);
													//pInfo에 값이 없으면 비어있는 문자열 [] 이 나온다.
		
//		for(Board b : boardList) {
//			System.out.println(b);
//		}
		
		// 4) 썸네일 목록 조회
		   if(!boardList.isEmpty()) { // 게시글 목록 조회 결과가 있을 경우
		         // 마이바티스 List 조회 시 조회 결과가 없어도 비어 있는 List[]가 반환이 됨.
		         List<Attachment> thList = boardService.selectThumbnailList(boardList);
		         
		        /* for(Attachment at: thList) {
		            System.out.println(at);
		         } */
		         
		         // 썸네일 목록을 응답페이지 전달
		         model.addAttribute("thList", thList);
		      }
		
		model.addAttribute("boardList", boardList );
		model.addAttribute("pInfo", pInfo );
		
		return "board/boardList";
	}
	
	// 게시글 상세조회
	// spring/board/1/500?cp=3
	@RequestMapping("{type}/{boardNo}")
	public String boardView(@PathVariable int type, @PathVariable int boardNo , Model model, 
							RedirectAttributes rdAttr, HttpServletRequest request) {
		System.out.println("type : " + type);
		System.out.println("boardNo : " + boardNo);
		
		// 게시글 하나 조회 Service 호출
		Board board = boardService.selectBoard(boardNo);
		
		System.out.println(board);
		
		String url = null;
		if(board != null) { // 게시글 조회 성공 시 
			
			// DB에서 해당 글에 작성된 이미지파일 모두 조회하기
			List<Attachment> files = boardService.selectFiles(boardNo);
			
			if(!files.isEmpty()) {
				model.addAttribute("files", files);
			}
			
			
			model.addAttribute("board", board);
			url = "board/boardView";
		}else {
			// 존재하지 않는 게시글 입니다' 출력 후 이전 요청 주소로 리다이렉트
			rdAttr.addFlashAttribute("status", "error");
			rdAttr.addFlashAttribute("msg", "해당 게시글이 존재하지 않습니다.");
			
			//url = "redirect:" + request.getHeader("referer"); 
			url = "redirect:/board/list/" + type;  //게시글 조회 첫 화면
		}
		return url;
	}
	
	// 게시글 등록 화면 이동
	@RequestMapping("{type}/insert")
	public String insertView(@PathVariable int type, Model model) {
		model.addAttribute("boardType", type);
		return "board/boardInsert";
	}


	
	
	
	// 게시글 등록
	@RequestMapping(value="{type}/insertAction", method=RequestMethod.POST)
	public String insertAction(@PathVariable int type, Board board, Model model, RedirectAttributes rdAttr, 
								@RequestParam(value="thumbnail", required=false) MultipartFile thumbnail, 
								@RequestParam(value="images", required=false) List<MultipartFile> images, HttpServletRequest request) {
											// 업로드 되는 이미지 파일이 필수 파라미터가 아니게 설정
		// MultipartFile 이건 뭐였지?
													//Board는 참조형(주소값)임 객체가 아니다
													// 얉은 복사 개념(주소만 복사하는것)
		// 게시글 등록에 필요한 내용
		// 카테고리, 제목, 내용, 게시판 타입, 작성자(회원번호)
		
		// Session에서 회원 정보 얻어오기 (@SeesionAttrubutes 확인하기)
		Member loginMember =  (Member)model.getAttribute("loginMember");
		
		// 타입, 회원번호 board에 세팅
		board.setBoardType(type);
		board.setBoardWriter(loginMember.getMemberNo() + "");
		
		// 파일 업로드, 게시글 입력값 확인
		System.out.println("썸네일 : " + thumbnail.getOriginalFilename());
		for(int i=0 ; i<images.size(); i++) {
			System.out.println("images[" + i + "] : " + images.get(i).getOriginalFilename());
		}

		System.out.println(board);
		
		//-----------------------------------------Summernote-----------------------------------------
				// name속성 값이 "images"인 파라미터 자체가 전달되지 않아 images 리스트가 생성되지 않아
						// images.add(0, thumbnail); 코드 진행 시 NullPointerException이 발생함.
				if(images.isEmpty()) { 
					images = new ArrayList<>();
				}
				//--------------------------------------------------------------------------------------------
				
				
				// 썸네일 이미지 정보를 images 리스트 제일 앞에 추가
				images.add(0, thumbnail);
		
		
		// 썸네일 이미지 정보를 images 리스트 제일 앞에 추가
		images.add(0, thumbnail);
		
		// 파일을 저장할 서버 컴퓨터의 로컬 경로
		String savePath = request.getSession().getServletContext().getRealPath("resources/uploadImages");
																				// 정적
		
		// 게시글 작성 Service 호출
		int result = boardService.insertBoard(board, images, savePath);
		
		String url = null;
		if(result > 0) {
			rdAttr.addFlashAttribute("status", "success");
			rdAttr.addFlashAttribute("msg", "게시글 작성 성공");
			
			//System.out.println("redirect:/board/" + type + "/" + board.getBoardNo() );
			
			url =  board.getBoardNo() + "?cp=1"; 
			
			///board/1/2093?cp=1 원하는 주소
			///board/1/insert 글 쓰는 주소
			
		}else {
			rdAttr.addFlashAttribute("status", "error");
			rdAttr.addFlashAttribute("msg", "게시글 작성 실패");
			url = "insert"; 
		}
		
		
		return "redirect:" + url;
	}
	
	// spring/board/1/515/delete
	@RequestMapping("{type}/{boardNo}/delete")
	public String deleteBoard(@PathVariable int type, 
							 @PathVariable int boardNo,
							 RedirectAttributes rdAttr,
							 HttpServletRequest request) {
		
		int result = boardService.deleteBoard(boardNo);
		
		String url = null;
		if(result > 0) {
			rdAttr.addFlashAttribute("status", "success");
			rdAttr.addFlashAttribute("msg", "게시글 삭제 성공");
			// 삭제 성공 시 해당 게시판 목록 1페이지
			
			// redirect 시 제일 앞 "/" 기호는 contextPath를 의미함
			url = "/board/list/" + type;
		}else {
			rdAttr.addFlashAttribute("status", "error");
			rdAttr.addFlashAttribute("msg", "게시글 삭제 실패");
			// 삭제 실패 시 이전 요청 주소(상세조회 페이지로 이동) 리다렉트
			//url = "/board/" + type + "/" + boardNo ; -> 내가 쓴거
			url = request.getHeader("referer");
		}
		
		
		
		return "redirect:" + url;
		
	}
	
	/*	ModelAndView
	 * 
	 * 	Model : 응답페이지에 값을 전달할 때 Map 형태로 저장하여 전달하는 객체
	 * 	View : 이동할 페이지 정보를 담는 객체(View라는 객체가 별도로 존재하지는 않음)
	 * 
	 * 반환형 String + Model 이라고 생각 (나)
	 * 단순히 응답페이지에 데이터 전달, viewName 설정 시 사용하는 객체
	 * */
	// 게시글 수정
	@RequestMapping("{type}/{boardNo}/update")
	public ModelAndView updateView(@PathVariable int boardNo, ModelAndView mv) {
		
		// 기존 게시글 정보를 얻어와 update 화면에 출력해 이전 작성 내용을 보여주어야 함.
		Board board = boardService.selectBoard(boardNo);
		
		// ------------------------------------------------
		// 기존 게시글 이미지 조회 및 전달
		if(board != null) {
			List<Attachment> files = boardService.selectFiles(boardNo);
												// 상세 조회할 때 사용함. 이미지 조회할 때 
			mv.addObject("files", files);
			
		}
		
		// ------------------------------------------------
		
		mv.addObject("board", board);
		// model.addAttribute( );
		// 모델과 똑같다 이름만 다름
		
		mv.setViewName("board/boardUpdate");
		
		return mv;
	}
	
	// 게시글 수정
	@RequestMapping("{type}/{boardNo}/updateAction")
	public ModelAndView updateAction(@PathVariable int type, @PathVariable int boardNo, ModelAndView mv,
									 Board upBoard,int cp, boolean[] deleteImages, RedirectAttributes rdAttr, HttpServletRequest request,
									 @RequestParam(value="thumbnail", required = false) MultipartFile thumbnail,
									 @RequestParam(value="images", required = false) List<MultipartFile> images) {
									// 기본적으로  insert와비슷한 모양
		//  boolean[] deleteImages -> delete 이미지가 차곡차곡 담겨옴
		
		System.out.println("deleteImages : " + Arrays.toString(deleteImages));
		// 안되는디?...
		
		// upBoard 안에 카테고리, 제목, 내용 만 담겨있음 boardNo도 같이 보내야하기 때문에 합치기
		upBoard.setBoardNo(boardNo);
		
		// 이미지 수정
		// ---------------------------------------------------------------------------------
		// 업로드된 파일 이름 확인
		// -> 파일 이름이 출력된 경우 == 이미지가 수정된 경우
		// 수정을 안하면 비어있는 값이 넘어온다.
		System.out.println("thumbnail" + thumbnail.getOriginalFilename());
		for(int i = 0; i<images.size(); i++) {
			System.out.println("images[" + i + "] : " + images.get(i).getOriginalFilename());
		}
		
		// 썸네일 이미지를 images리스트 0번 인덱스에 추가
		images.add(0, thumbnail);
		
		// 파일 저장 경로 설정
		String savePath = request.getSession().getServletContext().getRealPath("resources/uploadImages");
		
		int result = boardService.updateBoard(upBoard, savePath, images, deleteImages);
		// ---------------------------------------------------------------------------------
//		int result = boardService.updateBoard(upBoard);
		
		String url = null;
		if(result > 0) {
			rdAttr.addFlashAttribute("status", "success");
			rdAttr.addFlashAttribute("msg", "게시글 수정 성공");
			
			// 수정 성공 시 상세조회 화면으로
			url = "../" + boardNo + "?cp=" + cp;
					// "/board/{type}/{boardNo}?cp="+cp; 절대 경로
			
			
		}else {
			rdAttr.addFlashAttribute("status", "error");
			rdAttr.addFlashAttribute("msg", "게시글 수정 실패");
			
			// 실패 시 이전 요청주소(수정화면)
			url = request.getHeader("referer");
		}
		
		mv.setViewName("redirect:" + url);
		
		return mv;
		
	}
	
	// 게시판 조회수 높은 게시글 조회
	@ResponseBody
	@RequestMapping("topViews/{type}")
	public String topViews(@PathVariable int type ) {
		List<Board> list = boardService.selectTopViews(type);
		
		//Gson
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		
		
		return gson.toJson(list);
	}
	
	//-----------------------------------------Summernote-----------------------------------------
		// Summernote 이미지 업로드
		@ResponseBody
		@RequestMapping("{type}/insertImage")
		public String insertImage(@PathVariable int type,  HttpServletRequest request,
				@RequestParam(value="uploadFile", required=false) MultipartFile uploadFile) {
			
			String savePath =  request.getSession().getServletContext().getRealPath("resources/infoImages/");
			
			Map<String, String> result = boardService.insertImage(uploadFile, savePath);
			return new Gson().toJson(result);
		}
		//--------------------------------------------------------------------------------------------
	
		
		
		
		
}
