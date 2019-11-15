begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|Message
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
name|component
operator|.
name|zookeeper
operator|.
name|operations
operator|.
name|CreateOperation
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
name|component
operator|.
name|zookeeper
operator|.
name|operations
operator|.
name|DeleteOperation
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
name|component
operator|.
name|zookeeper
operator|.
name|operations
operator|.
name|GetChildrenOperation
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
name|component
operator|.
name|zookeeper
operator|.
name|operations
operator|.
name|OperationResult
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
name|component
operator|.
name|zookeeper
operator|.
name|operations
operator|.
name|SetDataOperation
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
name|support
operator|.
name|DefaultProducer
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
name|support
operator|.
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|AsyncCallback
operator|.
name|StatCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|AsyncCallback
operator|.
name|VoidCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|CreateMode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|KeeperException
operator|.
name|Code
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|ZooKeeper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|data
operator|.
name|Stat
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|String
operator|.
name|format
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
operator|.
name|ZooKeeperUtils
operator|.
name|getAclListFromMessage
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
operator|.
name|ZooKeeperUtils
operator|.
name|getCreateMode
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
operator|.
name|ZooKeeperUtils
operator|.
name|getCreateModeFromString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
operator|.
name|ZooKeeperUtils
operator|.
name|getNodeFromMessage
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
operator|.
name|ZooKeeperUtils
operator|.
name|getPayloadFromExchange
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
operator|.
name|ZooKeeperUtils
operator|.
name|getVersionFromMessage
import|;
end_import

