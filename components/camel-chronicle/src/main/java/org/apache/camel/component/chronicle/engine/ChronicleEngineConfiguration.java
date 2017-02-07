begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.chronicle.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|chronicle
operator|.
name|engine
package|;
end_package

begin_import
import|import
name|net
operator|.
name|openhft
operator|.
name|chronicle
operator|.
name|wire
operator|.
name|WireType
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

begin_class
annotation|@
name|UriParams
DECL|class|ChronicleEngineConfiguration
specifier|public
class|class
name|ChronicleEngineConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"BINARY"
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|wireType
specifier|private
name|WireType
name|wireType
init|=
name|WireType
operator|.
name|BINARY
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|subscribeMapEvents
specifier|private
name|boolean
name|subscribeMapEvents
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|filteredMapEvents
specifier|private
name|String
index|[]
name|filteredMapEvents
decl_stmt|;
annotation|@
name|UriParam
DECL|field|subscribeTopologicalEvents
specifier|private
name|boolean
name|subscribeTopologicalEvents
decl_stmt|;
annotation|@
name|UriParam
DECL|field|subscribeTopicEvents
specifier|private
name|boolean
name|subscribeTopicEvents
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"PUBLISH,PUBLISH_AND_INDEX,PPUT,PGET_AND_PUT,PPUT_ALL,PPUT_IF_ABSENT,PGET,PGET_AND_REMOVE,PREMOVE,PIS_EMPTY,PSIZE"
argument_list|)
DECL|field|action
specifier|private
name|String
name|action
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|persistent
specifier|private
name|boolean
name|persistent
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clusterName
specifier|private
name|String
name|clusterName
decl_stmt|;
comment|// ****************************
comment|// CLIENT OPTIONS
comment|// ****************************
DECL|method|getWireType ()
specifier|public
name|WireType
name|getWireType
parameter_list|()
block|{
return|return
name|wireType
return|;
block|}
comment|/**      * The Wire type to use, default to binary wire.      */
DECL|method|setWireType (String wireType)
specifier|public
name|void
name|setWireType
parameter_list|(
name|String
name|wireType
parameter_list|)
block|{
name|setWireType
argument_list|(
name|WireType
operator|.
name|valueOf
argument_list|(
name|wireType
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * The Wire type to use, default to binary wire.      */
DECL|method|setWireType (WireType wireType)
specifier|public
name|void
name|setWireType
parameter_list|(
name|WireType
name|wireType
parameter_list|)
block|{
name|this
operator|.
name|wireType
operator|=
name|wireType
expr_stmt|;
block|}
comment|// ****************************
comment|// MAP EVENTS OPTIONS
comment|// ****************************
DECL|method|isSubscribeMapEvents ()
specifier|public
name|boolean
name|isSubscribeMapEvents
parameter_list|()
block|{
return|return
name|subscribeMapEvents
return|;
block|}
comment|/**      * Set if consumer should subscribe to Map events, default true.      */
DECL|method|setSubscribeMapEvents (boolean subscribeMapEvents)
specifier|public
name|void
name|setSubscribeMapEvents
parameter_list|(
name|boolean
name|subscribeMapEvents
parameter_list|)
block|{
name|this
operator|.
name|subscribeMapEvents
operator|=
name|subscribeMapEvents
expr_stmt|;
block|}
DECL|method|getFilteredMapEvents ()
specifier|public
name|String
index|[]
name|getFilteredMapEvents
parameter_list|()
block|{
return|return
name|filteredMapEvents
return|;
block|}
comment|/**      * A comma separated list of Map event type to filer, valid values are: INSERT, UPDATE, REMOVE.      */
DECL|method|setFilteredMapEvents (String filteredMapEvents)
specifier|public
name|void
name|setFilteredMapEvents
parameter_list|(
name|String
name|filteredMapEvents
parameter_list|)
block|{
name|setFilteredMapEvents
argument_list|(
name|filteredMapEvents
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * The list of Map event type to filer, valid values are: INSERT, UPDATE, REMOVE.      */
DECL|method|setFilteredMapEvents (String[] filteredMapEvents)
specifier|public
name|void
name|setFilteredMapEvents
parameter_list|(
name|String
index|[]
name|filteredMapEvents
parameter_list|)
block|{
name|this
operator|.
name|filteredMapEvents
operator|=
name|filteredMapEvents
expr_stmt|;
block|}
comment|// ****************************
comment|// TOPOLOGICAL EVENTS OPTIONS
comment|// ****************************
DECL|method|isSubscribeTopologicalEvents ()
specifier|public
name|boolean
name|isSubscribeTopologicalEvents
parameter_list|()
block|{
return|return
name|subscribeTopologicalEvents
return|;
block|}
comment|/**      * Set if consumer should subscribe to TopologicalEvents,d efault false.      */
DECL|method|setSubscribeTopologicalEvents (boolean subscribeTopologicalEvents)
specifier|public
name|void
name|setSubscribeTopologicalEvents
parameter_list|(
name|boolean
name|subscribeTopologicalEvents
parameter_list|)
block|{
name|this
operator|.
name|subscribeTopologicalEvents
operator|=
name|subscribeTopologicalEvents
expr_stmt|;
block|}
comment|// ****************************
comment|// TOPIC EVENTS OPTIONS
comment|// ****************************
DECL|method|isSubscribeTopicEvents ()
specifier|public
name|boolean
name|isSubscribeTopicEvents
parameter_list|()
block|{
return|return
name|subscribeTopicEvents
return|;
block|}
comment|/**      * Set if consumer should subscribe to TopicEvents,d efault false.      */
DECL|method|setSubscribeTopicEvents (boolean subscribeTopicEvents)
specifier|public
name|void
name|setSubscribeTopicEvents
parameter_list|(
name|boolean
name|subscribeTopicEvents
parameter_list|)
block|{
name|this
operator|.
name|subscribeTopicEvents
operator|=
name|subscribeTopicEvents
expr_stmt|;
block|}
comment|// ****************************
comment|// Misc
comment|// ****************************
DECL|method|getAction ()
specifier|public
name|String
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
comment|/**      * The default action to perform, valid values are:      * - PUBLISH      * - PPUBLISH_AND_INDEX      * - PPUT      * - PGET_AND_PUT      * - PPUT_ALL      * - PPUT_IF_ABSENT      * - PGET      * - PGET_AND_REMOVE      * - PREMOVE      * - PIS_EMPTY      * - PSIZE      */
DECL|method|setAction (String action)
specifier|public
name|void
name|setAction
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
DECL|method|isPersistent ()
specifier|public
name|boolean
name|isPersistent
parameter_list|()
block|{
return|return
name|persistent
return|;
block|}
comment|/**      * Enable/disable data persistence      */
DECL|method|setPersistent (boolean persistent)
specifier|public
name|void
name|setPersistent
parameter_list|(
name|boolean
name|persistent
parameter_list|)
block|{
name|this
operator|.
name|persistent
operator|=
name|persistent
expr_stmt|;
block|}
DECL|method|getClusterName ()
specifier|public
name|String
name|getClusterName
parameter_list|()
block|{
return|return
name|clusterName
return|;
block|}
comment|/**      * Cluster name for queue      */
DECL|method|setClusterName (String clusterName)
specifier|public
name|void
name|setClusterName
parameter_list|(
name|String
name|clusterName
parameter_list|)
block|{
name|this
operator|.
name|clusterName
operator|=
name|clusterName
expr_stmt|;
block|}
block|}
end_class

end_unit

