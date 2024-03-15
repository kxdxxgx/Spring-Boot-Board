package com.board.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.board.dto.BoardDTO;
import com.board.dto.FileDTO;
import com.board.dto.ReplyDTO;
import com.board.dto.ReplyInterface;
import com.board.entity.BoardEntity;
import com.board.entity.FileEntity;
import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;
import com.board.entity.ReplyEntity;
import com.board.entity.repository.BoardRepository;
import com.board.entity.repository.FileRepository;
import com.board.entity.repository.LikeRepository;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class BoardServiceImpl implements BoardService {
	
	// @Autowired
	// BoardMapper mapper;
	
	private final BoardRepository boardRepository;
	private final FileRepository fileRepository;
	private final LikeRepository likeRepository;
	private final MemberRepository memberRepository;
	private final ReplyRepository replyRepository;
	
	// 게시물 목록 보기 - selectList
	@Override
	public Page<BoardEntity> list(int pageNum, int postNum, String keyword) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageNum-1, postNum, Sort.by(Direction.DESC,"seqno"));
		return boardRepository.findByTitleContainingOrWriterContainingOrContentContaining(keyword, keyword, keyword, pageRequest);
	/*
		Map<String, Object> data = new HashMap<>();
		data.put("startPoint", startPoint);
		data.put("endPoint", endPoint);
		data.put("keyword", keyword);
		return mapper.list(data);
	*/
		
	}
	
	/*
	// 게시물 전체 갯수 계산 --> Controller에서 처리
	public int getTotalCount(String keyword) {
		return mapper.getTotalCount(keyword);
	}
	*/

	// 게시물 내용 보기 - selectOne
	@Override
	public BoardDTO view(Long seqno) throws Exception {
		// return mapper.view(seqno);
		return boardRepository.findById(seqno).map(view -> new BoardDTO(view)).get();
	}
	
	// 게시물 내용 이전 보기 - selectOne
	@Override
	public Long pre_seqno(Long seqno, String keyword) throws Exception {
		Long result = boardRepository.pre_seqno(seqno, keyword, keyword, keyword);
		return (result==null) ? 0 : result;
		/* 
		Map<String, Object> data = new HashMap<>();
		data.put("seqno", seqno);
		data.put("keyword", keyword);
		return mapper.pre_seqno(data);
		*/
	}

	// 게시물 내용 다음 보기 - selectOne
	@Override
	public Long next_seqno(Long seqno, String keyword) throws Exception {
		Long result = boardRepository.next_seqno(seqno, keyword, keyword, keyword);
		return (result==null) ? 0 : result;
		/*
		Map<String, Object> data = new HashMap<>();
		data.put("seqno", seqno);
		data.put("keyword", keyword);
		return mapper.next_seqno(data);
		*/
	}

	// 게시물 조회수 증가 - update
	@Override
	public void hitno(Long seqno) throws Exception {
		//mapper.hitno(seqno);
		boardRepository.hitno(seqno);
	}
	
	// 게시물 등록 하기
	@Override
	public void write(BoardDTO board) throws Exception {
		// mapper.write(board);
		board.setRegdate(LocalDateTime.now());
		boardRepository.save(board.dtoToEntity(board));
	}
	
	// 게시물 번호 구하기(시퀀스의 nextval을 사용)
	@Override
	public Long getSeqnoWithNextval() throws Exception {
		// return mapper.getSeqnoWithNextval();
		return boardRepository.getSeqnoWithNextval();
	}
	
	// 첨부파일 정보 등록 하기
	@Override
	public void fileInfoRegistry(FileDTO file) {
		// mapper.fileInfoRegistry(data);
		fileRepository.save(file.dtoToEntity(file));
	}
	
	// 첨부 파일 목록 보기
	@Override
	public List<FileEntity> fileInfoView(Long seqno) {
		// return mapper.fileInfoView(seqno);
		return fileRepository.findBySeqnoAndCheckfile(seqno, "O");
	}
	
	// 첨부 파일 목록 보기
