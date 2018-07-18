begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kestrel.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kestrel
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
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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

begin_comment
comment|/**  * The kestrel component allows messages to be sent to (or consumed from)  * Kestrel brokers.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.kestrel"
argument_list|)
DECL|class|KestrelComponentConfiguration
specifier|public
class|class
name|KestrelComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the kestrel component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use a shared configured configuration as base for creating new      * endpoints.      */
DECL|field|configuration
specifier|private
name|KestrelConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|KestrelConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( KestrelConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|KestrelConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
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
DECL|class|KestrelConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|KestrelConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kestrel
operator|.
name|KestrelConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * The address(es) on which kestrel is running          */
DECL|field|addresses
specifier|private
name|String
index|[]
name|addresses
decl_stmt|;
comment|/**          * How long a given wait should block (server side), in milliseconds          */
DECL|field|waitTimeMs
specifier|private
name|Integer
name|waitTimeMs
init|=
literal|100
decl_stmt|;
comment|/**          * How many concurrent listeners to schedule for the thread pool          */
DECL|field|concurrentConsumers
specifier|private
name|Integer
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
DECL|method|getAddresses ()
specifier|public
name|String
index|[]
name|getAddresses
parameter_list|()
block|{
return|return
name|addresses
return|;
block|}
DECL|method|setAddresses (String[] addresses)
specifier|public
name|void
name|setAddresses
parameter_list|(
name|String
index|[]
name|addresses
parameter_list|)
block|{
name|this
operator|.
name|addresses
operator|=
name|addresses
expr_stmt|;
block|}
DECL|method|getWaitTimeMs ()
specifier|public
name|Integer
name|getWaitTimeMs
parameter_list|()
block|{
return|return
name|waitTimeMs
return|;
block|}
DECL|method|setWaitTimeMs (Integer waitTimeMs)
specifier|public
name|void
name|setWaitTimeMs
parameter_list|(
name|Integer
name|waitTimeMs
parameter_list|)
block|{
name|this
operator|.
name|waitTimeMs
operator|=
name|waitTimeMs
expr_stmt|;
block|}
DECL|method|getConcurrentConsumers ()
specifier|public
name|Integer
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
DECL|method|setConcurrentConsumers (Integer concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|Integer
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

