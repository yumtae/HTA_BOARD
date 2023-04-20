package com.naver.myboard2.controller;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naver.myboard2.domain.Board;
import com.naver.myboard2.domain.MySaveFolder;
import com.naver.myboard2.service.BoardService;
import com.naver.myboard2.service.CommentService;

@Controller
@RequestMapping(value = "/board")
public class BoardController {
   private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

   private MySaveFolder mysavefolder;
   private BoardService boardService;
   private CommentService commentService;

   
   @Autowired
   public BoardController(BoardService boardService, CommentService commentService, MySaveFolder mysavefolder) {
      this.boardService = boardService;
      this.commentService = commentService;
      this.mysavefolder=mysavefolder;
   }

   // 글 목록 가져오기
   @RequestMapping(value = "/list", method = RequestMethod.GET)
   public ModelAndView boardList(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
         ModelAndView mv) {

      int limit = 10;// 한 화면에 출력할 글 개수

      int listcount = boardService.getListCount();// 총 리스트 수 받아옴

      // 총 페이지 수
      int maxpage = (listcount + limit - 1) / limit;

      // 현재 페이지에 보여줄 시작 페이지 수
      int startpage = ((page - 1) / 10) * 10 + 1;

      // 현재 페이지에 보여줄 마지막 페이지 수
      int endpage = startpage + 10 - 1;

      if (endpage > maxpage)
         endpage = maxpage;

      List<Board> boardlist = boardService.getBoardList(page, limit);// 리스트 받아옴

      mv.setViewName("board/board_list");
      mv.addObject("page", page);
      mv.addObject("maxpage", maxpage);
      mv.addObject("startpage", startpage);
      mv.addObject("endpage", endpage);
      mv.addObject("listcount", listcount);
      mv.addObject("boardlist", boardlist);
      mv.addObject("limit", limit);
      return mv;
   }
   
   @ResponseBody
   @RequestMapping(value = "/list_ajax")
   public Map<String, Object> boardAjax(
         @RequestParam(value = "page", defaultValue = "1", required = false) int page,
         @RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {

      int listcount = boardService.getListCount();// 총 리스트 수 받아옴

      // 총 페이지 수
      int maxpage = (listcount + limit - 1) / limit;

      // 현재 페이지에 보여줄 시작 페이지 수
      int startpage = ((page - 1) / 10) * 10 + 1;

      // 현재 페이지에 보여줄 마지막 페이지 수
      int endpage = startpage + 10 - 1;

      if (endpage > maxpage)
         endpage = maxpage;

      List<Board> boardlist = boardService.getBoardList(page, limit);// 리스트 받아옴

      Map<String, Object> map = new HashMap<String, Object>();
      map.put("page", page);
      map.put("maxpage", maxpage);
      map.put("startpage", startpage);
      map.put("endpage", endpage);
      map.put("listcount", listcount);
      map.put("boardlist", boardlist);
      map.put("limit", limit);
      return map;
   }

   // 글쓰기
   @GetMapping(value = "/write")
   // @RequestMapping(value = "write", method = RequestMethod.GET)
   public String board_write() {
      return "board/board_write";
   }
   
   /*
      스프링 컨테이너는 매개변수 Board 객체를 생성하고
      Board 객체의 setter 메서드들을 호출하여 사용자 입력값을 설정합니다.
      매개변수의 이름과 setter의 property가 일치하면 됩니다.
   */
   @PostMapping(value = "/add")
   public String board_add(Board board, HttpServletRequest request) throws Exception {

      MultipartFile uploadfile = board.getUploadfile();

      if (!uploadfile.isEmpty()) {
         String fileName = uploadfile.getOriginalFilename();// 원래 파일명
         board.setBOARD_ORIGINAL(fileName);// 원래 파일명 저장
         
         String savefolder=mysavefolder.getSavefolder();
         String fileDBName = fileDBName(fileName, savefolder);
         logger.info("fileDBName = " + fileDBName);

         // transfer To(File Path) : 업로드한 파일을 매개변수의 경로에 저장합니다.
         uploadfile.transferTo(new File(savefolder + fileDBName));
         logger.info("transfer path = " + savefolder + fileDBName);
         // 바뀐 파일명으로 저장
         board.setBOARD_FILE(fileDBName);
      }

      boardService.insertBoard(board);// 저장 메서드 호출
      logger.info(board.toString());// selectKey로 정의한 BOARD_NUM 값 확인
      return "redirect:list";
   }

   private String fileDBName(String fileName, String savefolder) {
      // 새로운 폴더 이름 : 오늘 년+월+일
      Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);// 오늘 년도 구합니다.
      int month = c.get(Calendar.MONTH) + 1;// 오늘 월 구합니다.
      int date = c.get(Calendar.DATE);// 오늘 일 구합니다.

      String homedir = savefolder + "/" + year + "-" + month + "-" + date;
      logger.info(homedir);
      File path1 = new File(homedir);
      if (!(path1.exists())) {
         path1.mkdir();// 새로운 폴더 생성
      }

      // 난수를 구합니다.
      Random r = new Random();
      int random = r.nextInt(100000000);

      /**** 확장자 구하기 시작 ****/
      int index = fileName.lastIndexOf(".");
      // 문자열에서 특정 문자열의 위치 값(index)로 반환합니다.
      // indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
      // lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환합니다.
      // (파일명에 점이 여러개 있을 경우 맨 마지막에 발견되는 문자열의 위치를 리턴합니다.)
      logger.info("index = " + index);

      String fileExtension = fileName.substring(index + 1);
      logger.info("fileExtension = " + fileExtension);
      /**** 확장자 구하기 끝 ****/

      // 새로운 파일명
      String refileName = "bbs" + year + month + date + random + "." + fileExtension;
      logger.info("refileNAme = " + refileName);

      // 오라클DB에 저장될 파일 명
      // String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
      String fileDBname = File.separator + year + "-" + month + "-" + date + File.separator + refileName;
      logger.info("fileDBname = " + fileDBname);
      return fileDBname;
   }
   
