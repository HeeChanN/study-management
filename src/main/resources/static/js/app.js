// 전역 상태 관리
const state = {
    studyGroupId: null,
    selectedMemberId: null,
    studyMembers: [],
    penalties: [],
    errorToast: null,
    successToast: null,
    addMemberModal: null,
    addSubmissionModal: null
};

// API Base URL
const API_BASE_URL = '/api/v1';

// 초기화
document.addEventListener('DOMContentLoaded', async () => {
    initErrorToast();
    initEventListeners();
    await loadStudyGroup();
});

// Toast 초기화
function initErrorToast() {
    const errorToastElement = document.getElementById('errorToast');
    state.errorToast = new bootstrap.Toast(errorToastElement);

    const successToastElement = document.getElementById('successToast');
    state.successToast = new bootstrap.Toast(successToastElement);

    // 모달 초기화
    const addMemberModalElement = document.getElementById('addMemberModal');
    state.addMemberModal = new bootstrap.Modal(addMemberModalElement);

    const addSubmissionModalElement = document.getElementById('addSubmissionModal');
    state.addSubmissionModal = new bootstrap.Modal(addSubmissionModalElement);
}

// 이벤트 리스너 초기화
function initEventListeners() {
    // 새로고침 버튼
    document.getElementById('refreshBtn').addEventListener('click', () => {
        if (state.selectedMemberId) {
            loadMemberSubmissions(state.selectedMemberId);
        }
        loadPenalties();
    });

    // 멤버 검색
    document.getElementById('memberSearch').addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase();
        filterMembers(searchTerm);
    });

    // 벌금 탭 클릭 시 데이터 로드
    document.getElementById('penalties-tab').addEventListener('click', () => {
        loadPenalties();
    });

    // 멤버 추가 버튼
    document.getElementById('submitAddMember').addEventListener('click', addMember);

    // 제출 기록 추가 버튼
    document.getElementById('submitAddSubmission').addEventListener('click', addSubmission);

    // 엔터키로 폼 제출
    document.getElementById('memberName').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            addMember();
        }
    });
}

// 스터디 그룹 로드
async function loadStudyGroup() {
    try {
        const response = await fetch(`${API_BASE_URL}/study-groups`);
        if (!response.ok) throw new Error('스터디 그룹을 불러올 수 없습니다.');

        const studyGroups = await response.json();
        if (studyGroups.length === 0) {
            showError('등록된 스터디 그룹이 없습니다.');
            return;
        }

        // 첫 번째 그룹 선택
        const studyGroup = studyGroups[0];
        state.studyGroupId = studyGroup.id;

        // 스터디 그룹 정보 표시
        document.getElementById('studyGroupDescription').textContent = studyGroup.description || studyGroup.name;

        // 멤버 로드
        await loadStudyMembers(studyGroup.id);
    } catch (error) {
        console.error('Error loading study group:', error);
        showError(error.message);
    }
}

// 스터디 멤버 로드
async function loadStudyMembers(studyGroupId) {
    try {
        const response = await fetch(`${API_BASE_URL}/study-groups/${studyGroupId}/study-members`);
        if (!response.ok) throw new Error('멤버 목록을 불러올 수 없습니다.');

        state.studyMembers = await response.json();
        renderMembers(state.studyMembers);
    } catch (error) {
        console.error('Error loading study members:', error);
        showError(error.message);
    }
}

