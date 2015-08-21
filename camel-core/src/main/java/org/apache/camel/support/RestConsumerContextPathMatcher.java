begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|Locale
import|;
end_import

begin_comment
comment|/**  * A context path matcher when using rest-dsl that allows components to reuse the same matching logic.  *<p/>  * The component should use the {@link #matchBestPath(String, String, java.util.List)} with the request details  * and the matcher returns the best matched, or<tt>null</tt> if none could be determined.  *<p/>  * The {@link ConsumerPath} is used for the components to provide the details to the matcher.  */
end_comment

begin_class
DECL|class|RestConsumerContextPathMatcher
specifier|public
specifier|final
class|class
name|RestConsumerContextPathMatcher
block|{
comment|/**      * Consumer path details which must be implemented and provided by the components.      */
DECL|interface|ConsumerPath
specifier|public
interface|interface
name|ConsumerPath
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**          * Any HTTP restrict method that would not be allowed          */
DECL|method|getRestrictMethod ()
name|String
name|getRestrictMethod
parameter_list|()
function_decl|;
comment|/**          * The consumer context-path which may include wildcards          */
DECL|method|getConsumerPath ()
name|String
name|getConsumerPath
parameter_list|()
function_decl|;
comment|/**          * The consumer implementation          */
DECL|method|getConsumer ()
name|T
name|getConsumer
parameter_list|()
function_decl|;
block|}
comment|/**      * Finds the best matching of the list of consumer paths that should service the incoming request.      *      * @param requestMethod   the incoming request HTTP method      * @param requestPath     the incoming request context path      * @param consumerPaths   the list of consumer context path details      * @return the best matched consumer, or<tt>null</tt> if none could be determined.      */
DECL|method|matchBestPath (String requestMethod, String requestPath, List<ConsumerPath> consumerPaths)
specifier|public
specifier|static
name|ConsumerPath
name|matchBestPath
parameter_list|(
name|String
name|requestMethod
parameter_list|,
name|String
name|requestPath
parameter_list|,
name|List
argument_list|<
name|ConsumerPath
argument_list|>
name|consumerPaths
parameter_list|)
block|{
name|ConsumerPath
name|answer
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|ConsumerPath
argument_list|>
name|candidates
init|=
operator|new
name|ArrayList
argument_list|<
name|ConsumerPath
argument_list|>
argument_list|()
decl_stmt|;
comment|// first match by http method
for|for
control|(
name|ConsumerPath
name|entry
range|:
name|consumerPaths
control|)
block|{
if|if
condition|(
name|matchRestMethod
argument_list|(
name|requestMethod
argument_list|,
name|entry
operator|.
name|getRestrictMethod
argument_list|()
argument_list|)
condition|)
block|{
name|candidates
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
comment|// then see if we got a direct match
name|Iterator
argument_list|<
name|ConsumerPath
argument_list|>
name|it
init|=
name|candidates
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ConsumerPath
name|consumer
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|matchRestPath
argument_list|(
name|requestPath
argument_list|,
name|consumer
operator|.
name|getConsumerPath
argument_list|()
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|answer
operator|=
name|consumer
expr_stmt|;
break|break;
block|}
block|}
comment|// then match by wildcard path
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|it
operator|=
name|candidates
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ConsumerPath
name|consumer
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// filter non matching paths
if|if
condition|(
operator|!
name|matchRestPath
argument_list|(
name|requestPath
argument_list|,
name|consumer
operator|.
name|getConsumerPath
argument_list|()
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
comment|// if there is multiple candidates with wildcards then pick anyone with the least number of wildcards
name|int
name|bestWildcard
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
name|ConsumerPath
name|best
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|it
operator|=
name|candidates
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ConsumerPath
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|int
name|wildcards
init|=
name|countWildcards
argument_list|(
name|entry
operator|.
name|getConsumerPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|wildcards
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|best
operator|==
literal|null
operator|||
name|wildcards
operator|<
name|bestWildcard
condition|)
block|{
name|best
operator|=
name|entry
expr_stmt|;
name|bestWildcard
operator|=
name|wildcards
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|best
operator|!=
literal|null
condition|)
block|{
comment|// pick the best among the wildcards
name|answer
operator|=
name|best
expr_stmt|;
block|}
block|}
comment|// if there is one left then its our answer
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|candidates
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|answer
operator|=
name|candidates
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Matches the given request HTTP method with the configured HTTP method of the consumer      *      * @param method    the request HTTP method      * @param restrict  the consumer configured HTTP restrict method      * @return<tt>true</tt> if matched,<tt>false</tt> otherwise      */
DECL|method|matchRestMethod (String method, String restrict)
specifier|private
specifier|static
name|boolean
name|matchRestMethod
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|restrict
parameter_list|)
block|{
if|if
condition|(
name|restrict
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// always match OPTIONS as some REST clients uses that prior to calling the service
if|if
condition|(
literal|"OPTIONS"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|restrict
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
name|method
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Matches the given request path with the configured consumer path      *      * @param requestPath   the request path      * @param consumerPath  the consumer path which may use { } tokens      * @return<tt>true</tt> if matched,<tt>false</tt> otherwise      */
DECL|method|matchRestPath (String requestPath, String consumerPath, boolean wildcard)
specifier|private
specifier|static
name|boolean
name|matchRestPath
parameter_list|(
name|String
name|requestPath
parameter_list|,
name|String
name|consumerPath
parameter_list|,
name|boolean
name|wildcard
parameter_list|)
block|{
comment|// remove starting/ending slashes
if|if
condition|(
name|requestPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|requestPath
operator|=
name|requestPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|requestPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|requestPath
operator|=
name|requestPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|requestPath
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// remove starting/ending slashes
if|if
condition|(
name|consumerPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|consumerPath
operator|=
name|consumerPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|consumerPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|consumerPath
operator|=
name|consumerPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|consumerPath
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// split using single char / is optimized in the jdk
name|String
index|[]
name|requestPaths
init|=
name|requestPath
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|String
index|[]
name|consumerPaths
init|=
name|consumerPath
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
comment|// must be same number of path's
if|if
condition|(
name|requestPaths
operator|.
name|length
operator|!=
name|consumerPaths
operator|.
name|length
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|requestPaths
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|p1
init|=
name|requestPaths
index|[
name|i
index|]
decl_stmt|;
name|String
name|p2
init|=
name|consumerPaths
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|wildcard
operator|&&
name|p2
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
operator|&&
name|p2
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
comment|// always matches
continue|continue;
block|}
if|if
condition|(
operator|!
name|p1
operator|.
name|equals
argument_list|(
name|p2
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|// assume matching
return|return
literal|true
return|;
block|}
comment|/**      * Counts the number of wildcards in the path      *      * @param consumerPath  the consumer path which may use { } tokens      * @return number of wildcards, or<tt>0</tt> if no wildcards      */
DECL|method|countWildcards (String consumerPath)
specifier|private
specifier|static
name|int
name|countWildcards
parameter_list|(
name|String
name|consumerPath
parameter_list|)
block|{
name|int
name|wildcards
init|=
literal|0
decl_stmt|;
comment|// remove starting/ending slashes
if|if
condition|(
name|consumerPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|consumerPath
operator|=
name|consumerPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|consumerPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|consumerPath
operator|=
name|consumerPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|consumerPath
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|consumerPaths
init|=
name|consumerPath
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|p2
range|:
name|consumerPaths
control|)
block|{
if|if
condition|(
name|p2
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
operator|&&
name|p2
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
name|wildcards
operator|++
expr_stmt|;
block|}
block|}
return|return
name|wildcards
return|;
block|}
block|}
end_class

end_unit

