package com.envived.android.features;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.envived.android.Envived;
import com.envived.android.EnvivedFeatureDataRetrievalService;
import com.envived.android.api.AppClient;
import com.envived.android.api.Location;
import com.envived.android.api.Url;
import com.envived.android.api.agent.FactParam;
import com.envived.android.api.exceptions.EnvSocialContentException;
import com.envived.android.features.conferencerole.ConferenceRoleFeature;
import com.envived.android.features.description.BoothDescriptionFeature;
import com.envived.android.features.description.DescriptionFeature;
import com.envived.android.features.order.OrderFeature;
import com.envived.android.features.people.PeopleFeature;
import com.envived.android.features.program.ProgramFeature;
import com.envived.android.features.socialmedia.SocialMediaFeature;
import com.envived.android.utils.EnvivedAppUpdate;
import com.envived.android.utils.FeatureDbHelper;
import com.envived.android.utils.FeatureLRUEntry;
import com.envived.android.utils.FeatureLRUTracker;
import com.envived.android.utils.Preferences;
import com.envived.android.utils.ResponseHolder;
import com.envived.android.utils.Utils;

public abstract class Feature implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	
	public static final String ENVIRONMENT_TAG = "_environment_";
	public static final String AREA_TAG = "_area_";
	
	public static final String DESCRIPTION 			= 	"description";
	public static final String BOOTH_DESCRIPTION 	= 	"booth_description";
	
	public static final String ORDER 				= 	"order";
	public static final String PEOPLE 				= 	"people";
	public static final String PROGRAM 				= 	"program";
	public static final String SOCIAL_MEDIA 		= 	"social_media";
	public static final String CONFERENCE_ROLE 		= 	"conference_role";
	
	public static final String TAG 				= 	"Feature";
	public static final String SEARCH_FEATURE 	= 	"Search_Feature";
	
	public static final String RETRIEVE_CONTENT_NOTIFICATION = "retrieve_content";
	public static final String RETRIEVE_STRUCTURE_NOTIFICATION = "retrieve_structure";

	
	
	protected String category;
	protected int version;
	protected Calendar timestamp;
	protected boolean isGeneral = false;
	
	protected String featureResourceUrl;
	protected String environmentUrl;
	protected String areaUrl;
	protected boolean virtualAccess;
	
	
	/**
	 * This field will only be populated after a Data Retrieval request is made to the server. The retrieved data is then either
	 * cached locally in a SQLite database, or stored in the specific fields of classes that extend this base class.
	 */
	protected String retrievedData;
	
	/**
	 * Feature initialization will have to be performed every time an instance is created for this feature.
	 * This also mandates paying attention to a correct init / close cycle, so that no connections to DB
	 * accidentally remain open.
	 */
	private transient boolean initialized = false;
	
	protected Feature(String category, int version, Calendar timestamp, boolean isGeneral, 
			String featureResourceUrl, String environmentUrl, String areaUrl, String data, boolean virtualAccess) {
		this.category = category;
		this.version = version;		// TO BE USED IN THE FUTURE
		this.timestamp = timestamp;
		this.isGeneral = isGeneral;
		
		this.featureResourceUrl = featureResourceUrl;
		this.environmentUrl = environmentUrl;
		this.areaUrl = areaUrl;
		this.retrievedData = data;
		this.virtualAccess = virtualAccess;
		
		//this.displayThumbnail = com.envived.android.R.drawable.ic_envived_white;
		//this.displayName = "Feature";
	}
	
	public void init() throws EnvSocialContentException {
		if (hasData()) {
			String featureCacheFileName = getLocalCacheFileName(category, environmentUrl, areaUrl, version);
			
			if (retrievedData == null) {
				// check that data is not out-of-date
				FeatureLRUTracker featureLruTracker = Envived.getFeatureLRUTracker();
				
				FeatureLRUEntry featureLruEntry = null;
				if (featureLruTracker != null) {
					featureLruEntry = featureLruTracker.get(featureCacheFileName);
				}
				
				if (featureLruEntry != null) {
					Log.d(TAG, "FEATURE META DATA IN CACHE for: " + category);
					
					if (featureLruEntry.getFeatureTimestamp().compareTo(timestamp) < 0) {
						Log.d(TAG, "REFRESHING DATA for feature: " + category + ". Will start SERVER RETRIEVE.");
						
						// cached data must be refreshed
						// first remove current entry from lru tracker and "close" current feature data
						featureLruTracker.remove(featureCacheFileName);
						doClose(Envived.getContext());
						
						// afterwards start new data retrieval service
						startFeatureDataRetrievalService();
						return;
					}
					else {
						Log.d(TAG, "USING CACHED DATA for feature: " + category);
						featureInit(false);
					}
				}
				else {
					Log.d(TAG, "FEATURE META DATA IS NOT IN CACHE, but data exists for: " + category);
					featureInit(false);
				}
			}
			else {
				Log.d(TAG, "USING SERVER RETRIEVED DATA for feature: " + category);
				featureInit(true);
				
				// after initialization allow retrieved serialized data to be garbage collected
				// TODO: not sure if commenting the line below is ok, but it doesn't allow me to use the data retrieved
				//retrievedData = null;
			}
			
			
			// if feature data has been parsed correctly mark it as an entry in the feature lru tracker
			String locationUrl = environmentUrl != null ? environmentUrl : areaUrl;
			FeatureLRUEntry featureLruEntry = 
					new FeatureLRUEntry(category, featureCacheFileName, locationUrl, virtualAccess, timestamp);
			
			FeatureLRUTracker featureLruTracker = Envived.getFeatureLRUTracker();
			if (featureLruTracker != null) {
				featureLruTracker.put(featureCacheFileName, featureLruEntry);
			}
			
			initialized = true;
		}
		else {
			Log.d(TAG, "RETRIEVING DATA ANEW for feature: " + category);
			
			// start data retrieval service
			startFeatureDataRetrievalService();
		}
	}
	
	
	private void startFeatureDataRetrievalService() {
		Context context = Envived.getContext();
		String locationUri = (environmentUrl != null) ? environmentUrl : areaUrl;
		JSONObject paramsJSON = new JSONObject();
		
		ArrayList<FactParam> params = new ArrayList<FactParam>();
		params.add(new FactParam("type", Feature.RETRIEVE_CONTENT_NOTIFICATION));

		EnvivedAppUpdate appUpdate = new EnvivedAppUpdate(
				locationUri, category, featureResourceUrl, params);

		Intent updateService = new Intent(context, EnvivedFeatureDataRetrievalService.class);
		updateService.putExtra(
				EnvivedFeatureDataRetrievalService.DATA_RETRIEVE_SERVICE_INPUT,
				appUpdate);

		context.startService(updateService);
	}
	
	
	public void doUpdate() throws EnvSocialContentException {
		if (hasData()) {
			featureUpdate();
			
			Log.d(TAG, "doUpdate");
			
			// after update allow retrieved serialized data to be garbage collected
			retrievedData = null;
			
			initialized = true;
		}
	}
	
	
	public void doCleanup(Context context) {
		featureCleanup(context);
		initialized = false;
	}


	public void doClose(Context context) {
		String localCacheFileName = getLocalCacheFileName(category, 
				environmentUrl, areaUrl, version);
		
		if (initialized) {
			featureClose(context);
			
			// check to see if our feature data is still in the feature lru tracker
			FeatureLRUTracker featureLruTracker = Envived.getFeatureLRUTracker();
			
			if (featureLruTracker.get(localCacheFileName) == null) {
				Log.d(TAG, "---- NO FEATURE DATA CACHING: DB OR PREFERENCE DELETE ----");
				
				// if it is no longer in the cache then remove the database file entirely
				if (hasLocalDatabaseSupport()) {
					context.deleteDatabase(localCacheFileName);
				}
				else {
					Preferences.removeSerializedFeatureData(context, localCacheFileName);
				}
			}
			else {
				Log.d(TAG, "---- FEATURE DATA IS CACHED: NO DB OR PREFERENCE DELETE ----");
			}
		}
		else {
			featureClose(context);
			
			if (hasLocalDatabaseSupport()) {
				context.deleteDatabase(localCacheFileName);
			}
			else {
				Preferences.removeSerializedFeatureData(context, localCacheFileName);
			}
		}
		
		initialized = false;
	}
	
	
	public boolean hasVirtualAccess() {
		return virtualAccess;
	}
	
	public boolean isGeneral () {
		return isGeneral;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getResourceUri() {
		return featureResourceUrl;
	}

	public String getEnvironmentUri() {
		return environmentUrl;
	}

	public String getAreaUri() {
		return areaUrl;
	}

	public String getSerializedData() {
		return retrievedData;
	}
	
	
	public void setSerializedData(String serializedData) {
		retrievedData = serializedData;
	}
	
	/**
	 * Specifies if this feature has a local cache of it's relevant data.
	 * If the data is not present, it has to be retrieved from the server.
	 * @return
	 */
	public boolean hasData() {
		if (retrievedData != null) {
			Log.d(TAG, "THE DATA RETRIEVED FROM THE SERVER exists for feature: " + category);
			return true;
		}
		else {
			// search for an existing local cache (database) of this feature's data.
			String localCacheFileName = getLocalCacheFileName(category, environmentUrl, areaUrl, version);
			if (hasLocalDatabaseSupport()) {
				boolean exists = databaseExists(Envived.getContext(), localCacheFileName);
				if (exists) {
					Log.d(TAG, "DATABASE LOCAL FILE: " + localCacheFileName + " exists");
				}
				else {
					Log.d(TAG, "DATABASE LOCAL FILE: " + localCacheFileName + " does NOT exist");
				}
				
				return exists;
			}
			else {
				boolean exists = Preferences.featureDataCacheExists(Envived.getContext(), localCacheFileName);
				if (exists) {
					Log.d(TAG, "PREFERENCES LOCAL KEY: " + localCacheFileName + " exists");
				}
				else {
					Log.d(TAG, "PREFERENCES LOCAL KEY: " + localCacheFileName + " does NOT exist");
				}
				
				return exists;
			}
		}
	}
	
	private static boolean databaseExists(Context context, String databaseName) {
	    File dbFile = context.getDatabasePath(databaseName);
	    return dbFile.exists();
	}
	
	/**
	 * Specifies if the cached data for this feature has been initialized, i.e. if the local database
	 * is open and all feature relevant fields have been given their value 
	 * @return true if the cached data has been initialized, false otherwise
	 */
	public boolean isInitialized() {
		return initialized;
	}
	
	@Override
	public String toString() {
		String info = "Feature (" + category + ", " + featureResourceUrl + ")\n";
		info += "Feature Data: " + retrievedData;
		info += "\n";
		return info;
	}
	
	
	public static Feature getFromServer(Context context, String category, 
			String featureResourceUrl, boolean virtualAccess) {
		AppClient client = new AppClient(context);
		
		Log.d(TAG, "retrieving feature with URI: " + featureResourceUrl);
		
		Url url = new Url(Url.FEATURE_RESOURCE, category);
		url.setItemId(Url.resourceIdFromUrl(featureResourceUrl));
		
		/*
		url.setParameters(
			new String[] { "virtual", "category" }, 
			new String[] { Boolean.toString(virtualAccess), category }
		);
		*/
		url.setParameters(
			new String[] { "virtual"}, 
			new String[] { Boolean.toString(virtualAccess)}
		);
		
		
		try {
			HttpResponse response = client.makeGetRequest(url.toString());
			ResponseHolder holder = ResponseHolder.parseResponse(response);
			
			
			if (!holder.hasError() && holder.getCode() == HttpStatus.SC_OK) {
				// if all is Ok the response will be an object with the feature attributes including
				// its serialized 'data' attribute
				JSONObject featureObject = holder.getJsonContent();
				
				if (featureObject != null) {
					String remoteCategory = featureObject.optString("category", category);
					int remoteVersion = featureObject.optInt("version", 1);
					String remoteTimestampString = featureObject.optString("timestamp", null);
					Calendar remoteTimestamp = null;
					if (remoteTimestampString != null) {
						remoteTimestamp = Utils.stringToCalendar(remoteTimestampString, TIMESTAMP_FORMAT);
					}
					
					boolean remoteIsGeneral = featureObject.optBoolean("is_general", false);
					
					String environmentUri = featureObject.optString("environment", null);
					String areaUri = featureObject.optString("area", null);
					String resourceUri = featureObject.optString("resource_uri", null);
					
					String data = null;
					JSONObject featureSerializedDataObject = featureObject.optJSONObject("data");
					if (featureSerializedDataObject != null) {
						data = featureSerializedDataObject.toString();
					}
					
					return getInstance(remoteCategory, remoteVersion, remoteTimestamp, remoteIsGeneral, 
							resourceUri, environmentUri, areaUri, data, virtualAccess);
				}
			}
			else {
				Log.d(TAG, holder.getResponseBody(), holder.getError());
			}
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		}
		
		return null;
	}
	
	public static ResponseHolder putToServer(Context context, String category, 
			String featureResourceUrl, boolean virtualAccess, String data) {
		AppClient client = new AppClient(context);
		
		Url url = new Url(Url.FEATURE_RESOURCE, category);
		url.setItemId(Url.resourceIdFromUrl(featureResourceUrl));
		
		url.setParameters(
				new String[] { "virtual", "format"}, 
				new String[] { Boolean.toString(virtualAccess), "json"}
			);

		try {
			HttpResponse response = client.makePutRequest(url.toString(),
					data,
					new String[] {"content-type"},
					new String[] {"application/json"});//'content-type': 'application/json'
			ResponseHolder holder = ResponseHolder.parseResponse(response);
			
			return holder;
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		}
		return null;
	}
	
	public static Feature getInstance(String category, int version, Calendar timestamp, boolean isGeneral,  
			String resourceUri, String environmentUri, String areaUri, String data, boolean virtualAccess) 
					throws IllegalArgumentException, EnvSocialContentException {
		
		if (category == null) {
			throw new IllegalArgumentException("No feature category specified.");
		}
		
		if (category.equals(PROGRAM)) {
			return new ProgramFeature(category, version, timestamp, isGeneral, resourceUri, environmentUri, areaUri, data, virtualAccess); 
		}
		else if (category.equals(DESCRIPTION)) {
			return new DescriptionFeature(category, version, timestamp, isGeneral, resourceUri, environmentUri, areaUri, data, virtualAccess);
		}
		else if (category.equals(BOOTH_DESCRIPTION)) {
			return new BoothDescriptionFeature(category, version, timestamp, isGeneral, resourceUri, environmentUri, areaUri, data, virtualAccess);
		}
		else if (category.equals(ORDER)) {
			return new OrderFeature(category, version, timestamp, isGeneral, resourceUri, environmentUri, areaUri, data, virtualAccess);
		}
		else if (category.equals(PEOPLE)) {
			return new PeopleFeature(category, version, timestamp, isGeneral, resourceUri, environmentUri, areaUri, data, virtualAccess);
		}
		else if (category.equals(SOCIAL_MEDIA)) {
			return new SocialMediaFeature(category, version, timestamp, isGeneral, resourceUri, environmentUri, areaUri, data, virtualAccess);
		}
		else if (category.equals(CONFERENCE_ROLE)) {
			return new ConferenceRoleFeature(category, version, timestamp, isGeneral, resourceUri, environmentUri, areaUri, data, virtualAccess);
		}
		else {
			throw new IllegalArgumentException("No feature matching category (" + category + ").");
		}
	}
	
	
	public static Feature getFromSavedLocation(Location location, String category) {
		return location.getFeature(category);
	}
	
	
	public static String getLocalCacheFileName(String prefix, String environmentUrl, String areaUrl, int version) {
		
		if (environmentUrl != null) {
			return prefix + ENVIRONMENT_TAG + Url.resourceIdFromUrl(environmentUrl) + "_" + version;
		}
		else {
			return prefix + AREA_TAG + Url.resourceIdFromUrl(areaUrl) + "_" + version;
		}
	}
	
	
	protected abstract void featureInit(boolean insert) throws EnvSocialContentException;
	
	protected abstract void featureUpdate() throws EnvSocialContentException;
	
	protected abstract void featureCleanup(Context context);
	
	protected abstract void featureClose(Context context);
	
	public abstract boolean hasLocalQuerySupport();
	
	public abstract int getDisplayThumbnail();
	
	public abstract String getDisplayName();
	
	public abstract FeatureDbHelper getLocalDatabaseSupport();
	
	public abstract boolean hasLocalDatabaseSupport();
	
	
	public Cursor localSearchQuery(String query) {
		return null;
	}

}
