<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="CP" value="${pageContext.request.contextPath}" />
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="sysDate">
	<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss" />
</c:set>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>재민이</title>
<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico"
	type="image/x-icon" />
<link rel="stylesheet"
	href="${CP}/resources/template/Animate/backInUp.css" />

<style>
/* ===== Base ===== */
html, body {
	margin: 0;
	padding: 0;
	width: 100%;
	scroll-behavior: smooth;
	color: #fff;
	font-family: 'Noto Sans KR', system-ui, -apple-system, 'Segoe UI',
		Roboto, sans-serif
}

.main-background {
	background-image: url('${CP}/resources/image/mainboard.png');
	background-size: cover;
	background-position: center;
	background-repeat: no-repeat;
	background-attachment: fixed
}

/* ===== Hero ===== */
.main-section {
	height: 100vh;
	width: 100%;
	margin: 0 !important;
	padding: 0 !important;
	position: relative;
	display: flex;
	justify-content: center;
	align-items: center;
	text-align: center;
	overflow: hidden
}

.main-section.main-background {
	background: none
}

#bg-video, .main-section>video {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	object-fit: cover;
	z-index: -1
}

.main-section::before {
	content: "";
	position: absolute;
	inset: 0;
	background: rgba(0, 0, 0, .25);
	z-index: 0
}

/* ===== News / FAQ ===== */
.news-section {
	padding: 60px 0
}

.news-section .card {
	background: rgba(255, 255, 255, .85);
	color: #111
}

.news-title {
	text-align: center;
	font-size: 1.8rem;
	font-weight: 700;
	margin-bottom: 30px;
	color: #fff;
	text-shadow: 1px 1px 3px #000
}

.faq-section {
	background: #212529;
	color: #fff;
	padding: 60px 20px
}

.accordion-button {
	background: #343a40;
	color: #fff
}

.accordion-button:not(.collapsed) {
	background: #495057
}

.accordion-body {
	background: #2c2f33;
	color: #ccc
}

img.card-img-top {
	width: 100%;
	height: 200px;
	object-fit: cover
}

/* ===== Floating launcher ===== */
.floating-icon {
	position: fixed;
	right: 22px;
	bottom: 20px;
	z-index: 9999;
	cursor: pointer
}

.floating-icon img {
	width: 76px;
	height: 76px;
	object-fit: cover;
	border-radius: 50%;
	box-shadow: 0 6px 14px rgba(0, 0, 0, .35);
	transition: transform .18s, box-shadow .18s;
	background: #fff
}

.floating-icon:hover img {
	transform: scale(1.07);
	box-shadow: 0 10px 18px rgba(0, 0, 0, .45)
}

@media ( max-width :576px) {
	.floating-icon img {
		width: 60px;
		height: 60px
	}
}

/* ===== Chat modal ===== */
.chat-modal .modal-dialog {
	max-width: 560px;
	width: min(92vw, 560px);
	position: fixed;
	right: 28px;
	bottom: 120px;
	margin: 0;
	z-index: 1065
}

.chat-modal .modal-content {
	border-radius: 16px;
	overflow: visible;
	height: 68vh;
	max-height: 820px;
	display: flex;
	flex-direction: column;
	position: relative
}

.chat-header {
	position: relative;
	z-index: 3;
	background: #dc3545;
	color: #fff;
	padding: 14px 18px;
	font-weight: 700;
	min-height: 56px;
	display: flex;
	align-items: center;
	justify-content: space-between
}

.chat-head-actions {
	display: flex;
	align-items: center;
	gap: 8px;
}

.chat-min-btn {
	width: 28px;
	height: 28px;
	border: 0;
	border-radius: 6px;
	background: rgba(255, 255, 255, .25);
	color: #fff;
	font-weight: 900;
	line-height: 28px;
	display: inline-flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	transition: background .12s;
}

.chat-min-btn:hover {
	background: rgba(255, 255, 255, .35);
}

