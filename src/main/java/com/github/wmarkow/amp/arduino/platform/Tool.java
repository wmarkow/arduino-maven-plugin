package com.github.wmarkow.amp.arduino.platform;

import java.util.ArrayList;
import java.util.List;

import com.github.wmarkow.amp.arduino.platform.manager.HostInfo;

public class Tool {
	private String name;
	private String version;
	private List<System> systems = new ArrayList<System>();

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public List<System> getSystems() {
		return systems;
	}

	public System getSystemByHostInfo(HostInfo hostInfo) {
        List< System > systems = getSystemsByOsName( hostInfo.getOsName(), getSystems() );
		
		if(systems.size() == 0)
		{
			return null;
		}
		
		if(systems.size() == 1)
		{
			return systems.get(0);
		}
		
        systems = getSystemsByArch( hostInfo.getOsArch(), systems );
		
		if(systems.size() == 0)
		{
			return null;
		}
		
		if(systems.size() == 1)
		{
			return systems.get(0);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Can not find a correct system for %s .\n", hostInfo));
		sb.append("Current systems are:\n");
		for(System system : systems)
		{
			sb.append(system.toString() + "\n");
		}
		
		throw new RuntimeException(sb.toString());
	}

    private List< System > getSystemsByOsName( String osName, List< System > systems )
    {
		List<System> result = new ArrayList<System>();

        for( System system : systems )
        {
			if (system.getHost().toLowerCase().contains(osName.toLowerCase())) {
				result.add(system);

                continue;
            }

            if( osName.toLowerCase().contains( "windows" )
                && system.getHost().toLowerCase().contains( "mingw" ) )
            {
                result.add( system );

                continue;
			}
		}

		return result;
	}
	
    private List< System > getSystemsByArch( String osArch, List< System > systems )
    {
		List<System> result = new ArrayList<System>();

        for( System system : systems )
        {
			if (osArch.toLowerCase().contains("64") && system.getHost().toLowerCase().contains("x86_64")) {
				result.add(system);
			}
		}

		return result;
	}
}
