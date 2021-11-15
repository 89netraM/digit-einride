package api;

import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

/**
 * Allows for interfacing with the donkeycar API.
 */
public class DonkeyCar {

    private static boolean isOpenCVInitialized = false;
    private static final String OPENCV_ALREADY_INITIALIZED = "OpenCV has already been initialized";

    /**
     * Initializes OpenCV library bindings.
     */
    public static void initOpenCV() {
        if (isOpenCVInitialized) {
            throw new IllegalStateException(OPENCV_ALREADY_INITIALIZED);
        }

        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (UnsatisfiedLinkError e) {
            OpenCV.loadLocally();
        }

        isOpenCVInitialized = true;
    }

    /**
     * Creates stringified JSON data for controlling the car.
     *
     * @param angle    the angle of the wheels
     * @param throttle the throttle between 0 and 1
     * @return the data as a JSON string
     */
    public static String getInstructionsData(double angle, double throttle) {
        String angleProperty = getProperty("angle", angle);
        String throttleProperty = getProperty("throttle", throttle);
        return String.format("{ %s, %s }", angleProperty, throttleProperty);
    }

    /**
     * Creates an address with the given hostname, port and path.
     *
     * @param hostname the hostname
     * @param port     the port
     * @param path     the path
     * @return the address
     */
    public static String getAddress(String hostname, int port, String path) {
        return String.format("http://%s:%d%s", hostname, port, path);
    }

    /**
     * Creates a {@link VideoCapture} object from the given parameters.
     *
     * @param hostname  the hostname
     * @param port      the port
     * @param videoPath the path to the video feed
     * @return the videocapture
     */
    public static VideoCapture getVideoCapture(String hostname, int port, String videoPath) {
        return new VideoCapture(getAddress(hostname, port, videoPath));
    }

    private static String getProperty(String name, double value) {
        return String.format(Locale.ROOT, "%s: %.2f", name, value);
    }

    private static Socket getSocket(String hostname, int port) throws IOException {
        return new Socket(hostname, port);
    }

    private static PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }

    private static Mat createFrame() {
        return new Mat();
    }

    private static final String NOT_INITIALIZED = "Not initialized.";
    private static final String ALREADY_INITIALIZED = "Already initialized.";

    private static final String DEFAULT_VIDEO_PATH = "/video";
    private static final int DEFAULT_PORT = 8887;
    private static final String DEFAULT_HOSTNAME = "donkeycar";

    private final String hostname;
    private final int port;
    private final String videoPath;

    private Socket socket;
    private PrintWriter out;
    private Mat frame;

    private boolean isInitialized = false;

    /**
     * Creates an instance of the API.
     *
     * @param hostname  hostname of the car
     * @param port      port on the car
     * @param videoPath path to fetch the video from
     */
    public DonkeyCar(String hostname, int port, String videoPath) {
        this.hostname = hostname;
        this.port = port;
        this.videoPath = videoPath;
    }

    /**
     * Creates an instance of the API.
     *
     * @param hostname hostname of the car
     * @param port     port on the car
     */
    public DonkeyCar(String hostname, int port) {
        this(hostname, port, DEFAULT_VIDEO_PATH);
    }

    /**
     * Creates an instance of the API.
     *
     * @param hostname hostname of the car
     */
    public DonkeyCar(String hostname) {
        this(hostname, DEFAULT_PORT);
    }

    /**
     * Creates an instance of the API.
     */
    public DonkeyCar() {
        this(DEFAULT_HOSTNAME);
    }

    /**
     * Initializes the API instance.
     *
     * @throws IOException           when the websocket could not be created
     * @throws IllegalStateException when the instance has already been initialized
     */
    public void init() throws IOException {
        if (isInitialized) {
            throw new IllegalStateException(ALREADY_INITIALIZED);
        }

        socket = getSocket(hostname, port);
        out = getWriter(socket);
        frame = createFrame();

        try {
            initOpenCV();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        isInitialized = true;
    }

    /**
     * Deinitializes the API instance.
     *
     * @throws IOException           when the websocket could not be closed
     * @throws IllegalStateException when instance has not been initialized
     */
    public void deinit() throws IOException {
        if (!isInitialized) {
            throw new IllegalStateException(NOT_INITIALIZED);
        }

        socket.close();
        out.close();
        frame = null;

        isInitialized = false;
    }

    /**
     * Fetches a frame from the camera on the car.
     *
     * @return the frame
     */
    public Mat fetchFrame() {
        requireInitialized();

        VideoCapture video = getVideoCapture(hostname, port, videoPath);
        video.read(frame);
        return frame;
    }

    /**
     * Sends instructions to the car.
     *
     * @param angle    the angle of the wheels
     * @param throttle the throttle between 0 and 1
     */
    public void send(double angle, double throttle) {
        requireInitialized();

        out.write(getInstructionsData(angle, throttle));
    }

    private void requireInitialized() {
        if (!isInitialized) {
            throw new IllegalStateException(NOT_INITIALIZED);
        }
    }
}
