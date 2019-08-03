begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  *<code>SetDataOperation</code> sets the content of a ZooKeeper node. An optional version  * may be specified that the node must currently have for the operation to succeed.  * @see {@link ZooKeeper#setData(String, byte[], int)}  */
end_comment

begin_class
DECL|class|SetDataOperation
specifier|public
class|class
name|SetDataOperation
extends|extends
name|ZooKeeperOperation
argument_list|<
name|byte
index|[]
argument_list|>
block|{
DECL|field|data
specifier|private
name|byte
index|[]
name|data
decl_stmt|;
DECL|field|version
specifier|private
name|int
name|version
init|=
operator|-
literal|1
decl_stmt|;
DECL|method|SetDataOperation (ZooKeeper connection, String node, byte[] data)
specifier|public
name|SetDataOperation
parameter_list|(
name|ZooKeeper
name|connection
parameter_list|,
name|String
name|node
parameter_list|,
name|byte
index|[]
name|data
parameter_list|)
block|{
name|super
argument_list|(
name|connection
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
annotation|@
name|Override
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
try|try
block|{
name|Stat
name|statistics
init|=
name|connection
operator|.
name|setData
argument_list|(
name|node
argument_list|,
name|data
argument_list|,
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
name|format
argument_list|(
literal|"Set data of node '%s'  with '%d' bytes of data, retrieved statistics '%s' "
argument_list|,
name|node
argument_list|,
name|data
operator|!=
literal|null
condition|?
name|data
operator|.
name|length
else|:
literal|0
argument_list|,
name|statistics
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Set data of node '%s' with '%d' bytes of data"
argument_list|,
name|node
argument_list|,
name|data
operator|!=
literal|null
condition|?
name|data
operator|.
name|length
else|:
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|OperationResult
argument_list|<>
argument_list|(
name|data
argument_list|,
name|statistics
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
operator|new
name|OperationResult
argument_list|<>
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
DECL|method|setVersion (int version)
specifier|public
name|void
name|setVersion
parameter_list|(
name|int
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCopy ()
specifier|public
name|ZooKeeperOperation
argument_list|<
name|?
argument_list|>
name|createCopy
parameter_list|()
throws|throws
name|Exception
block|{
name|SetDataOperation
name|copy
init|=
operator|(
name|SetDataOperation
operator|)
name|super
operator|.
name|createCopy
argument_list|()
decl_stmt|;
name|copy
operator|.
name|version
operator|=
operator|-
literal|1
expr_stmt|;
comment|// set the version to -1 for 'any version'
return|return
name|copy
return|;
block|}
block|}
end_class

end_unit

