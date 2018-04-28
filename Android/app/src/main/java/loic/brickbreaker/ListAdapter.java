package loic.brickbreaker;
/**
 * Created by CollFnac on 17/04/2018.
 */


        import android.content.Context;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;

        import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by etiennepayet on 27/03/2018.
 */

public class ListAdapter extends ArrayAdapter<Slot> {

    private final Context context;
    public result test;
    public ListAdapter(Context context, List<Slot> values) {
        super(context, R.layout.text_layout, values);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;

        if (cellView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cellView = inflater.inflate(R.layout.text_layout, parent, false);
        }



        TextView levelView = (TextView) cellView.findViewById(R.id.Nom);
        TextView scoreView = (TextView) cellView.findViewById(R.id.Score);


        Slot t = getItem(position);
        levelView.setText(( t.nom));
        scoreView.setText((t.score));

        return cellView;
    }
}