/**
 * CSCE 548 Project 3 – Library API Web Client
 * Calls ALL GET methods for ALL tables: get one, get all, get subset (where applicable).
 * Also calls report/query endpoints.
 */

const STORAGE_KEY = 'library-api-base-url';

function getBaseUrl() {
  return document.getElementById('baseUrl').value.replace(/\/$/, '');
}

function getDefaultBaseUrl() {
  const origin = window.location.origin;
  if (origin && (origin.startsWith('http://') || origin.startsWith('https://')))
    return origin;
  return 'http://localhost:7000';
}

function loadSavedUrl() {
  const saved = localStorage.getItem(STORAGE_KEY);
  const input = document.getElementById('baseUrl');
  if (saved) {
    input.value = saved;
  } else {
    input.value = getDefaultBaseUrl();
  }
}

function saveUrl() {
  localStorage.setItem(STORAGE_KEY, getBaseUrl());
}

function setOutput(content, isError = false, status = '', endpoint = '', container = null) {
  if (container) {
    const meta = status || endpoint ? `<div class="response-meta ${isError ? 'error' : ''}">${escapeHtml((status || '') + (endpoint ? ' · GET ' + endpoint : ''))}</div>` : '';
    const contentClass = isError ? 'response-text error' : 'response-text';
    const contentBlock = '<div class="response-content"><pre class="' + contentClass + '">' + escapeHtml(content) + '</pre></div>';
    container.innerHTML = meta + contentBlock;
    container.classList.add('has-response');
    return;
  }
  const el = document.getElementById('globalMessage');
  if (el) {
    el.textContent = content;
    el.className = 'global-message' + (isError ? ' error' : '');
  }
}

function labelFromKey(key) {
  return key.replace(/([A-Z])/g, ' $1').replace(/^./, s => s.toUpperCase()).trim();
}

function isNestedReport(data) {
  if (typeof data !== 'object' || data === null || Array.isArray(data)) return false;
  const entries = Object.entries(data);
  if (entries.length === 0) return false;
  const allObjectOrNull = entries.every(([, v]) =>
    v === null || (typeof v === 'object' && !Array.isArray(v))
  );
  const hasAtLeastOneObject = entries.some(([, v]) => typeof v === 'object' && v !== null && !Array.isArray(v));
  return allObjectOrNull && hasAtLeastOneObject;
}

function buildNestedReportHtml(data) {
  let html = '<div class="nested-report">';
  for (const [sectionName, sectionData] of Object.entries(data)) {
    if (sectionData === null || typeof sectionData !== 'object') continue;
    const label = labelFromKey(sectionName);
    html += '<div class="nested-report-section"><div class="nested-report-title">' + escapeHtml(label) + '</div>';
    const entries = Object.entries(sectionData);
    if (entries.length === 0) {
      html += '<p class="empty-msg">—</p>';
    } else {
      html += '<table class="nested-table"><tbody>';
      for (const [k, v] of entries) {
        let val;
        if (v === null || v === undefined) val = '—';
        else if (typeof v === 'object') val = JSON.stringify(v);
        else {
          const dateFormatted = formatDateTime(v);
          val = dateFormatted !== null ? dateFormatted : String(v);
        }
        html += '<tr><td class="nested-key">' + escapeHtml(labelFromKey(k)) + '</td><td class="nested-val">' + escapeHtml(val) + '</td></tr>';
      }
      html += '</tbody></table>';
    }
    html += '</div>';
  }
  html += '</div>';
  return html;
}

function buildTableHtml(data) {
  if (data === null || data === undefined) return '';
  if (Array.isArray(data)) {
    if (data.length === 0) return '<p class="empty-msg">No records.</p>';
    const keys = Object.keys(data[0]);
    const thead = '<thead><tr>' + keys.map(k => '<th>' + escapeHtml(labelFromKey(k)) + '</th>').join('') + '</tr></thead>';
    const rows = data.map(row =>
      '<tr>' + keys.map(k => '<td>' + formatCell(row[k]) + '</td>').join('') + '</tr>'
    ).join('');
    return '<table>' + thead + '<tbody>' + rows + '</tbody></table>';
  }
  if (typeof data === 'object') {
    const entries = Object.entries(data);
    if (entries.length === 0) return '<p class="empty-msg">Empty object.</p>';
    if (isNestedReport(data)) return buildNestedReportHtml(data);
    return '<table><thead><tr><th>Field</th><th>Value</th></tr></thead><tbody>' +
      entries.map(([k, v]) => '<tr><td>' + escapeHtml(labelFromKey(k)) + '</td><td>' + formatCell(v) + '</td></tr>').join('') +
      '</tbody></table>';
  }
  return '';
}

