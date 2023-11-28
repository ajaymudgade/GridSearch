package nurasoftech.gridsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText rowsEditText, columnsEditText, alphabetEditText, searchTextEditText;
    private Button createGridButton, searchButton, resetButton;
    private GridLayout gridLayout;
    private char[][] grid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rowsEditText = findViewById(R.id.rowsEditText);
        columnsEditText = findViewById(R.id.columnsEditText);
        alphabetEditText = findViewById(R.id.alphabetEditText);
        searchTextEditText = findViewById(R.id.searchTextEditText);
        createGridButton = findViewById(R.id.createGridButton);
        searchButton = findViewById(R.id.searchButton);
        resetButton = findViewById(R.id.resetButton);
        gridLayout = findViewById(R.id.gridLayout);

        createGridButton.setOnClickListener(v -> createGrid());
        searchButton.setOnClickListener(v -> searchWord());
        resetButton.setOnClickListener(v -> reset());
    }

    private void createGrid() {
        int rows = Integer.parseInt(rowsEditText.getText().toString());
        int columns = Integer.parseInt(columnsEditText.getText().toString());
        String alphabets = alphabetEditText.getText().toString();

        int row = rows;
        int col = columns;
        int len = row * col;

        if (rows > 0 && columns > 0 && alphabets.length() == (rows * columns)) {
            gridLayout.removeAllViews();

            gridLayout.setColumnCount(columns);
            gridLayout.setRowCount(rows);

            grid = new char[rows][columns];

            int index = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    grid[i][j] = alphabets.charAt(index++);
                    TextView textView = new TextView(this);
                    textView.setText(String.valueOf(grid[i][j]));
                    textView.setTextColor(Color.GREEN);
                    textView.setTextSize(18);

                    int padding = 10; // You can adjust the padding size
                    textView.setPadding(padding, padding, padding, padding);

                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    params.setMargins(padding, padding, padding, padding);

                    textView.setLayoutParams(params);
                    gridLayout.addView(textView);
                }
            }
            Toast.makeText(this, "Grid created successfully", Toast.LENGTH_SHORT).show();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.CENTER;
            gridLayout.setLayoutParams(params);

        } else {
            Toast.makeText(this, "Invalid input for grid creation", Toast.LENGTH_SHORT).show();

            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.customdialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Button ok = dialog.findViewById(R.id.button1);
            TextView lengthTV = dialog.findViewById(R.id.lengthTV);
            lengthTV.setText(String.valueOf(len));
            dialog.setCancelable(false);
            ok.setOnClickListener(v13 -> {
                dialog.dismiss();
            });

            dialog.show();
        }
    }

    private void searchWord() {
        String searchText = searchTextEditText.getText().toString();

        if (grid == null || searchText.isEmpty()) {
            Toast.makeText(this, "Grid not created or search text is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        int rows = grid.length;
        int columns = grid[0].length;

        resetTextColor();

        boolean found = searchAllDirections(searchText, rows, columns);

        if (!found) {
            Toast.makeText(this, "Search text not found in the grid", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Search text found in the grid", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean searchAllDirections(String searchText, int rows, int columns) {
        boolean found = searchLeftToRight(searchText, rows, columns)
                || searchTopToBottom(searchText, rows, columns)
                || searchDiagonalSouthEast(searchText, rows, columns);
        return found;
    }

    private boolean searchLeftToRight(String searchText, int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= columns - searchText.length(); j++) {
                boolean found = true;
                for (int k = 0; k < searchText.length(); k++) {
                    if (grid[i][j + k] != searchText.charAt(k)) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    highlightWord(i, j, searchText.length(), columns);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean searchTopToBottom(String searchText, int rows, int columns) {
        for (int i = 0; i <= rows - searchText.length(); i++) {
            for (int j = 0; j < columns; j++) {
                boolean found = true;
                for (int k = 0; k < searchText.length(); k++) {
                    if (grid[i + k][j] != searchText.charAt(k)) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    highlightWord(i, j, searchText.length(), columns, true);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean searchDiagonalSouthEast(String searchText, int rows, int columns) {
        for (int i = 0; i <= rows - searchText.length(); i++) {
            for (int j = 0; j <= columns - searchText.length(); j++) {
                boolean found = true;

                for (int k = 0; k < searchText.length(); k++) {
                    if (grid[i + k][j + k] != searchText.charAt(k)) {
                        found = false;
                        break;
                    }
                }

                if (found) {
                    highlightWord(i, j, searchText.length(), columns + 1, true);
                    return true;
                }
            }
        }
        return false;
    }

    private void highlightWord(int row, int col, int length, int columns) {
        highlightWord(row, col, length, columns, false);
    }

    private void highlightWord(int row, int col, int length, int columns, boolean vertical) {
        for (int k = 0; k < length; k++) {
            int index = vertical ? ((row + k) * columns) + col : (row * columns) + (col + k);

            if (index < gridLayout.getChildCount() && index >= 0) {
                TextView textView = (TextView) gridLayout.getChildAt(index);
                if (textView != null) {
                    textView.setTextColor(Color.RED);
                }
            }
        }
    }

    private void resetTextColor() {
        int childCount = gridLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = gridLayout.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.GREEN);
            }
        }
    }

    private void reset() {
        gridLayout.removeAllViews();
        rowsEditText.getText().clear();
        columnsEditText.getText().clear();
        alphabetEditText.getText().clear();
        searchTextEditText.getText().clear();
    }
}