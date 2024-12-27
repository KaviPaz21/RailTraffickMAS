import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;

public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");

        ContainerController container = rt.createMainContainer(profile);

        try {

            // Create station agents with GUI
            container.createNewAgent("Station1", StationAgent.class.getName(), new Object[]{"Station1", 4}).start();
            container.createNewAgent("Station2", StationAgent.class.getName(), new Object[]{"Station2", 3}).start();
            container.createNewAgent("Station3", StationAgent.class.getName(), new Object[]{"Station3", 2}).start();
            container.createNewAgent("Station4", StationAgent.class.getName(), new Object[]{"Station4", 1}).start();
            container.createNewAgent("Station5", StationAgent.class.getName(), new Object[]{"Station5", 5}).start();


            // Create train agents
            container.createNewAgent("Train1007", TrainAgent.class.getName(), null).start();
            container.createNewAgent("Train7077", TrainAgent.class.getName(), null).start();
            container.createNewAgent("Train4501", TrainAgent.class.getName(), null).start();
            container.createNewAgent("Train5004", TrainAgent.class.getName(), null).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
