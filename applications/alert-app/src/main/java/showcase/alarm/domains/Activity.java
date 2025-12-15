package showcase.alarm.domains;

/**
 * Activity details
 * @param id the unique
 * @param account the account owner
 * @param icon the icon
 * @param time the time of the activities
 * @param activity the activity notes
 */
public record Activity(String id,
                       String account,
                       String icon,
                       String time,
                       String activity) {
}
