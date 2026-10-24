const form = document.querySelector('#student-form');
const nameInput = document.querySelector('#name');
const emailInput = document.querySelector('#email');
const submitButton = document.querySelector('#submit-button');
const cancelButton = document.querySelector('#cancel-button');
const status = document.querySelector('#status');
const tableBody = document.querySelector('#student-table tbody');
const emptyState = document.querySelector('#empty-state');
let editingId = null;

function setStatus(message, kind = '') {
  status.textContent = message;
  status.className = kind;
}

function element(tag, text) {
  const node = document.createElement(tag);
  node.textContent = text;
  return node;
}

async function request(url, options) {
  const response = await fetch(url, options);
  if (!response.ok) {
    throw new Error(`Request failed (${response.status}).`);
  }
  return response.status === 204 ? null : response.json();
}

function resetForm() {
  editingId = null;
  form.reset();
  submitButton.textContent = 'Create student';
  cancelButton.hidden = true;
}

function renderStudents(students) {
  tableBody.replaceChildren();
  emptyState.hidden = students.length !== 0;
  document.querySelector('#student-table').hidden = students.length === 0;
  for (const student of students) {
    const row = document.createElement('tr');
    row.append(element('td', String(student.id)), element('td', student.name), element('td', student.email));
    const actions = document.createElement('td');
    actions.className = 'actions';
    const edit = element('button', 'Edit');
    edit.className = 'secondary';
    edit.addEventListener('click', () => {
      editingId = student.id;
      nameInput.value = student.name;
      emailInput.value = student.email;
      submitButton.textContent = `Save changes to #${student.id}`;
      cancelButton.hidden = false;
      setStatus(`Editing record #${student.id}.`);
      nameInput.focus();
    });
    const remove = element('button', 'Delete');
    remove.className = 'delete';
    remove.addEventListener('click', async () => {
      if (!confirm(`Delete record #${student.id} (${student.name})?`)) return;
      try {
        await request(`/students/${student.id}`, { method: 'DELETE' });
        setStatus(`Deleted record #${student.id}. Reloaded records from the server.`, 'success');
        await loadStudents();
      } catch (error) { setStatus(error.message, 'error'); }
    });
    actions.append(edit, remove);
    row.append(actions);
    tableBody.append(row);
  }
}

async function loadStudents() {
  try {
    renderStudents(await request('/students'));
  } catch (error) { setStatus(`Could not load records: ${error.message}`, 'error'); }
}

form.addEventListener('submit', async (event) => {
  event.preventDefault();
  const payload = { name: nameInput.value.trim(), email: emailInput.value.trim() };
  const isEditing = editingId !== null;
  try {
    const student = await request(isEditing ? `/students/${editingId}` : '/students', {
      method: isEditing ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    setStatus(`${isEditing ? 'Updated' : 'Created'} record #${student.id}. Reloaded records from the server.`, 'success');
    resetForm();
    await loadStudents();
  } catch (error) { setStatus(error.message, 'error'); }
});

cancelButton.addEventListener('click', () => { resetForm(); setStatus('Edit cancelled.'); });
document.querySelector('#reload-button').addEventListener('click', async () => {
  await loadStudents(); setStatus('Records reloaded from the server.', 'success');
});
loadStudents();
