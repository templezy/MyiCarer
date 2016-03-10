package au.edu.adelaide.sensorlog.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class FileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FileFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class FileFragment extends Fragment {

        private ArrayAdapter<String> mFileAdapter;

        public FileFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_file, container, false);

            File dir = getActivity().getExternalFilesDir(null);
            ArrayList<String> filePath = new ArrayList<String>();
            walkDir(dir, filePath);

            mFileAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.list_item_file,
                    R.id.list_item_file_textview,
                    filePath
            );

            ListView listView = (ListView) rootView.findViewById(R.id.listview_files);
            listView.setAdapter(mFileAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String fileName = mFileAdapter.getItem(position);
                    Uri fileUri = Uri.fromFile(getActivity().getExternalFilesDir(null));
                    fileUri = Uri.withAppendedPath(fileUri, fileName);
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    startActivity(Intent.createChooser(sharingIntent, "Share"));
                }
            });
            return rootView;
        }

        private void walkDir(File dir, ArrayList<String> filePath) {
            File listFile[] = dir.listFiles();
            if (listFile != null) {
                for (int i = 0; i < listFile.length; i++) {
                    String path = listFile[i].getPath();
                    filePath.add(path.substring(path.lastIndexOf("/") + 1));
                }
            }
        }
    }
}