//	@Override
//	public List<FileDTO> fileInfoView(Long seqno) throws Exception{
//		List<FileDTO> fileDTOs = new ArrayList<>();
//		fileRepository.findBySeqnoAndCheckfile(seqno, "O").stream().forEach(list -> fileDTOs.add(new FileDTO(list)));
//		return fileDTOs;
//	}

	// 게시물 삭제 시 첨부 파일 삭제를 위한 checkfile 정보 변경
	@Override
	public void fileInfoUpdate(Long seqno) {
		// mapper.fileInfoUpdate(seqno);
		fileRepository.findBySeqno(seqno).stream().forEach(file -> {
			file.setCheckfile("X");
			fileRepository.save(file);
		});
		/*
		FileEntity fileEntity = fileRepository.findById(seqno).get();
		fileEntity.setCheckfile("X");
		fileRepository.save(fileEntity);
		*/
	}
	
	// 다운로드를 위한 파일 정보 가져 오기
	@Override
	public FileDTO fileInfo(Long fileseqno) {
		// return mapper.fileInfo(fileseqno);
		return fileRepository.findById(fileseqno).map(file -> new FileDTO(file)).get();
	}
	
	// 게시물 수정 하기 - update
	@Override
	public void modify(BoardDTO board) throws Exception {
		// mapper.modify(board);
		BoardEntity boardEntity = boardRepository.findById(board.getSeqno()).get();
		boardEntity.setTitle(board.getTitle());
		boardEntity.setContent(board.getContent());
		boardRepository.save(boardEntity);
	}
	
	
	// 게시물 좋아요/싫어요 갯수 수정
	@Override
	public void boardlikeUpdate(BoardDTO board) throws Exception {
		BoardEntity boardEntity = boardRepository.findById(board.getSeqno()).get();
		boardEntity.setLikecnt(board.getLikecnt());
		boardEntity.setDislikecnt(board.getDislikecnt());
		boardRepository.save(boardEntity);
		//mapper.boardlikeUpdate(board);		
	}

	// 게시물 삭제하기 - view
	@Override
	public void delete(Long seqno) throws Exception {
		// mapper.delete(seqno);
		BoardEntity boardEntity = boardRepository.findById(seqno).get();
		boardRepository.delete(boardEntity);
	}
	
	// 게시물 수정 시에 삭제할 파일 선택(checkfile을 X로 변경)
	@Override
	public void deleteFileList(Long fileseqno) throws Exception {
		// mapper.deleteFileList(fileseqno);
		FileEntity fileEntity = fileRepository.findById(fileseqno).get();
		fileEntity.setCheckfile("X");
		fileRepository.save(fileEntity);
	}

	// 좋아요/싫어요 등록 여부 확인
	@Override
	public LikeEntity likeCheckView(Long seqno, String email) {
		// return mapper.likeCheckView(like);
		BoardEntity boardEntity = boardRepository.findById(seqno).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		return likeRepository.findBySeqnoAndEmail(boardEntity,memberEntity);
	}

	// 좋아요/싫어요 신규 등록
	@Override
	public void likeCheckRegistry(Long seqno, String email, String mylikecheck,
			String mydislikeCheck, String likeDate, String dislikeDate) throws Exception {
		// mapper.likeCheckRegistry(like);
		BoardEntity boardEntity = boardRepository.findById(seqno).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		LikeEntity likeEntity = LikeEntity.builder()
										.seqno(boardEntity)
										.email(memberEntity)
										.mylikecheck(mylikecheck)
										.mydislikecheck(mydislikeCheck)
										.likedate(likeDate)
										.dislikedate(dislikeDate)
										.build();
		likeRepository.save(likeEntity);
	}

	// 좋아요/싫어요 수정
	@Override
	public void likeCheckUpdate(Long seqno, String email, String mylikecheck,
			String mydislikeCheck, String likeDate, String dislikeDate) {
		// mapper.likeCheckUpdate(like);
		BoardEntity boardEntity = boardRepository.findById(seqno).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		LikeEntity likeEntity = likeRepository.findBySeqnoAndEmail(boardEntity, memberEntity);
		likeEntity.setSeqno(boardEntity);
		likeEntity.setEmail(memberEntity);
		likeEntity.setMylikecheck(mylikecheck);
		likeEntity.setMydislikecheck(mydislikeCheck);
		likeEntity.setLikedate(likeDate);
		likeEntity.setDislikedate(dislikeDate);
	}

	// 댓글 목록 보기
	@Override
	public List<ReplyInterface> replyView(ReplyInterface reply) throws Exception {
		// return mapper.replyView(reply);
		return replyRepository.replyView(reply.getSeqno());	
	}

	// 댓글 등록
	@Override
	public void replyRegistry(ReplyInterface reply) throws Exception {
		// mapper.replyRegistry(reply);
		BoardEntity boardEntity = boardRepository.findById(reply.getSeqno()).get();
		MemberEntity memberEntity = memberRepository.findById(reply.getEmail()).get();
		
		ReplyEntity replyEntity = ReplyEntity.builder()
											.seqno(boardEntity)
											.email(memberEntity)
											.replywriter(reply.getReplywriter())
											.replycontent(reply.getReplycontent())
											.replyregdate(LocalDateTime.now())
											.build();
		replyRepository.save(replyEntity);
	}

	// 댓글 수정
	@Override
	public void replyUpdate(ReplyInterface reply) throws Exception {
		// mapper.replyUpdate(reply);
		ReplyEntity replyEntity = replyRepository.findById(reply.getReplyseqno()).get();
		replyEntity.setReplycontent(reply.getReplycontent());
		replyRepository.save(replyEntity);
	}
	
	// 댓글 삭제
	@Override
	public void replyDelete(ReplyInterface reply) throws Exception {
		// mapper.replyDelete(reply);
		ReplyEntity replyEntity = replyRepository.findById(reply.getReplyseqno()).get();
		replyRepository.delete(replyEntity);
	}
}
