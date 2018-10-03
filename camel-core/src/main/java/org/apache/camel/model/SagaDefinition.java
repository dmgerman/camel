begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|CamelContext
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
name|RuntimeCamelException
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
name|saga
operator|.
name|SagaProcessorBuilder
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
name|saga
operator|.
name|CamelSagaService
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
name|saga
operator|.
name|CamelSagaStep
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
name|RouteContext
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
name|CamelContextHelper
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
name|util
operator|.
name|ObjectHelper
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
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Enables sagas on the route  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"saga"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|SagaDefinition
specifier|public
class|class
name|SagaDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|SagaDefinition
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SagaDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"REQUIRED"
argument_list|)
DECL|field|propagation
specifier|private
name|SagaPropagation
name|propagation
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"AUTO"
argument_list|)
DECL|field|completionMode
specifier|private
name|SagaCompletionMode
name|completionMode
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|timeoutInMilliseconds
specifier|private
name|Long
name|timeoutInMilliseconds
decl_stmt|;
annotation|@
name|XmlElement
DECL|field|compensation
specifier|private
name|SagaActionUriDefinition
name|compensation
decl_stmt|;
annotation|@
name|XmlElement
DECL|field|completion
specifier|private
name|SagaActionUriDefinition
name|completion
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"option"
argument_list|)
DECL|field|options
specifier|private
name|List
argument_list|<
name|SagaOptionDefinition
argument_list|>
name|options
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|sagaService
specifier|private
name|CamelSagaService
name|sagaService
decl_stmt|;
comment|// TODO add ref for xml configuration
DECL|method|SagaDefinition ()
specifier|public
name|SagaDefinition
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|compensationEndpoint
init|=
name|Optional
operator|.
name|ofNullable
argument_list|(
name|this
operator|.
name|compensation
argument_list|)
operator|.
name|map
argument_list|(
name|SagaActionUriDefinition
operator|::
name|getUri
argument_list|)
operator|.
name|map
argument_list|(
name|routeContext
operator|::
name|resolveEndpoint
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|completionEndpoint
init|=
name|Optional
operator|.
name|ofNullable
argument_list|(
name|this
operator|.
name|completion
argument_list|)
operator|.
name|map
argument_list|(
name|SagaActionUriDefinition
operator|::
name|getUri
argument_list|)
operator|.
name|map
argument_list|(
name|routeContext
operator|::
name|resolveEndpoint
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Expression
argument_list|>
name|optionsMap
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|options
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|SagaOptionDefinition
name|optionDef
range|:
name|this
operator|.
name|options
control|)
block|{
name|String
name|optionName
init|=
name|optionDef
operator|.
name|getOptionName
argument_list|()
decl_stmt|;
name|Expression
name|expr
init|=
name|optionDef
operator|.
name|getExpression
argument_list|()
decl_stmt|;
name|optionsMap
operator|.
name|put
argument_list|(
name|optionName
argument_list|,
name|expr
argument_list|)
expr_stmt|;
block|}
block|}
name|CamelSagaStep
name|step
init|=
operator|new
name|CamelSagaStep
argument_list|(
name|compensationEndpoint
argument_list|,
name|completionEndpoint
argument_list|,
name|optionsMap
argument_list|,
name|Optional
operator|.
name|ofNullable
argument_list|(
name|timeoutInMilliseconds
argument_list|)
argument_list|)
decl_stmt|;
name|SagaPropagation
name|propagation
init|=
name|this
operator|.
name|propagation
decl_stmt|;
if|if
condition|(
name|propagation
operator|==
literal|null
condition|)
block|{
comment|// default propagation mode
name|propagation
operator|=
name|SagaPropagation
operator|.
name|REQUIRED
expr_stmt|;
block|}
name|SagaCompletionMode
name|completionMode
init|=
name|this
operator|.
name|completionMode
decl_stmt|;
if|if
condition|(
name|completionMode
operator|==
literal|null
condition|)
block|{
comment|// default completion mode
name|completionMode
operator|=
name|SagaCompletionMode
operator|.
name|defaultCompletionMode
argument_list|()
expr_stmt|;
block|}
name|Processor
name|childProcessor
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|CamelSagaService
name|camelSagaService
init|=
name|findSagaService
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|camelSagaService
operator|.
name|registerStep
argument_list|(
name|step
argument_list|)
expr_stmt|;
return|return
operator|new
name|SagaProcessorBuilder
argument_list|()
operator|.
name|camelContext
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
operator|.
name|childProcessor
argument_list|(
name|childProcessor
argument_list|)
operator|.
name|sagaService
argument_list|(
name|camelSagaService
argument_list|)
operator|.
name|step
argument_list|(
name|step
argument_list|)
operator|.
name|propagation
argument_list|(
name|propagation
argument_list|)
operator|.
name|completionMode
argument_list|(
name|completionMode
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isAbstract ()
specifier|public
name|boolean
name|isAbstract
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isTopLevelOnly ()
specifier|public
name|boolean
name|isTopLevelOnly
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isWrappingEntireOutput ()
specifier|public
name|boolean
name|isWrappingEntireOutput
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
name|String
name|desc
init|=
name|description
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|desc
argument_list|)
condition|)
block|{
return|return
literal|"saga"
return|;
block|}
else|else
block|{
return|return
literal|"saga["
operator|+
name|desc
operator|+
literal|"]"
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|desc
init|=
name|description
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|desc
argument_list|)
condition|)
block|{
return|return
literal|"Saga -> ["
operator|+
name|outputs
operator|+
literal|"]"
return|;
block|}
else|else
block|{
return|return
literal|"Saga["
operator|+
name|desc
operator|+
literal|"] -> ["
operator|+
name|outputs
operator|+
literal|"]"
return|;
block|}
block|}
comment|// Properties
DECL|method|getCompensation ()
specifier|public
name|SagaActionUriDefinition
name|getCompensation
parameter_list|()
block|{
return|return
name|compensation
return|;
block|}
comment|/**      * The compensation endpoint URI that must be called to compensate all changes done in the route.      * The route corresponding to the compensation URI must perform compensation and complete without error.      *      * If errors occur during compensation, the saga service may call again the compensation URI to retry.      */
DECL|method|setCompensation (SagaActionUriDefinition compensation)
specifier|public
name|void
name|setCompensation
parameter_list|(
name|SagaActionUriDefinition
name|compensation
parameter_list|)
block|{
name|this
operator|.
name|compensation
operator|=
name|compensation
expr_stmt|;
block|}
DECL|method|getCompletion ()
specifier|public
name|SagaActionUriDefinition
name|getCompletion
parameter_list|()
block|{
return|return
name|completion
return|;
block|}
comment|/**      * The completion endpoint URI that will be called when the Saga is completed successfully.      * The route corresponding to the completion URI must perform completion tasks and terminate without error.      *      * If errors occur during completion, the saga service may call again the completion URI to retry.      */
DECL|method|setCompletion (SagaActionUriDefinition completion)
specifier|public
name|void
name|setCompletion
parameter_list|(
name|SagaActionUriDefinition
name|completion
parameter_list|)
block|{
name|this
operator|.
name|completion
operator|=
name|completion
expr_stmt|;
block|}
DECL|method|getPropagation ()
specifier|public
name|SagaPropagation
name|getPropagation
parameter_list|()
block|{
return|return
name|propagation
return|;
block|}
comment|/**      * Set the Saga propagation mode (REQUIRED, REQUIRES_NEW, MANDATORY, SUPPORTS, NOT_SUPPORTED, NEVER).      */
DECL|method|setPropagation (SagaPropagation propagation)
specifier|public
name|void
name|setPropagation
parameter_list|(
name|SagaPropagation
name|propagation
parameter_list|)
block|{
name|this
operator|.
name|propagation
operator|=
name|propagation
expr_stmt|;
block|}
DECL|method|getCompletionMode ()
specifier|public
name|SagaCompletionMode
name|getCompletionMode
parameter_list|()
block|{
return|return
name|completionMode
return|;
block|}
comment|/**      * Determine how the saga should be considered complete. When set to AUTO, the saga is completed when the exchange that      * initiates the saga is processed successfully, or compensated when it completes exceptionally.      *      * When set to MANUAL, the user must complete or compensate the saga using the "saga:complete" or "saga:compensate" endpoints.      */
DECL|method|setCompletionMode (SagaCompletionMode completionMode)
specifier|public
name|void
name|setCompletionMode
parameter_list|(
name|SagaCompletionMode
name|completionMode
parameter_list|)
block|{
name|this
operator|.
name|completionMode
operator|=
name|completionMode
expr_stmt|;
block|}
DECL|method|getSagaService ()
specifier|public
name|CamelSagaService
name|getSagaService
parameter_list|()
block|{
return|return
name|sagaService
return|;
block|}
DECL|method|setSagaService (CamelSagaService sagaService)
specifier|public
name|void
name|setSagaService
parameter_list|(
name|CamelSagaService
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
DECL|method|getOptions ()
specifier|public
name|List
argument_list|<
name|SagaOptionDefinition
argument_list|>
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
comment|/**      * Allows to save properties of the current exchange in order to re-use them in a compensation/completion callback route.      * Options are usually helpful e.g. to store and retrieve identifiers of objects that should be deleted in compensating actions.      *      * Option values will be transformed into input headers of the compensation/completion exchange.      */
DECL|method|setOptions (List<SagaOptionDefinition> options)
specifier|public
name|void
name|setOptions
parameter_list|(
name|List
argument_list|<
name|SagaOptionDefinition
argument_list|>
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
DECL|method|getTimeoutInMilliseconds ()
specifier|public
name|Long
name|getTimeoutInMilliseconds
parameter_list|()
block|{
return|return
name|timeoutInMilliseconds
return|;
block|}
comment|/**      * Set the maximum amount of time for the Saga. After the timeout is expired, the saga will be compensated      * automatically (unless a different decision has been taken in the meantime).      */
DECL|method|setTimeoutInMilliseconds (Long timeoutInMilliseconds)
specifier|public
name|void
name|setTimeoutInMilliseconds
parameter_list|(
name|Long
name|timeoutInMilliseconds
parameter_list|)
block|{
name|this
operator|.
name|timeoutInMilliseconds
operator|=
name|timeoutInMilliseconds
expr_stmt|;
block|}
DECL|method|addOption (String option, Expression expression)
specifier|private
name|void
name|addOption
parameter_list|(
name|String
name|option
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|options
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|options
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|options
operator|.
name|add
argument_list|(
operator|new
name|SagaOptionDefinition
argument_list|(
name|option
argument_list|,
name|expression
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Builders
DECL|method|compensation (String compensation)
specifier|public
name|SagaDefinition
name|compensation
parameter_list|(
name|String
name|compensation
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|compensation
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Compensation has already been set"
argument_list|)
throw|;
block|}
name|this
operator|.
name|compensation
operator|=
operator|new
name|SagaActionUriDefinition
argument_list|(
name|compensation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|completion (String completion)
specifier|public
name|SagaDefinition
name|completion
parameter_list|(
name|String
name|completion
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|completion
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Completion has already been set"
argument_list|)
throw|;
block|}
name|this
operator|.
name|completion
operator|=
operator|new
name|SagaActionUriDefinition
argument_list|(
name|completion
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|propagation (SagaPropagation propagation)
specifier|public
name|SagaDefinition
name|propagation
parameter_list|(
name|SagaPropagation
name|propagation
parameter_list|)
block|{
name|setPropagation
argument_list|(
name|propagation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|sagaService (CamelSagaService sagaService)
specifier|public
name|SagaDefinition
name|sagaService
parameter_list|(
name|CamelSagaService
name|sagaService
parameter_list|)
block|{
name|setSagaService
argument_list|(
name|sagaService
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|completionMode (SagaCompletionMode completionMode)
specifier|public
name|SagaDefinition
name|completionMode
parameter_list|(
name|SagaCompletionMode
name|completionMode
parameter_list|)
block|{
name|setCompletionMode
argument_list|(
name|completionMode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|option (String option, Expression expression)
specifier|public
name|SagaDefinition
name|option
parameter_list|(
name|String
name|option
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|addOption
argument_list|(
name|option
argument_list|,
name|expression
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|timeout (long timeout, TimeUnit unit)
specifier|public
name|SagaDefinition
name|timeout
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|setTimeoutInMilliseconds
argument_list|(
name|unit
operator|.
name|toMillis
argument_list|(
name|timeout
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Utils
DECL|method|findSagaService (CamelContext context)
specifier|protected
name|CamelSagaService
name|findSagaService
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|CamelSagaService
name|sagaService
init|=
name|getSagaService
argument_list|()
decl_stmt|;
if|if
condition|(
name|sagaService
operator|!=
literal|null
condition|)
block|{
return|return
name|sagaService
return|;
block|}
name|sagaService
operator|=
name|context
operator|.
name|hasService
argument_list|(
name|CamelSagaService
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|sagaService
operator|!=
literal|null
condition|)
block|{
return|return
name|sagaService
return|;
block|}
name|sagaService
operator|=
name|CamelContextHelper
operator|.
name|findByType
argument_list|(
name|context
argument_list|,
name|CamelSagaService
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|sagaService
operator|!=
literal|null
condition|)
block|{
return|return
name|sagaService
return|;
block|}
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot find a CamelSagaService"
argument_list|)
throw|;
block|}
DECL|method|description ()
specifier|protected
name|String
name|description
parameter_list|()
block|{
name|StringBuilder
name|desc
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|addField
argument_list|(
name|desc
argument_list|,
literal|"compensation"
argument_list|,
name|compensation
argument_list|)
expr_stmt|;
name|addField
argument_list|(
name|desc
argument_list|,
literal|"completion"
argument_list|,
name|completion
argument_list|)
expr_stmt|;
name|addField
argument_list|(
name|desc
argument_list|,
literal|"propagation"
argument_list|,
name|propagation
argument_list|)
expr_stmt|;
return|return
name|desc
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|addField (StringBuilder builder, String key, Object value)
specifier|private
name|void
name|addField
parameter_list|(
name|StringBuilder
name|builder
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|builder
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

