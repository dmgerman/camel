begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.cw
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|cw
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|Protocol
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|cloudwatch
operator|.
name|AmazonCloudWatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriPath
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|CwConfiguration
specifier|public
class|class
name|CwConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|namespace
specifier|private
name|String
name|namespace
decl_stmt|;
annotation|@
name|UriParam
DECL|field|amazonCwClient
specifier|private
name|AmazonCloudWatch
name|amazonCwClient
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
DECL|field|value
specifier|private
name|Double
name|value
decl_stmt|;
annotation|@
name|UriParam
DECL|field|unit
specifier|private
name|String
name|unit
decl_stmt|;
annotation|@
name|UriParam
DECL|field|timestamp
specifier|private
name|Date
name|timestamp
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"HTTP,HTTPS"
argument_list|,
name|defaultValue
operator|=
literal|"HTTPS"
argument_list|)
DECL|field|proxyProtocol
specifier|private
name|Protocol
name|proxyProtocol
init|=
name|Protocol
operator|.
name|HTTPS
decl_stmt|;
annotation|@
name|UriParam
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
annotation|@
name|UriParam
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|accessKey
return|;
block|}
comment|/**      * Amazon AWS Access Key      */
DECL|method|setAccessKey (String accessKey)
specifier|public
name|void
name|setAccessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|this
operator|.
name|accessKey
operator|=
name|accessKey
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|secretKey
return|;
block|}
comment|/**      * Amazon AWS Secret Key      */
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|this
operator|.
name|secretKey
operator|=
name|secretKey
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * The metric name      */
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|Double
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * The metric value      */
DECL|method|setValue (Double value)
specifier|public
name|void
name|setValue
parameter_list|(
name|Double
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getUnit ()
specifier|public
name|String
name|getUnit
parameter_list|()
block|{
return|return
name|unit
return|;
block|}
comment|/**      * The metric unit      */
DECL|method|setUnit (String unit)
specifier|public
name|void
name|setUnit
parameter_list|(
name|String
name|unit
parameter_list|)
block|{
name|this
operator|.
name|unit
operator|=
name|unit
expr_stmt|;
block|}
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
comment|/**      * The metric namespace      */
DECL|method|setNamespace (String namespace)
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
comment|/**      * The metric timestamp      */
DECL|method|setTimestamp (Date timestamp)
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|Date
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
block|}
DECL|method|getTimestamp ()
specifier|public
name|Date
name|getTimestamp
parameter_list|()
block|{
return|return
name|timestamp
return|;
block|}
DECL|method|getAmazonCwClient ()
specifier|public
name|AmazonCloudWatch
name|getAmazonCwClient
parameter_list|()
block|{
return|return
name|amazonCwClient
return|;
block|}
comment|/**      * To use the AmazonCloudWatch as the client      */
DECL|method|setAmazonCwClient (AmazonCloudWatch amazonCwClient)
specifier|public
name|void
name|setAmazonCwClient
parameter_list|(
name|AmazonCloudWatch
name|amazonCwClient
parameter_list|)
block|{
name|this
operator|.
name|amazonCwClient
operator|=
name|amazonCwClient
expr_stmt|;
block|}
DECL|method|getProxyProtocol ()
specifier|public
name|Protocol
name|getProxyProtocol
parameter_list|()
block|{
return|return
name|proxyProtocol
return|;
block|}
comment|/**      * To define a proxy protocol when instantiating the CW client      */
DECL|method|setProxyProtocol (Protocol proxyProtocol)
specifier|public
name|void
name|setProxyProtocol
parameter_list|(
name|Protocol
name|proxyProtocol
parameter_list|)
block|{
name|this
operator|.
name|proxyProtocol
operator|=
name|proxyProtocol
expr_stmt|;
block|}
DECL|method|getProxyHost ()
specifier|public
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|proxyHost
return|;
block|}
comment|/**      * To define a proxy host when instantiating the CW client      */
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|this
operator|.
name|proxyHost
operator|=
name|proxyHost
expr_stmt|;
block|}
DECL|method|getProxyPort ()
specifier|public
name|Integer
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxyPort
return|;
block|}
comment|/**      * To define a proxy port when instantiating the CW client      */
DECL|method|setProxyPort (Integer proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|this
operator|.
name|proxyPort
operator|=
name|proxyPort
expr_stmt|;
block|}
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|region
return|;
block|}
comment|/**      * The region in which CW client needs to work. When using this parameter, the configuration will expect the capitalized name of the region (for example AP_EAST_1)      * You'll need to use the name Regions.EU_WEST_1.name()      */
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|this
operator|.
name|region
operator|=
name|region
expr_stmt|;
block|}
comment|// *************************************************
comment|//
comment|// *************************************************
DECL|method|copy ()
specifier|public
name|CwConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|CwConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

