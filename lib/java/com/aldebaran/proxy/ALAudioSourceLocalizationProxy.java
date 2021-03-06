// Author: Aldebaran-Robotics
// Auto generated file.



package com.aldebaran.proxy;
public class ALAudioSourceLocalizationProxy extends ALProxy
{
 	static
	{
	  System.loadLibrary("JNaoQi");
	}

 	public ALProxy proxy;

	public static void main(String[] args)
	{
	}

	/// <summary>
	/// Default Constructor.
	/// </summary>
	public ALAudioSourceLocalizationProxy(String ip, int port)
	{
		super("ALAudioSourceLocalization", ip, port);
	}

    /// <summary>
    /// Exits and unregisters the module.
    /// </summary>

    
    public void exit()
    {


	Variant result = call("exit" );

		// no return value
    }

    



    /// <summary>
    /// Gets the name of the parent broker.
    /// </summary>
    /// <returns> The name of the parent broker. </returns>

    
    public String getBrokerName()
    {


	Variant result = call("getBrokerName" );

	    	return  result.toString();
    }

    



    /// <summary>
    /// Gets the current period.
    /// </summary>
    /// <returns> Refresh period (in milliseconds). </returns>

    
    public int getCurrentPeriod()
    {


	Variant result = call("getCurrentPeriod" );

	    	return  result.toInt();
    }

    



    /// <summary>
    /// Gets the current precision.
    /// </summary>
    /// <returns> Precision of the extractor. </returns>

    
    public float getCurrentPrecision()
    {


	Variant result = call("getCurrentPrecision" );

	    	return  result.toFloat();
    }

    



    /// <summary>
    /// Retrieves a method's description.
    /// </summary>
    /// <param name="methodName"> The name of the method. </param>
    /// <returns> A structure containing the method's description. </returns>

    
    
    
    public Variant getMethodHelp( String methodName)
    {

	Variant vmethodName;
	vmethodName = new Variant(methodName);

	Variant result = call("getMethodHelp" ,vmethodName);

	    	return  result;
    }

    



    /// <summary>
    /// Retrieves the module's method list.
    /// </summary>
    /// <returns> An array of method names. </returns>

    
    public String[] getMethodList()
    {


	Variant result = call("getMethodList" );

	    	return (String[]) result.toStringArray();
    }

    



    /// <summary>
    /// Retrieves the module's description.
    /// </summary>
    /// <returns> A structure describing the module. </returns>

    
    public Variant getModuleHelp()
    {


	Variant result = call("getModuleHelp" );

	    	return  result;
    }

    



    /// <summary>
    /// Gets the period for a specific subscription.
    /// </summary>
    /// <param name="name"> Name of the module which has subscribed. </param>
    /// <returns> Refresh period (in milliseconds). </returns>

    
    
    
    public int getMyPeriod( String name)
    {

	Variant vname;
	vname = new Variant(name);

	Variant result = call("getMyPeriod" ,vname);

	    	return  result.toInt();
    }

    



    /// <summary>
    /// Gets the precision for a specific subscription.
    /// </summary>
    /// <param name="name"> name of the module which has subscribed </param>
    /// <returns> precision of the extractor </returns>

    
    
    
    public float getMyPrecision( String name)
    {

	Variant vname;
	vname = new Variant(name);

	Variant result = call("getMyPrecision" ,vname);

	    	return  result.toFloat();
    }

    



    /// <summary>
    /// Get the list of values updated in ALMemory.
    /// </summary>
    /// <returns> Array of values updated by this extractor in ALMemory </returns>

    
    public String[] getOutputNames()
    {


	Variant result = call("getOutputNames" );

	    	return (String[]) result.toStringArray();
    }

    



    /// <summary>
    /// Gets the parameters given by the module.
    /// </summary>
    /// <returns> Array of names and parameters of all subscribers. </returns>

    
    public Variant getSubscribersInfo()
    {


	Variant result = call("getSubscribersInfo" );

	    	return  result;
    }

    



