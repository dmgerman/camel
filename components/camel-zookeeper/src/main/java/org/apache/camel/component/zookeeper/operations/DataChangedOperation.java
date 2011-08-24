begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper.operations
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
operator|.
name|operations
package|;
end_package

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
name|DataCallback
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
name|Watcher
operator|.
name|Event
operator|.
name|EventType
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

begin_comment
comment|/**  *<code>DataChangedOperation</code> is an watch driven operation. It will wait  * for an watched event indicating that the data contained in a given  * node has changed before optionally retrieving the changed data.  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|class|DataChangedOperation
specifier|public
class|class
name|DataChangedOperation
extends|extends
name|FutureEventDrivenOperation
argument_list|<
name|byte
index|[]
argument_list|>
block|{
DECL|field|CONSTRUCTOR_ARGS
specifier|protected
specifier|static
specifier|final
name|Class
index|[]
name|CONSTRUCTOR_ARGS
init|=
block|{
name|ZooKeeper
operator|.
name|class
block|,
name|String
operator|.
name|class
block|,
name|boolean
operator|.
name|class
block|}
decl_stmt|;
DECL|field|getChangedData
specifier|private
name|boolean
name|getChangedData
decl_stmt|;
DECL|method|DataChangedOperation (ZooKeeper connection, String znode, boolean getChangedData)
specifier|public
name|DataChangedOperation
parameter_list|(
name|ZooKeeper
name|connection
parameter_list|,
name|String
name|znode
parameter_list|,
name|boolean
name|getChangedData
parameter_list|)
block|{
name|super
argument_list|(
name|connection
argument_list|,
name|znode
argument_list|,
name|EventType
operator|.
name|NodeDataChanged
argument_list|,
name|EventType
operator|.
name|NodeDeleted
argument_list|)
expr_stmt|;
name|this
operator|.
name|getChangedData
operator|=
name|getChangedData
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|installWatch ()
specifier|protected
name|void
name|installWatch
parameter_list|()
block|{
name|connection
operator|.
name|getData
argument_list|(
name|getNode
argument_list|()
argument_list|,
name|this
argument_list|,
operator|new
name|DataCallback
argument_list|()
block|{
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
parameter_list|,
name|byte
index|[]
name|data
parameter_list|,
name|Stat
name|stat
parameter_list|)
block|{             }
block|}
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|getResult ()
specifier|public
name|OperationResult
argument_list|<
name|byte
index|[]
argument_list|>
name|getResult
parameter_list|()
block|{
return|return
name|getChangedData
condition|?
operator|new
name|GetDataOperation
argument_list|(
name|connection
argument_list|,
name|getNode
argument_list|()
argument_list|)
operator|.
name|getResult
argument_list|()
else|:
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createCopy ()
specifier|public
name|ZooKeeperOperation
name|createCopy
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getConstructor
argument_list|(
name|CONSTRUCTOR_ARGS
argument_list|)
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{
name|connection
block|,
name|node
block|,
name|getChangedData
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

