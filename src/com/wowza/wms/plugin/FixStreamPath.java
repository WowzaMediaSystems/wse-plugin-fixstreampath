/**
 * Wowza server software and all components Copyright 2006 - 2015, Wowza Media Systems, LLC, licensed pursuant to the Wowza Media Software End User License Agreement.
 */
package com.wowza.wms.plugin;

import com.wowza.wms.amf.AMFData;
import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.client.IClient;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.request.RequestFunction;

public class FixStreamPath extends ModuleBase
{
	private void fixStreamPath(IClient client, AMFDataList params, int paramIndex)
	{
		while (true)
		{
			if (params.get(paramIndex).getType() != AMFData.DATA_TYPE_STRING)
				break;

			String connectApp = client.getProperties().getPropertyStr("connectapp");
			if (connectApp == null)
				break;

			String[] parts = connectApp.split("[/]");
			if (parts == null)
				break;

			if (parts.length <= 2)
				break;

			String streamName = params.getString(paramIndex);

			String newName = streamName;
			for (int i = (parts.length - 1); i >= 2; i--)
				newName = parts[i] + "/" + newName;

			params.set(paramIndex, newName);

			getLogger().info("FixStreamPath: from:" + streamName + " to:" + newName);
			break;
		}
	}

	public void getStreamLength(IClient client, RequestFunction function, AMFDataList params)
	{
		fixStreamPath(client, params, PARAM1);
		invokePrevious(client, function, params);
	}

	public void play(IClient client, RequestFunction function, AMFDataList params)
	{
		fixStreamPath(client, params, PARAM1);
		invokePrevious(client, function, params);
	}

	public void publish(IClient client, RequestFunction function, AMFDataList params)
	{
		fixStreamPath(client, params, PARAM1);
		invokePrevious(client, function, params);
	}

	public void releaseStream(IClient client, RequestFunction function, AMFDataList params)
	{
		fixStreamPath(client, params, PARAM1);
		invokePrevious(client, function, params);
	}
}
