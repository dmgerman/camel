begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.zipkin.starter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|zipkin
operator|.
name|starter
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.zipkin"
argument_list|)
DECL|class|ZipkinConfigurationProperties
specifier|public
class|class
name|ZipkinConfigurationProperties
block|{
DECL|field|hostName
specifier|private
name|String
name|hostName
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|rate
specifier|private
name|float
name|rate
init|=
literal|1.0f
decl_stmt|;
DECL|field|includeMessageBody
specifier|private
name|boolean
name|includeMessageBody
decl_stmt|;
DECL|field|serviceName
specifier|private
name|String
name|serviceName
decl_stmt|;
DECL|method|getHostName ()
specifier|public
name|String
name|getHostName
parameter_list|()
block|{
return|return
name|hostName
return|;
block|}
comment|/**      * Sets a hostname for the remote zipkin server to use.      */
DECL|method|setHostName (String hostName)
specifier|public
name|void
name|setHostName
parameter_list|(
name|String
name|hostName
parameter_list|)
block|{
name|this
operator|.
name|hostName
operator|=
name|hostName
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * Sets the port number for the remote zipkin server to use.      */
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getRate ()
specifier|public
name|float
name|getRate
parameter_list|()
block|{
return|return
name|rate
return|;
block|}
comment|/**      * Configures a rate that decides how many events should be traced by zipkin.      * The rate is expressed as a percentage (1.0f = 100%, 0.5f is 50%, 0.1f is 10%).      *      * @param rate minimum sample rate is 0.0001, or 0.01% of traces      */
DECL|method|setRate (float rate)
specifier|public
name|void
name|setRate
parameter_list|(
name|float
name|rate
parameter_list|)
block|{
name|this
operator|.
name|rate
operator|=
name|rate
expr_stmt|;
block|}
DECL|method|isIncludeMessageBody ()
specifier|public
name|boolean
name|isIncludeMessageBody
parameter_list|()
block|{
return|return
name|includeMessageBody
return|;
block|}
comment|/**      * Whether to include the Camel message body in the zipkin traces.      *<p/>      * This is not recommended for production usage, or when having big payloads. You can limit the size by      * configuring the<a href="http://camel.apache.org/how-do-i-set-the-max-chars-when-debug-logging-messages-in-camel.html">max debug log size</a>.      */
DECL|method|setIncludeMessageBody (boolean includeMessageBody)
specifier|public
name|void
name|setIncludeMessageBody
parameter_list|(
name|boolean
name|includeMessageBody
parameter_list|)
block|{
name|this
operator|.
name|includeMessageBody
operator|=
name|includeMessageBody
expr_stmt|;
block|}
DECL|method|getServiceName ()
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
comment|/**      * To use a global service name that matches all Camel events      */
DECL|method|setServiceName (String serviceName)
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
block|}
block|}
end_class

end_unit

