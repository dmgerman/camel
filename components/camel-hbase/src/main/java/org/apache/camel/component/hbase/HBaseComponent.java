begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hbase
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|hbase
operator|.
name|model
operator|.
name|HBaseCell
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
name|hbase
operator|.
name|model
operator|.
name|HBaseRow
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
name|impl
operator|.
name|DefaultComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|HBaseConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|HTablePool
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link HBaseEndpoint}.  */
end_comment

begin_class
DECL|class|HBaseComponent
specifier|public
class|class
name|HBaseComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|configuration
specifier|private
name|Configuration
name|configuration
decl_stmt|;
DECL|field|tablePool
specifier|private
name|HTablePool
name|tablePool
decl_stmt|;
DECL|field|poolMaxSize
specifier|private
name|int
name|poolMaxSize
init|=
literal|10
decl_stmt|;
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
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
name|HBaseConfiguration
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
name|tablePool
operator|=
operator|new
name|HTablePool
argument_list|(
name|configuration
argument_list|,
name|poolMaxSize
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|tablePool
operator|!=
literal|null
condition|)
block|{
name|tablePool
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|URI
name|endpointUri
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|String
name|tableName
init|=
name|endpointUri
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|HBaseEndpoint
name|endpoint
init|=
operator|new
name|HBaseEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|tablePool
argument_list|,
name|tableName
argument_list|)
decl_stmt|;
name|HBaseRow
name|parameterRowModel
init|=
name|createRowModel
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getRowModel
argument_list|()
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setRowModel
argument_list|(
name|parameterRowModel
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
comment|/**      * Creates an {@link HBaseRow} model from the specified endpoint parameters.      *      * @param parameters      * @return      */
DECL|method|createRowModel (Map<String, Object> parameters)
specifier|public
name|HBaseRow
name|createRowModel
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|HBaseRow
name|rowModel
init|=
operator|new
name|HBaseRow
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_ROW_TYPE
operator|.
name|asOption
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|rowType
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|parameters
operator|.
name|remove
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_ROW_TYPE
operator|.
name|asOption
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rowType
operator|!=
literal|null
operator|&&
operator|!
name|rowType
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|rowModel
operator|.
name|setRowType
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|rowType
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|parameters
operator|.
name|get
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_FAMILY
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
operator|!=
literal|null
operator|&&
name|parameters
operator|.
name|get
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_QUALIFIER
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
operator|!=
literal|null
condition|;
name|i
operator|++
control|)
block|{
name|HBaseCell
name|cellModel
init|=
operator|new
name|HBaseCell
argument_list|()
decl_stmt|;
name|cellModel
operator|.
name|setFamily
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|parameters
operator|.
name|remove
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_FAMILY
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|cellModel
operator|.
name|setQualifier
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|parameters
operator|.
name|remove
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_QUALIFIER
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|cellModel
operator|.
name|setValue
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|parameters
operator|.
name|remove
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_VALUE
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_VALUE_TYPE
operator|.
name|asOption
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|valueType
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|parameters
operator|.
name|remove
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_VALUE_TYPE
operator|.
name|asOption
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|valueType
operator|!=
literal|null
operator|&&
operator|!
name|valueType
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|rowModel
operator|.
name|setRowType
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|valueType
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|rowModel
operator|.
name|getCells
argument_list|()
operator|.
name|add
argument_list|(
name|cellModel
argument_list|)
expr_stmt|;
block|}
return|return
name|rowModel
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Configuration
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
DECL|method|getPoolMaxSize ()
specifier|public
name|int
name|getPoolMaxSize
parameter_list|()
block|{
return|return
name|poolMaxSize
return|;
block|}
DECL|method|setPoolMaxSize (int poolMaxSize)
specifier|public
name|void
name|setPoolMaxSize
parameter_list|(
name|int
name|poolMaxSize
parameter_list|)
block|{
name|this
operator|.
name|poolMaxSize
operator|=
name|poolMaxSize
expr_stmt|;
block|}
block|}
end_class

end_unit

