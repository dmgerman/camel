begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

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
name|AsyncCallback
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
name|Predicate
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
name|Traceable
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
name|IdAware
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
name|EventHelper
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
name|ExchangeHelper
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
comment|/**  * A processor which catches exceptions.  *  * @version   */
end_comment

begin_class
DECL|class|CatchProcessor
specifier|public
class|class
name|CatchProcessor
extends|extends
name|DelegateAsyncProcessor
implements|implements
name|Traceable
implements|,
name|IdAware
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
name|CatchProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|exceptions
specifier|private
specifier|final
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|exceptions
decl_stmt|;
DECL|field|onWhen
specifier|private
specifier|final
name|Predicate
name|onWhen
decl_stmt|;
DECL|field|handled
specifier|private
specifier|final
name|Predicate
name|handled
decl_stmt|;
DECL|method|CatchProcessor (List<Class<? extends Throwable>> exceptions, Processor processor, Predicate onWhen, Predicate handled)
specifier|public
name|CatchProcessor
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|exceptions
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Predicate
name|onWhen
parameter_list|,
name|Predicate
name|handled
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|exceptions
operator|=
name|exceptions
expr_stmt|;
name|this
operator|.
name|onWhen
operator|=
name|onWhen
expr_stmt|;
name|this
operator|.
name|handled
operator|=
name|handled
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Catch["
operator|+
name|exceptions
operator|+
literal|" -> "
operator|+
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"catch"
return|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Exception
name|e
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|Throwable
name|caught
init|=
name|catches
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
decl_stmt|;
comment|// If a previous catch clause handled the exception or if this clause does not match, exit
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_HANDLED
argument_list|)
operator|!=
literal|null
operator|||
name|caught
operator|==
literal|null
condition|)
block|{
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"This CatchProcessor catches the exception: {} caused by: {}"
argument_list|,
name|caught
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// store the last to endpoint as the failure endpoint
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|)
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_ENDPOINT
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// give the rest of the pipeline another chance
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_HANDLED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// and we should not be regarded as exhausted as we are in a try .. catch block
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_EXHAUSTED
argument_list|)
expr_stmt|;
comment|// is the exception handled by the catch clause
specifier|final
name|boolean
name|handled
init|=
name|handles
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"The exception is handled: {} for the exception: {} caused by: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|handled
block|,
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
block|,
name|e
operator|.
name|getMessage
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|handled
condition|)
block|{
comment|// emit event that the failure is being handled
name|EventHelper
operator|.
name|notifyExchangeFailureHandling
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|processor
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|boolean
name|sync
init|=
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
if|if
condition|(
name|handled
condition|)
block|{
comment|// emit event that the failure was handled
name|EventHelper
operator|.
name|notifyExchangeFailureHandled
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|processor
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// always clear redelivery exhausted in a catch clause
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_EXHAUSTED
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|doneSync
condition|)
block|{
comment|// signal callback to continue routing async
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|sync
return|;
block|}
comment|/**      * Returns with the exception that is caught by this processor.      *      * This method traverses exception causes, so sometimes the exception      * returned from this method might be one of causes of the parameter      * passed.      *      * @param exchange  the current exchange      * @param exception the thrown exception      * @return Throwable that this processor catches.<tt>null</tt> if nothing matches.      */
DECL|method|catches (Exchange exchange, Throwable exception)
specifier|protected
name|Throwable
name|catches
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
comment|// use the exception iterator to walk the caused by hierarchy
for|for
control|(
specifier|final
name|Throwable
name|e
range|:
name|ObjectHelper
operator|.
name|createExceptionIterable
argument_list|(
name|exception
argument_list|)
control|)
block|{
comment|// see if we catch this type
for|for
control|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|exceptions
control|)
block|{
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|e
argument_list|)
operator|&&
name|matchesWhen
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
block|}
comment|// not found
return|return
literal|null
return|;
block|}
comment|/**      * Whether this catch processor handles the exception it have caught      *      * @param exchange  the current exchange      * @return<tt>true</tt> if this processor handles it,<tt>false</tt> otherwise.      */
DECL|method|handles (Exchange exchange)
specifier|protected
name|boolean
name|handles
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|handled
operator|==
literal|null
condition|)
block|{
comment|// handle by default
return|return
literal|true
return|;
block|}
return|return
name|handled
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|getExceptions ()
specifier|public
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|getExceptions
parameter_list|()
block|{
return|return
name|exceptions
return|;
block|}
comment|/**      * Strategy method for matching the exception type with the current exchange.      *<p/>      * This default implementation will match as:      *<ul>      *<li>Always true if no when predicate on the exception type      *<li>Otherwise the when predicate is matches against the current exchange      *</ul>      *      * @param exchange the current {@link org.apache.camel.Exchange}      * @return<tt>true</tt> if matched,<tt>false</tt> otherwise.      */
DECL|method|matchesWhen (Exchange exchange)
specifier|protected
name|boolean
name|matchesWhen
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|onWhen
operator|==
literal|null
condition|)
block|{
comment|// if no predicate then it's always a match
return|return
literal|true
return|;
block|}
return|return
name|onWhen
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