/** Detect ISO date-time or date-only string and return a readable display string. */
function formatDateTime(val) {
  if (val === null || val === undefined) return '—';
  const s = String(val).trim();
  const isoDateTime = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}/;
  const isoDateOnly = /^\d{4}-\d{2}-\d{2}$/;
  if (isoDateTime.test(s)) {
    const d = new Date(s);
    if (!isNaN(d.getTime())) {
      return d.toLocaleString(undefined, { dateStyle: 'medium', timeStyle: 'short' });
    }
  }
  if (isoDateOnly.test(s)) {
    const d = new Date(s + 'T12:00:00');
    if (!isNaN(d.getTime())) {
      return d.toLocaleDateString(undefined, { dateStyle: 'medium' });
    }
  }
  return null;
}

function formatCell(val) {
  if (val === null || val === undefined) return '<em>—</em>';
  if (typeof val === 'object') return escapeHtml(JSON.stringify(val));
  const dateFormatted = formatDateTime(val);
  if (dateFormatted !== null) return escapeHtml(dateFormatted);
  return escapeHtml(String(val));
}

function escapeHtml(str) {
  if (str == null) return '';
  const div = document.createElement('div');
  div.textContent = str;
  return div.innerHTML;
}

function setOutputWithTable(body, status, endpoint, container) {
  if (!container) return;
  const html = buildTableHtml(body);
  const meta = '<div class="response-meta">' + escapeHtml((status || '') + (endpoint ? ' · GET ' + endpoint : '')) + '</div>';
  if (html) {
    container.innerHTML = meta + '<div class="card-response-table">' + html + '</div>';
  } else {
    container.innerHTML = meta + '<pre class="response-text">' + escapeHtml(typeof body === 'object' ? JSON.stringify(body, null, 2) : String(body)) + '</pre>';
  }
  container.classList.add('has-response');
}

async function apiGet(path, container) {
  const base = getBaseUrl();
  const url = `${base}${path}`;
  let res;
  try {
    res = await fetch(url, { method: 'GET', headers: { Accept: 'application/json' } });
  } catch (err) {
    const msg = err.message || String(err);
    const hint = msg === 'Failed to fetch'
      ? `Cannot reach the API at ${url}. Is the server running? (e.g. mvn exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer). Check the base URL and that nothing is blocking the connection (firewall, wrong port).`
      : msg;
    setOutput(hint, true, 'Error', path, container);
    return { ok: false };
  }
  const text = await res.text();
  let body;
  try {
    body = text ? JSON.parse(text) : null;
  } catch {
    body = text;
  }
  const status = `${res.status} ${res.statusText}`;
  if (res.ok && (Array.isArray(body) || (typeof body === 'object' && body !== null))) {
    setOutputWithTable(body, status, path, container);
  } else {
    const errMsg = getServerErrorMessage(res.status, body, text);
    setOutput(errMsg, !res.ok, status, path, container);
  }
  return { ok: res.ok, status: res.status, body };
}

function getServerErrorMessage(status, body, rawText) {
  let message = rawText || '';
  if (body !== undefined && body !== null) {
    if (typeof body === 'object' && (body.message || body.error || body.detail)) {
      message = body.message || body.error || body.detail;
      if (typeof message !== 'string') message = JSON.stringify(message);
      if (status >= 500 && body.detail && body.detail !== message) {
        message = message + '\n' + body.detail;
      }
    } else if (typeof body === 'object') {
      message = JSON.stringify(body, null, 2);
    } else {
      message = String(body);
    }
  }
  const prefix = status >= 400 ? 'Server error: ' : '';
  return (prefix + message).trim() || `Request failed with status ${status}.`;
}

function handleError(err, path, container) {
  const msg = err.message || String(err);
  const hint = msg === 'Failed to fetch'
    ? `Cannot reach the API. Is the server running? Check the API base URL (e.g. http://localhost:7000).`
    : msg;
  setOutput(hint, true, 'Error', path || '', container);
}

function getResponseContainer(button) {
  const card = button && button.closest('.card');
  return card ? card.querySelector('.card-response') : null;
}

/** Parse input as a positive integer (>= 1). Returns the number or null if empty/invalid. */
function parsePositiveId(value, fieldName) {
  if (value === undefined || value === null) return null;
  const s = String(value).trim();
  if (s === '') return null;
  const n = parseInt(s, 10);
  if (isNaN(n) || n < 1) return null;
  return n;
}

// Resource config: path segment and id param name for "get one"
const RESOURCES = {
  categories: { path: '/api/categories', idParam: 'id' },
  authors:    { path: '/api/authors', idParam: 'id' },
  members:    { path: '/api/members', idParam: 'id' },
  books:      { path: '/api/books', idParam: 'id' },
  loans:      { path: '/api/loans', idParam: 'id' },
};

function clearAllResponses() {
  document.querySelectorAll('.card-response').forEach(el => {
    el.innerHTML = '';
    el.parentElement?.classList.remove('has-response');
  });
  const globalMsg = document.getElementById('globalMessage');
  if (globalMsg) globalMsg.textContent = '';
}

