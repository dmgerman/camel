begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Processor
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
name|Producer
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
name|Metadata
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
name|UriEndpoint
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
name|UriPath
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
name|DefaultEndpoint
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
name|JavaRDDLike
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
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
operator|.
name|getLogger
import|;
end_import

begin_comment
comment|/**  * The spark component can be used to send RDD or DataFrame jobs to Apache Spark cluster.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.17.0"
argument_list|,
name|scheme
operator|=
literal|"spark"
argument_list|,
name|title
operator|=
literal|"Apache Spark"
argument_list|,
name|syntax
operator|=
literal|"spark:endpointType"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"bigdata,iot"
argument_list|)
DECL|class|SparkEndpoint
specifier|public
class|class
name|SparkEndpoint
extends|extends
name|DefaultEndpoint
block|{
comment|// Logger
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|getLogger
argument_list|(
name|SparkEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Endpoint collaborators
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|endpointType
specifier|private
name|EndpointType
name|endpointType
decl_stmt|;
annotation|@
name|UriParam
DECL|field|rdd
specifier|private
name|JavaRDDLike
name|rdd
decl_stmt|;
annotation|@
name|UriParam
DECL|field|rddCallback
specifier|private
name|RddCallback
name|rddCallback
decl_stmt|;
annotation|@
name|UriParam
DECL|field|dataFrame
specifier|private
name|Dataset
argument_list|<
name|Row
argument_list|>
name|dataFrame
decl_stmt|;
annotation|@
name|UriParam
DECL|field|dataFrameCallback
specifier|private
name|DataFrameCallback
name|dataFrameCallback
decl_stmt|;
comment|// Endpoint configuration
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|collect
specifier|private
name|boolean
name|collect
init|=
literal|true
decl_stmt|;
comment|// Constructors
DECL|method|SparkEndpoint (String endpointUri, SparkComponent component, EndpointType endpointType)
specifier|public
name|SparkEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|SparkComponent
name|component
parameter_list|,
name|EndpointType
name|endpointType
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpointType
operator|=
name|endpointType
expr_stmt|;
block|}
comment|// Life-cycle
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|rdd
operator|==
literal|null
condition|)
block|{
name|rdd
operator|=
name|getComponent
argument_list|()
operator|.
name|getRdd
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|rddCallback
operator|==
literal|null
condition|)
block|{
name|rddCallback
operator|=
name|getComponent
argument_list|()
operator|.
name|getRddCallback
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Overridden
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating {} Spark producer."
argument_list|,
name|endpointType
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpointType
operator|==
name|EndpointType
operator|.
name|rdd
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"About to create RDD producer."
argument_list|)
expr_stmt|;
return|return
operator|new
name|RddSparkProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|endpointType
operator|==
name|EndpointType
operator|.
name|dataframe
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"About to create DataFrame producer."
argument_list|)
expr_stmt|;
return|return
operator|new
name|DataFrameSparkProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"About to create Hive producer."
argument_list|)
expr_stmt|;
return|return
operator|new
name|HiveSparkProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Spark component supports producer endpoints only."
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|// Setters& getters
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|SparkComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|SparkComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|getEndpointType ()
specifier|public
name|EndpointType
name|getEndpointType
parameter_list|()
block|{
return|return
name|endpointType
return|;
block|}
comment|/**      * Type of the endpoint (rdd, dataframe, hive).      */
DECL|method|setEndpointType (EndpointType endpointType)
specifier|public
name|void
name|setEndpointType
parameter_list|(
name|EndpointType
name|endpointType
parameter_list|)
block|{
name|this
operator|.
name|endpointType
operator|=
name|endpointType
expr_stmt|;
block|}
DECL|method|getRdd ()
specifier|public
name|JavaRDDLike
name|getRdd
parameter_list|()
block|{
return|return
name|rdd
return|;
block|}
comment|/**      * RDD to compute against.      */
DECL|method|setRdd (JavaRDDLike rdd)
specifier|public
name|void
name|setRdd
parameter_list|(
name|JavaRDDLike
name|rdd
parameter_list|)
block|{
name|this
operator|.
name|rdd
operator|=
name|rdd
expr_stmt|;
block|}
DECL|method|getRddCallback ()
specifier|public
name|RddCallback
name|getRddCallback
parameter_list|()
block|{
return|return
name|rddCallback
return|;
block|}
comment|/**      * Function performing action against an RDD.      */
DECL|method|setRddCallback (RddCallback rddCallback)
specifier|public
name|void
name|setRddCallback
parameter_list|(
name|RddCallback
name|rddCallback
parameter_list|)
block|{
name|this
operator|.
name|rddCallback
operator|=
name|rddCallback
expr_stmt|;
block|}
DECL|method|getDataFrame ()
specifier|public
name|Dataset
argument_list|<
name|Row
argument_list|>
name|getDataFrame
parameter_list|()
block|{
return|return
name|dataFrame
return|;
block|}
comment|/**      * DataFrame to compute against.      */
DECL|method|setDataFrame (Dataset<Row> dataFrame)
specifier|public
name|void
name|setDataFrame
parameter_list|(
name|Dataset
argument_list|<
name|Row
argument_list|>
name|dataFrame
parameter_list|)
block|{
name|this
operator|.
name|dataFrame
operator|=
name|dataFrame
expr_stmt|;
block|}
DECL|method|getDataFrameCallback ()
specifier|public
name|DataFrameCallback
name|getDataFrameCallback
parameter_list|()
block|{
return|return
name|dataFrameCallback
return|;
block|}
comment|/**      * Function performing action against an DataFrame.      */
DECL|method|setDataFrameCallback (DataFrameCallback dataFrameCallback)
specifier|public
name|void
name|setDataFrameCallback
parameter_list|(
name|DataFrameCallback
name|dataFrameCallback
parameter_list|)
block|{
name|this
operator|.
name|dataFrameCallback
operator|=
name|dataFrameCallback
expr_stmt|;
block|}
DECL|method|isCollect ()
specifier|public
name|boolean
name|isCollect
parameter_list|()
block|{
return|return
name|collect
return|;
block|}
comment|/**      * Indicates if results should be collected or counted.      */
DECL|method|setCollect (boolean collect)
specifier|public
name|void
name|setCollect
parameter_list|(
name|boolean
name|collect
parameter_list|)
block|{
name|this
operator|.
name|collect
operator|=
name|collect
expr_stmt|;
block|}
block|}
end_class

end_unit