begin_comment
comment|/**  *<code>ZooKeeperProducer</code> attempts to set the content of nodes in the  * {@link ZooKeeper} cluster with the payloads of the of the exchanges it  * receives.  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|class|ZooKeeperProducer
specifier|public
class|class
name|ZooKeeperProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|ZK_OPERATION_WRITE
specifier|public
specifier|static
specifier|final
name|String
name|ZK_OPERATION_WRITE
init|=
literal|"WRITE"
decl_stmt|;
DECL|field|ZK_OPERATION_DELETE
specifier|public
specifier|static
specifier|final
name|String
name|ZK_OPERATION_DELETE
init|=
literal|"DELETE"
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|ZooKeeperConfiguration
name|configuration
decl_stmt|;
DECL|field|zkm
specifier|private
name|ZooKeeperConnectionManager
name|zkm
decl_stmt|;
DECL|field|connection
specifier|private
name|ZooKeeper
name|connection
decl_stmt|;
DECL|method|ZooKeeperProducer (ZooKeeperEndpoint endpoint)
specifier|public
name|ZooKeeperProducer
parameter_list|(
name|ZooKeeperEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|this
operator|.
name|zkm
operator|=
name|endpoint
operator|.
name|getConnectionManager
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|connection
operator|==
literal|null
condition|)
block|{
name|connection
operator|=
name|this
operator|.
name|zkm
operator|.
name|getConnection
argument_list|()
expr_stmt|;
block|}
name|ProductionContext
name|context
init|=
operator|new
name|ProductionContext
argument_list|(
name|connection
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|String
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_OPERATION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|isDelete
init|=
name|ZK_OPERATION_DELETE
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
decl_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
if|if
condition|(
name|isDelete
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Deleting znode '%s', waiting for confirmation"
argument_list|,
name|context
operator|.
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|OperationResult
name|result
init|=
name|synchronouslyDelete
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isListChildren
argument_list|()
condition|)
block|{
name|result
operator|=
name|listChildren
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
name|updateExchangeWithResult
argument_list|(
name|context
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Storing data to znode '%s', waiting for confirmation"
argument_list|,
name|context
operator|.
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|OperationResult
name|result
init|=
name|synchronouslySetData
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isListChildren
argument_list|()
condition|)
block|{
name|result
operator|=
name|listChildren
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
name|updateExchangeWithResult
argument_list|(
name|context
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|isDelete
condition|)
block|{
name|asynchronouslyDeleteNode
argument_list|(
name|connection
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|asynchronouslySetDataOnNode
argument_list|(
name|connection
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|connection
operator|=
name|zkm
operator|.
name|getConnection
argument_list|()
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Starting zookeeper producer of '%s'"
argument_list|,
name|configuration
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Shutting down zookeeper producer of '%s'"
argument_list|,
name|configuration
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|zkm
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|asynchronouslyDeleteNode (ZooKeeper connection, ProductionContext context)
specifier|private
name|void
name|asynchronouslyDeleteNode
parameter_list|(
name|ZooKeeper
name|connection
parameter_list|,
name|ProductionContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Deleting node '%s', not waiting for confirmation"
argument_list|,
name|context
operator|.
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|delete
argument_list|(
name|context
operator|.
name|node
argument_list|,
name|context
operator|.
name|version
argument_list|,
operator|new
name|AsyncDeleteCallback
argument_list|()
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|asynchronouslySetDataOnNode (ZooKeeper connection, ProductionContext context)
specifier|private
name|void
name|asynchronouslySetDataOnNode
parameter_list|(
name|ZooKeeper
name|connection
parameter_list|,
name|ProductionContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Storing data to node '%s', not waiting for confirmation"
argument_list|,
name|context
operator|.
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|setData
argument_list|(
name|context
operator|.
name|node
argument_list|,
name|context
operator|.
name|payload
argument_list|,
name|context
operator|.
name|version
argument_list|,
operator|new
name|AsyncSetDataCallback
argument_list|()
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|updateExchangeWithResult (ProductionContext context, OperationResult result)
specifier|private
name|void
name|updateExchangeWithResult
parameter_list|(
name|ProductionContext
name|context
parameter_list|,
name|OperationResult
name|result
parameter_list|)
block|{
name|ZooKeeperMessage
name|out
init|=
operator|new
name|ZooKeeperMessage
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|context
operator|.
name|node
argument_list|,
name|result
operator|.
name|getStatistics
argument_list|()
argument_list|,
name|context
operator|.
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|isOk
argument_list|()
condition|)
block|{
name|out
operator|.
name|setBody
argument_list|(
name|result
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|exchange
operator|.
name|setException
argument_list|(
name|result
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|exchange
operator|.
name|setOut
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|listChildren (ProductionContext context)
specifier|private
name|OperationResult
name|listChildren
parameter_list|(
name|ProductionContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|GetChildrenOperation
argument_list|(
name|context
operator|.
name|connection
argument_list|,
name|configuration
operator|.
name|getPath
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
comment|/** Simple container to avoid passing all these around as parameters */
DECL|class|ProductionContext
specifier|private
class|class
name|ProductionContext
block|{
DECL|field|connection
name|ZooKeeper
name|connection
decl_stmt|;
DECL|field|exchange
name|Exchange
name|exchange
decl_stmt|;
DECL|field|in
name|Message
name|in
decl_stmt|;
DECL|field|payload
name|byte
index|[]
name|payload
decl_stmt|;
DECL|field|version
name|int
name|version
decl_stmt|;
DECL|field|node
name|String
name|node
decl_stmt|;
DECL|method|ProductionContext (ZooKeeper connection, Exchange exchange)
name|ProductionContext
parameter_list|(
name|ZooKeeper
name|connection
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|in
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|getNodeFromMessage
argument_list|(
name|in
argument_list|,
name|configuration
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|getVersionFromMessage
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|this
operator|.
name|payload
operator|=
name|getPayloadFromExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|AsyncSetDataCallback
specifier|private
class|class
name|AsyncSetDataCallback
implements|implements
name|StatCallback
block|{
annotation|@
name|Override
DECL|method|processResult (int rc, String node, Object ctx, Stat statistics)
specifier|public
name|void
name|processResult
parameter_list|(
name|int
name|rc
parameter_list|,
name|String
name|node
parameter_list|,
name|Object
name|ctx
parameter_list|,
name|Stat
name|statistics
parameter_list|)
block|{
if|if
condition|(
name|Code
operator|.
name|NONODE
operator|.
name|equals
argument_list|(
name|Code
operator|.
name|get
argument_list|(
name|rc
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|configuration
operator|.
name|isCreate
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|format
argument_list|(
literal|"Node '%s' did not exist, creating it..."
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|ProductionContext
name|context
init|=
operator|(
name|ProductionContext
operator|)
name|ctx
decl_stmt|;
name|OperationResult
argument_list|<
name|String
argument_list|>
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|result
operator|=
name|createNode
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|format
argument_list|(
literal|"Error trying to create node '%s'"
argument_list|,
name|node
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|==
literal|null
operator|||
operator|!
name|result
operator|.
name|isOk
argument_list|()
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
name|format
argument_list|(
literal|"Error creating node '%s'"
argument_list|,
name|node
argument_list|)
argument_list|,
name|result
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|logStoreComplete
argument_list|(
name|node
argument_list|,
name|statistics
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|AsyncDeleteCallback
specifier|private
class|class
name|AsyncDeleteCallback
implements|implements
name|VoidCallback
block|{
annotation|@
name|Override
DECL|method|processResult (int rc, String path, Object ctx)
specifier|public
name|void
name|processResult
parameter_list|(
name|int
name|rc
parameter_list|,
name|String
name|path
parameter_list|,
name|Object
name|ctx
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
name|format
argument_list|(
literal|"Removed data node '%s'"
argument_list|,
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Removed data node '%s'"
argument_list|,
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|createNode (ProductionContext ctx)
specifier|private
name|OperationResult
argument_list|<
name|String
argument_list|>
name|createNode
parameter_list|(
name|ProductionContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|CreateOperation
name|create
init|=
operator|new
name|CreateOperation
argument_list|(
name|ctx
operator|.
name|connection
argument_list|,
name|ctx
operator|.
name|node
argument_list|)
decl_stmt|;
name|create
operator|.
name|setPermissions
argument_list|(
name|getAclListFromMessage
argument_list|(
name|ctx
operator|.
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|CreateMode
name|mode
init|=
literal|null
decl_stmt|;
name|String
name|modeString
init|=
name|configuration
operator|.
name|getCreateMode
argument_list|()
decl_stmt|;
if|if
condition|(
name|modeString
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|mode
operator|=
name|getCreateModeFromString
argument_list|(
name|modeString
argument_list|,
name|CreateMode
operator|.
name|EPHEMERAL
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{ }
block|}
else|else
block|{
name|mode
operator|=
name|getCreateMode
argument_list|(
name|ctx
operator|.
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|CreateMode
operator|.
name|EPHEMERAL
argument_list|)
expr_stmt|;
block|}
name|create
operator|.
name|setCreateMode
argument_list|(
name|mode
operator|==
literal|null
condition|?
name|CreateMode
operator|.
name|EPHEMERAL
else|:
name|mode
argument_list|)
expr_stmt|;
name|create
operator|.
name|setData
argument_list|(
name|ctx
operator|.
name|payload
argument_list|)
expr_stmt|;
return|return
name|create
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**      * Tries to set the data first and if a no node error is received then an      * attempt will be made to create it instead.      */
DECL|method|synchronouslySetData (ProductionContext ctx)
specifier|private
name|OperationResult
name|synchronouslySetData
parameter_list|(
name|ProductionContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|SetDataOperation
name|setData
init|=
operator|new
name|SetDataOperation
argument_list|(
name|ctx
operator|.
name|connection
argument_list|,
name|ctx
operator|.
name|node
argument_list|,
name|ctx
operator|.
name|payload
argument_list|)
decl_stmt|;
name|setData
operator|.
name|setVersion
argument_list|(
name|ctx
operator|.
name|version
argument_list|)
expr_stmt|;
name|OperationResult
name|result
init|=
name|setData
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isOk
argument_list|()
operator|&&
name|configuration
operator|.
name|isCreate
argument_list|()
operator|&&
name|result
operator|.
name|failedDueTo
argument_list|(
name|Code
operator|.
name|NONODE
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|format
argument_list|(
literal|"Node '%s' did not exist, creating it."
argument_list|,
name|ctx
operator|.
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|createNode
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|synchronouslyDelete (ProductionContext ctx)
specifier|private
name|OperationResult
name|synchronouslyDelete
parameter_list|(
name|ProductionContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|DeleteOperation
name|setData
init|=
operator|new
name|DeleteOperation
argument_list|(
name|ctx
operator|.
name|connection
argument_list|,
name|ctx
operator|.
name|node
argument_list|)
decl_stmt|;
name|setData
operator|.
name|setVersion
argument_list|(
name|ctx
operator|.
name|version
argument_list|)
expr_stmt|;
name|OperationResult
name|result
init|=
name|setData
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isOk
argument_list|()
operator|&&
name|configuration
operator|.
name|isCreate
argument_list|()
operator|&&
name|result
operator|.
name|failedDueTo
argument_list|(
name|Code
operator|.
name|NONODE
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|format
argument_list|(
literal|"Node '%s' did not exist, creating it."
argument_list|,
name|ctx
operator|.
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|createNode
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|logStoreComplete (String path, Stat statistics)
specifier|private
name|void
name|logStoreComplete
parameter_list|(
name|String
name|path
parameter_list|,
name|Stat
name|statistics
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
name|format
argument_list|(
literal|"Stored data to node '%s', and receive statistics %s"
argument_list|,
name|path
argument_list|,
name|statistics
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Stored data to node '%s'"
argument_list|,
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

