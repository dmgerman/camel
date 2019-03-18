begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spark
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
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
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|JavaRDD
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|sql
operator|.
name|Dataset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|sql
operator|.
name|Row
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
name|spark
operator|.
name|SparkConstants
operator|.
name|SPARK_DATAFRAME_CALLBACK_HEADER
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
name|spark
operator|.
name|SparkConstants
operator|.
name|SPARK_DATAFRAME_HEADER
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
name|spark
operator|.
name|SparkConstants
operator|.
name|SPARK_RDD_HEADER
import|;
end_import

begin_class
DECL|class|DataFrameSparkProducer
specifier|public
class|class
name|DataFrameSparkProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|DataFrameSparkProducer (SparkEndpoint endpoint)
specifier|public
name|DataFrameSparkProducer
parameter_list|(
name|SparkEndpoint
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
name|Dataset
argument_list|<
name|Row
argument_list|>
name|dataFrame
init|=
name|resolveDataFrame
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|DataFrameCallback
name|dataFrameCallback
init|=
name|resolveDataFrameCallback
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
init|=
name|body
operator|instanceof
name|List
condition|?
name|dataFrameCallback
operator|.
name|onDataFrame
argument_list|(
name|dataFrame
argument_list|,
operator|(
operator|(
name|List
operator|)
name|body
operator|)
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
argument_list|)
else|:
name|dataFrameCallback
operator|.
name|onDataFrame
argument_list|(
name|dataFrame
argument_list|,
name|body
argument_list|)
decl_stmt|;
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
name|SparkEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SparkEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|// Helpers
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
block|{
if|if
condition|(
name|result
operator|instanceof
name|JavaRDD
condition|)
block|{
name|JavaRDD
name|rddResults
init|=
operator|(
name|JavaRDD
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
name|rddResults
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
name|SPARK_RDD_HEADER
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
DECL|method|resolveDataFrame (Exchange exchange)
specifier|protected
name|Dataset
argument_list|<
name|Row
argument_list|>
name|resolveDataFrame
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
name|SPARK_DATAFRAME_HEADER
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|Dataset
argument_list|<
name|Row
argument_list|>
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SPARK_DATAFRAME_HEADER
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getDataFrame
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getDataFrame
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No Data Frame defined."
argument_list|)
throw|;
block|}
block|}
DECL|method|resolveDataFrameCallback (Exchange exchange)
specifier|protected
name|DataFrameCallback
name|resolveDataFrameCallback
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
name|SPARK_DATAFRAME_CALLBACK_HEADER
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|DataFrameCallback
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SPARK_DATAFRAME_CALLBACK_HEADER
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getDataFrameCallback
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getDataFrameCallback
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot resolve Data Frame."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

