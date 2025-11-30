document.addEventListener('DOMContentLoaded', () => {
    const alertsTableBody = document.querySelector('#alerts-table tbody');
    const activityTableBody = document.querySelector('#activity-table tbody');

    // --- DATA FOR ALERTS TABLE (Severity-based) ---
    const alertsData = [
        { level: 'Critical', time: '07:22 PM', event: 'Break-in Detected (Alarm Triggered)' },
        { level: 'High', time: '07:20 PM', event: 'Garage Door Left Open for >10 min' },
        { level: 'High', time: '06:45 PM', event: 'Smoke Detector Battery Low' },
        { level: 'Medium', time: '06:05 PM', event: 'Front Door Tamper Detected' },
        { level: 'Low', time: '05:30 PM', event: 'System Offline for 5 minutes' }
    ];

    // --- DATA FOR ACTIVITY TABLE (Routine actions with Icons) ---
    const activityData = [
        { icon: 'fa-shield-alt', time: '07:15 PM', activity: 'Alarm System Turned OFF' },
        { icon: 'fa-door-open', time: '07:14 PM', activity: 'Front Door Opened' },
        { icon: 'fa-door-closed', time: '07:14 PM', activity: 'Front Door Closed' },
        { icon: 'fa-temperature-low', time: '06:55 PM', activity: 'Thermostat Set to 68°F (Cool)' },
        { icon: 'fa-door-open', time: '06:30 PM', activity: 'Garage Door Opened' },
        { icon: 'fa-door-closed', time: '06:31 PM', activity: 'Garage Door Closed' },
        { icon: 'fa-shield-alt', time: '06:00 PM', activity: 'Alarm System Turned ON (Away)' },
        { icon: 'fa-temperature-high', time: '05:45 PM', activity: 'Thermostat Set to 72°F (Heat)' },
        { icon: 'fa-box', time: '05:00 PM', activity: 'Refrigerator Door Ajar' },
        { icon: 'fa-box', time: '05:01 PM', activity: 'Refrigerator Door Closed' }
    ];

    /** Renders the Alerts table */
    function renderAlerts() {
        alertsData.forEach(alert => {
            const row = alertsTableBody.insertRow();
            const level = alert.level.toLowerCase();

            // Cell 1: Level Tag
            row.insertCell().innerHTML = `<span class="level-tag level-${level}">${alert.level}</span>`;

            // Cell 2: Time
            row.insertCell().textContent = alert.time;

            // Cell 3: Event
            row.insertCell().textContent = alert.event;

            // Optional: Highlight entire row for Critical alerts
            if (level === 'critical') {
                row.style.backgroundColor = 'rgba(220, 53, 69, 0.2)';
            }
        });
    }

    /** Renders the Activity table */
    function renderActivity() {
        activityData.forEach(activity => {
            const row = activityTableBody.insertRow();

            // Cell 1: Icon
            row.insertCell().innerHTML = `<div class="activity-icon"><i class="fas ${activity.icon}"></i></div>`;

            // Cell 2: Time
            row.insertCell().textContent = activity.time;

            // Cell 3: Activity Description
            row.insertCell().textContent = activity.activity;
        });
    }

    renderAlerts();
    renderActivity();
});