    /// <summary>
    /// Gets the method usage string. This summarise how to use the method.
    /// </summary>
    /// <param name="name"> The name of the method. </param>
    /// <returns> A string that summarises the usage of the method. </returns>

    
    
    
    public String getUsage( String name)
    {

	Variant vname;
	vname = new Variant(name);

	Variant result = call("getUsage" ,vname);

	    	return  result.toString();
    }

    



    /// <summary>
    /// Returns true if the method is currently running.
    /// </summary>
    /// <param name="id"> The ID of the method that was returned when calling the method using 'post' </param>
    /// <returns> True if the method is currently running </returns>

    
    
    
    public Boolean isRunning( int id)
    {

	Variant vid;
	vid = new Variant(id);

	Variant result = call("isRunning" ,vid);

	    	return  result.toBoolean();
    }

    



    /// <summary>
    /// Just a ping. Always returns true
    /// </summary>
    /// <returns> returns true </returns>

    
    public Boolean ping()
    {


	Variant result = call("ping" );

	    	return  result.toBoolean();
    }

    



    /// <summary>
    /// enable/disable the printing of some debug information
    /// </summary>
    /// <param name="arg1"> arg </param>
    /// <param name="arg2"> arg </param>
    /// <param name="arg3"> arg </param>
    /// <param name="arg4"> arg </param>

    
    
    
    
    
    
    
    
    
    public void processRemote( int arg1,  int arg2,  Variant arg3,  Variant arg4)
    {

	Variant varg1;
	varg1 = new Variant(arg1);
	Variant varg2;
	varg2 = new Variant(arg2);
	Variant varg3;
	varg3 = new Variant(arg3);
	Variant varg4;
	varg4 = new Variant(arg4);

	Variant result = call("processRemote" ,varg1, varg2, varg3, varg4);

		// no return value
    }

    



    /// <summary>
    /// enable/disable the printing of some debug information
    /// </summary>
    /// <param name="arg1"> arg </param>
    /// <param name="arg2"> arg </param>
    /// <param name="arg3"> arg </param>

    
    
    
    
    
    
    
    public void processSoundRemote( int arg1,  int arg2,  Variant arg3)
    {

	Variant varg1;
	varg1 = new Variant(arg1);
	Variant varg2;
	varg2 = new Variant(arg2);
	Variant varg3;
	varg3 = new Variant(arg3);

	Variant result = call("processSoundRemote" ,varg1, varg2, varg3);

		// no return value
    }

    



    /// <summary>
    /// enable/disable the printing of some debug information
    /// </summary>
    /// <param name="bSetOrUnset"> enable the functionnality when true. </param>

    
    
    
    public void setDebugMode( Boolean bSetOrUnset)
    {

	Variant vbSetOrUnset;
	vbSetOrUnset = new Variant(bSetOrUnset);

	Variant result = call("setDebugMode" ,vbSetOrUnset);

		// no return value
    }

    



    /// <summary>
    /// Set the specified parameter.
    /// </summary>
    /// <param name="parameter"> Name of the parameter. \"Sensibility\" between 0 and 1 to adjust the capacity of NAO to locate quiet sounds. \"EnergyComputation\" (1 or 0) that activates the computation of the located source signal energy. This energy is added in the \"ALAudioSourceLocalization/SoundLocated\" ALMemory key. </param>
    /// <param name="value"> \"Sensibility\" : a float in [0,1]. \"EnergyComputation\" : (1 or 0). </param>

    
    
    
    
    
    public void setParameter( String parameter,  Variant value)
    {

	Variant vparameter;
	vparameter = new Variant(parameter);
	Variant vvalue;
	vvalue = new Variant(value);

	Variant result = call("setParameter" ,vparameter, vvalue);

		// no return value
    }

    



    /// <summary>
    /// returns true if the method is currently running
    /// </summary>
    /// <param name="id"> the ID of the method to wait for </param>

    
    
    
    public void stop( int id)
    {

	Variant vid;
	vid = new Variant(id);

	Variant result = call("stop" ,vid);

		// no return value
    }

    



