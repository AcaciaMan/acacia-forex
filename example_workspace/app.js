document.addEventListener('DOMContentLoaded', () => {
    const fileSelect = document.getElementById('file-select');
    const tableHeaders = document.getElementById('table-headers');
    const tableBody = document.getElementById('table-body');
    let sortDirections = {};

    fetch('data/dataFiles.json')
        .then(response => response.json())
        .then(dataFiles => {
            dataFiles.forEach(file => {
                const option = document.createElement('option');
                option.value = file;
                option.textContent = file.split('/').pop();
                fileSelect.appendChild(option);
            });

            fileSelect.value = dataFiles[0];
            loadAndDisplayData(dataFiles[0]);

            fileSelect.addEventListener('change', (event) => {
                loadAndDisplayData(event.target.value);
            });
        })
        .catch(error => console.error('Error loading file names:', error));

    function loadAndDisplayData(file) {
        fetch(file)
            .then(response => response.json())
            .then(data => {
                displayTable(data);
            })
            .catch(error => console.error('Error loading data:', error));
    }

    function displayTable(data) {
        tableHeaders.innerHTML = '';
        tableBody.innerHTML = '';

        const headers = Object.keys(data[0]);
        headers.forEach(header => {
            const th = document.createElement('th');
            th.textContent = header;
            th.addEventListener('click', () => sortTableByColumn(header, headers));
            tableHeaders.appendChild(th);
            sortDirections[header] = 'asc'; // Initialize sort direction
        });

        data.forEach(row => {
            const tr = document.createElement('tr');
            headers.forEach(header => {
                const td = document.createElement('td');
                td.textContent = row[header];
                tr.appendChild(td);
            });
            tableBody.appendChild(tr);
        });
    }

    function sortTableByColumn(column, headers) {
        const rows = Array.from(tableBody.querySelectorAll('tr'));
        const direction = sortDirections[column] === 'asc' ? 'desc' : 'asc';
        sortDirections[column] = direction;

        const columnIndex = headers.indexOf(column) + 1;
        const sortedRows = rows.sort((a, b) => {
            const aText = a.querySelector(`td:nth-child(${columnIndex})`).textContent;
            const bText = b.querySelector(`td:nth-child(${columnIndex})`).textContent;

            const aValue = isNaN(aText) ? aText : parseFloat(aText);
            const bValue = isNaN(bText) ? bText : parseFloat(bText);

            if (aValue < bValue) {
                return direction === 'asc' ? -1 : 1;
            }
            if (aValue > bValue) {
                return direction === 'asc' ? 1 : -1;
            }
            return 0;
        });

        tableBody.innerHTML = '';
        sortedRows.forEach(row => tableBody.appendChild(row));
    }
});