begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_comment
comment|/**  * A {@link org.apache.camel.component.netty.http.ContextPathMatcher} that supports the Rest DSL.  */
end_comment

begin_class
DECL|class|RestContextPathMatcher
specifier|public
class|class
name|RestContextPathMatcher
extends|extends
name|DefaultContextPathMatcher
block|{
DECL|field|rawPath
specifier|private
specifier|final
name|String
name|rawPath
decl_stmt|;
comment|// TODO: improve matching like we have done in camel-servlet
DECL|method|RestContextPathMatcher (String rawPath, String path, boolean matchOnUriPrefix)
specifier|public
name|RestContextPathMatcher
parameter_list|(
name|String
name|rawPath
parameter_list|,
name|String
name|path
parameter_list|,
name|boolean
name|matchOnUriPrefix
parameter_list|)
block|{
name|super
argument_list|(
name|path
argument_list|,
name|matchOnUriPrefix
argument_list|)
expr_stmt|;
name|this
operator|.
name|rawPath
operator|=
name|rawPath
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|matches (String target)
specifier|public
name|boolean
name|matches
parameter_list|(
name|String
name|target
parameter_list|)
block|{
if|if
condition|(
name|useRestMatching
argument_list|(
name|rawPath
argument_list|)
condition|)
block|{
return|return
name|matchRestPath
argument_list|(
name|target
argument_list|,
name|rawPath
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|matches
argument_list|(
name|target
argument_list|)
return|;
block|}
block|}
DECL|method|useRestMatching (String path)
specifier|private
name|boolean
name|useRestMatching
parameter_list|(
name|String
name|path
parameter_list|)
block|{
comment|// only need to do rest matching if using { } placeholders
return|return
name|path
operator|.
name|indexOf
argument_list|(
literal|'{'
argument_list|)
operator|>
operator|-
literal|1
return|;
block|}
comment|/**      * Matches the given request path with the configured consumer path      *      * @param requestPath   the request path      * @param consumerPath  the consumer path which may use { } tokens      * @return<tt>true</tt> if matched,<tt>false</tt> otherwise      */
DECL|method|matchRestPath (String requestPath, String consumerPath)
specifier|public
name|boolean
name|matchRestPath
parameter_list|(
name|String
name|requestPath
parameter_list|,
name|String
name|consumerPath
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
block|}
end_class

end_unit

