begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|builder
operator|.
name|DeadLetterChannelBuilder
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
name|ErrorHandlerBuilderRef
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
name|model
operator|.
name|AggregateDefinition
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
name|model
operator|.
name|ChoiceDefinition
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
name|model
operator|.
name|ConvertBodyDefinition
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
name|model
operator|.
name|ExpressionNode
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
name|model
operator|.
name|FromDefinition
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
name|model
operator|.
name|LoadBalanceDefinition
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
name|model
operator|.
name|OnCompletionDefinition
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
name|model
operator|.
name|OnExceptionDefinition
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
name|model
operator|.
name|OtherwiseDefinition
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
name|model
operator|.
name|OutputDefinition
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|ResequenceDefinition
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
name|model
operator|.
name|RouteDefinition
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
name|model
operator|.
name|RoutesDefinition
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
name|model
operator|.
name|RoutingSlipDefinition
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
name|model
operator|.
name|SendDefinition
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
name|model
operator|.
name|ThrottleDefinition
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
name|model
operator|.
name|WhenDefinition
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
name|processor
operator|.
name|loadbalancer
operator|.
name|FailOverLoadBalancer
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
name|processor
operator|.
name|loadbalancer
operator|.
name|LoadBalancer
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
name|processor
operator|.
name|loadbalancer
operator|.
name|RandomLoadBalancer
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
name|processor
operator|.
name|loadbalancer
operator|.
name|RoundRobinLoadBalancer
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
name|processor
operator|.
name|loadbalancer
operator|.
name|StickyLoadBalancer
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
name|processor
operator|.
name|loadbalancer
operator|.
name|TopicLoadBalancer
import|;
end_import

begin_comment
comment|/**  * Render routes in Groovy language  */
end_comment

