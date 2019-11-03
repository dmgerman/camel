begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nitrite.operation.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nitrite
operator|.
name|operation
operator|.
name|common
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
name|nitrite
operator|.
name|NitriteConstants
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
name|nitrite
operator|.
name|NitriteEndpoint
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
name|nitrite
operator|.
name|operation
operator|.
name|AbstractPayloadAwareOperation
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
name|nitrite
operator|.
name|operation
operator|.
name|CommonOperation
import|;
end_import

begin_comment
comment|/**  * Upsert (Insert or Update) document in collection or object in ObjectRepository. If parameter not specified, updates document from message body  */
end_comment

begin_class
DECL|class|UpsertOperation
specifier|public
class|class
name|UpsertOperation
extends|extends
name|AbstractPayloadAwareOperation
implements|implements
name|CommonOperation
block|{
DECL|method|UpsertOperation (Object payload)
specifier|public
name|UpsertOperation
parameter_list|(
name|Object
name|payload
parameter_list|)
block|{
name|super
argument_list|(
name|payload
argument_list|)
expr_stmt|;
block|}
DECL|method|UpsertOperation ()
specifier|public
name|UpsertOperation
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|execute (Exchange exchange, NitriteEndpoint endpoint)
specifier|protected
name|void
name|execute
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|NitriteEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setHeader
argument_list|(
name|NitriteConstants
operator|.
name|WRITE_RESULT
argument_list|,
name|endpoint
operator|.
name|getNitriteCollection
argument_list|()
operator|.
name|update
argument_list|(
name|getPayload
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

