package com.o2o_jiangchen.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.fanwe.library.utils.SDPackageUtil;

public class RequestModel
{
	public static final class RequestDataType
	{
		public static final int BASE64 = 0;
		public static final int AES = 4;
	}

	public static final class ResponseDataType
	{
		public static final int BASE64 = 0;
		public static final int JSON = 1;
		public static final int AES = 4;
	}

	// -----------------------------------------
	private int mRequestDataType = RequestDataType.BASE64;
	private int mResponseDataType = ResponseDataType.JSON;

	private Map<String, Object> mData = new HashMap<String, Object>();
	private Map<String, File> mDataFile = new HashMap<String, File>();

	private List<MultiFile> mMultiFile = new ArrayList<MultiFile>();

	private boolean mIsNeedCache = false;
	private boolean mIsNeedShowErrorTip = true;
	private boolean mIsNeedCheckLoginState = true;

	public List<MultiFile> getMultiFile()
	{
		return mMultiFile;
	}

	// 构造方法start
	public RequestModel(Map<String, Object> data)
	{
		super();
		this.mData = data;
		init();
	}

	public RequestModel()
	{
		super();
		init();
	}

	// 构造方法end

	private void init()
	{
		putAct("index");
		put("from", "android");
		put("version_name", SDPackageUtil.getVersionName());
	}


	public boolean isNeedCache()
	{
		return mIsNeedCache;
	}

	public void setIsNeedCache(boolean isNeedCache)
	{
		this.mIsNeedCache = isNeedCache;
	}

	public boolean isNeedShowErrorTip()
	{
		return mIsNeedShowErrorTip;
	}

	public void setIsNeedShowErrorTip(boolean isNeedShowErrorTip)
	{
		this.mIsNeedShowErrorTip = isNeedShowErrorTip;
	}

	public boolean isNeedCheckLoginState()
	{
		return mIsNeedCheckLoginState;
	}

	public void setIsNeedCheckLoginState(boolean isNeedCheckLoginState)
	{
		this.mIsNeedCheckLoginState = isNeedCheckLoginState;
	}

	public Map<String, File> getDataFile()
	{
		return mDataFile;
	}

	public void setDataFile(Map<String, File> dataFile)
	{
		this.mDataFile = dataFile;
	}

	public Map<String, Object> getData()
	{
		return mData;
	}

	public void setData(Map<String, Object> data)
	{
		this.mData = data;
	}

	public int getRequestDataType()
	{
		return mRequestDataType;
	}

	public void setRequestDataType(int requestDataType)
	{
		this.mRequestDataType = requestDataType;
	}

	public int getResponseDataType()
	{
		return mResponseDataType;
	}

	public void setResponseDataType(int responseDataType)
	{
		this.mResponseDataType = responseDataType;
	}

	public void put(String key, Object value)
	{
		mData.put(key, value);
	}

	public Object get(String key)
	{
		return mData.get(key);
	}

	public void putFile(String key, File file)
	{
		mDataFile.put(key, file);
	}

	public void putMultiFile(String key, File file)
	{
		mMultiFile.add(new MultiFile(key, file));
	}







	public void putCtl(String ctl)
	{
		put("ctl", ctl);
	}

	public void putAct(String act)
	{
		put("act", act);
	}

	public class MultiFile
	{
		public final String key;
		public final File file;

		public MultiFile(String key, File file)
		{
			this.key = key;
			this.file = file;
		}

		public String getKey()
		{
			return this.key;
		}

		public File getFile()
		{
			return this.file;
		}
	}

}