begin_class
DECL|class|GroovyRenderer
specifier|public
class|class
name|GroovyRenderer
block|{
DECL|field|header
specifier|public
specifier|static
specifier|final
name|String
name|header
init|=
literal|"import org.apache.camel.language.groovy.GroovyRouteBuilder;\nclass GroovyRoute extends GroovyRouteBuilder {\nvoid configure() {\n"
decl_stmt|;
DECL|field|footer
specifier|public
specifier|static
specifier|final
name|String
name|footer
init|=
literal|"\n}\n}"
decl_stmt|;
comment|/**      * render a RouteDefinition      *       * @throws IOException      */
DECL|method|renderRoute (StringBuilder buffer, RouteDefinition route)
specifier|public
specifier|static
name|void
name|renderRoute
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|RouteDefinition
name|route
parameter_list|)
block|{
name|List
argument_list|<
name|FromDefinition
argument_list|>
name|inputs
init|=
name|route
operator|.
name|getInputs
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
init|=
name|route
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
comment|// render the error handler
if|if
condition|(
operator|!
operator|(
name|route
operator|.
name|getErrorHandlerBuilder
argument_list|()
operator|instanceof
name|ErrorHandlerBuilderRef
operator|)
condition|)
block|{
if|if
condition|(
name|route
operator|.
name|getErrorHandlerBuilder
argument_list|()
operator|instanceof
name|DeadLetterChannelBuilder
condition|)
block|{
name|DeadLetterChannelBuilder
name|deadLetter
init|=
operator|(
name|DeadLetterChannelBuilder
operator|)
name|route
operator|.
name|getErrorHandlerBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"errorHandler(deadLetterChannel(\""
argument_list|)
operator|.
name|append
argument_list|(
name|deadLetter
operator|.
name|getDeadLetterUri
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|".maximumRedeliveries("
argument_list|)
operator|.
name|append
argument_list|(
name|deadLetter
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getMaximumRedeliveries
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|".redeliverDelay("
argument_list|)
operator|.
name|append
argument_list|(
name|deadLetter
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getRedeliverDelay
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|".handled("
argument_list|)
operator|.
name|append
argument_list|(
name|deadLetter
operator|.
name|getHandledPolicy
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|");"
argument_list|)
expr_stmt|;
block|}
block|}
comment|// render the global dsl not started with from, like global
comment|// onCompletion, onException, intercept
for|for
control|(
name|ProcessorDefinition
name|processor
range|:
name|outputs
control|)
block|{
if|if
condition|(
name|processor
operator|.
name|getParent
argument_list|()
operator|==
literal|null
condition|)
block|{
name|renderProcessor
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|";"
argument_list|)
expr_stmt|;
block|}
block|}
comment|// render the inputs of the router
name|buffer
operator|.
name|append
argument_list|(
literal|"from("
argument_list|)
expr_stmt|;
for|for
control|(
name|FromDefinition
name|input
range|:
name|inputs
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
name|input
operator|.
name|getUri
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|input
operator|!=
name|inputs
operator|.
name|get
argument_list|(
name|inputs
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
comment|// render the outputs of the router
for|for
control|(
name|ProcessorDefinition
name|processor
range|:
name|outputs
control|)
block|{
if|if
condition|(
name|processor
operator|.
name|getParent
argument_list|()
operator|==
name|route
condition|)
block|{
name|renderProcessor
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * render a RoutesDefinition      */
DECL|method|renderRoutes (StringBuilder buffer, RoutesDefinition routes)
specifier|public
specifier|static
name|void
name|renderRoutes
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|RoutesDefinition
name|routes
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
comment|/**      * render a ProcessorDefiniton      */
DECL|method|renderProcessor (StringBuilder buffer, ProcessorDefinition processor)
specifier|private
specifier|static
name|void
name|renderProcessor
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|ProcessorDefinition
name|processor
parameter_list|)
block|{
if|if
condition|(
name|processor
operator|instanceof
name|AggregateDefinition
condition|)
block|{
name|AggregateDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|ChoiceDefinition
condition|)
block|{
name|ChoiceDefinition
name|choice
init|=
operator|(
name|ChoiceDefinition
operator|)
name|processor
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|choice
operator|.
name|getShortName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"()"
argument_list|)
expr_stmt|;
for|for
control|(
name|WhenDefinition
name|when
range|:
name|choice
operator|.
name|getWhenClauses
argument_list|()
control|)
block|{
name|renderProcessor
argument_list|(
name|buffer
argument_list|,
name|when
argument_list|)
expr_stmt|;
block|}
name|OtherwiseDefinition
name|other
init|=
name|choice
operator|.
name|getOtherwise
argument_list|()
decl_stmt|;
if|if
condition|(
name|other
operator|!=
literal|null
condition|)
block|{
name|renderProcessor
argument_list|(
name|buffer
argument_list|,
name|other
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|".end()"
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|ConvertBodyDefinition
condition|)
block|{
name|ConvertBodyDefinition
name|convertBody
init|=
operator|(
name|ConvertBodyDefinition
operator|)
name|processor
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|convertBody
operator|.
name|getShortName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
if|if
condition|(
name|convertBody
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"[B"
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"byte[].class"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|convertBody
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|".class"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|convertBody
operator|.
name|getCharset
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", \""
argument_list|)
operator|.
name|append
argument_list|(
name|convertBody
operator|.
name|getCharset
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|ExpressionNode
condition|)
block|{
name|ExpressionNodeRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|LoadBalanceDefinition
condition|)
block|{
name|LoadBalanceDefinition
name|loadB
init|=
operator|(
name|LoadBalanceDefinition
operator|)
name|processor
decl_stmt|;
comment|// buffer.append(".").append(output.getShortName()).append("()");
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
literal|"loadBalance"
argument_list|)
operator|.
name|append
argument_list|(
literal|"()"
argument_list|)
expr_stmt|;
name|LoadBalancer
name|lb
init|=
name|loadB
operator|.
name|getLoadBalancerType
argument_list|()
operator|.
name|getLoadBalancer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|lb
operator|instanceof
name|FailOverLoadBalancer
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".failover("
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|exceptions
init|=
operator|(
operator|(
name|FailOverLoadBalancer
operator|)
name|lb
operator|)
operator|.
name|getExceptions
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|excep
range|:
name|exceptions
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|excep
operator|.
name|getSimpleName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|".class"
argument_list|)
expr_stmt|;
if|if
condition|(
name|excep
operator|!=
name|exceptions
operator|.
name|get
argument_list|(
name|exceptions
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|lb
operator|instanceof
name|RandomLoadBalancer
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".random()"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|lb
operator|instanceof
name|RoundRobinLoadBalancer
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".roundRobin()"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|lb
operator|instanceof
name|StickyLoadBalancer
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".sticky()"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|lb
operator|instanceof
name|TopicLoadBalancer
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".topic()"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|branches
init|=
name|loadB
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
name|branch
range|:
name|branches
control|)
block|{
name|renderProcessor
argument_list|(
name|buffer
argument_list|,
name|branch
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|OnCompletionDefinition
condition|)
block|{
name|OnCompletionDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|OnExceptionDefinition
condition|)
block|{
name|OnExceptionDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|OutputDefinition
condition|)
block|{
name|OutputDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|ResequenceDefinition
condition|)
block|{
name|ResequenceDefinition
name|resequence
init|=
operator|(
name|ResequenceDefinition
operator|)
name|processor
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|processor
operator|.
name|getShortName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Expression
argument_list|>
name|exps
init|=
name|resequence
operator|.
name|getExpressionList
argument_list|()
decl_stmt|;
for|for
control|(
name|Expression
name|exp
range|:
name|exps
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|exp
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"()"
argument_list|)
expr_stmt|;
if|if
condition|(
name|exp
operator|!=
name|exps
operator|.
name|get
argument_list|(
name|exps
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|RoutingSlipDefinition
condition|)
block|{
name|RoutingSlipDefinition
name|routingSlip
init|=
operator|(
name|RoutingSlipDefinition
operator|)
name|processor
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|routingSlip
operator|.
name|getShortName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"(\""
argument_list|)
operator|.
name|append
argument_list|(
name|routingSlip
operator|.
name|getHeaderName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\", \""
argument_list|)
operator|.
name|append
argument_list|(
name|routingSlip
operator|.
name|getUriDelimiter
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|SendDefinition
condition|)
block|{
name|SendDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|ThrottleDefinition
condition|)
block|{
name|ThrottleDefinition
name|throttle
init|=
operator|(
name|ThrottleDefinition
operator|)
name|processor
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|throttle
operator|.
name|getShortName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|throttle
operator|.
name|getMaximumRequestsPerPeriod
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|throttle
operator|.
name|getTimePeriodMillis
argument_list|()
operator|!=
literal|1000
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".timePeriodMillis("
argument_list|)
operator|.
name|append
argument_list|(
name|throttle
operator|.
name|getTimePeriodMillis
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|processor
operator|.
name|getShortName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"()"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
init|=
name|processor
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
name|nextProcessor
range|:
name|outputs
control|)
block|{
name|renderProcessor
argument_list|(
name|buffer
argument_list|,
name|nextProcessor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