function clearTabResponse(card) {
  const container = card && card.querySelector('.card-response');
  if (container) {
    container.innerHTML = '';
    card.classList.remove('has-response');
  }
}

function bindButtons() {
  document.getElementById('saveUrl').addEventListener('click', () => {
    saveUrl();
    setOutput('Base URL saved.', false, '', '', null);
  });

  document.getElementById('clearAll').addEventListener('click', clearAllResponses);

  document.querySelectorAll('.clear-tab').forEach(btn => {
    btn.addEventListener('click', () => {
      const card = btn.closest('.card');
      clearTabResponse(card);
    });
  });

  loadSavedUrl();

  // Start with no default number so values like "1" (which may be null in DB) don't show in the spinner
  document.querySelectorAll('input[type="number"][min="1"]').forEach(input => {
    input.value = '';
  });

  // Get all
  document.querySelectorAll('[data-get-all]').forEach(btn => {
    btn.addEventListener('click', async () => {
      const resource = btn.getAttribute('data-get-all');
      const { path } = RESOURCES[resource];
      const container = getResponseContainer(btn);
      try {
        await apiGet(path, container);
      } catch (e) {
        handleError(e, path, container);
      }
    });
  });

  // Get one
  document.querySelectorAll('[data-get-one]').forEach(btn => {
    btn.addEventListener('click', async () => {
      const resource = btn.getAttribute('data-get-one');
      const idInput = document.getElementById(btn.getAttribute('data-id-input'));
      const id = idInput ? parsePositiveId(idInput.value, 'ID') : null;
      const { path } = RESOURCES[resource];
      const container = getResponseContainer(btn);
      if (id == null) {
        setOutput('Please enter a valid ID (1 or greater).', true, '', '', container);
        return;
      }
      const endpoint = `${path}/${id}`;
      try {
        await apiGet(endpoint, container);
      } catch (e) {
        handleError(e, endpoint, container);
      }
    });
  });

  // Get subset (loans by member / by status)
  document.querySelectorAll('[data-get-subset]').forEach(btn => {
    btn.addEventListener('click', async () => {
      const subset = btn.getAttribute('data-subset');
      const idInput = document.getElementById(btn.getAttribute('data-id-input'));
      const container = getResponseContainer(btn);
      if (subset === 'member') {
        const memberId = idInput ? parsePositiveId(idInput.value, 'Member ID') : null;
        if (memberId == null) {
          setOutput('Please enter a valid member ID (1 or greater).', true, '', '', container);
          return;
        }
        try {
          await apiGet(`/api/loans/member/${memberId}`, container);
        } catch (e) {
          handleError(e, '/api/loans/member/...', container);
        }
      } else {
        const value = idInput && idInput.value.trim();
        if (!value) {
          setOutput('Please enter a status.', true, '', '', container);
          return;
        }
        try {
          await apiGet(`/api/loans/status/${encodeURIComponent(value)}`, container);
        } catch (e) {
          handleError(e, '/api/loans/status/...', container);
        }
      }
    });
  });

  // Reports
  document.querySelectorAll('[data-report]').forEach(btn => {
    btn.addEventListener('click', async () => {
      const report = btn.getAttribute('data-report');
      const idInput = btn.getAttribute('data-id-input')
        ? document.getElementById(btn.getAttribute('data-id-input'))
        : null;
      const container = getResponseContainer(btn);

      let path;
      switch (report) {
        case 'loans-with-details':
          path = '/api/loans/with-details';
          break;
        case 'loan-details': {
          const loanId = idInput ? parsePositiveId(idInput.value, 'Loan ID') : null;
          if (loanId == null) {
            setOutput('Please enter a valid loan ID (1 or greater).', true, '', '', container);
            return;
          }
          path = `/api/loans/${loanId}/details`;
          break;
        }
        case 'member-summary': {
          const memberId = idInput ? parsePositiveId(idInput.value, 'Member ID') : null;
          if (memberId == null) {
            setOutput('Please enter a valid member ID (1 or greater).', true, '', '', container);
            return;
          }
          path = `/api/members/${memberId}/summary`;
          break;
        }
        case 'book-popularity':
          path = '/api/books/popularity';
          break;
        case 'record-counts':
          path = '/api/records/counts';
          break;
        default:
          setOutput('Unknown report.', true, '', '', container);
          return;
      }

      try {
        await apiGet(path, container);
      } catch (e) {
        handleError(e, path, container);
      }
    });
  });

  // Clamp number inputs to min 1 so spinner never produces 0 or negative
  document.querySelectorAll('input[type="number"][min="1"]').forEach(input => {
    input.addEventListener('input', () => {
      const n = parseInt(input.value, 10);
      if (input.value !== '' && (isNaN(n) || n < 1)) {
        input.value = '1';
      }
    });
    input.addEventListener('change', () => {
      const n = parseInt(input.value, 10);
      if (input.value !== '' && (isNaN(n) || n < 1)) {
        input.value = '1';
      }
    });
  });
}

bindButtons();
