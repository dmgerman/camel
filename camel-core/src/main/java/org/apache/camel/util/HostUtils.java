begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|NetworkInterface
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_class
DECL|class|HostUtils
specifier|public
specifier|final
class|class
name|HostUtils
block|{
DECL|method|HostUtils ()
specifier|private
name|HostUtils
parameter_list|()
block|{
comment|//Utility Class
block|}
comment|/**      * Returns a {@link} of {@link InetAddress} per {@link NetworkInterface} as a {@link Map}.      */
DECL|method|getNetworkInterfaceAddresses ()
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|InetAddress
argument_list|>
argument_list|>
name|getNetworkInterfaceAddresses
parameter_list|()
block|{
comment|//JVM returns interfaces in a non-predictable order, so to make this more predictable
comment|//let's have them sort by interface name (by using a TreeMap).
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|InetAddress
argument_list|>
argument_list|>
name|interfaceAddressMap
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|InetAddress
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|Enumeration
name|ifaces
init|=
name|NetworkInterface
operator|.
name|getNetworkInterfaces
argument_list|()
decl_stmt|;
while|while
condition|(
name|ifaces
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|NetworkInterface
name|iface
init|=
operator|(
name|NetworkInterface
operator|)
name|ifaces
operator|.
name|nextElement
argument_list|()
decl_stmt|;
comment|//We only care about usable non-loopback interfaces.
if|if
condition|(
name|iface
operator|.
name|isUp
argument_list|()
operator|&&
operator|!
name|iface
operator|.
name|isLoopback
argument_list|()
operator|&&
operator|!
name|iface
operator|.
name|isPointToPoint
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|iface
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|InetAddress
argument_list|>
name|ifaceAdresses
init|=
name|iface
operator|.
name|getInetAddresses
argument_list|()
decl_stmt|;
while|while
condition|(
name|ifaceAdresses
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|InetAddress
name|ia
init|=
name|ifaceAdresses
operator|.
name|nextElement
argument_list|()
decl_stmt|;
comment|//We want to filter out mac addresses
if|if
condition|(
operator|!
name|ia
operator|.
name|isLoopbackAddress
argument_list|()
operator|&&
operator|!
name|ia
operator|.
name|getHostAddress
argument_list|()
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|Set
argument_list|<
name|InetAddress
argument_list|>
name|addresses
init|=
name|interfaceAddressMap
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|addresses
operator|==
literal|null
condition|)
block|{
name|addresses
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|InetAddress
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|addresses
operator|.
name|add
argument_list|(
name|ia
argument_list|)
expr_stmt|;
name|interfaceAddressMap
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|addresses
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SocketException
name|ex
parameter_list|)
block|{
comment|//noop
block|}
return|return
name|interfaceAddressMap
return|;
block|}
comment|/**      * Returns a {@link Set} of {@link InetAddress} that are non-loopback or mac.      */
DECL|method|getAddresses ()
specifier|public
specifier|static
name|Set
argument_list|<
name|InetAddress
argument_list|>
name|getAddresses
parameter_list|()
block|{
name|Set
argument_list|<
name|InetAddress
argument_list|>
name|allAddresses
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|InetAddress
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|InetAddress
argument_list|>
argument_list|>
name|interfaceAddressMap
init|=
name|getNetworkInterfaceAddresses
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|InetAddress
argument_list|>
argument_list|>
name|entry
range|:
name|interfaceAddressMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|InetAddress
argument_list|>
name|addresses
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|addresses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|InetAddress
name|address
range|:
name|addresses
control|)
block|{
name|allAddresses
operator|.
name|add
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|allAddresses
return|;
block|}
comment|/**      * Chooses one of the available {@link InetAddress} based on the specified preference.      */
DECL|method|chooseAddress ()
specifier|private
specifier|static
name|InetAddress
name|chooseAddress
parameter_list|()
throws|throws
name|UnknownHostException
block|{
name|Set
argument_list|<
name|InetAddress
argument_list|>
name|addresses
init|=
name|getAddresses
argument_list|()
decl_stmt|;
if|if
condition|(
name|addresses
operator|.
name|contains
argument_list|(
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
argument_list|)
condition|)
block|{
comment|//Then if local host address is not bound to a loop-back interface, use it.
return|return
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|addresses
operator|!=
literal|null
operator|&&
operator|!
name|addresses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|//else return the first available addrress
return|return
name|addresses
operator|.
name|toArray
argument_list|(
operator|new
name|InetAddress
index|[
name|addresses
operator|.
name|size
argument_list|()
index|]
argument_list|)
index|[
literal|0
index|]
return|;
block|}
else|else
block|{
comment|//else we are forcedt to use the localhost address.
return|return
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
return|;
block|}
block|}
comment|/**      * Returns the local hostname. It loops through the network interfaces and returns the first non loopback hostname      */
DECL|method|getLocalHostName ()
specifier|public
specifier|static
name|String
name|getLocalHostName
parameter_list|()
throws|throws
name|UnknownHostException
block|{
return|return
name|chooseAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
return|;
block|}
comment|/**      * Returns the local IP. It loops through the network interfaces and returns the first non loopback address      */
DECL|method|getLocalIp ()
specifier|public
specifier|static
name|String
name|getLocalIp
parameter_list|()
throws|throws
name|UnknownHostException
block|{
return|return
name|chooseAddress
argument_list|()
operator|.
name|getHostAddress
argument_list|()
return|;
block|}
block|}
end_class

end_unit