// 멤버 목록 렌더링
function renderMembers(members) {
    const memberList = document.getElementById('memberList');

    if (members.length === 0) {
        memberList.innerHTML = `
            <div class="text-center py-5">
                <i class="bi bi-people display-3 text-muted"></i>
                <p class="text-muted mt-3">등록된 멤버가 없습니다.</p>
            </div>
        `;
        return;
    }

    memberList.innerHTML = members.map(member => `
        <div class="member-card ${state.selectedMemberId === member.id ? 'active' : ''}"
             data-member-id="${member.id}"
             onclick="selectMember(${member.id}, '${member.name}')">
            <div class="d-flex align-items-center">
                <div class="member-avatar">
                    ${getInitial(member.name)}
                </div>
                <div class="member-info">
                    <div class="member-name">${member.name}</div>
                    <div class="member-stats">
                        <i class="bi bi-file-text"></i> 제출 기록 보기
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

// 멤버 필터링
function filterMembers(searchTerm) {
    const filteredMembers = state.studyMembers.filter(member =>
        member.name.toLowerCase().includes(searchTerm)
    );
    renderMembers(filteredMembers);
}

// 멤버 선택
async function selectMember(memberId, memberName) {
    state.selectedMemberId = memberId;

    // UI 업데이트
    document.getElementById('selectedMemberName').textContent = memberName;
    document.getElementById('contentSubtitle').textContent = `${memberName}님의 활동 내역`;

    // 제출 기록 작성 버튼 표시
    document.getElementById('addSubmissionBtn').style.display = 'inline-block';

    // 멤버 카드 활성화 상태 업데이트
    document.querySelectorAll('.member-card').forEach(card => {
        card.classList.remove('active');
    });
    document.querySelector(`[data-member-id="${memberId}"]`).classList.add('active');

    // 제출 기록 로드
    await loadMemberSubmissions(memberId);
}

// 멤버의 제출 기록 로드
async function loadMemberSubmissions(memberId) {
    const submissionsContent = document.getElementById('submissionsContent');

    // 로딩 스피너 표시
    submissionsContent.innerHTML = `
        <div class="text-center py-5">
            <div class="spinner-border text-success" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    `;

    try {
        const response = await fetch(`${API_BASE_URL}/study-members/${memberId}/daily-submissions`);
        if (!response.ok) throw new Error('제출 기록을 불러올 수 없습니다.');

        const submissions = await response.json();
        renderSubmissions(submissions);
    } catch (error) {
        console.error('Error loading submissions:', error);
        showError(error.message);
        submissionsContent.innerHTML = `
            <div class="empty-state text-center py-5">
                <i class="bi bi-exclamation-circle display-1 text-danger"></i>
                <p class="text-muted mt-3">제출 기록을 불러오는 중 오류가 발생했습니다.</p>
            </div>
        `;
    }
}

// 제출 기록 렌더링
function renderSubmissions(submissions) {
    const submissionsContent = document.getElementById('submissionsContent');

    if (submissions.length === 0) {
        submissionsContent.innerHTML = `
            <div class="empty-state text-center py-5">
                <i class="bi bi-inbox display-1 text-muted"></i>
                <p class="text-muted mt-3">아직 제출한 기록이 없습니다.</p>
            </div>
        `;
        return;
    }

    // 최신순 정렬
    submissions.sort((a, b) => new Date(b.submissionAt) - new Date(a.submissionAt));

    submissionsContent.innerHTML = submissions.map((submission, index) => {
        const isLongContent = submission.content.length > 150;
        const previewContent = isLongContent ? submission.content.substring(0, 150) + '...' : submission.content;

        return `
            <div class="submission-card" style="animation-delay: ${index * 0.05}s">
                <div class="submission-header">
                    <div class="submission-author">
                        <div class="author-avatar">
                            ${getInitial(submission.studyMemberName)}
                        </div>
                        <div class="author-info">
                            <h6 class="mb-0">${submission.studyMemberName}</h6>
                        </div>
                    </div>
                    <div class="submission-date">
                        <i class="bi bi-clock"></i>
                        ${formatDate(submission.submissionAt)}
                    </div>
                </div>
                <div class="submission-content ${isLongContent ? 'collapsed' : 'expanded'}" id="content-${submission.id}">
                    ${isLongContent ? previewContent : submission.content}
                </div>
                ${isLongContent ? `
                    <button class="btn btn-sm btn-outline-success expand-btn" onclick="toggleContent(${submission.id}, '${escapeHtml(submission.content)}')">
                        <i class="bi bi-chevron-down"></i> 더보기
                    </button>
                ` : ''}
            </div>
        `;
    }).join('');
}

// 콘텐츠 펼치기/접기
function toggleContent(submissionId, fullContent) {
    const contentElement = document.getElementById(`content-${submissionId}`);
    const button = event.target.closest('button');

    if (contentElement.classList.contains('collapsed')) {
        contentElement.classList.remove('collapsed');
        contentElement.classList.add('expanded');
        contentElement.textContent = unescapeHtml(fullContent);
        button.innerHTML = '<i class="bi bi-chevron-up"></i> 접기';
    } else {
        contentElement.classList.remove('expanded');
        contentElement.classList.add('collapsed');
        const preview = unescapeHtml(fullContent).substring(0, 150) + '...';
        contentElement.textContent = preview;
        button.innerHTML = '<i class="bi bi-chevron-down"></i> 더보기';
    }
}

// 벌금 내역 로드
async function loadPenalties() {
    if (!state.studyGroupId) return;

    const penaltiesContent = document.getElementById('penaltiesContent');

    // 로딩 스피너 표시
    penaltiesContent.innerHTML = `
        <div class="text-center py-5">
            <div class="spinner-border text-success" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    `;

    try {
        const response = await fetch(`${API_BASE_URL}/study-groups/${state.studyGroupId}/penalties`);
        if (!response.ok) throw new Error('벌금 내역을 불러올 수 없습니다.');

        const penalties = await response.json();
        state.penalties = penalties;
        renderPenalties(penalties);
    } catch (error) {
        console.error('Error loading penalties:', error);
        showError(error.message);
        penaltiesContent.innerHTML = `
            <div class="empty-state text-center py-5">
                <i class="bi bi-exclamation-circle display-1 text-danger"></i>
                <p class="text-muted mt-3">벌금 내역을 불러오는 중 오류가 발생했습니다.</p>
            </div>
        `;
    }
}

// 벌금 내역 렌더링
function renderPenalties(penalties) {
    const penaltiesContent = document.getElementById('penaltiesContent');

    if (penalties.length === 0) {
        penaltiesContent.innerHTML = `
            <div class="empty-state text-center py-5">
                <i class="bi bi-check-circle display-1 text-success"></i>
                <p class="text-muted mt-3">아직 발생한 벌금이 없습니다.</p>
            </div>
        `;
        return;
    }

    // 최신순 정렬
    penalties.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

    // 총 벌금액 계산
    const totalAmount = penalties.reduce((sum, penalty) => sum + penalty.amount, 0);

    penaltiesContent.innerHTML = `
        <div class="mb-4">
            <div class="alert alert-info d-flex align-items-center">
                <i class="bi bi-info-circle me-2 fs-5"></i>
                <div>
                    <strong>총 벌금 내역:</strong> ${penalties.length}건
                    <span class="ms-3"><strong>총 금액:</strong> ${formatCurrency(totalAmount)}</span>
                </div>
            </div>
        </div>
        <div class="penalty-table">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th scope="col" style="width: 10%">#</th>
                        <th scope="col" style="width: 40%">제목</th>
                        <th scope="col" style="width: 30%">생성일</th>
                        <th scope="col" style="width: 20%" class="text-end">금액</th>
                    </tr>
                </thead>
                <tbody>
                    ${penalties.map((penalty, index) => `
                        <tr>
                            <th scope="row">${penalties.length - index}</th>
                            <td>${penalty.title}</td>
                            <td>
                                <i class="bi bi-calendar3"></i>
                                ${formatDate(penalty.createdAt)}
                            </td>
                            <td class="text-end">
                                <span class="penalty-badge badge bg-danger">
                                    ${formatCurrency(penalty.amount)}
                                </span>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

// 유틸리티 함수들

// 이름에서 이니셜 추출
function getInitial(name) {
    if (!name) return '?';
    return name.charAt(0).toUpperCase();
}

// 날짜 포맷팅
function formatDate(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diff = now - date;
    const diffDays = Math.floor(diff / (1000 * 60 * 60 * 24));

    if (diffDays === 0) {
        return `오늘 ${date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })}`;
    } else if (diffDays === 1) {
        return `어제 ${date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })}`;
    } else if (diffDays < 7) {
        return `${diffDays}일 전`;
    } else {
        return date.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }
}

// 통화 포맷팅
function formatCurrency(amount) {
    return `${amount.toLocaleString('ko-KR')}원`;
}

// HTML 이스케이프
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, m => map[m]);
}

// HTML 언이스케이프
function unescapeHtml(text) {
    const map = {
        '&amp;': '&',
        '&lt;': '<',
        '&gt;': '>',
        '&quot;': '"',
        '&#039;': "'"
    };
    return text.replace(/&amp;|&lt;|&gt;|&quot;|&#039;/g, m => map[m]);
}

// 에러 토스트 표시
function showError(message) {
    document.getElementById('errorToastBody').textContent = message;
    state.errorToast.show();
}

// 성공 토스트 표시
function showSuccess(message) {
    document.getElementById('successToastBody').textContent = message;
    state.successToast.show();
}

// 멤버 추가
async function addMember() {
    const memberName = document.getElementById('memberName').value.trim();

    if (!memberName) {
        showError('이름을 입력해주세요.');
        return;
    }

    if (!state.studyGroupId) {
        showError('스터디 그룹을 찾을 수 없습니다.');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/study-groups/${state.studyGroupId}/study-members`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: memberName,
                studyGroupId: state.studyGroupId
            })
        });

        if (!response.ok) throw new Error('멤버 추가에 실패했습니다.');

        // 모달 닫기
        state.addMemberModal.hide();

        // 폼 초기화
        document.getElementById('memberName').value = '';

        // 성공 메시지 표시
        showSuccess(`${memberName}님이 추가되었습니다.`);

        // 멤버 목록 새로고침
        await loadStudyMembers(state.studyGroupId);

    } catch (error) {
        console.error('Error adding member:', error);
        showError(error.message);
    }
}

// 제출 기록 추가
async function addSubmission() {
    const content = document.getElementById('submissionContent').value.trim();

    if (!content) {
        showError('내용을 입력해주세요.');
        return;
    }

    if (!state.selectedMemberId) {
        showError('멤버를 선택해주세요.');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/daily-submissions`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                studyMemberId: state.selectedMemberId,
                content: content
            })
        });

        if (!response.ok) throw new Error('제출 기록 작성에 실패했습니다.');

        // 모달 닫기
        state.addSubmissionModal.hide();

        // 폼 초기화
        document.getElementById('submissionContent').value = '';

        // 성공 메시지 표시
        showSuccess('제출 기록이 작성되었습니다.');

        // 제출 기록 새로고침
        await loadMemberSubmissions(state.selectedMemberId);

    } catch (error) {
        console.error('Error adding submission:', error);
        showError(error.message);
    }
}