.chat-body {
	flex: 1;
	min-height: 0;
	overflow-y: auto;
	background: #f6f7f9;
	padding: 14px
}

.chat-footer {
	display: flex;
	gap: 10px;
	padding: 12px 14px 16px;
	background: #fff;
	border-top: 1px solid #eee
}

.chat-input {
	flex: 1;
	resize: none;
	height: 52px;
	padding: 12px 14px;
	border: 1px solid #ddd;
	border-radius: 10px;
	outline: none
}

.chat-send-btn {
	border: none;
	border-radius: 10px;
	padding: 0 18px;
	background: #dc3545;
	color: #fff;
	font-weight: 700;
	height: 52px
}

.chat-bubble {
	max-width: 86%;
	margin: 8px 0;
	padding: 12px 14px;
	border-radius: 14px;
	line-height: 1.5;
	word-break: break-word;
	box-shadow: 0 1px 2px rgba(0, 0, 0, .06);
	font-size: 1rem;
	color: #111
}

.chat-bubble.user {
	margin-left: auto;
	background: #ffe3e6
}

.chat-bubble.bot {
	margin-right: auto;
	background: #fff
}

.chat-time {
	display: block;
	margin-top: 2px;
	font-size: .78rem;
	opacity: .6
}

/* ===== 내 대화방 패널 ===== */
.history-dock {
	position: absolute;
	left: 0;
	top: -12px;
	transform: translateY(-100%);
	width: 320px;
	max-height: 60vh;
	background: rgba(17, 24, 39, .98);
	color: #fff;
	border-radius: 12px;
	box-shadow: 0 10px 24px rgba(0, 0, 0, .35);
	display: flex;
	flex-direction: column;
	overflow: hidden;
	z-index: 2000;
	transition: max-height .18s ease-in-out;
}

.history-dock.collapsed {
	max-height: 46px
}

.history-dock.below {
	left: 12px;
	right: 12px;
	top: 56px;
	transform: none;
	width: auto;
	border-radius: 0 0 12px 12px;
	z-index: 2;
}

.history-head {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 10px 12px;
	background: #111827;
	border-bottom: 1px solid rgba(255, 255, 255, .08);
	font-weight: 700;
	font-size: .95rem;
	cursor: pointer
}

.history-toggle {
	border: 0;
	border-radius: 8px;
	width: 32px;
	height: 28px;
	background: #374151;
	color: #fff;
	cursor: pointer;
	transition: transform .18s
}

.history-dock:not(.collapsed) .history-toggle {
	transform: rotate(180deg)
}

.history-body {
	overflow-y: auto;
	padding: 8px 6px;
	gap: 4px;
	display: flex;
	flex-direction: column
}

.history-item {
	cursor: pointer;
	border-radius: 10px;
	padding: 8px 10px;
	transition: background .12s;
	position: relative;
	padding-right: 42px; /* delete 버튼 공간 */
}

.history-item:hover {
	background: rgba(255, 255, 255, .08)
}

.history-item.active {
	background: rgba(220, 53, 69, .92)
}

.history-title {
	font-weight: 700;
	font-size: .9rem
}

.history-sub {
	font-size: .78rem;
	color: #c7d2fe;
	opacity: .9;
	margin-top: 2px
}

.history-time {
	font-size: .72rem;
	opacity: .6;
	margin-top: 2px
}

.history-foot {
	padding: 8px;
	border-top: 1px solid rgba(255, 255, 255, .08)
}

.history-new {
	width: 100%;
	height: 34px;
	border: 0;
	border-radius: 8px;
	font-weight: 700;
	background: #0d6efd;
	color: #fff;
	cursor: pointer
}
/* 삭제 버튼 */
.history-del {
	position: absolute;
	right: 8px;
	top: 50%;
	transform: translateY(-50%);
	width: 28px;
	height: 28px;
	border: 0;
	border-radius: 6px;
	background: #374151;
	color: #fff;
	font-weight: 900;
	line-height: 28px;
	cursor: pointer;
	opacity: .0;
	transition: opacity .12s, background .12s;
}