   @GetMapping(value = "/detail")
   public ModelAndView detail(int num, ModelAndView mv, HttpServletRequest request,
         @RequestHeader(value = "referer", required = false) String beforeURL) {
      /*
         1. String beforeURL = request.getHeader("referer"); 의미로
            어느 주소에서 detail로 이동했는지 header의 정보 중에서 "referer"를 통해 알 수 있습니다.
         2. 수정 후 이곳으로 이동하는 경우 조회수는 증가하지 않도록 합니다.
         3. myboard2/board/list에서 제목을 클릭한 경우 조회수가 증가하도록 합니다.
         4. detail을 새로고침하는 경우 referer는 header에 존재하지 않아 오류가 발생하므로
            required=false로 설정합니다. 이 경우 beforeURL의 값은 null입니다.
       */
      logger.info("referer: " + beforeURL);
      if (beforeURL != null && beforeURL.endsWith("list")) {
         boardService.setReadCountUpdate(num);
      }

      Board board = boardService.getDetail(num);
      // board=null //error 페이지 이동 확인하고자 임의로 지정합니다.
      if (board == null) {
         logger.info("상세보기 실패");
         mv.setViewName("error/error");
         mv.addObject("url", request.getRequestURL());
         mv.addObject("message", "상세보기 실패입니다.");
      } else {
         logger.info("상세보기 성공");
         int count = commentService.getListCount(num);
         mv.setViewName("board/board_view");
         mv.addObject("count", count);
         mv.addObject("boarddata", board);
      }
      return mv;
   }
   
   @GetMapping(value = "/modifyView")
   public ModelAndView BoardModifyView(int num, ModelAndView mv, HttpServletRequest request) {
      Board boarddata = boardService.getDetail(num);

      // 글 내용 불러오기 실패한 경우입니다.
      if (boarddata == null) {
         logger.info("수정보기 실패");
         mv.setViewName("error/error");
         mv.addObject("url", request.getRequestURL());
         mv.addObject("message", "수정보기 실패입니다.");
         return mv;
      }
      logger.info("(수정)상세보기 성공");
      // 수정 폼 페이지로 이동할 때 원문 글 내용을 보여주기 때문에 boarddata 객체를
      // ModelAndView 객체에 저장합니다.
      mv.addObject("boarddata", boarddata);
      //글 수정 폼 페이지로 이동하기 위해 경로를 설정합니다.
      mv.setViewName("board/board_modify");
      return mv;
   }
   
