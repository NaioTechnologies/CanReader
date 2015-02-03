package com.naio.canreader.canframeclasses;
import java.text.DecimalFormat;
import java.util.List;
import com.naio.canreader.R;
import com.naio.canreader.utils.BytesFunction;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
* Display the data of the Brain
*
* @author bodereau
*
*/
public class BrainCanFrame extends CanFrame {
private Integer temperature
private View rl_second_layout;
public BrainCanFrame(int id, int dlc, List<Integer> data, Double time) {
super(id, dlc, data);
this.type = "BRAIN";
}
public BrainCanFrame() {
this.type = "BRAIN";
}
public BrainCanFrame setParams(int id, int dlc, List<Integer> data) {
super.setParams(id, dlc, data);
return this;
}
public void action(RelativeLayout rl, ViewPager vp) {
if (vp != null) {
this.rl_second_layout = (RelativeLayout) vp.getChildAt(0).findViewById(
R.id.rl_imu_activity);
}
switch (idMess) {
case "1110":
display_data_temperature(rl);
break;
default:
break;
}
}

/**
* @param rl
*/
private void display_data_temperature(RelativeLayout rl) {
temperature = new UInt8(getData().get(3));
if (rl == null) {
if (rl_second_layout == null) {
return;
}
((TextView) rl_second_layout.findViewById(R.id.temperature_cpu)).setText(""
+ temperature.toString());
return;
}
((TextView) rl.findViewById(R.id.temperature_cpu)).setText("" + temperature.toString);
}

}
