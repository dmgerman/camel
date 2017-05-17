begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.multimap.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|multimap
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
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
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
comment|/**  * The hazelcast-multimap component is used to to access Hazelcast distributed  * multimap.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.hazelcast-multimap"
argument_list|)
DECL|class|HazelcastMultimapComponentConfiguration
specifier|public
class|class
name|HazelcastMultimapComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * The hazelcast instance reference which can be used for hazelcast      * endpoint. If you don't specify the instance reference camel use the      * default hazelcast instance from the camel-hazelcast instance.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|hazelcastInstance
specifier|private
name|HazelcastInstance
name|hazelcastInstance
decl_stmt|;
comment|/**      * The hazelcast mode reference which kind of instance should be used. If      * you don't specify the mode then the node mode will be the default.      */
DECL|field|hazelcastMode
specifier|private
name|String
name|hazelcastMode
init|=
literal|"node"
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getHazelcastInstance ()
specifier|public
name|HazelcastInstance
name|getHazelcastInstance
parameter_list|()
block|{
return|return
name|hazelcastInstance
return|;
block|}
DECL|method|setHazelcastInstance (HazelcastInstance hazelcastInstance)
specifier|public
name|void
name|setHazelcastInstance
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|)
block|{
name|this
operator|.
name|hazelcastInstance
operator|=
name|hazelcastInstance
expr_stmt|;
block|}
DECL|method|getHazelcastMode ()
specifier|public
name|String
name|getHazelcastMode
parameter_list|()
block|{
return|return
name|hazelcastMode
return|;
block|}
DECL|method|setHazelcastMode (String hazelcastMode)
specifier|public
name|void
name|setHazelcastMode
parameter_list|(
name|String
name|hazelcastMode
parameter_list|)
block|{
name|this
operator|.
name|hazelcastMode
operator|=
name|hazelcastMode
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

