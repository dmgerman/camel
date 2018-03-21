begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.call
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
operator|.
name|call
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CompletableFuture
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|core
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|OpcUaServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|api
operator|.
name|AccessContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|api
operator|.
name|DataItem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|api
operator|.
name|MethodInvocationHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|api
operator|.
name|MonitoredItem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|api
operator|.
name|Namespace
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|api
operator|.
name|ServerNodeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|model
operator|.
name|nodes
operator|.
name|objects
operator|.
name|FolderNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|nodes
operator|.
name|AttributeContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|nodes
operator|.
name|ServerNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|nodes
operator|.
name|UaFolderNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|nodes
operator|.
name|UaMethodNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|util
operator|.
name|SubscriptionModel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|Identifiers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|StatusCodes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|UaException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|DataValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|LocalizedText
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|NodeId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|QualifiedName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|StatusCode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|unsigned
operator|.
name|UShort
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|enumerated
operator|.
name|TimestampsToReturn
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|structured
operator|.
name|ReadValueId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|structured
operator|.
name|WriteValue
import|;
end_import

begin_class
DECL|class|MockNamespace
specifier|public
class|class
name|MockNamespace
implements|implements
name|Namespace
block|{
DECL|field|FOLDER_ID
specifier|public
specifier|static
specifier|final
name|int
name|FOLDER_ID
init|=
literal|1
decl_stmt|;
DECL|field|URI
specifier|public
specifier|static
specifier|final
name|String
name|URI
init|=
literal|"urn:mock:namespace"
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|UShort
name|index
decl_stmt|;
DECL|field|nodeMap
specifier|private
specifier|final
name|ServerNodeMap
name|nodeMap
decl_stmt|;
DECL|field|subscriptionModel
specifier|private
specifier|final
name|SubscriptionModel
name|subscriptionModel
decl_stmt|;
DECL|method|MockNamespace (final UShort index, final OpcUaServer server, List<UaMethodNode> methods)
specifier|public
name|MockNamespace
parameter_list|(
specifier|final
name|UShort
name|index
parameter_list|,
specifier|final
name|OpcUaServer
name|server
parameter_list|,
name|List
argument_list|<
name|UaMethodNode
argument_list|>
name|methods
parameter_list|)
block|{
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|this
operator|.
name|nodeMap
operator|=
name|server
operator|.
name|getNodeMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|subscriptionModel
operator|=
operator|new
name|SubscriptionModel
argument_list|(
name|server
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|registerItems
argument_list|(
name|methods
argument_list|)
expr_stmt|;
block|}
DECL|method|registerItems (List<UaMethodNode> methods)
specifier|private
name|void
name|registerItems
parameter_list|(
name|List
argument_list|<
name|UaMethodNode
argument_list|>
name|methods
parameter_list|)
block|{
comment|// create a folder
specifier|final
name|UaFolderNode
name|folder
init|=
operator|new
name|UaFolderNode
argument_list|(
name|this
operator|.
name|nodeMap
argument_list|,
operator|new
name|NodeId
argument_list|(
name|this
operator|.
name|index
argument_list|,
name|FOLDER_ID
argument_list|)
argument_list|,
operator|new
name|QualifiedName
argument_list|(
name|this
operator|.
name|index
argument_list|,
literal|"FooBarFolder"
argument_list|)
argument_list|,
name|LocalizedText
operator|.
name|english
argument_list|(
literal|"Foo Bar Folder"
argument_list|)
argument_list|)
decl_stmt|;
comment|// add our folder to the objects folder
name|this
operator|.
name|nodeMap
operator|.
name|getNode
argument_list|(
name|Identifiers
operator|.
name|ObjectsFolder
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|node
lambda|->
block|{
operator|(
operator|(
name|FolderNode
operator|)
name|node
operator|)
operator|.
name|addComponent
argument_list|(
name|folder
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
comment|// add method calls
name|methods
operator|.
name|forEach
argument_list|(
name|folder
operator|::
name|addComponent
argument_list|)
expr_stmt|;
block|}
comment|// default method implementations follow
annotation|@
name|Override
DECL|method|read (final ReadContext context, final Double maxAge, final TimestampsToReturn timestamps, final List<ReadValueId> readValueIds)
specifier|public
name|void
name|read
parameter_list|(
specifier|final
name|ReadContext
name|context
parameter_list|,
specifier|final
name|Double
name|maxAge
parameter_list|,
specifier|final
name|TimestampsToReturn
name|timestamps
parameter_list|,
specifier|final
name|List
argument_list|<
name|ReadValueId
argument_list|>
name|readValueIds
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|DataValue
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|readValueIds
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|ReadValueId
name|id
range|:
name|readValueIds
control|)
block|{
specifier|final
name|ServerNode
name|node
init|=
name|this
operator|.
name|nodeMap
operator|.
name|get
argument_list|(
name|id
operator|.
name|getNodeId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|DataValue
name|value
init|=
name|node
operator|!=
literal|null
condition|?
name|node
operator|.
name|readAttribute
argument_list|(
operator|new
name|AttributeContext
argument_list|(
name|context
argument_list|)
argument_list|,
name|id
operator|.
name|getAttributeId
argument_list|()
argument_list|)
else|:
operator|new
name|DataValue
argument_list|(
name|StatusCodes
operator|.
name|Bad_NodeIdUnknown
argument_list|)
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|// report back with result
name|context
operator|.
name|complete
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (final WriteContext context, final List<WriteValue> writeValues)
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|WriteContext
name|context
parameter_list|,
specifier|final
name|List
argument_list|<
name|WriteValue
argument_list|>
name|writeValues
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|StatusCode
argument_list|>
name|results
init|=
name|writeValues
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|value
lambda|->
block|{
if|if
condition|(
name|this
operator|.
name|nodeMap
operator|.
name|containsKey
argument_list|(
name|value
operator|.
name|getNodeId
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|StatusCode
argument_list|(
name|StatusCodes
operator|.
name|Bad_NotWritable
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|StatusCode
argument_list|(
name|StatusCodes
operator|.
name|Bad_NodeIdUnknown
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
decl_stmt|;
comment|// report back with result
name|context
operator|.
name|complete
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|browse (final AccessContext context, final NodeId nodeId)
specifier|public
name|CompletableFuture
argument_list|<
name|List
argument_list|<
name|Reference
argument_list|>
argument_list|>
name|browse
parameter_list|(
specifier|final
name|AccessContext
name|context
parameter_list|,
specifier|final
name|NodeId
name|nodeId
parameter_list|)
block|{
specifier|final
name|ServerNode
name|node
init|=
name|this
operator|.
name|nodeMap
operator|.
name|get
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
return|return
name|CompletableFuture
operator|.
name|completedFuture
argument_list|(
name|node
operator|.
name|getReferences
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
specifier|final
name|CompletableFuture
argument_list|<
name|List
argument_list|<
name|Reference
argument_list|>
argument_list|>
name|f
init|=
operator|new
name|CompletableFuture
argument_list|<>
argument_list|()
decl_stmt|;
name|f
operator|.
name|completeExceptionally
argument_list|(
operator|new
name|UaException
argument_list|(
name|StatusCodes
operator|.
name|Bad_NodeIdUnknown
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|f
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getInvocationHandler (final NodeId methodId)
specifier|public
name|Optional
argument_list|<
name|MethodInvocationHandler
argument_list|>
name|getInvocationHandler
parameter_list|(
specifier|final
name|NodeId
name|methodId
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|this
operator|.
name|nodeMap
operator|.
name|get
argument_list|(
name|methodId
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|n
lambda|->
name|n
operator|instanceof
name|UaMethodNode
argument_list|)
operator|.
name|flatMap
argument_list|(
name|n
lambda|->
block|{
name|final
name|UaMethodNode
name|m
operator|=
operator|(
name|UaMethodNode
operator|)
name|n
argument_list|;             return
name|m
operator|.
name|getInvocationHandler
argument_list|()
argument_list|;
block|}
block|)
class|;
end_class

begin_function
unit|}      @
name|Override
DECL|method|onDataItemsCreated (final List<DataItem> dataItems)
specifier|public
name|void
name|onDataItemsCreated
parameter_list|(
specifier|final
name|List
argument_list|<
name|DataItem
argument_list|>
name|dataItems
parameter_list|)
block|{
name|this
operator|.
name|subscriptionModel
operator|.
name|onDataItemsCreated
argument_list|(
name|dataItems
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|onDataItemsModified (final List<DataItem> dataItems)
specifier|public
name|void
name|onDataItemsModified
parameter_list|(
specifier|final
name|List
argument_list|<
name|DataItem
argument_list|>
name|dataItems
parameter_list|)
block|{
name|this
operator|.
name|subscriptionModel
operator|.
name|onDataItemsModified
argument_list|(
name|dataItems
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|onDataItemsDeleted (final List<DataItem> dataItems)
specifier|public
name|void
name|onDataItemsDeleted
parameter_list|(
specifier|final
name|List
argument_list|<
name|DataItem
argument_list|>
name|dataItems
parameter_list|)
block|{
name|this
operator|.
name|subscriptionModel
operator|.
name|onDataItemsDeleted
argument_list|(
name|dataItems
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|onMonitoringModeChanged (final List<MonitoredItem> monitoredItems)
specifier|public
name|void
name|onMonitoringModeChanged
parameter_list|(
specifier|final
name|List
argument_list|<
name|MonitoredItem
argument_list|>
name|monitoredItems
parameter_list|)
block|{
name|this
operator|.
name|subscriptionModel
operator|.
name|onMonitoringModeChanged
argument_list|(
name|monitoredItems
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|getNamespaceIndex ()
specifier|public
name|UShort
name|getNamespaceIndex
parameter_list|()
block|{
return|return
name|this
operator|.
name|index
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|getNamespaceUri ()
specifier|public
name|String
name|getNamespaceUri
parameter_list|()
block|{
return|return
name|URI
return|;
block|}
end_function

unit|}
end_unit