.history-item:hover .history-del {
	opacity: .9
}

.history-del:hover {
	background: #ef4444
}

@media ( max-width :576px) {
	.chat-modal .modal-dialog {
		right: 14px;
		bottom: 90px;
		width: 94vw;
		max-width: none
	}
	.chat-modal .modal-content {
		height: 72vh
	}
	.chat-input, .chat-send-btn {
		height: 48px
	}
	.history-dock {
		width: 260px;
		top: -8px
	}
	.history-dock.below {
		left: 8px;
		right: 8px;
		top: 52px
	}
}
</style>
</head>

<body data-login-user-no="${empty loginUserNo ? '' : loginUserNo}">
	<!-- Hero -->
	<div class="main-section main-background">
		<video id="bg-video" autoplay muted loop preload="auto"
			poster="${CP}/resources/image/mainboard.png">
			<source src="${CP}/resources/video/jaemini.mp4" type="video/mp4" />
			동영상을 재생할 수 없습니다.
		</video>
	</div>

	<!-- News -->
	<div class="news-section main-background animate-on-scroll" id="news">
		<div class="container">
			<div class="news-title">주요 뉴스</div>
			<div class="row">
				<div class="col-md-4 mb-3">
					<div class="card h-100">
						<img src="${CP}/resources/image/earth.jpg" class="card-img-top"
							alt="지진 이미지" />
						<div class="card-body">
							<h5 class="card-title">[속보] 강진 발생</h5>
							<p class="card-text">해당 지역 주민은 안전한 곳으로 대피 바랍니다.</p>
						</div>
					</div>
				</div>
				<div class="col-md-4 mb-3">
					<div class="card h-100">
						<img src="${CP}/resources/image/dis.jpg" class="card-img-top"
							alt="화재 이미지" />
						<div class="card-body">
							<h5 class="card-title">화재 시 행동요령 안내</h5>
							<p class="card-text">119 긴급 행동요령 숙지로 생명을 지키세요.</p>
						</div>
					</div>
				</div>
				<div class="col-md-4 mb-3">
					<div class="card h-100">
						<img src="${CP}/resources/image/med.jpg" class="card-img-top"
							alt="응급 이미지" />
						<div class="card-body">
							<h5 class="card-title">응급상황 대응 시스템 강화</h5>
							<p class="card-text">최근 대응 시간 단축을 위한 시스템 개편 발표.</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- FAQ -->
	<div class="faq-section animate-on-scroll" id="faq">
		<div class="container">
			<h4 class="mb-4">자주 묻는 질문</h4>
			<div class="accordion" id="faqAccordion">
				<div class="accordion-item">
					<h2 class="accordion-header" id="headingOne">
						<button class="accordion-button" type="button"
							data-bs-toggle="collapse" data-bs-target="#faq1"
							aria-expanded="true">대피소 위치는 어디서 확인할 수 있나요?</button>
					</h2>
					<div id="faq1" class="accordion-collapse collapse show"
						data-bs-parent="#faqAccordion">
						<div class="accordion-body">상단 '지도 페이지' 메뉴에서 확인 가능합니다.</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header" id="headingTwo">
						<button class="accordion-button collapsed" type="button"
							data-bs-toggle="collapse" data-bs-target="#faq2">재난 종류에는
							무엇이 있나요?</button>
					</h2>
					<div id="faq2" class="accordion-collapse collapse"
						data-bs-parent="#faqAccordion">
						<div class="accordion-body">화재, 지진, 태풍, 폭염, 감염병 등 다양한 유형이
							있습니다.</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Launcher -->
	<a class="floating-icon" href="#" aria-label="도움말 또는 채팅 열기" title="재민이">
		<img src="${CP}/resources/image/Jaemini_bo.jpg" alt="재민이 아이콘" />
	</a>

	<!-- Chat Modal -->
	<div class="modal fade chat-modal" id="chatModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- 내 대화방 패널 -->
				<div id="historyDock" class="history-dock collapsed">
					<div id="historyHead" class="history-head">
						<span>내 대화방</span>
						<div style="display: flex; gap: 6px; align-items: center">
							<button id="historyToggle" class="history-toggle" title="내기록"
								aria-expanded="false" aria-controls="historyBody">▾</button>
						</div>
					</div>
					<div id="historyBody" class="history-body"></div>
					<div class="history-foot">
						<button id="historyNew" class="history-new">+ 새 대화</button>
					</div>
				</div>

				<div class="chat-header">
					<strong>재민이 채팅</strong>
					<div class="chat-head-actions">
						<button id="chatMin" type="button" class="chat-min-btn"
							title="out" aria-label="Minimize">–</button>
					</div>
				</div>
				<div id="chatBody" class="chat-body">
					<div class="chat-bubble bot">
						안녕하세요! 재난 알림 도우미 <b>재민이</b>입니다. 무엇을 도와드릴까요? <span
							class="chat-time"></span>
					</div>
				</div>

				<div class="chat-footer">
					<textarea id="chatInput" class="chat-input"
						placeholder="궁금한거 있으면 물어봐요!"></textarea>
					<button id="chatSend" class="chat-send-btn">전송</button>
				</div>

			</div>
		</div>
	</div>

	<!-- JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
	<script>
  document.addEventListener("DOMContentLoaded", function(){
    var CP='${CP}';

    /* animate */
    try{
      var els=document.querySelectorAll(".animate-on-scroll");
      var io=new IntersectionObserver(function(entries,obs){
        entries.forEach(function(en){ if(en.isIntersecting){ en.target.classList.add("backInUp"); obs.unobserve(en.target);} });
      },{threshold:0.2});
      els.forEach(function(el){ io.observe(el); });
    }catch(e){}

    /* refs */
    var chatModalEl=document.getElementById('chatModal');
    // ▼ 변경: backdrop true 로
    var chatModal=new bootstrap.Modal(chatModalEl,{backdrop:true,keyboard:true});
    var chatBody=document.getElementById('chatBody');
    var chatInput=document.getElementById('chatInput');
    var chatMinBtn=document.getElementById('chatMin');

    /* login */
    var LOGIN_USER_NO_RAW=(document.body.dataset.loginUserNo||'').trim();
    var LOGIN_USER_NO=LOGIN_USER_NO_RAW===''?null:parseInt(LOGIN_USER_NO_RAW,10);

    /* utils */
    function nowText(){ return new Date().toLocaleTimeString([], {hour:'2-digit',minute:'2-digit'}); }
    function escapeHtml(s){ return (s||'').replace(/[&<>\"']/g,function(c){ return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c];}); }
    function appendBubble(text,who){
      var d=document.createElement('div');
      d.className='chat-bubble '+(who==='bot'?'bot':'user');
      d.innerHTML=text+'<span class="chat-time">'+nowText()+'</span>';
      chatBody.appendChild(d); chatBody.scrollTop=chatBody.scrollHeight;
    }
    function previewOf(t){ t=(String(t||'')).replace(/\s+/g,' ').trim(); return t.length>40?t.slice(0,40)+'…':t; }

    /* session & cache */
    var SESSION_ID=localStorage.getItem('X-Session-Id')||null;
    function getLocalSessions(){ try{ return JSON.parse(localStorage.getItem('JM_SESSIONS')||'[]'); }catch(e){ return []; } }
    function setLocalSessions(arr){ localStorage.setItem('JM_SESSIONS',JSON.stringify(arr.slice(0,50))); }
    function addLocalSession(sid){ if(!sid)return; var list=getLocalSessions().filter(function(x){ return x!==sid; }); list.unshift(sid); setLocalSessions(list); }
    function _msgsKey(sid){ return 'JM_MSGS_'+sid; }
    function getLocalMsgs(sid){ if(!sid)return[]; try{ return JSON.parse(localStorage.getItem(_msgsKey(sid))||'[]'); }catch(e){ return []; } }
    function setLocalMsgs(sid,a){ if(!sid)return; localStorage.setItem(_msgsKey(sid),JSON.stringify((a||[]).slice(-500))); }
    function pushLocalMsg(sid,role,text){ if(!sid)return; var arr=getLocalMsgs(sid); arr.push({role:role,text:String(text||''),ts:Date.now()}); setLocalMsgs(sid,arr); }
    function migrateCache(oldSid,newSid){ if(!oldSid||!newSid||oldSid===newSid)return; var o=getLocalMsgs(oldSid), n=getLocalMsgs(newSid); if(o.length&&!n.length) setLocalMsgs(newSid,o); localStorage.removeItem(_msgsKey(oldSid)); }

    /* dock */
    var dockEl=document.getElementById('historyDock');
    var dockHead=document.getElementById('historyHead');
    var dockBody=document.getElementById('historyBody');
    var dockTgl=document.getElementById('historyToggle');
    var dockNew=document.getElementById('historyNew');

    /* body padding when dock is below */
    function applyBodyPadding(){
      if(!dockEl || !chatBody) return;
      var isBelow=dockEl.classList.contains('below') && !dockEl.classList.contains('collapsed');
      var h=isBelow ? Math.min(dockEl.scrollHeight, window.innerHeight*0.6) : 0;
      chatBody.style.paddingTop=isBelow ? (h+'px') : '0px';
    }
    function setDockBelow(on){
      if(!dockEl) return;
      dockEl.classList.toggle('below', !!on);
      applyBodyPadding();
    }
    window.addEventListener('resize', applyBodyPadding);

    /* stop propagation */
    (function protectDock(){
      var events=['click','mousedown','mouseup','touchstart','touchend','pointerdown','pointerup','wheel'];
      function stopAll(e){ e.stopPropagation(); }
      [dockEl,dockBody,dockTgl,dockNew].forEach(function(el){ if(!el)return; events.forEach(function(ev){ el.addEventListener(ev,stopAll,false); }); });
    })();

    /* 토글: 아래로 펼치기 */
    function toggleDock(){
      if(!dockEl)return;
      var collapsed=dockEl.classList.toggle('collapsed');
      if(dockTgl){ dockTgl.setAttribute('aria-expanded', String(!collapsed)); }
      if(!collapsed){ setDockBelow(true); } else { setDockBelow(false); }
    }
    if(dockHead){
      dockHead.addEventListener('click',function(e){ e.stopPropagation(); toggleDock(); });
      dockHead.tabIndex=0;
      dockHead.addEventListener('keydown',function(e){ if(e.key==='Enter'||e.key===' '){ e.preventDefault(); toggleDock(); } });
    }
    if(dockTgl){ dockTgl.addEventListener('click',function(e){ e.stopPropagation(); toggleDock(); }); }

    /* 팝업 열기 공용 함수 (Alt/중클릭 전용) */
    async function openInPopup(){
      let sid;
      try{
        const r=await fetch(CP+'/api/chat/sessions/new',{method:'POST'});
        sid=(await r.text()) || (crypto.randomUUID?crypto.randomUUID():String(Date.now()));
      }catch(_){
        sid=(crypto.randomUUID?crypto.randomUUID():String(Date.now()));
      }
      addLocalSession(sid);
      const base=location.origin+location.pathname;
      const url =base+'?popup=1&sid='+encodeURIComponent(sid);
      const win =window.open(url,'jaemini_'+sid,'width=420,height=720,resizable=yes,scrollbars=yes');
      if(!win){ location.href=url; }
    }

    /* ===== 삭제 관련 유틸/동작 ===== */
    function removeLocalSession(sid){
      if(!sid) return;
      var list=getLocalSessions().filter(function(x){ return x!==sid; });
      setLocalSessions(list);
      localStorage.removeItem(_msgsKey(sid));
    }

    async function deleteSession(sid){
      if(!sid) return;

      if(LOGIN_USER_NO!=null){
        try{
          const res = await fetch(CP+'/api/chat/sessions/'+encodeURIComponent(sid), {
            method:'DELETE',
            headers:{ 'X-User-No': LOGIN_USER_NO }
          });
          if(!res.ok){ console.warn('delete api failed', res.status); }
        }catch(e){ console.warn('delete api error', e); }
      }

      removeLocalSession(sid);

      if(SESSION_ID===sid){
        const rest=getLocalSessions();
        SESSION_ID = rest[0] || null;
        if(SESSION_ID){
          localStorage.setItem('X-Session-Id', SESSION_ID);
          await openDockRoom(SESSION_ID);
        }else{
          localStorage.removeItem('X-Session-Id');
          chatBody.innerHTML='';
        }
      }
      await loadDockSessions();
      applyBodyPadding();
    }

    function renderDock(list){
      dockBody.innerHTML='';
      if(!list||!list.length){
        var emp=document.createElement('div');
        emp.style.cssText='padding:10px;color:#cfd6e3;font-size:.86rem;';
        emp.textContent='저장된 대화가 없습니다.';
        dockBody.appendChild(emp);
        applyBodyPadding(); return;
      }
      list.forEach(function(s){
        var cached=getLocalMsgs(s.sessionId);
        if((!s.lastMsg||s.lastMsg==='')&&cached.length){ s.lastMsg=previewOf(cached[cached.length-1].text); }

        var it=document.createElement('div');
        it.className='history-item'+(s.sessionId===SESSION_ID?' active':'');
        it.dataset.sid=s.sessionId;

        it.innerHTML =
          '<div class="history-title">'+escapeHtml(s.title||'새 대화')+'</div>'
         +'<div class="history-sub">'+escapeHtml(s.lastMsg||'')+'</div>'
         +'<div class="history-time">'+escapeHtml(s.updatedAt||'')+'</div>'
         +'<button class="history-del" title="삭제" aria-label="대화 삭제" data-del="'+s.sessionId+'">×</button>';

        it.addEventListener('click',function(e){
          if(e.target && e.target.classList.contains('history-del')) return;
          e.stopPropagation();
          SESSION_ID=s.sessionId; localStorage.setItem('X-Session-Id',SESSION_ID);
          openDockRoom(SESSION_ID).then(function(){
            Array.prototype.forEach.call(document.querySelectorAll('.history-item'),function(x){
              x.classList.toggle('active',x.dataset.sid===SESSION_ID);
            });
            dockEl.classList.add('collapsed');
          });
        });

        it.querySelector('.history-del').addEventListener('click', function(e){
          e.stopPropagation();
          var sid=e.currentTarget.getAttribute('data-del');
          if(confirm('이 대화를 삭제할까요?')){ deleteSession(sid); }
        });

        dockBody.appendChild(it);
      });
      applyBodyPadding();
    }

    async function loadDockSessions(){
      if(!dockBody)return;
      if(LOGIN_USER_NO!=null){
        try{
          var res=await fetch(CP+'/api/chat/sessions?limit=100',{headers:{'X-User-No':LOGIN_USER_NO}});
          if(res.ok){
            var list=await res.json();
            renderDock(Array.isArray(list)?list.map(function(s){
              return {sessionId:s.sessionId,title:s.title||'새 대화',lastMsg:s.lastMsg||'',updatedAt:s.updatedAt||''};
            }):[]);
            return;
          }
        }catch(e){ console.warn('sessions api fallback(local)',e); }
      }
      var ids=getLocalSessions();
      renderDock(ids.map(function(id){ return {sessionId:id,title:'내 대화',lastMsg:'',updatedAt:''}; }));
    }

    function renderRowsAndCache(sid,rows){
      if(!Array.isArray(rows)||!rows.length) return false;
      chatBody.innerHTML=''; var fresh=[];
      rows.forEach(function(m){
        if(m.question && String(m.question).trim()!==''){
          var q=String(m.question);
          appendBubble(escapeHtml(q).replace(/\n/g,'<br>'),'user');
          fresh.push({role:'user',text:q,ts:Date.now()});
        }
        if(m.answer && String(m.answer).trim()!==''){
          var a=String(m.answer);
          appendBubble(escapeHtml(a).replace(/\n/g,'<br>'),'bot');
          fresh.push({role:'bot',text:a,ts:Date.now()});
        }
        if(!m.question && !m.answer && m.text){
          var role=(m.role==='bot'||m.role==='assistant')?'bot':'user';
          var t=String(m.text);
          appendBubble(escapeHtml(t).replace(/\n/g,'<br>'),role);
          fresh.push({role:role,text:t,ts:Date.now()});
        }
      });
      chatBody.scrollTop=chatBody.scrollHeight; setLocalMsgs(sid,fresh); return true;
    }

    async function openDockRoom(sid){
      if(!sid) return; chatBody.innerHTML='';
      var cached=getLocalMsgs(sid);
      if(cached.length){
        cached.forEach(function(m){
          appendBubble(escapeHtml(m.text).replace(/\n/g,'<br>'), m.role==='bot'?'bot':'user');
        });
        chatBody.scrollTop=chatBody.scrollHeight;
      }
      var replaced=false;
      if(LOGIN_USER_NO!=null){
        try{
          var res=await fetch(CP+'/api/chat/sessions/'+encodeURIComponent(sid)+'/messages?limit=200',{headers:{'X-User-No':LOGIN_USER_NO}});
          if(res.ok){ var rows=await res.json(); replaced=renderRowsAndCache(sid,rows); }
        }catch(e){ console.warn('member history load failed',e); }
      }
      if(!replaced){
        try{
          var res2=await fetch(CP+'/api/chat/history?limit=200',{headers:{'X-Session-Id':sid}});
          if(res2.ok){ var rows2=await res2.json(); renderRowsAndCache(sid,rows2); }  // ▼ 오타 수정(res2.json)
        }catch(err){ console.error(err); }
      }
      applyBodyPadding();
    }

    /* + 새 대화 */
    if(dockNew){
      dockNew.addEventListener('click', async function(e){
        e.stopPropagation();
        if(e.altKey){ e.preventDefault(); return openInPopup(); }
        try{
          var res=await fetch(CP+'/api/chat/sessions/new',{method:'POST'});
          var sid=(await res.text())||crypto.randomUUID();
          SESSION_ID=sid; localStorage.setItem('X-Session-Id',sid); addLocalSession(sid);
          await loadDockSessions(); await openDockRoom(sid);
          dockEl.classList.remove('collapsed'); setDockBelow(true);
        }catch(e){
          var sid2=crypto.randomUUID(); SESSION_ID=sid2; localStorage.setItem('X-Session-Id',sid2); addLocalSession(sid2);
          await loadDockSessions(); await openDockRoom(sid2);
          dockEl.classList.remove('collapsed'); setDockBelow(true);
        }
      });
      dockNew.addEventListener('mouseup', function(e){
        if(e.button===1){ e.preventDefault(); e.stopPropagation(); openInPopup(); }
      });
    }

    /* send */
    async function sendMessage(){
      var text=document.getElementById('chatInput').value.trim(); if(!text) return;
      if(!SESSION_ID){
        SESSION_ID=localStorage.getItem('X-Session-Id')||(crypto.randomUUID?crypto.randomUUID():String(Date.now()));
        localStorage.setItem('X-Session-Id',SESSION_ID); addLocalSession(SESSION_ID);
      }
      appendBubble(escapeHtml(text).replace(/\n/g,'<br>'),'user'); pushLocalMsg(SESSION_ID,'user',text); document.getElementById('chatInput').value='';
      try{
        var headers={'Content-Type':'application/json'};
        if(SESSION_ID) headers['X-Session-Id']=SESSION_ID;
        if(LOGIN_USER_NO!=null) headers['X-User-No']=LOGIN_USER_NO;
        var prevSid=SESSION_ID;
        var res=await fetch(CP+'/api/chat/send',{method:'POST',headers:headers,body:JSON.stringify({question:text,userNo:LOGIN_USER_NO||undefined})});
        var sid=res.headers.get('X-Session-Id');
        if(sid){
          if(sid!==SESSION_ID){ migrateCache(prevSid,sid); SESSION_ID=sid; }
          localStorage.setItem('X-Session-Id',SESSION_ID); addLocalSession(SESSION_ID);
        }
        var raw=await res.text(); var data=null; try{ data=JSON.parse(raw);}catch(e){}
        if(!res.ok){
          var err='AI 호출 실패('+res.status+') '+(raw||'');
          appendBubble(escapeHtml(err),'bot'); pushLocalMsg(SESSION_ID,'bot',err); console.error('chat/send error',res.status,raw); return;
        }
        var answer=(data&&data.answer)?String(data.answer):'응답을 불러오지 못했어요.';
        appendBubble(escapeHtml(answer).replace(/\n/g,'<br>'),'bot'); pushLocalMsg(SESSION_ID,'bot',answer);
        loadDockSessions();
      }catch(e){
        var net='네트워크 오류가 발생했어요. 연결 상태를 확인해 주세요.';
        appendBubble(escapeHtml(net),'bot'); pushLocalMsg(SESSION_ID,'bot',net); console.error(e);
      }
    }
    document.getElementById('chatSend').addEventListener('click',sendMessage);
    document.getElementById('chatInput').addEventListener('keydown',function(e){ if(e.key==='Enter'&&!e.shiftKey){ e.preventDefault(); sendMessage(); } });

    /* 모달 열릴 때 기본을 '아래 모드'로 */
    chatModalEl.addEventListener('shown.bs.modal', async function(){
      chatBody.scrollTop=chatBody.scrollHeight;
      await loadDockSessions();
      if(SESSION_ID) await openDockRoom(SESSION_ID);
      setDockBelow(true);
    });

    /* – 버튼으로 모달 숨기기 */
    if(chatMinBtn){
      chatMinBtn.addEventListener('click', function(e){
        e.preventDefault();
        chatModal.hide();
      });
    }

    /* ▼ 안전망: 모달 밖(메인화면) 클릭 시 닫기 */
    document.addEventListener('click', function(e){
      if(!chatModalEl.classList.contains('show')) return;                 // 모달 열렸을 때만
      if(e.target.closest('#chatModal .modal-content')) return;           // 모달 내부 클릭은 무시
      if(e.target.closest('.floating-icon')) return;                      // 런처 클릭은 무시
      chatModal.hide();
    }, true); // 캡처 단계

    /* launcher */
    var iconEl=document.querySelector('.floating-icon');
    if(iconEl) iconEl.addEventListener('click',function(e){
      e.preventDefault(); chatModal.show(); setTimeout(function(){ chatInput && chatInput.focus(); },200);
    });

    /* popup (?popup=1&sid=...) */
    (async function initPopupIfNeeded(){
      const params=new URLSearchParams(location.search);
      const isPopup=params.get('popup')==='1';
      const sidParam=params.get('sid');
      if(!isPopup) return;
      chatModal.show();
      setTimeout(function(){ chatInput && chatInput.focus(); },200);
      if(sidParam){
        SESSION_ID=sidParam; localStorage.setItem('X-Session-Id',SESSION_ID); addLocalSession(SESSION_ID);
        try{ await openDockRoom(SESSION_ID); }catch(_){}
      }else{
        const sid=await (async()=>{
          try{ const r=await fetch(CP+'/api/chat/sessions/new',{method:'POST'}); return (await r.text()) || (crypto.randomUUID?crypto.randomUUID():String(Date.now())); }
          catch(_){ return (crypto.randomUUID?crypto.randomUUID():String(Date.now())); }
        })();
        SESSION_ID=sid; localStorage.setItem('X-Session-Id',sid); addLocalSession(sid); await openDockRoom(sid);
      }
      setDockBelow(true);
    })();
  });
</script>
</body>
</html>