    /// <summary>
    /// Subscribes to the extractor. This causes the extractor to start writing information to memory using the keys described by getOutputNames(). These can be accessed in memory using ALMemory.getData(\"keyName\"). In many cases you can avoid calling subscribe on the extractor by just calling ALMemory.subscribeToEvent() supplying a callback method. This will automatically subscribe to the extractor for you.
    /// </summary>
    /// <param name="name"> Name of the module which subscribes. </param>
    /// <param name="period"> Refresh period (in milliseconds) if relevant. </param>
    /// <param name="precision"> Precision of the extractor if relevant. </param>

    
    
    
    
    
    
    
    public void subscribe( String name,  int period,  float precision)
    {

	Variant vname;
	vname = new Variant(name);
	Variant vperiod;
	vperiod = new Variant(period);
	Variant vprecision;
	vprecision = new Variant(precision);

	Variant result = call("subscribe" ,vname, vperiod, vprecision);

		// no return value
    }

    



    /// <summary>
    /// Subscribes to the extractor. This causes the extractor to start writing information to memory using the keys described by getOutputNames(). These can be accessed in memory using ALMemory.getData(\"keyName\"). In many cases you can avoid calling subscribe on the extractor by just calling ALMemory.subscribeToEvent() supplying a callback method. This will automatically subscribe to the extractor for you.
    /// </summary>
    /// <param name="name"> Name of the module which subscribes. </param>

    
    
    
    public void subscribe( String name)
    {

	Variant vname;
	vname = new Variant(name);

	Variant result = call("subscribe" ,vname);

		// no return value
    }

    



    /// <summary>
    /// Unsubscribes from the extractor.
    /// </summary>
    /// <param name="name"> Name of the module which had subscribed. </param>

    
    
    
    public void unsubscribe( String name)
    {

	Variant vname;
	vname = new Variant(name);

	Variant result = call("unsubscribe" ,vname);

		// no return value
    }

    



    /// <summary>
    /// Updates the period if relevant.
    /// </summary>
    /// <param name="name"> Name of the module which has subscribed. </param>
    /// <param name="period"> Refresh period (in milliseconds). </param>

    
    
    
    
    
    public void updatePeriod( String name,  int period)
    {

	Variant vname;
	vname = new Variant(name);
	Variant vperiod;
	vperiod = new Variant(period);

	Variant result = call("updatePeriod" ,vname, vperiod);

		// no return value
    }

    



    /// <summary>
    /// Updates the precision if relevant.
    /// </summary>
    /// <param name="name"> Name of the module which has subscribed. </param>
    /// <param name="precision"> Precision of the extractor. </param>

    
    
    
    
    
    public void updatePrecision( String name,  float precision)
    {

	Variant vname;
	vname = new Variant(name);
	Variant vprecision;
	vprecision = new Variant(precision);

	Variant result = call("updatePrecision" ,vname, vprecision);

		// no return value
    }

    



    /// <summary>
    /// Returns the version of the module.
    /// </summary>
    /// <returns> A string containing the version of the module. </returns>

    
    public String version()
    {


	Variant result = call("version" );

	    	return  result.toString();
    }

    



    /// <summary>
    /// Wait for the end of a long running method that was called using 'post'
    /// </summary>
    /// <param name="id"> The ID of the method that was returned when calling the method using 'post' </param>
    /// <param name="timeoutPeriod"> The timeout period in ms. To wait indefinately, use a timeoutPeriod of zero. </param>
    /// <returns> True if the timeout period terminated. False if the method returned. </returns>

    
    
    
    
    
    public Boolean wait( int id,  int timeoutPeriod)
    {

	Variant vid;
	vid = new Variant(id);
	Variant vtimeoutPeriod;
	vtimeoutPeriod = new Variant(timeoutPeriod);

	Variant result = call("wait" ,vid, vtimeoutPeriod);

	    	return  result.toBoolean();
    }

    




}


