document.addEventListener('DOMContentLoaded', () => {
    const alertsTableBody = document.querySelector('#alerts-table tbody');
    const acknowledgeBtn = document.querySelector('.clear-alerts-btn');

    // Sample data for demonstration
    const alertsData = [
        {
            time: '05:05 PM',
            severity: 'Critical',
            event: 'System Alarm Triggered (Break-in)',
            device: 'Front Motion Sensor'
        },
        {
            time: '04:15 PM',
            severity: 'Warning',
            event: 'Garage Door Sensor Damage Detected',
            device: 'Garage Door Sensor'
        },
        {
            time: '04:00 PM',
            severity: 'Warning',
            event: 'Front Door Left Open',
            device: 'Front Door'
        },
        {
            time: '03:30 PM',
            severity: 'Info',
            event: 'System Disarmed',
            device: 'User - John Doe'
        },
        {
            time: '03:00 PM',
            severity: 'Info',
            event: 'Thermostat Set to 70°F',
            device: 'Main Thermostat'
        }
    ];

    /**
     * Renders a single row in the alerts table
     * @param {object} alert - The alert data object
     */
    function renderAlertRow(alert) {
        const row = alertsTableBody.insertRow();

        // Determine CSS class based on severity
        let severityClass = 'info';
        if (alert.severity === 'Critical') {
            severityClass = 'critical';
        } else if (alert.severity === 'Warning') {
            severityClass = 'warning';
        }

        // Cell 1: Time
        row.insertCell().textContent = alert.time;

        // Cell 2: Severity (as a styled tag)
        row.insertCell().innerHTML = `<span class="tag tag-${severityClass}">${alert.severity}</span>`;

        // Cell 3: Event
        row.insertCell().textContent = alert.event;

        // Cell 4: Device / Zone
        row.insertCell().textContent = alert.device;

        // Add a class to the row for visual highlighting if it's a critical/unacknowledged event
        if (severityClass !== 'info') {
            row.classList.add(`alert-row-${severityClass}`);
        }
    }

    // Populate the table on load
    alertsData.forEach(renderAlertRow);

    // Acknowledge All Button functionality
    acknowledgeBtn.addEventListener('click', () => {
        // Clear the table visually
        alertsTableBody.innerHTML = '';

        // Optionally, add a placeholder row
        const row = alertsTableBody.insertRow();
        row.insertCell().textContent = '--';
        row.insertCell().innerHTML = '<span class="tag tag-info">CLEARED</span>';
        row.insertCell().textContent = 'All alerts acknowledged.';
        row.insertCell().textContent = 'System';

        alert('All alerts have been acknowledged.');
        // In a real application, this would trigger an API call to mark all events as read.
    });
});