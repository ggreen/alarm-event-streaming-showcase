package showcase.alarm.domains;

/**
 * Activity details
 * @param id the unique
 * @param icon the icon
 * @param time the time of the activities
 * @param activity the activity notes
 */
public record Activity(String id,
                       String icon,
                       String time,
                       String activity) {
}
