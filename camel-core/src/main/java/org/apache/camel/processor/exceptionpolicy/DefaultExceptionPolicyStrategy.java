begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.exceptionpolicy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|exceptionpolicy
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|model
operator|.
name|ExceptionDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * The default strategy used in Camel to resolve the {@link org.apache.camel.model.ExceptionDefinition} that should  * handle the thrown exception.  *<p/>  *<b>Selection strategy:</b>  *<br/>This strategy applies the following rules:  *<ul>  *<li>Will walk the exception hieracy from bottom upwards till the thrown exception, meaning that the most outer caused  * by is selected first, ending with the thrown exception itself. The method {@link #createExceptionIterator(Throwable)}  * provides the Iterator used for the walking.</li>  *<li>The exception type must be configured with an Exception that is an instance of the thrown exception, this  * is tested using the {@link #filter(org.apache.camel.model.ExceptionDefinition, Class, Throwable)} method.  * By default the filter uses<tt>instanceof</tt> test.</li>  *<li>If the exception type has<b>exactly</b> the thrown exception then its selected as its an exact match</li>  *<li>Otherwise the type that has an exception that is the closets super of the thrown exception is selected  * (recurring up the exception hierarchy)</li>  *</ul>  *<p/>  *<b>Fine grained matching:</b>  *<br/> If the {@link ExceptionDefinition} has a when defined with an expression the type is also matches against  * the current exchange using the {@link #matchesWhen(org.apache.camel.model.ExceptionDefinition, org.apache.camel.Exchange)}  * method. This can be used to for more fine grained matching, so you can e.g. define multiple sets of  * exception types with the same exception class(es) but have a predicate attached to select which to select at runtime.  */
end_comment

begin_class
DECL|class|DefaultExceptionPolicyStrategy
specifier|public
class|class
name|DefaultExceptionPolicyStrategy
implements|implements
name|ExceptionPolicyStrategy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultExceptionPolicyStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|getExceptionPolicy (Map<ExceptionPolicyKey, ExceptionDefinition> exceptionPolicices, Exchange exchange, Throwable exception)
specifier|public
name|ExceptionDefinition
name|getExceptionPolicy
parameter_list|(
name|Map
argument_list|<
name|ExceptionPolicyKey
argument_list|,
name|ExceptionDefinition
argument_list|>
name|exceptionPolicices
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
comment|// recursive up the tree using the iterator
name|Iterator
argument_list|<
name|Throwable
argument_list|>
name|it
init|=
name|createExceptionIterator
argument_list|(
name|exception
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ExceptionDefinition
name|type
init|=
name|findMatchedExceptionPolicy
argument_list|(
name|exceptionPolicices
argument_list|,
name|exchange
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|type
return|;
block|}
block|}
comment|// no type found
return|return
literal|null
return|;
block|}
DECL|method|findMatchedExceptionPolicy (Map<ExceptionPolicyKey, ExceptionDefinition> exceptionPolicices, Exchange exchange, Throwable exception)
specifier|private
name|ExceptionDefinition
name|findMatchedExceptionPolicy
parameter_list|(
name|Map
argument_list|<
name|ExceptionPolicyKey
argument_list|,
name|ExceptionDefinition
argument_list|>
name|exceptionPolicices
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
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
literal|"Finding best suited exception policy for thrown exception "
operator|+
name|exception
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// the goal is to find the exception with the same/closet inheritance level as the target exception being thrown
name|int
name|targetLevel
init|=
name|getInheritanceLevel
argument_list|(
name|exception
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
comment|// candidate is the best candidate found so far to return
name|ExceptionDefinition
name|candidate
init|=
literal|null
decl_stmt|;
comment|// difference in inheritance level between the current candidate and the thrown exception (target level)
name|int
name|candidateDiff
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
comment|// loop through all the entries and find the best candidates to use
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|ExceptionPolicyKey
argument_list|,
name|ExceptionDefinition
argument_list|>
argument_list|>
name|entries
init|=
name|exceptionPolicices
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|ExceptionPolicyKey
argument_list|,
name|ExceptionDefinition
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|Class
name|clazz
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getExceptionClass
argument_list|()
decl_stmt|;
name|ExceptionDefinition
name|type
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|filter
argument_list|(
name|type
argument_list|,
name|clazz
argument_list|,
name|exception
argument_list|)
condition|)
block|{
comment|// must match
if|if
condition|(
operator|!
name|matchesWhen
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
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
literal|"The type did not match when: "
operator|+
name|type
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
comment|// exact match then break
if|if
condition|(
name|clazz
operator|.
name|equals
argument_list|(
name|exception
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|candidate
operator|=
name|type
expr_stmt|;
break|break;
block|}
comment|// not an exact match so find the best candidate
name|int
name|level
init|=
name|getInheritanceLevel
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|int
name|diff
init|=
name|targetLevel
operator|-
name|level
decl_stmt|;
if|if
condition|(
name|diff
operator|<
name|candidateDiff
condition|)
block|{
comment|// replace with a much better candidate
name|candidate
operator|=
name|type
expr_stmt|;
name|candidateDiff
operator|=
name|diff
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|candidate
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using "
operator|+
name|candidate
operator|+
literal|" as the exception policy"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No candidate found to be used as exception policy"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|candidate
return|;
block|}
comment|/**      * Strategy to filter the given type exception class with the thrown exception      *      * @param type           the exception type      * @param exceptionClass the current exception class for testing      * @param exception      the thrown exception      * @return<tt>true</tt> if the to current exception class is a candidate,<tt>false</tt> to skip it.      */
DECL|method|filter (ExceptionDefinition type, Class exceptionClass, Throwable exception)
specifier|protected
name|boolean
name|filter
parameter_list|(
name|ExceptionDefinition
name|type
parameter_list|,
name|Class
name|exceptionClass
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
comment|// must be instance of check to ensure that the exceptionClass is one type of the thrown exception
return|return
name|exceptionClass
operator|.
name|isInstance
argument_list|(
name|exception
argument_list|)
return|;
block|}
comment|/**      * Strategy method for matching the exception type with the current exchange.      *<p/>      * This default implementation will match as:      *<ul>      *<li>Always true if no when predicate on the exception type      *<li>Otherwise the when predicate is matches against the current exchange      *</ul>      *      * @param type     the exception type      * @param exchange the current {@link Exchange}      * @return<tt>true</tt> if matched,<tt>false</tt> otherwise.      */
DECL|method|matchesWhen (ExceptionDefinition type, Exchange exchange)
specifier|protected
name|boolean
name|matchesWhen
parameter_list|(
name|ExceptionDefinition
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|type
operator|.
name|getOnWhen
argument_list|()
operator|==
literal|null
operator|||
name|type
operator|.
name|getOnWhen
argument_list|()
operator|.
name|getExpression
argument_list|()
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
name|type
operator|.
name|getOnWhen
argument_list|()
operator|.
name|getExpression
argument_list|()
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
comment|/**      * Strategy method creating the iterator to walk the exception in the order Camel should use      * for find the {@link ExceptionDefinition} should be used.      *<p/>      * The default iterator will walk from the bottom upwards      * (the last caused by going upwards to the exception)      *      * @param exception  the exception      * @return the iterator      */
DECL|method|createExceptionIterator (Throwable exception)
specifier|protected
name|Iterator
argument_list|<
name|Throwable
argument_list|>
name|createExceptionIterator
parameter_list|(
name|Throwable
name|exception
parameter_list|)
block|{
return|return
operator|new
name|ExceptionIterator
argument_list|(
name|exception
argument_list|)
return|;
block|}
DECL|method|getInheritanceLevel (Class clazz)
specifier|private
specifier|static
name|int
name|getInheritanceLevel
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|clazz
operator|==
literal|null
operator|||
literal|"java.lang.Object"
operator|.
name|equals
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
literal|1
operator|+
name|getInheritanceLevel
argument_list|(
name|clazz
operator|.
name|getSuperclass
argument_list|()
argument_list|)
return|;
block|}
DECL|class|ExceptionIterator
specifier|private
class|class
name|ExceptionIterator
implements|implements
name|Iterator
argument_list|<
name|Throwable
argument_list|>
block|{
DECL|field|tree
specifier|private
name|List
argument_list|<
name|Throwable
argument_list|>
name|tree
init|=
operator|new
name|ArrayList
argument_list|<
name|Throwable
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|it
specifier|private
name|Iterator
argument_list|<
name|Throwable
argument_list|>
name|it
decl_stmt|;
DECL|method|ExceptionIterator (Throwable exception)
specifier|public
name|ExceptionIterator
parameter_list|(
name|Throwable
name|exception
parameter_list|)
block|{
name|Throwable
name|current
init|=
name|exception
decl_stmt|;
comment|// spool to the bottom of the caused by tree
while|while
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|tree
operator|.
name|add
argument_list|(
name|current
argument_list|)
expr_stmt|;
name|current
operator|=
name|current
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
comment|// reverse tree so we go from bottom to top
name|Collections
operator|.
name|reverse
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|it
operator|=
name|tree
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|it
operator|.
name|hasNext
argument_list|()
return|;
block|}
DECL|method|next ()
specifier|public
name|Throwable
name|next
parameter_list|()
block|{
return|return
name|it
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

