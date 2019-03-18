begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**      * Sets the POST URL for zipkin's<a href="http://zipkin.io/zipkin-api/#/">v2 api</a>, usually      * "http://zipkinhost:9411/api/v2/spans"      */
DECL|field|endpoint
specifier|private
name|String
name|endpoint
decl_stmt|;
comment|/**      * Sets the hostname if sending spans to a remote zipkin scribe (thrift RPC) collector.      */
DECL|field|hostName
specifier|private
name|String
name|hostName
decl_stmt|;
comment|/**      * Sets the port if sending spans to a remote zipkin scribe (thrift RPC) collector.      */
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
comment|/**      * Configures a rate that decides how many events should be traced by zipkin.      * The rate is expressed as a percentage (1.0f = 100%, 0.5f is 50%, 0.1f is 10%).      */
DECL|field|rate
specifier|private
name|float
name|rate
init|=
literal|1.0f
decl_stmt|;
comment|/**      * Whether to include the Camel message body in the zipkin traces.      *      * This is not recommended for production usage, or when having big payloads.      * You can limit the size by configuring camel.springboot.log-debug-max-chars option.      */
DECL|field|includeMessageBody
specifier|private
name|boolean
name|includeMessageBody
decl_stmt|;
comment|/**      * Whether to include message bodies that are stream based in the zipkin traces.      *      * This is not recommended for production usage, or when having big payloads.      * You can limit the size by configuring camel.springboot.log-debug-max-chars option.      */
DECL|field|includeMessageBodyStreams
specifier|private
name|boolean
name|includeMessageBodyStreams
decl_stmt|;
comment|/**      * To use a global service name that matches all Camel events      */
DECL|field|serviceName
specifier|private
name|String
name|serviceName
decl_stmt|;
comment|/**      * Sets exclude pattern(s) that will disable tracing with zipkin for Camel messages that matches the pattern.      */
DECL|field|excludePatterns
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|excludePatterns
decl_stmt|;
comment|/**      * Sets client service mapping(s) that matches Camel events to the given zipkin service name.      * The key is the pattern, the value is the service name.      */
DECL|field|clientServiceMappings
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|clientServiceMappings
decl_stmt|;
comment|/**      * Sets server service mapping(s) that matches Camel events to the given zipkin service name.      * The key is the pattern, the value is the service name.      */
DECL|field|serverServiceMappings
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|serverServiceMappings
decl_stmt|;
comment|// Getters& setters
DECL|method|getEndpoint ()
specifier|public
name|String
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|setEndpoint (String endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|String
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
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
DECL|method|isIncludeMessageBodyStreams ()
specifier|public
name|boolean
name|isIncludeMessageBodyStreams
parameter_list|()
block|{
return|return
name|includeMessageBodyStreams
return|;
block|}
DECL|method|setIncludeMessageBodyStreams (boolean includeMessageBodyStreams)
specifier|public
name|void
name|setIncludeMessageBodyStreams
parameter_list|(
name|boolean
name|includeMessageBodyStreams
parameter_list|)
block|{
name|this
operator|.
name|includeMessageBodyStreams
operator|=
name|includeMessageBodyStreams
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
DECL|method|getExcludePatterns ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getExcludePatterns
parameter_list|()
block|{
return|return
name|excludePatterns
return|;
block|}
DECL|method|setExcludePatterns (Set<String> excludePatterns)
specifier|public
name|void
name|setExcludePatterns
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|excludePatterns
parameter_list|)
block|{
name|this
operator|.
name|excludePatterns
operator|=
name|excludePatterns
expr_stmt|;
block|}
DECL|method|getClientServiceMappings ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getClientServiceMappings
parameter_list|()
block|{
return|return
name|clientServiceMappings
return|;
block|}
DECL|method|setClientServiceMappings (Map<String, String> clientServiceMappings)
specifier|public
name|void
name|setClientServiceMappings
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|clientServiceMappings
parameter_list|)
block|{
name|this
operator|.
name|clientServiceMappings
operator|=
name|clientServiceMappings
expr_stmt|;
block|}
DECL|method|getServerServiceMappings ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getServerServiceMappings
parameter_list|()
block|{
return|return
name|serverServiceMappings
return|;
block|}
DECL|method|setServerServiceMappings (Map<String, String> serverServiceMappings)
specifier|public
name|void
name|setServerServiceMappings
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|serverServiceMappings
parameter_list|)
block|{
name|this
operator|.
name|serverServiceMappings
operator|=
name|serverServiceMappings
expr_stmt|;
block|}
block|}
end_class

end_unit

