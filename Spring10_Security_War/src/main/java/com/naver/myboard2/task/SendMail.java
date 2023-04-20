package com.naver.myboard2.task;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import com.naver.myboard2.domain.MailVO;
import com.naver.myboard2.domain.MySaveFolder;

@Component
public class SendMail {

		private static final Logger logger = LoggerFactory.getLogger(SendMail.class);
		
		@Autowired
		private JavaMailSenderImpl mailSender;
		
		@Autowired
		private MySaveFolder mysaveFolder;
				
		
		public void sendMail(MailVO vo) {
			
			String sendfile = mysaveFolder.getSendfile();
			
			MimeMessagePreparator mp = new MimeMessagePreparator() {

				
				@Override
				public void prepare(MimeMessage mimeMessage) throws Exception {

				

					/*
					 * MimeMessage : �� Ŭ������ MIME ��Ÿ�� �̸��� �޽����� ��Ÿ���ϴ�.
					 * MIME(���� : Multipurpose Internet Mail Extensions)��
					 		���� ������ ���� ���ͳ� ǥ�� ���� �Դϴ�.
					 * 
					 * */
					
					/*
					  MimeMessageHelper�� �̿��ϸ� ÷�� �����̳� Ư�� ���� ���ڵ����� �۾��� �� ���޵� mimeMessage�� ä��� �� ���մϴ�
					 * */
					
					//�� ��° ���� true�� ��Ƽ ��Ʈ �޽����� ����ϰڴٴ� �ǹ��Դϴ�.
					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true , "UTF-8");
					helper.setFrom(vo.getFrom());
					helper.setTo(vo.getTo());
					helper.setSubject(vo.getSubject());
					
					
					//1. ���ڷθ� �����ϴ� ���
					//�� ��° ���ڴ� html�� ����ϰڴٴ� ���Դϴ�.
					//helper.setText(vo.getContent(),true);
					
					//2. �̹����� �����ؼ� ������ ���
					//cid (content id)
					
					String content = "<img src='cid:Home'>" + vo.getContent();
					helper.setText(content,true);
					
					FileSystemResource file = new FileSystemResource(new File(sendfile));
					
					// addInline �޼����� ù��° �޼��忡�� cid(content id)�� �����մϴ�
					helper.addInline("Home", file);
					
					//3.������ ÷���ؼ� ������ ���
					// ù��° ���� : ÷�ε� ������ �̸�
					// �ι�° ���� : ÷�ε� ����
					helper.addAttachment("����.jpg", file);
					
				}
				
				
			}; // new MimeMessagePreparator
			
			mailSender.send(mp);
			logger.info("���� �����մϴ�");
		}
		
		
	
}
