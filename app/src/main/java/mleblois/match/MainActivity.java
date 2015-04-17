package mleblois.match;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import mleblois.match.adapter.PanelItemAdapter;
import mleblois.match.model.ItemState;
import mleblois.match.model.PanelItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public static class PlaceholderFragment extends Fragment {


        private PanelItemAdapter panelAdapter;

        private PanelItemAdapter lineAdapter;

        private Integer currentPanelItemPosition;
        private Integer currentLineItemPosition;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
            panelAdapter = new PanelItemAdapter(getActivity(),25);
            gridview.setAdapter(panelAdapter);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    onPanelItemClick(position);
                    lineAdapter.notifyDataSetChanged();
                    panelAdapter.notifyDataSetChanged();
                }
            });


            GridView list = (GridView) rootView.findViewById(R.id.list);
            lineAdapter = new PanelItemAdapter(getActivity(),5);
            list.setAdapter(lineAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    onLineItemClick(position);
                    lineAdapter.notifyDataSetChanged();
                    panelAdapter.notifyDataSetChanged();

                }
            });

            return rootView;
        }

        private void onPanelItemClick(int panelItemPosition){
            Toast.makeText(getActivity(), "" + panelItemPosition,
                    Toast.LENGTH_SHORT).show();
            PanelItem item =  (PanelItem)  panelAdapter.getItem(panelItemPosition);

            if (item.getState() == ItemState.NORMAL){
                //C'est cet item qui devient l'élément courant dans le panel
                if (currentPanelItemPosition!=null) {
                    ((PanelItem) panelAdapter.getItem(currentPanelItemPosition)).setState(ItemState.NORMAL);
                }

                item.setState(ItemState.WAITING);
                currentPanelItemPosition = panelItemPosition;
                associateIfMatch();

            } else if (item.getState() == ItemState.WAITING){
                //On clique sur l'élément déjà courant
                item.setState(ItemState.NORMAL);
                //On le dé-courantise
                currentPanelItemPosition = null;
            }

                //Si élément déjà associé : ne rien faire



        }

        private void onLineItemClick(int lineItemPosition){
            Toast.makeText(getActivity(), "" + lineItemPosition,
                    Toast.LENGTH_SHORT).show();

            PanelItem item =  (PanelItem)  lineAdapter.getItem(lineItemPosition);
            if (item.getState() == ItemState.NORMAL){
                //C'est cet item qui devient l'élément courant dans la ligne
                if (currentLineItemPosition!=null) {
                    ((PanelItem) lineAdapter.getItem(currentLineItemPosition)).setState(ItemState.NORMAL);
                }
                item.setState(ItemState.WAITING);
                currentLineItemPosition = lineItemPosition;
                associateIfMatch();

            } else if (item.getState() == ItemState.WAITING){
                //On clique sur l'élément déjà courant
                item.setState(ItemState.NORMAL);
                currentLineItemPosition = null;
            }
            //Si élément déjà associé : ne rien faire


        }

        private void associateIfMatch(){
            if (currentLineItemPosition!=null && currentPanelItemPosition!=null){
                PanelItem panelItem = (PanelItem) panelAdapter.getItem(currentPanelItemPosition);
                PanelItem lineItem = (PanelItem) lineAdapter.getItem(currentLineItemPosition);
                if (match(lineItem, panelItem)){
                    lineItem.setState(ItemState.MATCHED);
                    panelItem.setState(ItemState.MATCHED);
                    currentPanelItemPosition = null;
                    currentLineItemPosition = null;
                }
            }

        }

        private boolean match(PanelItem lineItem, PanelItem panelItem){
           return lineItem.getDrawable().equals(panelItem.getDrawable());
        }
    }
}
