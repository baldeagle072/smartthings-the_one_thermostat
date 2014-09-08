//script to put in head

<script>
	
	function changeTemp() {
		
		var url = "https://graph.api.smartthings.com/api/smartapps/installations/459fd19c-f611-4156-ae53-ae8de87f4b08/setTemp/0/";
		
		var temp = document.getElementById('temp').value;
		
		url += temp;
		
		document.getElementById('cmd').src=url;
	}
	
	function turnOnHeat() {
		
		var url = "https://graph.api.smartthings.com/api/smartapps/installations/459fd19c-f611-4156-ae53-ae8de87f4b08/setMode/0/heat";
		
		document.getElementById('cmd').src=url;
		document.getElementById('mode').innerHTML = '<h4>Mode: heat</h4>';
	}
	
	function turnOnCool() {
		
		var url = "https://graph.api.smartthings.com/api/smartapps/installations/459fd19c-f611-4156-ae53-ae8de87f4b08/setMode/0/cool";
		
		document.getElementById('cmd').src=url;
		document.getElementById('mode').innerHTML = '<h4>Mode: cool</h4>';
	}
	
	function turnOffThermostat() {
		
		var url = "https://graph.api.smartthings.com/api/smartapps/installations/459fd19c-f611-4156-ae53-ae8de87f4b08/setMode/0/off";
		
		document.getElementById('cmd').src=url;
		document.getElementById('mode').innerHTML = '<h4>Mode: off</h4>';
	}

</script>

//div to insert in body

<div class="room" id="thermostat-div">
	<h3>Thermostat</h3>
	<form method="post" action="changeTemp.php" target="cmd">
		<label name="temp">Change Temperature: </label><?php
		
			$username = '';
			$password = '';
			$installedAppId = '' //Use the ID # that shows up in the logs on the IDE for the installed program
			
			$page = "https://graph.api.smartthings.com/api/smartapps/installations/" + $installedAppId + "/currentTemp/0";
			
			$ch = curl_init();
    
			curl_setopt($ch, CURLOPT_URL,            $page );
			curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1 );
			curl_setopt($ch, CURLOPT_USERPWD, "$username:$password");
			curl_setopt($ch, CURLOPT_POST,           0 );
			curl_setopt($ch, CURLOPT_HTTPHEADER,     array('Content-Type: application/json')); 
		 
			$response =  json_decode(curl_exec($ch),true);
		 
			curl_close($ch);
			
			$currentTemp = $response['currentTemp'];
    
			$setTemp = $response['setTemp'];
			
			$mode = $response['setMode'];
			
			print("<input type='text' id='temp' name='temp' size='3' value='".$setTemp."'>");
		?><br>
		<input type="button" value="Heat" onclick="turnOnHeat();">
		<input type="button" value="Cool" onclick="turnOnCool();">
		<input type="button" value="Change Temperature" onclick="changeTemp();">
		<input type="button" value="Off" onclick="turnOffThermostat();">
		<div id="currentTemp"><?php print('<h3>Curent Temperature: '.$currentTemp.' &deg;F</h3>'); ?></div>
		<div id="mode"><?php print('<h4>Mode: '.$mode.'</h4>'); ?></div>
	</form>
</div>
