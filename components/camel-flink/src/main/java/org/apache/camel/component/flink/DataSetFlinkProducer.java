begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flink
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flink
package|;
end_package

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
name|flink
operator|.
name|api
operator|.
name|java
operator|.
name|DataSet
import|;
end_import

begin_class
DECL|class|DataSetFlinkProducer
specifier|public
class|class
name|DataSetFlinkProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|DataSetFlinkProducer (FlinkEndpoint endpoint)
specifier|public
name|DataSetFlinkProducer
parameter_list|(
name|FlinkEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
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
name|DataSet
name|ds
init|=
name|resolveDataSet
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|DataSetCallback
name|dataSetCallback
init|=
name|resolveDataSetCallback
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Object
name|result
decl_stmt|;
name|ClassLoader
name|tccl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|DataSet
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|List
condition|)
block|{
name|List
name|list
init|=
operator|(
name|List
operator|)
name|body
decl_stmt|;
name|Object
index|[]
name|array
init|=
name|list
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
name|list
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|result
operator|=
name|dataSetCallback
operator|.
name|onDataSet
argument_list|(
name|ds
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|dataSetCallback
operator|.
name|onDataSet
argument_list|(
name|ds
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|tccl
argument_list|)
expr_stmt|;
block|}
name|collectResults
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|FlinkEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|FlinkEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|collectResults (Exchange exchange, Object result)
specifier|protected
name|void
name|collectResults
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|result
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|result
operator|instanceof
name|DataSet
condition|)
block|{
name|DataSet
name|dsResults
init|=
operator|(
name|DataSet
operator|)
name|result
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isCollect
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|dsResults
operator|.
name|collect
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|FlinkConstants
operator|.
name|FLINK_DATASET_HEADER
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resolveDataSet (Exchange exchange)
specifier|protected
name|DataSet
name|resolveDataSet
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FlinkConstants
operator|.
name|FLINK_DATASET_HEADER
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|DataSet
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FlinkConstants
operator|.
name|FLINK_DATASET_HEADER
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getDataSet
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getDataSet
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No DataSet defined"
argument_list|)
throw|;
block|}
block|}
DECL|method|resolveDataSetCallback (Exchange exchange)
specifier|protected
name|DataSetCallback
name|resolveDataSetCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FlinkConstants
operator|.
name|FLINK_DATASET_CALLBACK_HEADER
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|DataSetCallback
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FlinkConstants
operator|.
name|FLINK_DATASET_CALLBACK_HEADER
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getDataSetCallback
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getDataSetCallback
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot resolve DataSet callback."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

