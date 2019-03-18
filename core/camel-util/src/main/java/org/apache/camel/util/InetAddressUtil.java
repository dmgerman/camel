begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|UnknownHostException
import|;
end_import

begin_comment
comment|/**  * Util class for {@link java.net.InetAddress}  */
end_comment

begin_class
DECL|class|InetAddressUtil
specifier|public
specifier|final
class|class
name|InetAddressUtil
block|{
DECL|method|InetAddressUtil ()
specifier|private
name|InetAddressUtil
parameter_list|()
block|{
comment|// util class
block|}
comment|/**      * When using the {@link java.net.InetAddress#getHostName()} method in an      * environment where neither a proper DNS lookup nor an<tt>/etc/hosts</tt>      * entry exists for a given host, the following exception will be thrown:      *<p/>      *<code>      * java.net.UnknownHostException:&lt;hostname&gt;:&lt;hostname&gt;      * at java.net.InetAddress.getLocalHost(InetAddress.java:1425)      * ...      *</code>      *<p/>      * Instead of just throwing an UnknownHostException and giving up, this      * method grabs a suitable hostname from the exception and prevents the      * exception from being thrown. If a suitable hostname cannot be acquired      * from the exception, only then is the<tt>UnknownHostException</tt> thrown.      *      * @return the hostname      * @throws UnknownHostException is thrown if hostname could not be resolved      */
DECL|method|getLocalHostName ()
specifier|public
specifier|static
name|String
name|getLocalHostName
parameter_list|()
throws|throws
name|UnknownHostException
block|{
try|try
block|{
return|return
operator|(
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
operator|)
operator|.
name|getHostName
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|uhe
parameter_list|)
block|{
name|String
name|host
init|=
name|uhe
operator|.
name|getMessage
argument_list|()
decl_stmt|;
comment|// host = "hostname: hostname"
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
name|int
name|colon
init|=
name|host
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|colon
operator|>
literal|0
condition|)
block|{
return|return
name|host
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|colon
argument_list|)
return|;
block|}
block|}
throw|throw
name|uhe
throw|;
block|}
block|}
block|}
end_class

end_unit

