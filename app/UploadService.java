import android.app.Service;

public class UploadService extends Service {

    private static final String API_KEY = "YOUR_API_KEY";    private static final String VIDEO_ID = "YOUR_VIDEO_ID";

    private ApiVideoClient apiVideoClient;

    @Override
    public void onCreate() {
        super.onCreate();

        apiVideoClient = new ApiVideoClient(API_KEY);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get the video file from the Intent
        File videoFile = (File) intent.getExtras().get("videoFile");

        // Upload the video file
        upload(videoFile);

        // Stop the service
        stopSelf();

        return START_NOT_STICKY;
    }

    private void upload(File videoFile) {
        // Upload the video file to api.video
        Video video = apiVideoClient.upload(videoFile);

        // Show a Toast message indicating the status of the upload
        Toast.makeText(this, "Video uploaded successfully", Toast.LENGTH_SHORT).show();
    }
}