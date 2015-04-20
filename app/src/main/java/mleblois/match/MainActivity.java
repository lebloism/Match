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

import lombok.Getter;
import lombok.Setter;
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

        @Getter
        @Setter
        private class GameState {
            private Integer currentPanelItemPosition;
            private Integer currentLineItemPosition;
        }

        private GameState gameState = new GameState();

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
            panelAdapter = getNewPanelItemAdapter();
            gridview.setAdapter(panelAdapter);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    onPanelItemClick(position);

                    if (hasWon()){
                        onGameWon();

                    }


                    lineAdapter.notifyDataSetChanged();
                    panelAdapter.notifyDataSetChanged();


                }
            });


            GridView list = (GridView) rootView.findViewById(R.id.list);
            lineAdapter = getNewLineItemAdapter();
            list.setAdapter(lineAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    onLineItemClick(position);

                    if (hasWon()){
                        onGameWon();

                    }

                    lineAdapter.notifyDataSetChanged();
                    panelAdapter.notifyDataSetChanged();

                }
            });

            return rootView;
        }

        private boolean hasWon(){
            return lineAdapter.isAllMatched();
        }

        private void onGameWon() {
            Toast.makeText(getActivity(), "You won ! New game ...",
                    Toast.LENGTH_SHORT).show();
            panelAdapter.initializeItems();
            lineAdapter.initializeItems();
        }

        private PanelItemAdapter getNewLineItemAdapter() {
            return new PanelItemAdapter(getActivity(),5);
        }

        private PanelItemAdapter getNewPanelItemAdapter() {
            return new PanelItemAdapter(getActivity(),25);
        }

        private void onPanelItemClick(int panelItemPosition){
            onClick(panelAdapter, panelItemPosition,  lineAdapter, gameState, false);
        }

        private void onLineItemClick(int lineItemPosition){
            onClick(lineAdapter, lineItemPosition, panelAdapter, gameState, true);
        }


        private void onClick(PanelItemAdapter adapter, int position, PanelItemAdapter otherAdapter, GameState gameState, boolean clickOnLine) {
            PanelItem item = (PanelItem) adapter.getItem(position);
            if (item.getState() == ItemState.NORMAL) {
                Integer otherBoardWaiting = clickOnLine ? gameState.currentPanelItemPosition : gameState.currentLineItemPosition;
               Integer previousBoardWaiting = clickOnLine ? gameState.currentLineItemPosition : gameState.currentPanelItemPosition;
                if (otherBoardWaiting == null) {
                    if (previousBoardWaiting != null) {
                        ((PanelItem) adapter.getItem(previousBoardWaiting)).setState(ItemState.NORMAL);
                    }
                    item.setState(ItemState.WAITING);
                    if (clickOnLine){
                        gameState.setCurrentLineItemPosition(position);
                    } else {
                        gameState.setCurrentPanelItemPosition(position);
                    }

                } else {

                    PanelItem otherBoardWaitingItem = (PanelItem) otherAdapter.getItem(otherBoardWaiting);
                    if (match(item, otherBoardWaitingItem)) {
                        otherBoardWaitingItem.setState(ItemState.MATCHED);

                        ((PanelItem) adapter.getItem(position)).setState(ItemState.MATCHED);


                        gameState.setCurrentLineItemPosition(null);
                        gameState.setCurrentPanelItemPosition(null);



                    }
                }
            } else if (item.getState() == ItemState.WAITING) {

                //On annule le WAITING
                item.setState(ItemState.NORMAL);
                if (clickOnLine){
                    gameState.setCurrentLineItemPosition(null);
                } else {
                    gameState.setCurrentPanelItemPosition(null);
                }

            }
        }


        private boolean match(PanelItem lineItem, PanelItem panelItem){
           return lineItem.getDrawable().equals(panelItem.getDrawable());
        }
    }
}
