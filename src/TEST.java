class ContinuousStatusUpdate {

    private static boolean running = true;

    public static void main(String[] args) {
        // Start the updateStatus method in a new thread
        Thread statusThread = new Thread(ContinuousStatusUpdate::updateStatus);
        statusThread.start();

        // Main method logic
        System.out.println("Main method is running...");
        try {
            // the whole method of our project
            Thread.sleep(10000); // mani lidhu ke main method 10 second chalse
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop the updateStatus method
        running = false;

        try {
            statusThread.join(); // Ensure the status thread has finished before exiting
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main method has completed.");
    }

    private static void updateStatus() {
        while (running) {
            System.out.println("Status is being updated...");
            try {
                Thread.sleep(1000); // Update status every second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Status update has stopped.");
    }
}
