package radesh.example.twosideseekbar;

import android.content.Context;

import java.lang.reflect.Method;

public class test {
    public int getDefaultSim(Context context) {
        Object tm = context.getSystemService(Context.TELEPHONY_SERVICE);
        Method method_getDefaultSim;
        int defaultSim = -1;
        try {
            method_getDefaultSim = tm.getClass().getDeclaredMethod("getDefaultSim");
            method_getDefaultSim.setAccessible(true);
            defaultSim = (Integer) method_getDefaultSim.invoke(tm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultSim;
    }
}
