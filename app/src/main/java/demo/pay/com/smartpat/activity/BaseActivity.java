package demo.pay.com.smartpat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Stack;

import demo.pay.com.smartpat.R;
import demo.pay.com.smartpat.utility.AppConstant;
import demo.pay.com.smartpat.utility.AppLog;


public class BaseActivity extends AppCompatActivity {
    HashMap<String, Stack<Fragment>> fragmentStack;
    String className = this.getClass().getSimpleName();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fragmentStack = new HashMap<String, Stack<Fragment>>();
        fragmentStack.put(AppConstant.FRAG_HOME, new Stack<Fragment>());
    }

    public void removeFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            AppLog.d(className, "Remove fragment,null fragment");
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
        AppLog.d(className, "Fragment removed tag" + tag);
    }

    public void clearStack() {
        int stackSize = fragmentStack.get(AppConstant.FRAG_HOME).size();

        if (stackSize > 0) {
            fragmentStack.get(AppConstant.FRAG_HOME).clear();
        }
    }

    public void popUpToLast(int main_container) {
        Fragment fragment = fragmentStack.get(AppConstant.FRAG_HOME).firstElement();
        clearStack();
        pushFragments(AppConstant.FRAG_HOME, fragment, main_container);

    }

    public void pushFragments(String tag, Fragment fragment, int main_container) {
        fragmentStack.get(AppConstant.FRAG_HOME).push(fragment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        //ft.addToBackStack(null);
        ft.add(main_container, fragment, fragment.getClass().getSimpleName());
        ft.commit();
    }


}
