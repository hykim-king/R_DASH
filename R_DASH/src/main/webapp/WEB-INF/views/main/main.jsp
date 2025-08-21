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
		Roboto, sans-serif;
}

.main-background {
	background-image: url('${CP}/resources/image/mainboard.png');
	background-size: cover;
	background-position: center;
	background-repeat: no-repeat;
	background-attachment: fixed;
	color: #fff
}

/* ===== Hero (video) ===== */
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

/* ===== 메뉴/뉴스/FAQ 요약 스타일 ===== */
.dropdown-wrapper {
	display: none;
	position: absolute;
	top: 100%;
	left: 0;
	width: 100%;
	padding-top: 10px;
	justify-content: center;
	gap: 40px;
	z-index: 999;
	background: transparent
}

.dropdown-column {
	display: flex;
	flex-direction: column
}

.dropdown-column a {
	color: #fff;
	text-decoration: none;
	padding: 6px 12px;
	white-space: nowrap;
	font-size: .9rem
}

.dropdown-column a:hover {
	text-decoration: underline;
	background-color: rgba(255, 255, 255, .1)
}

.nav-item {
	position: relative
}

.submenu {
	position: absolute;
	top: 100%;
	left: 0;
	display: none;
	flex-direction: column;
	background: rgba(0, 0, 0, .9);
	padding: 10px 0;
	border-radius: 6px;
	min-width: 160px;
	z-index: 1000
}

.submenu a {
	color: #fff;
	padding: 8px 20px;
	text-decoration: none;
	font-size: .9rem;
	white-space: nowrap
}

.submenu a:hover {
	background: rgba(255, 255, 255, .2)
}

.dropdown:hover .submenu {
	display: flex
}

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

/* === Floating icon === */
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
	overflow: hidden;
	height: 68vh;
	max-height: 820px;
	display: flex;
	flex-direction: column;
	position: relative
}

