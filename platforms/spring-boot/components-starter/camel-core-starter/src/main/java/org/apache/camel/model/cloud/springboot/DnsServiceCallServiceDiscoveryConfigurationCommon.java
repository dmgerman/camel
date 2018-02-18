begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.cloud.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|cloud
operator|.
name|springboot
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
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_comment
comment|/**  * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
DECL|class|DnsServiceCallServiceDiscoveryConfigurationCommon
specifier|public
class|class
name|DnsServiceCallServiceDiscoveryConfigurationCommon
block|{
comment|/**      * The transport protocol of the desired service.      */
DECL|field|proto
specifier|private
name|String
name|proto
init|=
literal|"_tcp"
decl_stmt|;
comment|/**      * The domain name;      */
DECL|field|domain
specifier|private
name|String
name|domain
decl_stmt|;
comment|/**      * Set client properties to use. These properties are specific to what      * service call implementation are in use. For example if using ribbon, then      * the client properties are define in      * com.netflix.client.config.CommonClientConfigKey.      */
DECL|field|properties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
decl_stmt|;
DECL|method|getProto ()
specifier|public
name|String
name|getProto
parameter_list|()
block|{
return|return
name|proto
return|;
block|}
DECL|method|setProto (String proto)
specifier|public
name|void
name|setProto
parameter_list|(
name|String
name|proto
parameter_list|)
block|{
name|this
operator|.
name|proto
operator|=
name|proto
expr_stmt|;
block|}
DECL|method|getDomain ()
specifier|public
name|String
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
DECL|method|setDomain (String domain)
specifier|public
name|void
name|setDomain
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|setProperties (Map<String, String> properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
block|}
end_class

end_unit

