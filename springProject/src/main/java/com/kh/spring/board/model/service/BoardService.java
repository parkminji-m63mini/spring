package com.kh.spring.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;

public interface BoardService {

	/** 페이징 처리를 위한 Service
	 * @param type
	 * @param cp
	 * @return pInfo
	 */
	PageInfo pagination(int type, int cp);

	/** 게시글 목록 조회 Service
	 * @param pInfo
	 * @return boardList
	 */
	List<Board> selectList(PageInfo pInfo);

	/** 게시글 상세조회 Service
	 * @param boardNo
	 * @return board
	 */
	Board selectBoard(int boardNo);

	/** 게시글 등록 Service
	 * @param board
	 * @param savePath 
	 * @param images 
	 * @return result
	 */
	int insertBoard(Board board, List<MultipartFile> images, String savePath);

	/** 게시글 삭제
	 * @param boardNo
	 * @return result
	 */
	int deleteBoard(int boardNo);

	/** 게시글 수정
	 * @param upBoard
	 * @param images 
	 * @param savePath 
	 * @param deleteImages 
	 * @return result
	 */
	int updateBoard(Board upBoard, String savePath, List<MultipartFile> images, boolean[] deleteImages);

	/** 게시글 이미지 조회 service
	 * @param boardNo
	 * @return files
	 */
	List<Attachment> selectFiles(int boardNo);

	/** 게시글 썸네일 목록 조회 Service
	 * @param boardList
	 * @return thList
	 */
	List<Attachment> selectThumbnailList(List<Board> boardList);

	/** 게시판 조회수 높은 게시글 조회 Service
	 * @param type
	 * @return list
	 */
	List<Board> selectTopViews(int type);

	
	//-----------------------------------------Summernote-----------------------------------------
	/** Summernote 이미지 업로드 Service
	 * @param uploadFile
	 * @param savePath
	 * @return map
	 */
	public abstract Map<String, String> insertImage(MultipartFile uploadFile, String savePath);

	
	
	
	/** DB에 저장된 파일 목록 조회 Service
	 * @return dbFileList
	 */
	public abstract List<String> selectDbFileList();
	//---------------------------------------------------------------------------------------------
	
}
