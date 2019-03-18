begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|xray
operator|.
name|entities
operator|.
name|Entity
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
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|decorators
operator|.
name|AbstractSegmentDecorator
import|;
end_import

begin_comment
comment|/**  * This interface represents a decorator specific to the component/endpoint being instrumented.  */
end_comment

begin_interface
DECL|interface|SegmentDecorator
specifier|public
interface|interface
name|SegmentDecorator
block|{
comment|/* Prefix for camel component tag */
DECL|field|CAMEL_COMPONENT
name|String
name|CAMEL_COMPONENT
init|=
literal|"camel-"
decl_stmt|;
DECL|field|DEFAULT
name|SegmentDecorator
name|DEFAULT
init|=
operator|new
name|AbstractSegmentDecorator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
comment|/**      * This method indicates whether the component associated with the SegmentDecorator should result in a new segment      * being created.      *      * @return Whether a new segment should be created      */
DECL|method|newSegment ()
name|boolean
name|newSegment
parameter_list|()
function_decl|;
comment|/**      * The camel component associated with the decorator.      *      * @return The camel component name      */
DECL|method|getComponent ()
name|String
name|getComponent
parameter_list|()
function_decl|;
comment|/**      * This method returns the operation name to use with the segment representing this exchange and endpoint.      *      * @param exchange The exchange      * @param endpoint The endpoint      * @return The operation name      */
DECL|method|getOperationName (Exchange exchange, Endpoint endpoint)
name|String
name|getOperationName
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * This method adds appropriate details (tags/logs) to the supplied segment based on the pre processing of the      * exchange.      *      * @param segment The segment      * @param exchange The exchange      * @param endpoint The endpoint      */
DECL|method|pre (Entity segment, Exchange exchange, Endpoint endpoint)
name|void
name|pre
parameter_list|(
name|Entity
name|segment
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * This method adds appropriate details (tags/logs) to the supplied segment based on the post processing of the      * exchange.      *      * @param segment The segment      * @param exchange The exchange      * @param endpoint The endpoint      */
DECL|method|post (Entity segment, Exchange exchange, Endpoint endpoint)
name|void
name|post
parameter_list|(
name|Entity
name|segment
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

