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
name|impl
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
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|AbstractJavaRDDLike
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
name|DataFrame
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
comment|/**  * Spark endpoint can be used to create various type of producers, including RDD-, DataFrame- and Hive-based.  */
end_comment

begin_comment
comment|// @UriEndpoint(scheme = "spark", producerOnly = true, title = "Apache Spark", syntax = "spark:jobType", label = "bigdata,iot")
end_comment

begin_class
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
name|UriParam
argument_list|(
name|name
operator|=
literal|"rdd"
argument_list|,
name|description
operator|=
literal|"RDD to compute against."
argument_list|)
DECL|field|rdd
specifier|private
name|AbstractJavaRDDLike
name|rdd
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"rddCallback"
argument_list|,
name|description
operator|=
literal|"Function performing action against an RDD."
argument_list|)
DECL|field|rddCallback
specifier|private
name|RddCallback
name|rddCallback
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"dataFrame"
argument_list|,
name|description
operator|=
literal|"DataFrame to compute against."
argument_list|)
DECL|field|dataFrame
specifier|private
name|DataFrame
name|dataFrame
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"dataFrameCallback"
argument_list|,
name|description
operator|=
literal|"Function performing action against an DataFrame."
argument_list|)
DECL|field|dataFrameCallback
specifier|private
name|DataFrameCallback
name|dataFrameCallback
decl_stmt|;
comment|// Endpoint configuration
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"endpointType"
argument_list|,
name|description
operator|=
literal|"Type of the endpoint (rdd, dataframe, hive)."
argument_list|)
DECL|field|endpointType
specifier|private
specifier|final
name|EndpointType
name|endpointType
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"collect"
argument_list|,
name|description
operator|=
literal|"Indicates if results should be collected or counted."
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
name|debug
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
name|debug
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
name|debug
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
name|debug
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
DECL|method|getRdd ()
specifier|public
name|AbstractJavaRDDLike
name|getRdd
parameter_list|()
block|{
return|return
name|rdd
return|;
block|}
DECL|method|setRdd (AbstractJavaRDDLike rdd)
specifier|public
name|void
name|setRdd
parameter_list|(
name|AbstractJavaRDDLike
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
name|DataFrame
name|getDataFrame
parameter_list|()
block|{
return|return
name|dataFrame
return|;
block|}
DECL|method|setDataFrame (DataFrame dataFrame)
specifier|public
name|void
name|setDataFrame
parameter_list|(
name|DataFrame
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

