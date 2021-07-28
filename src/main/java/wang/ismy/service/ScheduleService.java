package wang.ismy.service;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class ScheduleService {
    private static final ScheduleService INSTANCE = new ScheduleService();
    private Scheduler scheduler;
    public ScheduleService() {
        SchedulerFactory sfact = new StdSchedulerFactory();
        try {
            scheduler = sfact.getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void submit(Class<? extends Job> klass, String cron) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(klass)
                .withIdentity(klass.getName(), "group1").build();

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(klass.getName(), "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public static ScheduleService getInstance() {
        return INSTANCE;
    }
}
