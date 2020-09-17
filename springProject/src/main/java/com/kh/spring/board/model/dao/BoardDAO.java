package com.kh.spring.board.model.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;

@Repository 
public class BoardDAO {

	// IOC : 제어 역전 (어떠한 객체의 생명주기 관리해주는것 대신 bean으로 등록되어있어야함)
	@Autowired //DI : 의존성 주입
	private SqlSessionTemplate sqlSession;

	/** 전체 게시글 수 조회
	 * @param type
	 * @return listCount
	 */
	public int getListCount(int type) {
		return sqlSession.selectOne("boardMapper.getListCount", type);
	}

	/** 게시글 목록 조회 DAO
	 * @param pInfo
	 * @return boardList
	 */
	public List<Board> selectList(PageInfo pInfo) {
		
		// RowBounds 
		// 조회된 내용 중 지정한 만큼의 수를 건너 뛰고 나서
		// 이후 몇개를 조회 할지를 정할 수 있는 객체
		
		// offset : 건너 뛸 게시글 수 지정
		int offset = (pInfo.getCurrentPage()-1) * pInfo.getLimit();
						// (1 - 1) * 10 = 0
						// (2 - 1) * 10 = 10
						// (3 - 1) * 10 = 20
		
		RowBounds rowBounds = new RowBounds(offset, pInfo.getLimit());
		
						// selectList는 List 타입 반환
		return sqlSession.selectList("boardMapper.selectList", pInfo.getBoardType() , rowBounds);
	}								//		매퍼이름.태그id , 		파라미터(없으면 null) ,	 RowBounds
						// 알아서 list.add 해줌


	/** 게시글 상세 조회 DAO
	 * @param boardNo
	 * @return board
	 */
	public Board selectBoard(int boardNo) {
		return sqlSession.selectOne("boardMapper.selectBoard", boardNo);
	}

	/** 조회수 증가 DAO
	 * @param boardNo
	 * @return result
	 */
	public int increaseCount(int boardNo) {
		return sqlSession.update("boardMapper.increaseCount", boardNo);
	}

	/** 다음 게시글 번호 가져오기 DAO
	 * @return boardNo
	 */
	public int selectNextNo() {
		return sqlSession.selectOne("boardMapper.selectNextNo");
	}

	/** 게시글 삽입 DAO
	 * @param board
	 * @return result
	 */
	public int insertBoard(Board board) {
		return sqlSession.insert("boardMapper.insertBoard", board);
	}

	/** 게시글 삭제
	 * @param boardNo
	 * @return result
	 */
	public int deleteBoard(int boardNo) {
		return sqlSession.update("boardMapper.deleteBoard", boardNo);
	}

	/** 게시글 수정
	 * @param upBoard
	 * @return result
	 */
	public int updateBaord(Board upBoard) {
		return sqlSession.update("boardMapper.updateBoard", upBoard);
	}

	/** 파일 정보 삽입 DAO
	 * @param at
	 * @return reuslt
	 */
	public int insertAttachment(Attachment at) {
		return sqlSession.insert("boardMapper.insertAttachment", at);
	}

	/** 게시글 이미지 조회 DAO
	 * @param boardNo
	 * @return result
	 */
	public List<Attachment> selectFiles(int boardNo) {
		return sqlSession.selectList("boardMapper.selectFiles", boardNo);
	}

	/** 파일 저장 오류 시 DB에 저장된 내용 삭제 DAO
	 * @param boardNo
	 */
	public void deleteAttachment(int boardNo) {
		sqlSession.delete("boardMapper.deleteAttachment", boardNo);
	}

	/** 게시글 썸네일 목록 조회 DAO
	 * @param boardList
	 * @return thList
	 */
	public List<Attachment> selectThumbnailList(List<Board> boardList) {
		return sqlSession.selectList("boardMapper.selectThumbnailList", boardList);
	}

	/** 파일 정보 수정 DAO
	 * @param at
	 * @return result
	 */
	public int updateAttachment(Attachment at) {
		return sqlSession.update("boardMapper.updateAttachment", at);
	}

	/** 파일 한개 정보 삭제 DAO
	 * @param fileNo
	 * @return result
	 */
	public int deleteAttachment2(int fileNo) {
		return sqlSession.delete("boardMapper.deleteAttachment2", fileNo);
	}

	/** 게시판 조회수 높은 게시글 조회 DAO
	 * @param type
	 * @return
	 */
	public List<Board> selectTopViews(int type) {
		return sqlSession.selectList("boardMapper.selectTopViews", type);
	}
	
	
	//-----------------------------------------Summernote-----------------------------------------
	/**   DB에 저장된 파일 목록 조회 DAO
	 * @return dbFileList
	 */
	public List<String> selectDbFileList() {
		return sqlSession.selectList("boardMapper.selectDbFileList");
	} 
	//---------------------------------------------------------------------------------------------
	
	
	
		
	
}
