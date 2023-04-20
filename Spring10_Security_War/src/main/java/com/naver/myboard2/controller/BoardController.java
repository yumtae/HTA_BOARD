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

   // �� ��� ��������
   @RequestMapping(value = "/list", method = RequestMethod.GET)
   public ModelAndView boardList(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
         ModelAndView mv) {

      int limit = 10;// �� ȭ�鿡 ����� �� ����

      int listcount = boardService.getListCount();// �� ����Ʈ �� �޾ƿ�

      // �� ������ ��
      int maxpage = (listcount + limit - 1) / limit;

      // ���� �������� ������ ���� ������ ��
      int startpage = ((page - 1) / 10) * 10 + 1;

      // ���� �������� ������ ������ ������ ��
      int endpage = startpage + 10 - 1;

      if (endpage > maxpage)
         endpage = maxpage;

      List<Board> boardlist = boardService.getBoardList(page, limit);// ����Ʈ �޾ƿ�

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

      int listcount = boardService.getListCount();// �� ����Ʈ �� �޾ƿ�

      // �� ������ ��
      int maxpage = (listcount + limit - 1) / limit;

      // ���� �������� ������ ���� ������ ��
      int startpage = ((page - 1) / 10) * 10 + 1;

      // ���� �������� ������ ������ ������ ��
      int endpage = startpage + 10 - 1;

      if (endpage > maxpage)
         endpage = maxpage;

      List<Board> boardlist = boardService.getBoardList(page, limit);// ����Ʈ �޾ƿ�

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

   // �۾���
   @GetMapping(value = "/write")
   // @RequestMapping(value = "write", method = RequestMethod.GET)
   public String board_write() {
      return "board/board_write";
   }
   
   /*
      ������ �����̳ʴ� �Ű����� Board ��ü�� �����ϰ�
      Board ��ü�� setter �޼������ ȣ���Ͽ� ����� �Է°��� �����մϴ�.
      �Ű������� �̸��� setter�� property�� ��ġ�ϸ� �˴ϴ�.
   */
   @PostMapping(value = "/add")
   public String board_add(Board board, HttpServletRequest request) throws Exception {

      MultipartFile uploadfile = board.getUploadfile();

      if (!uploadfile.isEmpty()) {
         String fileName = uploadfile.getOriginalFilename();// ���� ���ϸ�
         board.setBOARD_ORIGINAL(fileName);// ���� ���ϸ� ����
         
         String savefolder=mysavefolder.getSavefolder();
         String fileDBName = fileDBName(fileName, savefolder);
         logger.info("fileDBName = " + fileDBName);

         // transfer To(File Path) : ���ε��� ������ �Ű������� ��ο� �����մϴ�.
         uploadfile.transferTo(new File(savefolder + fileDBName));
         logger.info("transfer path = " + savefolder + fileDBName);
         // �ٲ� ���ϸ����� ����
         board.setBOARD_FILE(fileDBName);
      }

      boardService.insertBoard(board);// ���� �޼��� ȣ��
      logger.info(board.toString());// selectKey�� ������ BOARD_NUM �� Ȯ��
      return "redirect:list";
   }

   private String fileDBName(String fileName, String savefolder) {
      // ���ο� ���� �̸� : ���� ��+��+��
      Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);// ���� �⵵ ���մϴ�.
      int month = c.get(Calendar.MONTH) + 1;// ���� �� ���մϴ�.
      int date = c.get(Calendar.DATE);// ���� �� ���մϴ�.

      String homedir = savefolder + "/" + year + "-" + month + "-" + date;
      logger.info(homedir);
      File path1 = new File(homedir);
      if (!(path1.exists())) {
         path1.mkdir();// ���ο� ���� ����
      }

      // ������ ���մϴ�.
      Random r = new Random();
      int random = r.nextInt(100000000);

      /**** Ȯ���� ���ϱ� ���� ****/
      int index = fileName.lastIndexOf(".");
      // ���ڿ����� Ư�� ���ڿ��� ��ġ ��(index)�� ��ȯ�մϴ�.
      // indexOf�� ó�� �߰ߵǴ� ���ڿ��� ���� index�� ��ȯ�ϴ� �ݸ�,
      // lastIndexOf�� ���������� �߰ߵǴ� ���ڿ��� index�� ��ȯ�մϴ�.
      // (���ϸ� ���� ������ ���� ��� �� �������� �߰ߵǴ� ���ڿ��� ��ġ�� �����մϴ�.)
      logger.info("index = " + index);

      String fileExtension = fileName.substring(index + 1);
      logger.info("fileExtension = " + fileExtension);
      /**** Ȯ���� ���ϱ� �� ****/

      // ���ο� ���ϸ�
      String refileName = "bbs" + year + month + date + random + "." + fileExtension;
      logger.info("refileNAme = " + refileName);

      // ����ŬDB�� ����� ���� ��
      // String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
      String fileDBname = File.separator + year + "-" + month + "-" + date + File.separator + refileName;
      logger.info("fileDBname = " + fileDBname);
      return fileDBname;
   }
   
   @GetMapping(value = "/detail")
   public ModelAndView detail(int num, ModelAndView mv, HttpServletRequest request,
         @RequestHeader(value = "referer", required = false) String beforeURL) {
      /*
         1. String beforeURL = request.getHeader("referer"); �ǹ̷�
            ��� �ּҿ��� detail�� �̵��ߴ��� header�� ���� �߿��� "referer"�� ���� �� �� �ֽ��ϴ�.
         2. ���� �� �̰����� �̵��ϴ� ��� ��ȸ���� �������� �ʵ��� �մϴ�.
         3. myboard2/board/list���� ������ Ŭ���� ��� ��ȸ���� �����ϵ��� �մϴ�.
         4. detail�� ���ΰ�ħ�ϴ� ��� referer�� header�� �������� �ʾ� ������ �߻��ϹǷ�
            required=false�� �����մϴ�. �� ��� beforeURL�� ���� null�Դϴ�.
       */
      logger.info("referer: " + beforeURL);
      if (beforeURL != null && beforeURL.endsWith("list")) {
         boardService.setReadCountUpdate(num);
      }

      Board board = boardService.getDetail(num);
      // board=null //error ������ �̵� Ȯ���ϰ��� ���Ƿ� �����մϴ�.
      if (board == null) {
         logger.info("�󼼺��� ����");
         mv.setViewName("error/error");
         mv.addObject("url", request.getRequestURL());
         mv.addObject("message", "�󼼺��� �����Դϴ�.");
      } else {
         logger.info("�󼼺��� ����");
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

      // �� ���� �ҷ����� ������ ����Դϴ�.
      if (boarddata == null) {
         logger.info("�������� ����");
         mv.setViewName("error/error");
         mv.addObject("url", request.getRequestURL());
         mv.addObject("message", "�������� �����Դϴ�.");
         return mv;
      }
      logger.info("(����)�󼼺��� ����");
      // ���� �� �������� �̵��� �� ���� �� ������ �����ֱ� ������ boarddata ��ü��
      // ModelAndView ��ü�� �����մϴ�.
      mv.addObject("boarddata", boarddata);
      //�� ���� �� �������� �̵��ϱ� ���� ��θ� �����մϴ�.
      mv.setViewName("board/board_modify");
      return mv;
   }
   
   @PostMapping(value = "/modifyAction")
   public String BoardModifyAction(Board boarddata, String check, Model mv, HttpServletRequest request,
         RedirectAttributes rattr) throws Exception {
      boolean usercheck = boardService.isBoardWriter(boarddata.getBOARD_NUM(), boarddata.getBOARD_PASS());
      String url = "";
      // ��й�ȣ�� �ٸ����
      if (usercheck == false) {
         rattr.addFlashAttribute("result", "passFail");
         rattr.addAttribute("num", boarddata.getBOARD_NUM());
         return "redirect:modifyView";
      }

      MultipartFile uploadfile = boarddata.getUploadfile();
      String savefolder=mysavefolder.getSavefolder();
      
      if (check != null && !check.equals("")) {// �������� �״�� ����ϴ� ���
         logger.info("�������� �״�� ����մϴ�.");
         boarddata.setBOARD_ORIGINAL(check);
         // <input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
         // �� ���� ������ board.setBOARD_FILE()���� �ڵ� ����˴ϴ�.
      } else {
         // �亯���� ��� ���� ÷�ο� ���� ����� �����ϴ�.
         // ���� �亯���� ������ ���
         // <input type="file" id="upfile" name="uploadfile">
         // private MultipartFile uploadfile���� uploadfile�� null�Դϴ�.
         if (uploadfile != null && !uploadfile.isEmpty()) {
            logger.info("���� �߰�/���� �Ǿ����ϴ�.");

            String fileName = uploadfile.getOriginalFilename();// ���� ���ϸ�
            boarddata.setBOARD_ORIGINAL(fileName);

            String fileDBName = fileDBName(fileName, savefolder);
            logger.info("fileDBName = " + fileDBName);
            // transferTo(File path) : ���ε��� ������ �Ű������� ��ο� �����մϴ�.
            uploadfile.transferTo(new File(savefolder + fileDBName));
            logger.info("transferTo path = " + savefolder + fileDBName);
            // �ٲ� ���ϸ����� ����
            boarddata.setBOARD_FILE(fileDBName);
         } else {// ���� ������ ���µ� ���� �������� ������� �Ǵ� ���� ������ �־��µ� ������ ���
            logger.info("���������� �����ϴ�.");
            // <input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
            // �� �±׿� ���� �ִٸ� ""�� ���� �����մϴ�.
            boarddata.setBOARD_FILE("");// ""�� �ʱ�ȭ�մϴ�.
            boarddata.setBOARD_ORIGINAL("");// ""�� �ʱ�ȭ�մϴ�.
         } // else
      } // else

      // DAO���� ���� �޼��� ȣ���Ͽ� �����մϴ�.
      int result = boardService.boardModify(boarddata);
      // ���� ������ ���
      if (result == 0) {
         logger.info("�Խ��� ���� ����");
         mv.addAttribute("url", request.getRequestURL());
         mv.addAttribute("message", "�Խ��� ���� ����");
         url = "error/error";
      } else {// ���� ������ ���
            // ������ �� ������ �����ֱ� ���� �� ���� ���� �������� �̵��ϱ� ���� ��θ� �����մϴ�.
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
         mv.addObject("message", "�Խ��� �亯�� �������� ����");
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
         mv.addObject("message", "�Խ��� �亯ó�� ����");
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
      // �� ���� ����� ��û�� ����ڰ� ���� �ۼ��� ��������� �Ǵ��ϱ� ����
      // �Է��� ��й�ȣ�� ����� ��й�ȣ�� ���Ͽ� ��ġ�ϸ� �����մϴ�.
      boolean usercheck = boardService.isBoardWriter(num, BOARD_PASS);

      // ��й�ȣ ��ġ���� �ʴ� ���
      if (usercheck == false) {
         rattr.addFlashAttribute("result", "passFail");
         rattr.addAttribute("num", num);
         return "redirect:detail";
      }

      // ��й�ȣ ��ġ�ϴ� ��� ����ó�� �մϴ�.
      int result = boardService.boardDelete(num);
      System.out.println("result = "+result);
      // ����ó�� ������ ���
      if (result == 0) {
         logger.info("�Խ��� ���� ����");
         mv.addAttribute("url", request.getRequestURL());
         mv.addAttribute("message", "��������");
         return "error/error";
      }

      // ���� ó�� ������ ��� - �� ��� ���� ��û�� �����ϴ� �κ��Դϴ�.
      logger.info("�Խ��� ���� ����");
      rattr.addFlashAttribute("result", "deleteSuccess");
      return "redirect:list";
   }
   
   @ResponseBody
   @PostMapping("/down")
   public byte[] BoardFileDown(String filename, HttpServletRequest request, String original,
         HttpServletResponse response) throws Exception {
      //String savePath = "resources/upload";
      // ���� ���� ȯ�� ������ ����ִ� ��ü�� �����մϴ�.
      //ServletContext context = request.getSession().getServletContext();
      //String sDownloadPath = context.getRealPath(savePath);
      
      String savefolder=mysavefolder.getSavefolder();
      String sFilePath = savefolder + filename;

      File file = new File(sFilePath);

      byte[] bytes = FileCopyUtils.copyToByteArray(file);

      String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");

      // Content-Disposition: attachment: �������� �ش� Content�� ó������ �ʰ�, �ٿ�ε��ϰ� �˴ϴ�.
      response.setHeader("Content-Disposition", "attachment;filename=" + sEncoding);

      response.setContentLength(bytes.length);
      return bytes;
   }
   
}