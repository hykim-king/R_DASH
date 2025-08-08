/**
 * Package Name : com.pcwk.ehr.markdown.service <br/>
 * Class Name: MarkdownService.java <br/>
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

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.WorkDiv;

/**
 * @author user
 *
 */
public interface MarkdownService extends WorkDiv<DTO>{
	/**
	 * markdown을 HTML로 변환
	 * @param markdown
	 * @return String(HTML)
	 */
	public String convertToMarkdownHtml(String markdown);
}
