package cn.redinfo.chenzhi.Fantasy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 13-1-8
 * Time: 下午2:10
 */
public class EditDialog extends AlertDialog {
  private EditText inputBox = null;
  private OnEditorFinishedListener listener = null;

  public void setOnEditorFinishedListener(OnEditorFinishedListener listener) {
    this.listener = listener;
  }

  public String getValue() {
    return this.inputBox.getText().toString();
  }

  public void setValue(String value) {
    this.inputBox.setText(value);
  }

  public EditDialog(Context context) {
    super(context);
    this.inputBox = new EditText(context);
    this.setIcon(android.R.drawable.ic_dialog_info);
    this.setView(this.inputBox);
    this.setButton(BUTTON1, "取消", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        if (listener != null)
          listener.onEditorFinished(false, null);
      }
    });

    this.setButton(BUTTON2, "完成", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        if (listener != null)
          listener.onEditorFinished(true, inputBox.getText().toString());
      }
    });
  }
}
