begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.errorhandler
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|errorhandler
package|;
end_package

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
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|TreeMap
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
comment|/**  * The default strategy used in Camel to resolve the {@link ExceptionPolicyKey} that should  * handle the thrown exception.  *<p/>  *<b>Selection strategy:</b>  *<br/>This strategy applies the following rules:  *<ul>  *<li>Will walk the exception hierarchy from bottom upwards till the thrown exception, meaning that the most outer caused  * by is selected first, ending with the thrown exception itself. The method {@link #createExceptionIterable(Throwable)}  * provides the Iterator used for the walking.</li>  *<li>The exception type must be configured with an Exception that is an instance of the thrown exception, this  * is tested using the {@link #filter(ExceptionPolicyKey, Class, Throwable)} method.  * By default the filter uses<tt>instanceof</tt> test.</li>  *<li>If the exception type has<b>exactly</b> the thrown exception then its selected as its an exact match</li>  *<li>Otherwise the type that has an exception that is the closest super of the thrown exception is selected  * (recurring up the exception hierarchy)</li>  *</ul>  *<p/>  *<b>Fine grained matching:</b>  *<br/> If the {@link ExceptionPolicyKey} has a when defined with an expression the type is also matches against  * the current exchange using the {@link #matchesWhen(ExceptionPolicyKey, org.apache.camel.Exchange)}  * method. This can be used to for more fine grained matching, so you can e.g. define multiple sets of  * exception types with the same exception class(es) but have a predicate attached to select which to select at runtime.  */
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultExceptionPolicyStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|getExceptionPolicy (Set<ExceptionPolicyKey> exceptionPolicies, Exchange exchange, Throwable exception)
specifier|public
name|ExceptionPolicyKey
name|getExceptionPolicy
parameter_list|(
name|Set
argument_list|<
name|ExceptionPolicyKey
argument_list|>
name|exceptionPolicies
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|ExceptionPolicyKey
argument_list|>
name|candidates
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ExceptionPolicyKey
argument_list|>
name|routeScoped
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ExceptionPolicyKey
argument_list|>
name|contextScoped
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|// split policies into route and context scoped
name|initRouteAndContextScopedExceptionPolicies
argument_list|(
name|exceptionPolicies
argument_list|,
name|routeScoped
argument_list|,
name|contextScoped
argument_list|)
expr_stmt|;
comment|// at first check route scoped as we prefer them over context scoped
comment|// recursive up the tree using the iterator
name|Iterable
argument_list|<
name|Throwable
argument_list|>
name|throwables
init|=
name|createExceptionIterable
argument_list|(
name|exception
argument_list|)
decl_stmt|;
name|boolean
name|exactMatch
init|=
literal|false
decl_stmt|;
name|Iterator
argument_list|<
name|Throwable
argument_list|>
name|it
init|=
name|throwables
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|exactMatch
operator|&&
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// we should stop looking if we have found an exact match
name|exactMatch
operator|=
name|findMatchedExceptionPolicy
argument_list|(
name|routeScoped
argument_list|,
name|exchange
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|,
name|candidates
argument_list|)
expr_stmt|;
block|}
comment|// fallback to check context scoped (only do this if there was no exact match)
name|it
operator|=
name|throwables
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|exactMatch
operator|&&
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// we should stop looking if we have found an exact match
name|exactMatch
operator|=
name|findMatchedExceptionPolicy
argument_list|(
name|contextScoped
argument_list|,
name|exchange
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|,
name|candidates
argument_list|)
expr_stmt|;
block|}
comment|// now go through the candidates and find the best
name|LOG
operator|.
name|trace
argument_list|(
literal|"Found {} candidates"
argument_list|,
name|candidates
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// no type found
return|return
literal|null
return|;
block|}
else|else
block|{
comment|// return the first in the map as its sorted and we checked route scoped first, which we prefer
return|return
name|candidates
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
DECL|method|initRouteAndContextScopedExceptionPolicies (Set<ExceptionPolicyKey> exceptionPolicies, Set<ExceptionPolicyKey> routeScoped, Set<ExceptionPolicyKey> contextScoped)
specifier|private
name|void
name|initRouteAndContextScopedExceptionPolicies
parameter_list|(
name|Set
argument_list|<
name|ExceptionPolicyKey
argument_list|>
name|exceptionPolicies
parameter_list|,
name|Set
argument_list|<
name|ExceptionPolicyKey
argument_list|>
name|routeScoped
parameter_list|,
name|Set
argument_list|<
name|ExceptionPolicyKey
argument_list|>
name|contextScoped
parameter_list|)
block|{
comment|// loop through all the entries and split into route and context scoped
for|for
control|(
name|ExceptionPolicyKey
name|entry
range|:
name|exceptionPolicies
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getRouteId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|routeScoped
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|contextScoped
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|findMatchedExceptionPolicy (Iterable<ExceptionPolicyKey> exceptionPolicies, Exchange exchange, Throwable exception, Map<Integer, ExceptionPolicyKey> candidates)
specifier|private
name|boolean
name|findMatchedExceptionPolicy
parameter_list|(
name|Iterable
argument_list|<
name|ExceptionPolicyKey
argument_list|>
name|exceptionPolicies
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|,
name|Map
argument_list|<
name|Integer
argument_list|,
name|ExceptionPolicyKey
argument_list|>
name|candidates
parameter_list|)
block|{
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
literal|"Finding best suited exception policy for thrown exception {}"
argument_list|,
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
name|ExceptionPolicyKey
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
for|for
control|(
name|ExceptionPolicyKey
name|type
range|:
name|exceptionPolicies
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|type
operator|.
name|getExceptionClass
argument_list|()
decl_stmt|;
comment|// if ExceptionPolicy is route scoped then the current route (Exchange) must match
comment|// so we will not pick an ExceptionPolicy from another route
name|String
name|typeRoute
init|=
name|type
operator|.
name|getRouteId
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|typeRoute
argument_list|)
condition|)
block|{
name|String
name|route
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getRouteContext
argument_list|()
operator|!=
literal|null
condition|?
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRouteId
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
operator|&&
operator|!
name|route
operator|.
name|equals
argument_list|(
name|typeRoute
argument_list|)
condition|)
block|{
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
literal|"The type is scoped for route: {} however Exchange is at route: {}"
argument_list|,
name|typeRoute
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
block|}
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"The type did not match when: {}"
argument_list|,
name|type
argument_list|)
expr_stmt|;
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
name|candidateDiff
operator|=
literal|0
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
name|candidate
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|candidates
operator|.
name|containsKey
argument_list|(
name|candidateDiff
argument_list|)
condition|)
block|{
comment|// only add as candidate if we do not already have it registered with that level
name|LOG
operator|.
name|trace
argument_list|(
literal|"Adding {} as candidate at level {}"
argument_list|,
name|candidate
argument_list|,
name|candidateDiff
argument_list|)
expr_stmt|;
name|candidates
operator|.
name|put
argument_list|(
name|candidateDiff
argument_list|,
name|candidate
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we have an existing candidate already which we should prefer to use
comment|// for example we check route scope before context scope (preferring route scopes)
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
literal|"Existing candidate {} takes precedence over{} at level {}"
argument_list|,
name|candidates
operator|.
name|get
argument_list|(
name|candidateDiff
argument_list|)
argument_list|,
name|candidate
argument_list|,
name|candidateDiff
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// if we found a exact match then we should stop continue looking
name|boolean
name|exactMatch
init|=
name|candidateDiff
operator|==
literal|0
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
operator|&&
name|exactMatch
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Exact match found for candidate: {}"
argument_list|,
name|candidate
argument_list|)
expr_stmt|;
block|}
return|return
name|exactMatch
return|;
block|}
comment|/**      * Strategy to filter the given type exception class with the thrown exception      *      * @param type           the exception type      * @param exceptionClass the current exception class for testing      * @param exception      the thrown exception      * @return<tt>true</tt> if the to current exception class is a candidate,<tt>false</tt> to skip it.      */
DECL|method|filter (ExceptionPolicyKey type, Class<?> exceptionClass, Throwable exception)
specifier|protected
name|boolean
name|filter
parameter_list|(
name|ExceptionPolicyKey
name|type
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
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
comment|/**      * Strategy method for matching the exception type with the current exchange.      *<p/>      * This default implementation will match as:      *<ul>      *<li>Always true if no when predicate on the exception type      *<li>Otherwise the when predicate is matches against the current exchange      *</ul>      *      * @param definition     the exception definition      * @param exchange the current {@link Exchange}      * @return<tt>true</tt> if matched,<tt>false</tt> otherwise.      */
DECL|method|matchesWhen (ExceptionPolicyKey definition, Exchange exchange)
specifier|protected
name|boolean
name|matchesWhen
parameter_list|(
name|ExceptionPolicyKey
name|definition
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|definition
operator|.
name|getWhen
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
name|definition
operator|.
name|getWhen
argument_list|()
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
comment|/**      * Strategy method creating the iterator to walk the exception in the order Camel should use      * for find the {@link ExceptionPolicyKey} should be used.      *<p/>      * The default iterator will walk from the bottom upwards      * (the last caused by going upwards to the exception)      *      * @param exception  the exception      * @return the list to iterate      */
DECL|method|createExceptionIterable (Throwable exception)
specifier|protected
name|Iterable
argument_list|<
name|Throwable
argument_list|>
name|createExceptionIterable
parameter_list|(
name|Throwable
name|exception
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|createExceptionIterable
argument_list|(
name|exception
argument_list|)
return|;
block|}
DECL|method|getInheritanceLevel (Class<?> clazz)
specifier|private
specifier|static
name|int
name|getInheritanceLevel
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
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
block|}
end_class

end_unit