   @PostMapping(value = "/modifyAction")
   public String BoardModifyAction(Board boarddata, String check, Model mv, HttpServletRequest request,
         RedirectAttributes rattr) throws Exception {
      boolean usercheck = boardService.isBoardWriter(boarddata.getBOARD_NUM(), boarddata.getBOARD_PASS());
      String url = "";
      // 비밀번호가 다른경우
      if (usercheck == false) {
         rattr.addFlashAttribute("result", "passFail");
         rattr.addAttribute("num", boarddata.getBOARD_NUM());
         return "redirect:modifyView";
      }

      MultipartFile uploadfile = boarddata.getUploadfile();
      String savefolder=mysavefolder.getSavefolder();
      
      if (check != null && !check.equals("")) {// 기존파일 그대로 사용하는 경우
         logger.info("기존파일 그대로 사용합니다.");
         boarddata.setBOARD_ORIGINAL(check);
         // <input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
         // 위 문장 때문에 board.setBOARD_FILE()값은 자동 저장됩니다.
      } else {
         // 답변글의 경우 파일 첨부에 대한 기능이 없습니다.
         // 만약 답변글을 수정할 경우
         // <input type="file" id="upfile" name="uploadfile">
         // private MultipartFile uploadfile에서 uploadfile은 null입니다.
         if (uploadfile != null && !uploadfile.isEmpty()) {
            logger.info("파일 추가/변경 되었습니다.");

            String fileName = uploadfile.getOriginalFilename();// 원래 파일명
            boarddata.setBOARD_ORIGINAL(fileName);

            String fileDBName = fileDBName(fileName, savefolder);
            logger.info("fileDBName = " + fileDBName);
            // transferTo(File path) : 업로드한 파일을 매개변수의 경로에 저장합니다.
            uploadfile.transferTo(new File(savefolder + fileDBName));
            logger.info("transferTo path = " + savefolder + fileDBName);
            // 바뀐 파일명으로 저장
            boarddata.setBOARD_FILE(fileDBName);
         } else {// 기존 파일이 없는데 파일 선택하지 않은경우 또는 기존 파일이 있었는데 삭제한 경우
            logger.info("선택파일이 없습니다.");
            // <input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
            // 위 태그에 값이 있다면 ""로 값을 변경합니다.
            boarddata.setBOARD_FILE("");// ""로 초기화합니다.
            boarddata.setBOARD_ORIGINAL("");// ""로 초기화합니다.
         } // else
      } // else

      // DAO에서 수정 메서드 호출하여 수정합니다.
      int result = boardService.boardModify(boarddata);
      // 수정 실패한 경우
      if (result == 0) {
         logger.info("게시판 수정 실패");
         mv.addAttribute("url", request.getRequestURL());
         mv.addAttribute("message", "게시판 수정 실패");
         url = "error/error";
      } else {// 수정 성공한 경우
            // 수정한 글 내용을 보여주기 위해 글 내용 보기 페이지로 이동하기 위해 경로를 설정합니다.
         url = "redirect:detail";
         rattr.addAttribute("num", boarddata.getBOARD_NUM());
      }
      return url;
   }
   
   @GetMapping("/replyView")
   public ModelAndView BoardReplyView(int num, ModelAndView mv, HttpServletRequest request) {
      Board board = boardService.getDetail(num);
      if (board == null) {
         mv.setViewName("error/error");
         mv.addObject("url", request.getRequestURL());
         mv.addObject("message", "게시판 답변글 가져오기 실패");
      } else {
         mv.addObject("boarddata", board);
         mv.setViewName("board/board_reply");
      }
      return mv;
   }
   
   @PostMapping("/replyAction")
   public ModelAndView BoardReplyAction(Board board, ModelAndView mv, HttpServletRequest request,
         RedirectAttributes rattr) {
      int result = boardService.boardReply(board);
      if (result == 0) {
         mv.setViewName("error/error");
         mv.addObject("url", request.getRequestURL());
         mv.addObject("message", "게시판 답변처리 실패");
      } else {
         // mv.setViewName("redirect:list");
         // mv.setViewName("redirect:detail?num="+board.getBOARD_NUM());
         rattr.addAttribute("num", board.getBOARD_NUM());
         mv.setViewName("redirect:list");
      }
      return mv;
   }
   
   @PostMapping("/delete")
   public String BoardDeleteAction(String BOARD_PASS, int num, Model mv, RedirectAttributes rattr,
         HttpServletRequest request) {
      // 글 삭제 명령을 요청한 사용자가 글을 작성한 사용자인지 판단하기 위해
      // 입력한 비밀번호와 저장된 비밀번호를 비교하여 일치하면 삭제합니다.
      boolean usercheck = boardService.isBoardWriter(num, BOARD_PASS);

      // 비밀번호 일치하지 않는 경우
      if (usercheck == false) {
         rattr.addFlashAttribute("result", "passFail");
         rattr.addAttribute("num", num);
         return "redirect:detail";
      }

      // 비밀번호 일치하는 경우 삭제처리 합니다.
      int result = boardService.boardDelete(num);
      System.out.println("result = "+result);
      // 삭제처리 실패한 경우
      if (result == 0) {
         logger.info("게시판 삭제 실패");
         mv.addAttribute("url", request.getRequestURL());
         mv.addAttribute("message", "삭제실패");
         return "error/error";
      }

      // 삭제 처리 성공한 경우 - 글 목록 보기 요청을 전송하는 부분입니다.
      logger.info("게시판 삭제 성공");
      rattr.addFlashAttribute("result", "deleteSuccess");
      return "redirect:list";
   }
   
   @ResponseBody
   @PostMapping("/down")
   public byte[] BoardFileDown(String filename, HttpServletRequest request, String original,
         HttpServletResponse response) throws Exception {
      //String savePath = "resources/upload";
      // 서블릿 실행 환경 정보를 담고있는 객체를 리턴합니다.
      //ServletContext context = request.getSession().getServletContext();
      //String sDownloadPath = context.getRealPath(savePath);
      
      String savefolder=mysavefolder.getSavefolder();
      String sFilePath = savefolder + filename;

      File file = new File(sFilePath);

      byte[] bytes = FileCopyUtils.copyToByteArray(file);

      String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");

      // Content-Disposition: attachment: 브라우저는 해당 Content를 처리하지 않고, 다운로드하게 됩니다.
      response.setHeader("Content-Disposition", "attachment;filename=" + sEncoding);

      response.setContentLength(bytes.length);
      return bytes;
   }
   
}