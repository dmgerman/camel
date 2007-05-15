begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
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
name|Expression
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
name|bam
operator|.
name|Activity
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
name|bam
operator|.
name|model
operator|.
name|ActivityState
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
name|ProcessorFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision: $  */
end_comment

begin_class
DECL|class|ActivityBuilder
specifier|public
class|class
name|ActivityBuilder
implements|implements
name|ProcessorFactory
block|{
DECL|field|processBuilder
specifier|private
name|ProcessBuilder
name|processBuilder
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|activity
specifier|private
name|Activity
name|activity
decl_stmt|;
DECL|field|correlationExpression
specifier|private
name|Expression
name|correlationExpression
decl_stmt|;
DECL|method|ActivityBuilder (ProcessBuilder processBuilder, Endpoint endpoint)
specifier|public
name|ActivityBuilder
parameter_list|(
name|ProcessBuilder
name|processBuilder
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|processBuilder
operator|=
name|processBuilder
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|activity
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
operator|.
name|Activity
argument_list|(
name|processBuilder
operator|.
name|getProcess
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|activity
operator|.
name|setName
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|createProcessor ()
specifier|public
name|Processor
name|createProcessor
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|processBuilder
operator|.
name|createActivityProcessor
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|// Builder methods
comment|//-----------------------------------------------------------------------
DECL|method|correlate (Expression correlationExpression)
specifier|public
name|ActivityBuilder
name|correlate
parameter_list|(
name|Expression
name|correlationExpression
parameter_list|)
block|{
name|this
operator|.
name|correlationExpression
operator|=
name|correlationExpression
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|name (String name)
specifier|public
name|ActivityBuilder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|activity
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Create a temporal rule for when this step starts      */
DECL|method|starts ()
specifier|public
name|TimeExpression
name|starts
parameter_list|()
block|{
return|return
name|createTimeExpression
argument_list|(
operator|new
name|ActivityExpressionSupport
argument_list|(
name|activity
argument_list|)
block|{
specifier|protected
name|Object
name|evaluateState
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ActivityState
name|state
parameter_list|)
block|{
return|return
name|state
operator|.
name|getStartTime
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * Create a temporal rule for when this step completes      */
DECL|method|completes ()
specifier|public
name|TimeExpression
name|completes
parameter_list|()
block|{
return|return
name|createTimeExpression
argument_list|(
operator|new
name|ActivityExpressionSupport
argument_list|(
name|activity
argument_list|)
block|{
specifier|protected
name|Object
name|evaluateState
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ActivityState
name|state
parameter_list|)
block|{
return|return
name|state
operator|.
name|getCompleteTime
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
DECL|method|getCorrelationExpression ()
specifier|public
name|Expression
name|getCorrelationExpression
parameter_list|()
block|{
return|return
name|correlationExpression
return|;
block|}
DECL|method|getActivity ()
specifier|public
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
operator|.
name|Activity
name|getActivity
parameter_list|()
block|{
return|return
name|activity
return|;
block|}
DECL|method|getProcessBuilder ()
specifier|public
name|ProcessBuilder
name|getProcessBuilder
parameter_list|()
block|{
return|return
name|processBuilder
return|;
block|}
comment|// Implementation methods
comment|//-----------------------------------------------------------------------
DECL|method|createTimeExpression (ActivityExpressionSupport expression)
specifier|protected
name|TimeExpression
name|createTimeExpression
parameter_list|(
name|ActivityExpressionSupport
name|expression
parameter_list|)
block|{
return|return
operator|new
name|TimeExpression
argument_list|(
name|activity
argument_list|,
name|expression
argument_list|)
return|;
block|}
block|}
end_class

end_unit

