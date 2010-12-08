package org.joldersma.damien.DreamSpell;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joldersma.damien.DreamSpell.SessionEvents.AuthListener;
import org.joldersma.damien.DreamSpell.SessionEvents.LogoutListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class Friends extends ListActivity {

	public static final String TAG = "Friends";
	
	public static final String APP_ID = "32395165793";
	private Facebook mFacebook;
    private AsyncFacebookRunner mAsyncRunner;
	
	private ListView mFriendList;
	FriendListCursorAdapter friendListCursorAdapter;
	
	private LoginButton mLoginButton;
	
	private List<Map<String, String>> friendsData;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        
		mFacebook = new Facebook(APP_ID);
       	mAsyncRunner = new AsyncFacebookRunner(mFacebook);
       	
        SessionStore.restore(mFacebook, this);
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
        mLoginButton = (LoginButton) findViewById(R.id.login);
        mLoginButton.init(this, mFacebook);

        // Obtain handles to UI objects
        //mFriendList = (ListView) findViewById(R.id.);
        
        
//        mLoginButton.setVisibility(mFacebook.isSessionValid() ?
//                View.INVISIBLE :
//                View.VISIBLE);

        
        if ( mFacebook.isSessionValid() )
        {
        	// Populate the contact list
        	Log.d(TAG,"onCreate valid session, going to populate");
        	populateContactList();
        }
             
    }

	/**
     * Populate the contact list based on account currently selected in the account spinner.
	 * @param friendsData 
     */
    private void populateContactList() {
    	 Log.d(TAG,"populateContactList begin");
    	 
    	 if ( friendsData == null )
    	 {
    		 Log.d(TAG,"Going to skip populate, friendsData is null");
    		 return;
    	 }
    	 
    	// mLoginButton.setVisibility(View.INVISIBLE);
    	 
        // Build adapter with contact entries
    	// Cursor cursor = getContacts();
    	
    	/*
    	Cursor cursor = getAllContactData(0);
        String[] fields = new String[] {
                ContactsContract.Data.DISPLAY_NAME,
                Event.START_DATE,
                ContactsContract.Contacts.PHOTO_ID
        };
        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.friend_view, cursor,
        //        fields, new int[] {R.id.friendViewText, R.id.friendViewBirthDay});
        friendListCursorAdapter = new FriendListCursorAdapter(this, R.layout.friend_view, cursor,
                fields, new int[] {R.id.friendViewText, R.id.friendViewBirthDay, R.id.friendViewImage});
        //mFriendList.setAdapter(adapter);
        setListAdapter(friendListCursorAdapter);
        */
  
//	  	List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
//    	Map<String, String> group;
//    	group = new HashMap<String, String>();
//        group.put("name", "damien");
//        group.put("birthday", "022277");
//        group.put("picture", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs338.snc4/41773_775949236_4548_q.jpg");
//    	groupData.add(group);
//    	
//    	group = new HashMap<String, String>();
//        group.put("name", "damien1");
//        group.put("birthday", "022278");
//        group.put("picture", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs338.snc4/41773_775949236_4548_q.jpg");
//        groupData.add(group);
//    	 
//    	group = new HashMap<String, String>();
//        group.put("name", "damien2");
//        group.put("birthday", "022279");
//        group.put("picture", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs338.snc4/41773_775949236_4548_q.jpg");
//        groupData.add(group);
    	 
//    	SimpleAdapter simpleAdapter = new SimpleAdapter(this,groupData,R.layout.friend_view, new String[] { "name","birthday"},
//                new int[]{ R.id.friendViewText, R.id.friendViewBirthDay });
//    	setListAdapter(simpleAdapter);
    	
    	FriendListFacebookAdapter friendListCursorAdapter = new FriendListFacebookAdapter(this, friendsData, R.layout.friend_view,
        	new String[] { "name","birthday","picture"}, new int[] {R.id.friendViewText, R.id.friendViewBirthDay, R.id.friendViewImage});
    	setListAdapter(friendListCursorAdapter);
        
        //Cursor c = getAllContactData(0);
        //getColumnData(c);
        /*
       if (cursor.moveToFirst()) 
       {
    	   Log.d(TAG,"Yes second pass through cursor");
    	   do {
    		   long contactId = cursor.getLong(cursor.getColumnIndex(Data._ID));
    		   String name = cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME));
    		   //Log.d(TAG,"Getting contact data for " + contactId + " " + name);
    		   Cursor c = getAllContactData(contactId);
    		   getColumnData(c);
    	   } while (cursor.moveToNext());
       }
       else
       {
    	   Log.d(TAG,"No second pass through cursor");
       }
       */
    }

    private Cursor getAllContactData(long contactId)
    {
//    	Cursor c = getContentResolver().query(Data.CONTENT_URI,
//  	          new String[] {Data._ID, Phone.NUMBER, Phone.TYPE, Phone.LABEL},
//  	          Data.CONTACT_ID + "=?" + " AND "
//  	                  + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'",
//  	          new String[] {String.valueOf(contactId)}, null);
//    	return c;
    	Cursor c = getContentResolver().query(Data.CONTENT_URI,
    	          new String[] {Data._ID, Event.START_DATE, Event.TYPE, Event.LABEL, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_ID},
    	          Data.MIMETYPE + "='" + Event.CONTENT_ITEM_TYPE + "'",
    	          null, null);
    	          //Data.CONTACT_ID + "=?" + " AND "
    	          //        + Data.MIMETYPE + "='" + Event.CONTENT_ITEM_TYPE + "'",
    	          //new String[] {String.valueOf(contactId)}, null);
      	return c;
    }
    
    private void getColumnData(Cursor cur){ 
        if (cur.moveToFirst()) {

            String birthDay; 
            String label; 
            int birthdayColumn = cur.getColumnIndex(Event.START_DATE); 
            int labelColumn = cur.getColumnIndex(Event.LABEL);
           
        
            do {
                // Get the field values
            	birthDay = cur.getString(birthdayColumn);
            	label = cur.getString(labelColumn);
               
                // Do something with the values. 
                Log.d(TAG, "birthday: " + birthDay + " label: " + label);
                
                
                
            } while (cur.moveToNext());

        }
    }
    
    public static final String KEY_DATE = "date";
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position,  id);
        Log.d(TAG,"onListItemClick: click, id is " + id);
        
        Date birthday = friendListCursorAdapter.getBirthdays().get(String.valueOf(id));
        Log.d(TAG,"onListItemClick: putting birthday into extra - " + birthday);
        
        Intent i = new Intent(this, DreamSpell.class);
        i.putExtra(KEY_DATE, birthday);
        startActivity(i);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
    	Log.d(TAG,String.format("onActivityResult begin requestCode=%s, resultCode=%s",requestCode,resultCode));
        mFacebook.authorizeCallback(requestCode, resultCode, data);
        Log.d(TAG,String.format("onActivityResult end requestCode=%s, resultCode=%s",requestCode,resultCode));
    }

    
    
    
    public class SampleFriendsListener extends BaseRequestListener {

        public void onComplete(final String response) {
            try {
                // process the response here: executed in background thread
                Log.d("Facebook-Example", "Response: " + response.toString());
//                Log.d("Facebook-Example", "*************** BEGIN Response Formated *******");
//                Log.d("Facebook-Example", response.replace(",", ",\n"));
//                Log.d("Facebook-Example", "*************** END Response Formated *******");
                
                Log.d("Facebook-Example", "*************** BEGIN PARSE *******");
        	  	friendsData = new ArrayList<Map<String, String>>();
            	Map<String, String> friend;
            	
                
                JSONObject json = Util.parseJson(response);
      
                JSONArray data = json.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {                	
					Log.d(TAG,String.format("data %s",i));

					JSONObject d = data.getJSONObject(i);
					String name = d.getString("name");
					String birthday = null;
					try { birthday = d.getString("birthday"); } catch (Exception e) {} 
					String id = d.getString("id");
					String picture = d.getString("picture");
					
					friend = new HashMap<String, String>();
					friend.put("id", name);
	            	friend.put("name", name);
	            	friend.put("birthday", birthday);
	            	friend.put("picture", picture);
	            	
	            	if ( birthday != null )
	            		friendsData.add(friend);
					
					Log.d(TAG,String.format("data %s, name=%s, id=%s, birthday=%s, pic=%s",i,name,id,birthday,picture));
				}
                
                Log.d("Facebook-Example", "*************** END PARSE *******");
                
      
                
                // then post the processed result back to the UI thread
                // if we do not do this, an runtime exception will be generated
                // e.g. "CalledFromWrongThreadException: Only the original
                // thread that created a view hierarchy can touch its views."
                Friends.this.runOnUiThread(new Runnable() {
                    public void run() {
                    	populateContactList();
                    }
                });
                
                //final String name = "FOO";// json.getString("data[0]name");

                // then post the processed result back to the UI thread
                // if we do not do this, an runtime exception will be generated
                // e.g. "CalledFromWrongThreadException: Only the original
                // thread that created a view hierarchy can touch its views."
//                Example.this.runOnUiThread(new Runnable() {
//                    public void run() {
//                        mText.setText("Hello there, " + name + "!");
//                    }
//                });
            } catch (JSONException e) {
                Log.w("Facebook-Example", "JSON Error in response: " + e.toString());
                Log.w(TAG,e);
                e.printStackTrace();
                
            } catch (FacebookError e) {
                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
            }
        }

		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub
			Log.e(TAG,"FacebookError=" + e.toString());
			e.printStackTrace();
		}
    }
    
    public class SampleAuthListener implements AuthListener {

        public void onAuthSucceed() {
            Log.d(TAG,"You have logged in! ");
            
            //mLoginButton.setVisibility(View.INVISIBLE);

            
           
            
            Log.d(TAG,"DreamSpell now with new and improved me/friends?fields=id,name,picture,birthday!");
            Bundle params = new Bundle();
//            //String[] fields = { "id","name","picture" };
            params.putString("fields", "id,name,picture,birthday");
          //  mAsyncRunner.request("me/friends", params, new SampleFriendsListener());
            
            new Thread() {
            	  @Override public void run() {
            		  Log.d(TAG,"doing cyclone thread");
                      Bundle params = new Bundle();
                      params.putString("fields", "id,name,picture,birthday");
            		  mAsyncRunner.request("me/friends", params, new SampleFriendsListener());
            	  }
            	}.start();
    
        	// Populate the contact list
        	Log.d(TAG,"onAuthSucceed valid, going to populate");
        	populateContactList();

        }

        public void onAuthFail(String error) {
        	Log.d(TAG,"Login Failed: " + error);
        }
    }

    public class SampleLogoutListener implements LogoutListener {
        public void onLogoutBegin() {
        	Log.d(TAG,"Logging out...");
        }

        public void onLogoutFinish() {
        	Log.d(TAG,"You have logged out! ");
        }
    }
    
    public class SampleDialogListener extends BaseDialogListener {

        public void onComplete(Bundle values) {
            final String postId = values.getString("post_id");
            if (postId != null) {
                Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
                
            } else {
                Log.d("Facebook-Example", "No wall post made");
            }
        }
    }
}
