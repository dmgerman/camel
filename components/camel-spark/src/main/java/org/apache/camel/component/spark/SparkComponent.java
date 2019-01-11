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
name|spi
operator|.
name|annotations
operator|.
name|Component
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
name|DefaultComponent
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

begin_class
annotation|@
name|Component
argument_list|(
literal|"spark"
argument_list|)
DECL|class|SparkComponent
specifier|public
class|class
name|SparkComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|rdd
specifier|private
name|JavaRDDLike
name|rdd
decl_stmt|;
DECL|field|rddCallback
specifier|private
name|RddCallback
name|rddCallback
decl_stmt|;
DECL|method|SparkComponent ()
specifier|public
name|SparkComponent
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
name|SparkEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|type
argument_list|)
return|;
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
block|}
end_class

end_unit

