/**
 * Package Name : com.pcwk.ehr.markdown.service <br/>
 * Class Name: MarkdownServiceImpl.java <br/>
 * Description:  <br/>
 * Modification imformation : <br/> 
 * ------------------------------------------<br/>
 * 최초 생성일 : 2025-07-23<br/>
 *
 * ------------------------------------------<br/>
 * @author :user
 * @since  :2025-07-23
 * @version: 0.5
 */
package com.pcwk.ehr.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.SearchDTO;

import org.commonmark.node.*;
import org.commonmark.parser.*;
import org.commonmark.renderer.html.*;
import org.springframework.stereotype.Service;

/**
 * @author user
 *
 */
@Service
public class MarkdownServiceImpl implements MarkdownService{
	
	Logger log = LogManager.getLogger(getClass());

	public MarkdownServiceImpl() {
		log.debug("┌─────────────────────────────────────────────────────────┐");
		log.debug("│ MarkdownServiceImpl()                                   │");
		log.debug("└─────────────────────────────────────────────────────────┘");
	}

	@Override
	public String convertToMarkdownHtml(String markdown) {
		log.debug("┌─────────────────────────────────────────────────────────┐");
		log.debug("│ convertToMarkdownHtml()                                 │");
		log.debug("└─────────────────────────────────────────────────────────┘");
		String htmlString ="";
		log.debug("markdown: {}"+markdown);
		
		Parser parser = Parser.builder().build(); //객체 생성
		Node document = parser.parse(markdown); //markdown doc
		
		//HTML 바꿔주는 객체 생성
		HtmlRenderer render = HtmlRenderer.builder().build();
		htmlString = render.render(document);
		log.debug("htmlString: {}"+htmlString);
		
		return htmlString;
	}
	
	

	@Override
	public int doDelete(DTO param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int doUpdate(DTO param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DTO doSelectOne(DTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int doSave(DTO param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<DTO> doRetrieve(SearchDTO param) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
