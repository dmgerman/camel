begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.service.lra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|service
operator|.
name|lra
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|builder
operator|.
name|RouteBuilder
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
name|service
operator|.
name|lra
operator|.
name|LRAConstants
operator|.
name|PARTICIPANT_PATH_COMPENSATE
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
name|service
operator|.
name|lra
operator|.
name|LRAConstants
operator|.
name|PARTICIPANT_PATH_COMPLETE
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
name|service
operator|.
name|lra
operator|.
name|LRAConstants
operator|.
name|URL_COMPENSATION_KEY
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
name|service
operator|.
name|lra
operator|.
name|LRAConstants
operator|.
name|URL_COMPLETION_KEY
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|LRASagaRoutes
specifier|public
class|class
name|LRASagaRoutes
extends|extends
name|RouteBuilder
block|{
DECL|field|sagaService
specifier|private
name|LRASagaService
name|sagaService
decl_stmt|;
DECL|method|LRASagaRoutes (LRASagaService sagaService)
specifier|public
name|LRASagaRoutes
parameter_list|(
name|LRASagaService
name|sagaService
parameter_list|)
block|{
name|this
operator|.
name|sagaService
operator|=
name|sagaService
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|rest
argument_list|(
name|sagaService
operator|.
name|getLocalParticipantContextPath
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|PARTICIPANT_PATH_COMPENSATE
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|id
argument_list|(
literal|"lra-compensation"
argument_list|)
operator|.
name|process
argument_list|(
name|this
operator|::
name|verifyRequest
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|URL_COMPENSATION_KEY
argument_list|)
operator|.
name|isNotNull
argument_list|()
argument_list|)
operator|.
name|toD
argument_list|(
literal|"header."
operator|+
name|URL_COMPENSATION_KEY
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|rest
argument_list|(
name|sagaService
operator|.
name|getLocalParticipantContextPath
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|PARTICIPANT_PATH_COMPLETE
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|id
argument_list|(
literal|"lra-completion"
argument_list|)
operator|.
name|process
argument_list|(
name|this
operator|::
name|verifyRequest
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|URL_COMPLETION_KEY
argument_list|)
operator|.
name|isNotNull
argument_list|()
argument_list|)
operator|.
name|toD
argument_list|(
literal|"header."
operator|+
name|URL_COMPLETION_KEY
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
comment|/**      * Check if the request is pointing to an allowed URI to prevent unauthorized remote uri invocation      */
DECL|method|verifyRequest (Exchange exchange)
specifier|private
name|void
name|verifyRequest
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
name|Exchange
operator|.
name|SAGA_LONG_RUNNING_ACTION
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Missing "
operator|+
name|Exchange
operator|.
name|SAGA_LONG_RUNNING_ACTION
operator|+
literal|" header in received request"
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|usedURIs
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|compensationURI
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|URL_COMPENSATION_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|compensationURI
operator|!=
literal|null
condition|)
block|{
name|usedURIs
operator|.
name|add
argument_list|(
name|compensationURI
argument_list|)
expr_stmt|;
block|}
name|String
name|completionURI
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|URL_COMPLETION_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|completionURI
operator|!=
literal|null
condition|)
block|{
name|usedURIs
operator|.
name|add
argument_list|(
name|completionURI
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|uri
range|:
name|usedURIs
control|)
block|{
if|if
condition|(
operator|!
name|sagaService
operator|.
name|getRegisteredURIs
argument_list|()
operator|.
name|contains
argument_list|(
name|uri
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"URI "
operator|+
name|uri
operator|+
literal|" is not allowed"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