.chat-header {
	background: #dc3545;
	color: #fff;
	padding: 14px 18px;
	font-weight: 700;
	min-height: 56px;
	display: flex;
	align-items: center;
	justify-content: space-between
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

/* ===== “내 대화방” 패널: 모달 ‘밖’ 좌상단 ===== */
.history-dock {
	position: absolute;
	bottom: calc(100% + 10px);
	left: 0;
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
	transition: max-height .18s ease-in-out, opacity .18s ease-in-out
}

.history-dock.collapsed {
	max-height: 46px
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
	cursor: pointer
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
	transition: background .12s
}

.history-item:hover {
	background: rgba(255, 255, 255, .08)
}

.history-item.active {
	background: rgba(220, 53, 69, .92)
}

.history-title {
	font-weight: 700;
	font-size: .9rem;
	color: #fff
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

/* Mobile */
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
		left: 0;
		bottom: calc(100% + 8px)
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

	<!-- Floating chat launcher -->
	<a class="floating-icon" href="#" aria-label="도움말 또는 채팅 열기" title="재민이">
		<img src="${CP}/resources/image/Jaemini_bo.jpg" alt="재민이 아이콘" />
	</a>

	<!-- Chat Modal -->
	<div class="modal fade chat-modal" id="chatModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">

			<!-- ■ 대화방 패널(모달 밖, 좌상단 붙임) -->
			<div id="historyDock" class="history-dock collapsed">
				<div class="history-head">
					<span>내 대화방</span>
					<button id="historyToggle" class="history-toggle" title="펼치기/접기">▾</button>
				</div>
				<div id="historyBody" class="history-body"></div>
				<div class="history-foot">
					<button id="historyNew" class="history-new">+ 새 대화</button>
				</div>
			</div>

			<div class="modal-content">
				<div class="chat-header">
					<strong>재민이 채팅</strong>
					<button id="chatClose" type="button"
						class="btn-close btn-close-white" data-bs-dismiss="modal"
						aria-label="Close"></button>
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
  document.addEventListener("DOMContentLoaded", function () {
    var CP='${CP}';

    /* ===== Animations ===== */
    try{
      var elements=document.querySelectorAll(".animate-on-scroll");
      var io=new IntersectionObserver(function(entries,obs){
        entries.forEach(function(entry){ if(entry.isIntersecting){ entry.target.classList.add("backInUp"); obs.unobserve(entry.target);} });
      },{threshold:0.2});
      elements.forEach(function(el){ io.observe(el); });
    }catch(e){ console.error('animate init error',e); }

    /* ===== Elements ===== */
    var icon=document.querySelector('.floating-icon');
    var chatModalEl=document.getElementById('chatModal');
    var chatModal=new bootstrap.Modal(chatModalEl,{backdrop:'static',keyboard:true});
    var chatBody=document.getElementById('chatBody');
    var chatInput=document.getElementById('chatInput');
    var chatSend=document.getElementById('chatSend');
    var chatCloseBtn=document.getElementById('chatClose');

    /* ===== Login ===== */
    var LOGIN_USER_NO_RAW=(document.body.dataset.loginUserNo||'').trim();
    var LOGIN_USER_NO=LOGIN_USER_NO_RAW===''?null:parseInt(LOGIN_USER_NO_RAW,10);

    /* ===== Utils ===== */
    function nowText(){ return new Date().toLocaleTimeString([], {hour:'2-digit',minute:'2-digit'}); }
    function escapeHtml(s){ return (s||'').replace(/[&<>\"']/g,function(c){ return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c];}); }
    function appendBubble(text,who){
      var wrap=document.createElement('div');
      wrap.className='chat-bubble '+(who==='bot'?'bot':'user');
      wrap.innerHTML=text+'<span class="chat-time">'+nowText()+'</span>';
      chatBody.appendChild(wrap);
      chatBody.scrollTop=chatBody.scrollHeight;
    }
    function previewOf(text){ var t=(String(text||'')).replace(/\s+/g,' ').trim(); return t.length>40?t.slice(0,40)+'…':t; }

    /* ===== Modal open/close ===== */
    if(chatCloseBtn) chatCloseBtn.addEventListener('click',function(e){ e.preventDefault(); e.stopPropagation(); chatModal.hide(); });
    if(icon) icon.addEventListener('click',function(e){ e.preventDefault(); chatModal.show(); setTimeout(function(){ chatInput && chatInput.focus(); },200); });

    /* ===== Session state ===== */
    var SESSION_ID=localStorage.getItem('X-Session-Id')||null;
    function getLocalSessions(){ try{ return JSON.parse(localStorage.getItem('JM_SESSIONS')||'[]'); }catch(e){ return []; } }
    function setLocalSessions(arr){ localStorage.setItem('JM_SESSIONS',JSON.stringify(arr.slice(0,50))); }
    function addLocalSession(sid){ if(!sid) return; var list=getLocalSessions().filter(function(x){ return x!==sid; }); list.unshift(sid); setLocalSessions(list); }

    /* ===== Local message cache ===== */
    function _msgsKey(sid){ return 'JM_MSGS_'+sid; }
    function getLocalMsgs(sid){ if(!sid) return []; try{ return JSON.parse(localStorage.getItem(_msgsKey(sid))||'[]'); }catch(e){ return []; } }
    function setLocalMsgs(sid,arr){ if(!sid) return; localStorage.setItem(_msgsKey(sid),JSON.stringify((arr||[]).slice(-500))); }
    function pushLocalMsg(sid,role,text){ if(!sid) return; var arr=getLocalMsgs(sid); arr.push({role:role,text:String(text||''),ts:Date.now()}); setLocalMsgs(sid,arr); }
    function migrateCache(oldSid,newSid){ if(!oldSid||!newSid||oldSid===newSid) return; var oldMsgs=getLocalMsgs(oldSid); var newMsgs=getLocalMsgs(newSid); if(oldMsgs.length&&!newMsgs.length) setLocalMsgs(newSid,oldMsgs); localStorage.removeItem(_msgsKey(oldSid)); }

    /* ===== Dock (room list) ===== */
    var dockEl=document.getElementById('historyDock');
    var dockBody=document.getElementById('historyBody');
    var dockTgl=document.getElementById('historyToggle');
    var dockNew=document.getElementById('historyNew');

    // 패널 조작 시 모달 닫힘 방지 + 스크롤 등 상호작용 보호
    (function protectDock(){
      var events=['click','mousedown','mouseup','touchstart','touchend','pointerdown','pointerup','wheel'];
      function stopAll(e){ e.stopPropagation(); }
      [dockEl,dockBody,dockTgl,dockNew].forEach(function(el){ if(!el) return; events.forEach(function(ev){ el.addEventListener(ev,stopAll,true); }); });
    })();

    // 헤더 전체를 눌러도 토글
    var dockHead = dockEl ? dockEl.querySelector('.history-head') : null;
    function toggleDock(){ if(dockEl) dockEl.classList.toggle('collapsed'); }
    if(dockHead) dockHead.addEventListener('click', function(e){ e.stopPropagation(); toggleDock(); });
    if(dockTgl)  dockTgl.addEventListener('click', function(e){ e.stopPropagation(); toggleDock(); });

    function renderDock(list){
      dockBody.innerHTML='';
      if(!list||!list.length){
        var emp=document.createElement('div'); emp.style.cssText='padding:10px;color:#cfd6e3;font-size:.86rem;'; emp.textContent='저장된 대화가 없습니다.'; dockBody.appendChild(emp); return;
      }
      list.forEach(function(s){
        var cached=getLocalMsgs(s.sessionId);
        if((!s.lastMsg||s.lastMsg==='')&&cached.length){ s.lastMsg=previewOf(cached[cached.length-1].text); }
        var it=document.createElement('div');
        it.className='history-item'+(s.sessionId===SESSION_ID?' active':'');
        it.dataset.sid=s.sessionId;
        it.innerHTML='<div class="history-title">'+escapeHtml(s.title||'새 대화')+'</div>'
                    +'<div class="history-sub">'+escapeHtml(s.lastMsg||'')+'</div>'
                    +'<div class="history-time">'+escapeHtml(s.updatedAt||'')+'</div>';
        it.addEventListener('click',function(e){
          e.stopPropagation();
          SESSION_ID=s.sessionId;
          localStorage.setItem('X-Session-Id',SESSION_ID);
          openDockRoom(SESSION_ID).then(function(){
            Array.prototype.forEach.call(document.querySelectorAll('.history-item'),function(x){ x.classList.toggle('active',x.dataset.sid===SESSION_ID); });
            dockEl.classList.add('collapsed');
          });
        });
        dockBody.appendChild(it);
      });
    }

    async function loadDockSessions(){
      if(!dockBody) return;
      if(LOGIN_USER_NO!=null){
        try{
          var res=await fetch(CP+'/api/chat/sessions?limit=100',{headers:{'X-User-No':LOGIN_USER_NO}});
          if(res.ok){
            var list=await res.json();
            renderDock(list&&Array.isArray(list)?list.map(function(s){ return {sessionId:s.sessionId,title:s.title||'새 대화',lastMsg:s.lastMsg||'',updatedAt:s.updatedAt||''}; }):[]);
            return;
          }
        }catch(e){ console.warn('sessions api fallback(local)',e); }
      }
      var ids=getLocalSessions();
      renderDock(ids.map(function(id){ return {sessionId:id,title:'내 대화',lastMsg:'',updatedAt:''}; }));
    }

    function renderRowsAndCache(sid,rows){
      if(!Array.isArray(rows)||!rows.length) return false;
      chatBody.innerHTML='';
      var fresh=[];
      rows.forEach(function(m){
        if(m.question && String(m.question).trim()!==''){ var q=String(m.question); appendBubble(escapeHtml(q).replace(/\n/g,'<br>'),'user'); fresh.push({role:'user',text:q,ts:Date.now()}); }
        if(m.answer && String(m.answer).trim()!==''){ var a=String(m.answer); appendBubble(escapeHtml(a).replace(/\n/g,'<br>'),'bot');  fresh.push({role:'bot', text:a,ts:Date.now()}); }
        if(!m.question && !m.answer && m.text){ var role=(m.role==='bot'||m.role==='assistant')?'bot':'user'; var t=String(m.text); appendBubble(escapeHtml(t).replace(/\n/g,'<br>'),role); fresh.push({role:role,text:t,ts:Date.now()}); }
      });
      chatBody.scrollTop=chatBody.scrollHeight; setLocalMsgs(sid,fresh); return true;
    }

    async function openDockRoom(sid){
      if(!sid) return; chatBody.innerHTML='';
      var cached=getLocalMsgs(sid);
      if(cached.length){ cached.forEach(function(m){ appendBubble(escapeHtml(m.text).replace(/\n/g,'<br>'), m.role==='bot'?'bot':'user'); }); chatBody.scrollTop=chatBody.scrollHeight; }
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
          if(res2.ok){ var rows2=await res2.json(); renderRowsAndCache(sid,rows2); }
        }catch(err){ console.error(err); }
      }
    }

    if(dockNew) dockNew.addEventListener('click',async function(e){
      e.stopPropagation();
      try{
        var res=await fetch(CP+'/api/chat/sessions/new',{method:'POST'});
        var sid=(await res.text())||crypto.randomUUID();
        SESSION_ID=sid; localStorage.setItem('X-Session-Id',sid); addLocalSession(sid);
        await loadDockSessions(); await openDockRoom(sid);
      }catch(e){
        var sid2=crypto.randomUUID(); SESSION_ID=sid2; localStorage.setItem('X-Session-Id',sid2); addLocalSession(sid2);
        await loadDockSessions(); await openDockRoom(sid2);
      }
    });

    /* ===== Send ===== */
    async function sendMessage(){
      var text=chatInput.value.trim(); if(!text) return;
      if(!SESSION_ID){ SESSION_ID=localStorage.getItem('X-Session-Id')||(crypto.randomUUID?crypto.randomUUID():String(Date.now())); localStorage.setItem('X-Session-Id',SESSION_ID); addLocalSession(SESSION_ID); }
      appendBubble(escapeHtml(text).replace(/\n/g,'<br>'),'user'); pushLocalMsg(SESSION_ID,'user',text); chatInput.value='';
      try{
        var headers={'Content-Type':'application/json'}; if(SESSION_ID) headers['X-Session-Id']=SESSION_ID; if(LOGIN_USER_NO!=null) headers['X-User-No']=LOGIN_USER_NO;
        var prevSid=SESSION_ID;
        var res=await fetch(CP+'/api/chat/send',{method:'POST',headers:headers,body:JSON.stringify({question:text,userNo:LOGIN_USER_NO||undefined})});
        var sid=res.headers.get('X-Session-Id');
        if(sid){ if(sid!==SESSION_ID){ migrateCache(prevSid,sid); SESSION_ID=sid; } localStorage.setItem('X-Session-Id',SESSION_ID); addLocalSession(SESSION_ID); }
        var raw=await res.text(); var data=null; try{ data=JSON.parse(raw); }catch(e){}
        if(!res.ok){ var err='AI 호출 실패('+res.status+') '+(raw||''); appendBubble(escapeHtml(err),'bot'); pushLocalMsg(SESSION_ID,'bot',err); console.error('chat/send error',res.status,raw); return; }
        var answer=(data && data.answer)?String(data.answer):'응답을 불러오지 못했어요.'; appendBubble(escapeHtml(answer).replace(/\n/g,'<br>'),'bot'); pushLocalMsg(SESSION_ID,'bot',answer);
        loadDockSessions();
      }catch(e){ var net='네트워크 오류가 발생했어요. 연결 상태를 확인해 주세요.'; appendBubble(escapeHtml(net),'bot'); pushLocalMsg(SESSION_ID,'bot',net); console.error(e); }
    }

    chatSend.addEventListener('click',sendMessage);
    chatInput.addEventListener('keydown',function(e){ if(e.key==='Enter'&&!e.shiftKey){ e.preventDefault(); sendMessage(); } });

    chatModalEl.addEventListener('shown.bs.modal',async function(){ chatBody.scrollTop=chatBody.scrollHeight; await loadDockSessions(); if(SESSION_ID) await openDockRoom(SESSION_ID); });
  });
  </script>
</body>
</html>