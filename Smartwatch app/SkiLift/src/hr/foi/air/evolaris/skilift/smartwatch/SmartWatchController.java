package hr.foi.air.evolaris.skilift.smartwatch;

import hr.foi.air.evolaris.skilift.LiftDataManager;
import hr.foi.air.evolaris.skilift.R;

import java.lang.reflect.Constructor;
import java.util.Stack;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlListItem;
import com.sonyericsson.extras.liveware.extension.util.control.ControlObjectClickEvent;

//Main class for controlling and changing controls on smartwatch
//Controls is presented like functional user interface on smartwatch
//Intents is used for changing between controls/ui-s
public class SmartWatchController extends SmartWatchControllerBase {

	private Stack<Intent> mControlStack;
	LiftDataManager liftDataManager = LiftDataManager.getInstance();

	// Constructor
	public SmartWatchController(Context context, String packageName,
			Intent intent) {
		super(context, packageName);
		mControlStack = new Stack<Intent>();
		// Create an initial control extension
		mCurrentControl = createControl(intent);
		// Set Screen state to be turned on all the time when extension is
		// opened
		setScreenState(Control.Intents.SCREEN_STATE_ON);
	}

	// Getter for smartwatch width
	public static int getSupportedControlWidth(Context context) {
		return context.getResources().getDimensionPixelSize(
				R.dimen.smart_watch_2_control_width);
	}

	// Getter for smartwatch height
	public static int getSupportedControlHeight(Context context) {
		return context.getResources().getDimensionPixelSize(
				R.dimen.smart_watch_2_control_height);
	}

	// ///////////////////////////////////////////////////////////////////////
	// Abstract methods from ControlExtension
	// ////////////////////////////////////////////////////////////////////////
	@Override
	public void onRequestListItem(int layoutReference, int listItemPosition) {
		if (mCurrentControl != null) {
			mCurrentControl
					.onRequestListItem(layoutReference, listItemPosition);
		}
	}

	@Override
	public void onListItemClick(ControlListItem listItem, int clickType,
			int itemLayoutReference) {
		if (mCurrentControl != null) {
			mCurrentControl.onListItemClick(listItem, clickType,
					itemLayoutReference);
		}
	}

	@Override
	public void onListItemSelected(ControlListItem listItem) {
		if (mCurrentControl != null) {
			mCurrentControl.onListItemSelected(listItem);
		}
	}

	@Override
	public void onListRefreshRequest(int layoutReference) {
		if (mCurrentControl != null) {
			mCurrentControl.onListRefreshRequest(layoutReference);
		}
	}

	@Override
	public void onObjectClick(ControlObjectClickEvent event) {
		if (mCurrentControl != null) {
			mCurrentControl.onObjectClick(event);
		}
	}

	@Override
	public void onKey(int action, int keyCode, long timeStamp) {
		if (action == Control.Intents.KEY_ACTION_RELEASE
				&& keyCode == Control.KeyCodes.KEYCODE_BACK) {
			onBack();
		} else if (mCurrentControl != null) {
			super.onKey(action, keyCode, timeStamp);
		}
	}

	@Override
	public void onMenuItemSelected(int menuItem) {
		if (mCurrentControl != null) {
			mCurrentControl.onMenuItemSelected(menuItem);
		}
	}

	// ///////////////////////////////////////////////////////////////////////
	// Abstract methods from ControlExtension
	// ////////////////////////////////////////////////////////////////////////

	// When back is pressed this callback is called
	// Closes the currently open control extension. If there is a control on the
	// back stack it is opened, otherwise extension is closed.
	public void onBack() {
		Log.v(SmartWatchService.LOG_TAG, "onBack");
		if (!mControlStack.isEmpty()) {
			Intent backControl = mControlStack.pop();
			ControlExtension newControl = createControl(backControl);
			startControl(newControl);
		} else {
			stopRequest();
		}
	}

	// Method that is used when control/ui is started
	public void startControl(Intent intent) {
		// addCurrentToControlStack();
		ControlExtension newControl = createControl(intent);
		
		startControl(newControl);
	}

	// Method that is called from start control and
	// Method that creates new control/ui using given intent
	public ControlExtension createControl(Intent intent) {
		ComponentName component = intent.getComponent();
		String className = component.getClassName();

		UserInterface object = getObjectInstance(intent, className);
		object.setData(liftDataManager.getLiftDataWatch());

		if (object instanceof ListControlManager) {
			((ListControlManager) object).setLayout();
			return (ListControlManager) object;
		} else if (object instanceof ChartControlManager) {
			return (ChartControlManager) object;
		}
		return null;
	}

	// Method that returns instance of class that implements UserInterface
	// interface
	// We can get instance of object from given intent
	private UserInterface getObjectInstance(Intent intent, String className) {
		try {

			Class<?> classObject = Class.forName(className);
			Constructor<?> ctor = classObject.getConstructor(Context.class,
					String.class, Intent.class);
			// Return null if class not exists
			if (ctor == null) {
				return null;
			}
			// return new instance of Control Class (ui class for smartwatch)
			return (UserInterface) ctor.newInstance(new Object[] { mContext,
					mHostAppPackageName, intent });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}