/*
	Copyright (c) 2004-2007, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/book/dojo-book-0-9/introduction/licensing
*/

/*
	This is a compiled version of Dojo, built for deployment and not for
	development. To get an editable version, please visit:

		http://dojotoolkit.org

	for documentation and information on getting the source.
*/

if(!dojo._hasResource["dojo.AdapterRegistry"]){dojo._hasResource["dojo.AdapterRegistry"]=true;dojo.provide("dojo.AdapterRegistry");dojo.AdapterRegistry=function(_1){this.pairs=[];this.returnWrappers=_1||false;};dojo.extend(dojo.AdapterRegistry,{register:function(_2,_3,_4,_5,_6){this.pairs[((_6)?"unshift":"push")]([_2,_3,_4,_5]);},match:function(){for(var i=0;i<this.pairs.length;i++){var _8=this.pairs[i];if(_8[1].apply(this,arguments)){if((_8[3])||(this.returnWrappers)){return _8[2];}else{return _8[2].apply(this,arguments);}}}throw new Error("No match found");},unregister:function(_9){for(var i=0;i<this.pairs.length;i++){var _b=this.pairs[i];if(_b[0]==_9){this.pairs.splice(i,1);return true;}}return false;}});}if(!dojo._hasResource["dojo.io.script"]){dojo._hasResource["dojo.io.script"]=true;dojo.provide("dojo.io.script");dojo.io.script={get:function(_c){var _d=this._makeScriptDeferred(_c);var _e=_d.ioArgs;dojo._ioAddQueryToUrl(_e);this.attach(_e.id,_e.url);dojo._ioWatch(_d,this._validCheck,this._ioCheck,this._resHandle);return _d;},attach:function(id,url){var _11=dojo.doc.createElement("script");_11.type="text/javascript";_11.src=url;_11.id=id;dojo.doc.getElementsByTagName("head")[0].appendChild(_11);},remove:function(id){dojo._destroyElement(dojo.byId(id));if(this["jsonp_"+id]){delete this["jsonp_"+id];}},_makeScriptDeferred:function(_13){var dfd=dojo._ioSetArgs(_13,this._deferredCancel,this._deferredOk,this._deferredError);var _15=dfd.ioArgs;_15.id="dojoIoScript"+(this._counter++);_15.canDelete=false;if(_13.callbackParamName){_15.query=_15.query||"";if(_15.query.length>0){_15.query+="&";}_15.query+=_13.callbackParamName+"=dojo.io.script.jsonp_"+_15.id+"._jsonpCallback";_15.canDelete=true;dfd._jsonpCallback=this._jsonpCallback;this["jsonp_"+_15.id]=dfd;}return dfd;},_deferredCancel:function(dfd){dfd.canceled=true;if(dfd.ioArgs.canDelete){dojo.io.script._deadScripts.push(dfd.ioArgs.id);}},_deferredOk:function(dfd){if(dfd.ioArgs.canDelete){dojo.io.script._deadScripts.push(dfd.ioArgs.id);}if(dfd.ioArgs.json){return dfd.ioArgs.json;}else{return dfd.ioArgs;}},_deferredError:function(_18,dfd){if(dfd.ioArgs.canDelete){if(_18.dojoType=="timeout"){dojo.io.script.remove(dfd.ioArgs.id);}else{dojo.io.script._deadScripts.push(dfd.ioArgs.id);}}console.debug("dojo.io.script error",_18);return _18;},_deadScripts:[],_counter:1,_validCheck:function(dfd){var _1b=dojo.io.script;var _1c=_1b._deadScripts;if(_1c&&_1c.length>0){for(var i=0;i<_1c.length;i++){_1b.remove(_1c[i]);}dojo.io.script._deadScripts=[];}return true;},_ioCheck:function(dfd){if(dfd.ioArgs.json){return true;}var _1f=dfd.ioArgs.args.checkString;if(_1f&&eval("typeof("+_1f+") != 'undefined'")){return true;}return false;},_resHandle:function(dfd){if(dojo.io.script._ioCheck(dfd)){dfd.callback(dfd);}else{dfd.errback(new Error("inconceivable dojo.io.script._resHandle error"));}},_jsonpCallback:function(_21){this.ioArgs.json=_21;}};}if(!dojo._hasResource["dojox.cometd._base"]){dojo._hasResource["dojox.cometd._base"]=true;dojo.provide("dojox.cometd._base");dojox.cometd=new function(){this.DISCONNECTED="DISCONNECTED";this.CONNECTING="CONNECTING";this.CONNECTED="CONNECTED";this.DISCONNECTING="DISCONNECING";this._initialized=false;this._connected=false;this._polling=false;this.connectionTypes=new dojo.AdapterRegistry(true);this.version="1.0";this.minimumVersion="0.9";this.clientId=null;this.messageId=0;this.batch=0;this._isXD=false;this.handshakeReturn=null;this.currentTransport=null;this.url=null;this.lastMessage=null;this._messageQ=[];this.handleAs="json-comment-optional";this._advice={};this._maxInterval=30000;this._backoffInterval=1000;this._deferredSubscribes={};this._deferredUnsubscribes={};this._subscriptions=[];this._extendInList=[];this._extendOutList=[];this.state=function(){return this._initialized?(this._connected?this.CONNECTED:this.CONNECTING):(this._connected?this.DISCONNECTING:this.DISCONNECTED);};this.init=function(_22,_23,_24){_23=_23||{};_23.version=this.version;_23.minimumVersion=this.minimumVersion;_23.channel="/meta/handshake";_23.id=""+this.messageId++;this.url=_22||djConfig["cometdRoot"];if(!this.url){console.debug("no cometd root specified in djConfig and no root passed");return null;}var _25="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";var _26=(""+window.location).match(new RegExp(_25));if(_26[4]){var tmp=_26[4].split(":");var _28=tmp[0];var _29=tmp[1]||"80";_26=this.url.match(new RegExp(_25));if(_26[4]){tmp=_26[4].split(":");var _2a=tmp[0];var _2b=tmp[1]||"80";this._isXD=((_2a!=_28)||(_2b!=_29));}}if(!this._isXD){if(_23.ext){if(_23.ext["json-comment-filtered"]!==true&&_23.ext["json-comment-filtered"]!==false){_23.ext["json-comment-filtered"]=true;}}else{_23.ext={"json-comment-filtered":true};}}_23=this._extendOut(_23);var _2c={url:this.url,handleAs:this.handleAs,content:{"message":dojo.toJson([_23])},load:dojo.hitch(this,function(msg){this._finishInit(msg);}),error:dojo.hitch(this,function(e){console.debug("handshake error!:",e);this._finishInit([{}]);})};if(_24){dojo.mixin(_2c,_24);}this._props=_23;for(var _2f in this._subscriptions){for(var sub in this._subscriptions[_2f]){if(this._subscriptions[_2f][sub].topic){dojo.unsubscribe(this._subscriptions[_2f][sub].topic);}}}this._messageQ=[];this._subscriptions=[];this._initialized=true;this.batch=0;this.startBatch();var r;if(this._isXD){_2c.callbackParamName="jsonp";r=dojo.io.script.get(_2c);}else{r=dojo.xhrPost(_2c);}dojo.publish("/cometd/meta",[{cometd:this,action:"handshake",successful:true,state:this.state()}]);return r;};this.publish=function(_32,_33,_34){var _35={data:_33,channel:_32};if(_34){dojo.mixin(_35,_34);}this._sendMessage(_35);};this.subscribe=function(_36,_37,_38){if(_37){var _39="/cometd"+_36;var _3a=this._subscriptions[_39];if(!_3a||_3a.length==0){_3a=[];this._sendMessage({channel:"/meta/subscribe",subscription:_36});var _ds=this._deferredSubscribes;_ds[_36]=new dojo.Deferred();if(_ds[_36]){_ds[_36].cancel();delete _ds[_36];}}for(var i in _3a){if(_3a[i].objOrFunc===_37&&(!_3a[i].funcName&&!_38||_3a[i].funcName==_38)){return null;}}var _3d=dojo.subscribe(_39,_37,_38);_3a.push({topic:_3d,objOrFunc:_37,funcName:_38});this._subscriptions[_39]=_3a;}return this._deferredSubscribes[_36];};this.unsubscribe=function(_3e,_3f,_40){var _41="/cometd"+_3e;var _42=this._subscriptions[_41];if(!_42||_42.length==0){return null;}var s=0;for(var i in _42){var sb=_42[i];if((!_3f)||(sb.objOrFunc===_3f&&(!sb.funcName&&!_40||sb.funcName==_40))){dojo.unsubscribe(_42[i].topic);delete _42[i];}else{s++;}}if(s==0){delete this._subscriptions[_41];this._sendMessage({channel:"/meta/unsubscribe",subscription:_3e});this._deferredUnsubscribes[_3e]=new dojo.Deferred();if(this._deferredSubscribes[_3e]){this._deferredSubscribes[_3e].cancel();delete this._deferredSubscribes[_3e];}}return this._deferredUnsubscribes[_3e];};this.disconnect=function(){for(var _46 in this._subscriptions){for(var sub in this._subscriptions[_46]){if(this._subscriptions[_46][sub].topic){dojo.unsubscribe(this._subscriptions[_46][sub].topic);}}}this._subscriptions=[];this._messageQ=[];if(this._initialized&&this.currentTransport){this._initialized=false;this.currentTransport.disconnect();}if(!this._polling){this._connected=false;dojo.publish("/cometd/meta",[{cometd:this,action:"connect",successful:false,state:this.state()}]);}this._initialized=false;dojo.publish("/cometd/meta",[{cometd:this,action:"disconnect",successful:true,state:this.state()}]);};this.subscribed=function(_48,_49){};this.unsubscribed=function(_4a,_4b){};this.tunnelInit=function(_4c,_4d){};this.tunnelCollapse=function(){};this._backoff=function(){if(!this._advice||!this._advice.interval){this._advice={reconnect:"retry",interval:0};}if(this._advice.interval<this._maxInterval){this._advice.interval+=this._backoffInterval;}};this._finishInit=function(_4e){_4e=_4e[0];this.handshakeReturn=_4e;if(_4e["advice"]){this._advice=_4e.advice;}var _4f=_4e.successful?_4e.successful:false;if(_4e.version<this.minimumVersion){console.debug("cometd protocol version mismatch. We wanted",this.minimumVersion,"but got",_4e.version);_4f=false;this._advice.reconnect="none";}if(_4f){this.currentTransport=this.connectionTypes.match(_4e.supportedConnectionTypes,_4e.version,this._isXD);this.currentTransport._cometd=this;this.currentTransport.version=_4e.version;this.clientId=_4e.clientId;this.tunnelInit=dojo.hitch(this.currentTransport,"tunnelInit");this.tunnelCollapse=dojo.hitch(this.currentTransport,"tunnelCollapse");this.currentTransport.startup(_4e);}dojo.publish("/cometd/meta",[{cometd:this,action:"handshook",successful:_4f,state:this.state()}]);if(!_4f){console.debug("cometd init failed");this._backoff();if(this._advice&&this._advice["reconnect"]=="none"){console.debug("cometd reconnect: none");}else{if(this._advice&&this._advice["interval"]&&this._advice.interval>0){setTimeout(dojo.hitch(this,function(){this.init(cometd.url,this._props);}),this._advice.interval);}else{this.init(this.url,this._props);}}}};this._extendIn=function(_50){var m=_50;dojo.forEach(dojox.cometd._extendInList,function(f){var n=f(m);if(n){m=n;}});return m;};this._extendOut=function(_54){var m=_54;dojo.forEach(dojox.cometd._extendOutList,function(f){var n=f(m);if(n){m=n;}});return m;};this.deliver=function(_58){dojo.forEach(_58,this._deliver,this);return _58;};this._deliver=function(_59){_59=this._extendIn(_59);if(!_59["channel"]){if(_59["success"]!==true){console.debug("cometd error: no channel for message!",_59);return;}}this.lastMessage=_59;if(_59.advice){this._advice=_59.advice;}var _5a=null;if((_59["channel"])&&(_59.channel.length>5)&&(_59.channel.substr(0,5)=="/meta")){switch(_59.channel){case "/meta/connect":if(_59.successful&&!this._connected){this._connected=this._initialized;this.endBatch();}else{if(!this._initialized){this._connected=false;}}dojo.publish("/cometd/meta",[{cometd:this,action:"connect",successful:_59.successful,state:this.state()}]);break;case "/meta/subscribe":_5a=this._deferredSubscribes[_59.subscription];if(!_59.successful){if(_5a){_5a.errback(new Error(_59.error));}return;}dojox.cometd.subscribed(_59.subscription,_59);if(_5a){_5a.callback(true);}break;case "/meta/unsubscribe":_5a=this._deferredUnsubscribes[_59.subscription];if(!_59.successful){if(_5a){_5a.errback(new Error(_59.error));}return;}this.unsubscribed(_59.subscription,_59);if(_5a){_5a.callback(true);}break;}}this.currentTransport.deliver(_59);if(_59.data){try{var _5b="/cometd"+_59.channel;dojo.publish(_5b,[_59]);}catch(e){console.debug(e);}}};this._sendMessage=function(_5c){if(this.currentTransport&&this._connected&&this.batch==0){return this.currentTransport.sendMessages([_5c]);}else{this._messageQ.push(_5c);return null;}};this.startBatch=function(){this.batch++;};this.endBatch=function(){if(--this.batch<=0&&this.currentTransport&&this._connected){this.batch=0;var _5d=this._messageQ;this._messageQ=[];if(_5d.length>0){this.currentTransport.sendMessages(_5d);}}};this._onUnload=function(){dojo.addOnUnload(dojox.cometd,"disconnect");};};dojox.cometd.longPollTransport=new function(){this._connectionType="long-polling";this._cometd=null;this.check=function(_5e,_5f,_60){return ((!_60)&&(dojo.indexOf(_5e,"long-polling")>=0));};this.tunnelInit=function(){var _61={channel:"/meta/connect",clientId:this._cometd.clientId,connectionType:this._connectionType,id:""+this._cometd.messageId++};_61=this._cometd._extendOut(_61);this.openTunnelWith({message:dojo.toJson([_61])});};this.tunnelCollapse=function(){if(!this._cometd._initialized){return;}if(this._cometd._advice){if(this._cometd._advice["reconnect"]=="none"){return;}if((this._cometd._advice["interval"])&&(this._cometd._advice.interval>0)){setTimeout(dojo.hitch(this,function(){this._connect();}),this._cometd._advice.interval);}else{this._connect();}}else{this._connect();}};this._connect=function(){if(!this._cometd._initialized){return;}if(this._cometd._polling){console.debug("wait for poll to complete or fail");return;}if((this._cometd._advice)&&(this._cometd._advice["reconnect"]=="handshake")){this._cometd._connected=false;this._initialized=false;this._cometd.init(this._cometd.url,this._cometd._props);}else{if(this._cometd._connected){var _62={channel:"/meta/connect",connectionType:this._connectionType,clientId:this._cometd.clientId,id:""+this._cometd.messageId++};_62=this._cometd._extendOut(_62);this.openTunnelWith({message:dojo.toJson([_62])});}}};this.deliver=function(_63){};this.openTunnelWith=function(_64,url){this._cometd._polling=true;var d=dojo.xhrPost({url:(url||this._cometd.url),content:_64,handleAs:this._cometd.handleAs,load:dojo.hitch(this,function(_67){this._cometd._polling=false;this._cometd.deliver(_67);this.tunnelCollapse();}),error:dojo.hitch(this,function(err){this._cometd._polling=false;console.debug("tunnel opening failed:",err);dojo.publish("/cometd/meta",[{cometd:this._cometd,action:"connect",successful:false,state:this._cometd.state()}]);this._cometd._backoff();this.tunnelCollapse();})});};this.sendMessages=function(_69){for(var i=0;i<_69.length;i++){_69[i].clientId=this._cometd.clientId;_69[i].id=""+this._cometd.messageId++;_69[i]=this._cometd._extendOut(_69[i]);}return dojo.xhrPost({url:this._cometd.url||djConfig["cometdRoot"],handleAs:this._cometd.handleAs,load:dojo.hitch(this._cometd,"deliver"),content:{message:dojo.toJson(_69)}});};this.startup=function(_6b){if(this._cometd._connected){return;}this.tunnelInit();};this.disconnect=function(){var _6c={channel:"/meta/disconnect",clientId:this._cometd.clientId,id:""+this._cometd.messageId++};_6c=this._cometd._extendOut(_6c);dojo.xhrPost({url:this._cometd.url||djConfig["cometdRoot"],handleAs:this._cometd.handleAs,content:{message:dojo.toJson([_6c])}});};};dojox.cometd.callbackPollTransport=new function(){this._connectionType="callback-polling";this._cometd=null;this.check=function(_6d,_6e,_6f){return (dojo.indexOf(_6d,"callback-polling")>=0);};this.tunnelInit=function(){var _70={channel:"/meta/connect",clientId:this._cometd.clientId,connectionType:this._connectionType,id:""+this._cometd.messageId++};_70=this._cometd._extendOut(_70);this.openTunnelWith({message:dojo.toJson([_70])});};this.tunnelCollapse=dojox.cometd.longPollTransport.tunnelCollapse;this._connect=dojox.cometd.longPollTransport._connect;this.deliver=dojox.cometd.longPollTransport.deliver;this.openTunnelWith=function(_71,url){this._cometd._polling=true;dojo.io.script.get({load:dojo.hitch(this,function(_73){this._cometd._polling=false;this._cometd.deliver(_73);this.tunnelCollapse();}),error:dojo.hitch(this,function(err){this._cometd._polling=false;console.debug("tunnel opening failed:",err);dojo.publish("/cometd/meta",[{cometd:this._cometd,action:"connect",successful:false,state:this._cometd.state()}]);this._cometd._backoff();this.tunnelCollapse();}),url:(url||this._cometd.url),content:_71,callbackParamName:"jsonp"});};this.sendMessages=function(_75){for(var i=0;i<_75.length;i++){_75[i].clientId=this._cometd.clientId;_75[i].id=""+this._cometd.messageId++;_75[i]=this._cometd._extendOut(_75[i]);}var _77={url:this._cometd.url||djConfig["cometdRoot"],load:dojo.hitch(this._cometd,"deliver"),callbackParamName:"jsonp",content:{message:dojo.toJson(_75)}};return dojo.io.script.get(_77);};this.startup=function(_78){if(this._cometd._connected){return;}this.tunnelInit();};this.disconnect=dojox.cometd.longPollTransport.disconnect;this.disconnect=function(){var _79={channel:"/meta/disconnect",clientId:this._cometd.clientId,id:""+this._cometd.messageId++};_79=this._cometd._extendOut(_79);dojo.io.script.get({url:this._cometd.url||djConfig["cometdRoot"],callbackParamName:"jsonp",content:{message:dojo.toJson([_79])}});};};dojox.cometd.connectionTypes.register("long-polling",dojox.cometd.longPollTransport.check,dojox.cometd.longPollTransport);dojox.cometd.connectionTypes.register("callback-polling",dojox.cometd.callbackPollTransport.check,dojox.cometd.callbackPollTransport);dojo.addOnUnload(dojox.cometd,"_onUnload");}if(!dojo._hasResource["dojox.cometd"]){dojo._hasResource["dojox.cometd"]=true;dojo.provide("dojox.cometd");}
