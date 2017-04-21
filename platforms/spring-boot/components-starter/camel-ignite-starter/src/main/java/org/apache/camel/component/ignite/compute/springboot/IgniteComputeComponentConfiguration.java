begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.compute.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
operator|.
name|compute
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|Ignite
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|configuration
operator|.
name|IgniteConfiguration
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
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The Ignite Compute endpoint is one of camel-ignite endpoints which allows you  * to run compute operations on the cluster by passing in an IgniteCallable an  * IgniteRunnable an IgniteClosure or collections of them along with their  * parameters if necessary.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.ignite-compute"
argument_list|)
DECL|class|IgniteComputeComponentConfiguration
specifier|public
class|class
name|IgniteComputeComponentConfiguration
block|{
comment|/**      * Sets the Ignite instance.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|ignite
specifier|private
name|Ignite
name|ignite
decl_stmt|;
comment|/**      * Sets the resource from where to load the configuration. It can be a: URI      * String (URI) or an InputStream.      */
DECL|field|configurationResource
specifier|private
name|Object
name|configurationResource
decl_stmt|;
comment|/**      * Allows the user to set a programmatic IgniteConfiguration.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|igniteConfiguration
specifier|private
name|IgniteConfiguration
name|igniteConfiguration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getIgnite ()
specifier|public
name|Ignite
name|getIgnite
parameter_list|()
block|{
return|return
name|ignite
return|;
block|}
DECL|method|setIgnite (Ignite ignite)
specifier|public
name|void
name|setIgnite
parameter_list|(
name|Ignite
name|ignite
parameter_list|)
block|{
name|this
operator|.
name|ignite
operator|=
name|ignite
expr_stmt|;
block|}
DECL|method|getConfigurationResource ()
specifier|public
name|Object
name|getConfigurationResource
parameter_list|()
block|{
return|return
name|configurationResource
return|;
block|}
DECL|method|setConfigurationResource (Object configurationResource)
specifier|public
name|void
name|setConfigurationResource
parameter_list|(
name|Object
name|configurationResource
parameter_list|)
block|{
name|this
operator|.
name|configurationResource
operator|=
name|configurationResource
expr_stmt|;
block|}
DECL|method|getIgniteConfiguration ()
specifier|public
name|IgniteConfiguration
name|getIgniteConfiguration
parameter_list|()
block|{
return|return
name|igniteConfiguration
return|;
block|}
DECL|method|setIgniteConfiguration (IgniteConfiguration igniteConfiguration)
specifier|public
name|void
name|setIgniteConfiguration
parameter_list|(
name|IgniteConfiguration
name|igniteConfiguration
parameter_list|)
block|{
name|this
operator|.
name|igniteConfiguration
operator|=
name|igniteConfiguration
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

