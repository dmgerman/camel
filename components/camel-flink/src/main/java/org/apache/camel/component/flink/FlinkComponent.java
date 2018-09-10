begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|flink
operator|.
name|api
operator|.
name|java
operator|.
name|DataSet
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
name|streaming
operator|.
name|api
operator|.
name|datastream
operator|.
name|DataStream
import|;
end_import

begin_comment
comment|/**  * The flink component can be used to send DataSet or DataStream jobs to Apache Flink cluster.  */
end_comment

begin_class
DECL|class|FlinkComponent
specifier|public
class|class
name|FlinkComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|ds
specifier|private
name|DataSet
name|ds
decl_stmt|;
DECL|field|dataSetCallback
specifier|private
name|DataSetCallback
name|dataSetCallback
decl_stmt|;
DECL|field|dataStream
specifier|private
name|DataStream
name|dataStream
decl_stmt|;
DECL|field|dataStreamCallback
specifier|private
name|DataStreamCallback
name|dataStreamCallback
decl_stmt|;
DECL|method|FlinkComponent ()
specifier|public
name|FlinkComponent
parameter_list|()
block|{     }
annotation|@
name|Override
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
name|EndpointType
name|type
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|EndpointType
operator|.
name|class
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
return|return
operator|new
name|FlinkEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|getDataSet ()
specifier|public
name|DataSet
name|getDataSet
parameter_list|()
block|{
return|return
name|ds
return|;
block|}
comment|/**      * DataSet to compute against.      */
DECL|method|setDataSet (DataSet ds)
specifier|public
name|void
name|setDataSet
parameter_list|(
name|DataSet
name|ds
parameter_list|)
block|{
name|this
operator|.
name|ds
operator|=
name|ds
expr_stmt|;
block|}
DECL|method|getDataStream ()
specifier|public
name|DataStream
name|getDataStream
parameter_list|()
block|{
return|return
name|dataStream
return|;
block|}
comment|/**      * DataStream to compute against.      */
DECL|method|setDataStream (DataStream dataStream)
specifier|public
name|void
name|setDataStream
parameter_list|(
name|DataStream
name|dataStream
parameter_list|)
block|{
name|this
operator|.
name|dataStream
operator|=
name|dataStream
expr_stmt|;
block|}
DECL|method|getDataSetCallback ()
specifier|public
name|DataSetCallback
name|getDataSetCallback
parameter_list|()
block|{
return|return
name|dataSetCallback
return|;
block|}
comment|/**      * Function performing action against a DataSet.      */
DECL|method|setDataSetCallback (DataSetCallback dataSetCallback)
specifier|public
name|void
name|setDataSetCallback
parameter_list|(
name|DataSetCallback
name|dataSetCallback
parameter_list|)
block|{
name|this
operator|.
name|dataSetCallback
operator|=
name|dataSetCallback
expr_stmt|;
block|}
DECL|method|getDataStreamCallback ()
specifier|public
name|DataStreamCallback
name|getDataStreamCallback
parameter_list|()
block|{
return|return
name|dataStreamCallback
return|;
block|}
comment|/**      * Function performing action against a DataStream.      */
DECL|method|setDataStreamCallback (DataStreamCallback dataStreamCallback)
specifier|public
name|void
name|setDataStreamCallback
parameter_list|(
name|DataStreamCallback
name|dataStreamCallback
parameter_list|)
block|{
name|this
operator|.
name|dataStreamCallback
operator|=
name|dataStreamCallback
expr_stmt|;
block|}
block|}
end_class

end_unit

