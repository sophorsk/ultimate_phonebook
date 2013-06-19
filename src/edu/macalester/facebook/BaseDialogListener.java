package edu.macalester.facebook;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * Skeleton base class for RequestListeners, providing default error 
 * handling. Applications should handle these error conditions.
 *
 */
public abstract class BaseDialogListener implements DialogListener {
	/**
	 * Facebook error
	 */
    @Override
	public void onFacebookError(FacebookError e) {
        e.printStackTrace();
    }
    /**
     * Dialog error
     */
    @Override
	public void onError(DialogError e) {
        e.printStackTrace();        
    }
    /**
     * Cancel
     */
    @Override
	public void onCancel() {        
    }
    
}
