import jade.core.Agent;

public class PlatformAgent extends Agent {
    private boolean isAvailable = true;  // True if the platform is free, false otherwise

    @Override
    protected void setup() {
        System.out.println("Platform " + getLocalName() + " is ready.");
    }

    // Method to check if the platform is available
    public boolean isAvailable() {
        return isAvailable;
    }

    // Method to mark the platform as occupied (train is arriving)
    public void occupy() {
        isAvailable = false;
    }

    // Method to clear the platform (train departs)
    public void clear() {
        isAvailable = true;
    }
}
