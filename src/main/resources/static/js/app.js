const API_URL = '/api/projects';

document.addEventListener('DOMContentLoaded', () => {
    fetchProjects();

    // Event Listeners
    document.getElementById('btn-open-add-modal').addEventListener('click', openAddModal);
    document.getElementById('btn-close-modal').addEventListener('click', closeModal);
    document.getElementById('btn-cancel').addEventListener('click', closeModal);
    document.getElementById('project-form').addEventListener('submit', handleFormSubmit);
});

async function fetchProjects() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Failed to fetch projects');
        
        const projects = await response.json();
        renderProjects(projects);
        updateStatistics(projects);
    } catch (error) {
        showToast('Error loading projects: ' + error.message, 'error');
        document.getElementById('projects-table-body').innerHTML = `
            <tr><td colspan="8" class="text-center" style="color: red;">Failed to load projects.</td></tr>
        `;
    }
}

function renderProjects(projects) {
    const tableBody = document.getElementById('projects-table-body');
    tableBody.innerHTML = '';

    if (projects.length === 0) {
        tableBody.innerHTML = `
            <tr><td colspan="8" class="text-center">No projects found. Click "Add Project" to get started!</td></tr>
        `;
        return;
    }

    projects.forEach(project => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>#${project.id}</td>
            <td><strong>${escapeHtml(project.name)}</strong></td>
            <td>${escapeHtml(project.description || '-')}</td>
            <td>${escapeHtml(project.owner)}</td>
            <td><span class="badge badge-${project.status}">${project.status}</span></td>
            <td>${project.startDate || '-'}</td>
            <td>${project.endDate || '-'}</td>
            <td>
                <button class="btn btn-action btn-edit" onclick="editProject(${project.id})">
                    <i class="fa-solid fa-pen-to-square"></i> Edit
                </button>
                <button class="btn btn-action btn-delete" onclick="deleteProject(${project.id})">
                    <i class="fa-solid fa-trash"></i> Delete
                </button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function updateStatistics(projects) {
    document.getElementById('stat-total').textContent = projects.length;
    document.getElementById('stat-planned').textContent = projects.filter(p => p.status === 'PLANNED').length;
    document.getElementById('stat-in-progress').textContent = projects.filter(p => p.status === 'IN_PROGRESS').length;
    document.getElementById('stat-completed').textContent = projects.filter(p => p.status === 'COMPLETED').length;
}

function openAddModal() {
    document.getElementById('modal-title').textContent = 'Add New Project';
    document.getElementById('project-form').reset();
    document.getElementById('project-id').value = '';
    document.getElementById('project-modal').classList.remove('hidden');
}

function closeModal() {
    document.getElementById('project-modal').classList.add('hidden');
}

async function editProject(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error('Failed to fetch project details');
        
        const project = await response.json();
        
        document.getElementById('modal-title').textContent = 'Edit Project #' + project.id;
        document.getElementById('project-id').value = project.id;
        document.getElementById('project-name').value = project.name;
        document.getElementById('project-description').value = project.description || '';
        document.getElementById('project-owner').value = project.owner;
        document.getElementById('project-status').value = project.status;
        document.getElementById('project-start-date').value = project.startDate || '';
        document.getElementById('project-end-date').value = project.endDate || '';

        document.getElementById('project-modal').classList.remove('hidden');
    } catch (error) {
        showToast('Error loading project: ' + error.message, 'error');
    }
}

async function handleFormSubmit(event) {
    event.preventDefault();

    const id = document.getElementById('project-id').value;
    const projectData = {
        name: document.getElementById('project-name').value,
        description: document.getElementById('project-description').value,
        owner: document.getElementById('project-owner').value,
        status: document.getElementById('project-status').value,
        startDate: document.getElementById('project-start-date').value || null,
        endDate: document.getElementById('project-end-date').value || null
    };

    const isEdit = id !== '';
    const url = isEdit ? `${API_URL}/${id}` : API_URL;
    const method = isEdit ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(projectData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Operation failed');
        }

        showToast(`Project ${isEdit ? 'updated' : 'created'} successfully!`, 'success');
        closeModal();
        fetchProjects();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function deleteProject(id) {
    if (!confirm(`Are you sure you want to delete Project #${id}?`)) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('Failed to delete project');

        showToast('Project deleted successfully!', 'success');
        fetchProjects();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type}`;
    
    setTimeout(() => {
        toast.classList.add('hidden');
    }, 3500);
}

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/[&<>"']/g, function(m) {
        return {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        }[m];
    });
}
