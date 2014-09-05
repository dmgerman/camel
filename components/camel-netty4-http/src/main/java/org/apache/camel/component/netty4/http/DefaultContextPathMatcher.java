begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
operator|.
name|http
package|;
end_package

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
comment|/**  * A default {@link ContextPathMatcher} which supports the<tt>matchOnUriPrefix</tt> option.  */
end_comment

begin_class
DECL|class|DefaultContextPathMatcher
specifier|public
class|class
name|DefaultContextPathMatcher
implements|implements
name|ContextPathMatcher
block|{
DECL|field|path
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
DECL|field|matchOnUriPrefix
specifier|private
specifier|final
name|boolean
name|matchOnUriPrefix
decl_stmt|;
DECL|method|DefaultContextPathMatcher (String path, boolean matchOnUriPrefix)
specifier|public
name|DefaultContextPathMatcher
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|matchOnUriPrefix
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
expr_stmt|;
name|this
operator|.
name|matchOnUriPrefix
operator|=
name|matchOnUriPrefix
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|matches (String path)
specifier|public
name|boolean
name|matches
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|path
operator|=
name|path
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|matchOnUriPrefix
condition|)
block|{
comment|// exact match
return|return
name|path
operator|.
name|equals
argument_list|(
name|this
operator|.
name|path
argument_list|)
return|;
block|}
else|else
block|{
comment|// match on prefix, then we just need to match the start of the context-path
return|return
name|path
operator|.
name|startsWith
argument_list|(
name|this
operator|.
name|path
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|matchesRest (String path, boolean wildcard)
specifier|public
name|boolean
name|matchesRest
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|wildcard
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|matchMethod (String method, String restrict)
specifier|public
name|boolean
name|matchMethod
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|restrict
parameter_list|)
block|{
comment|// always match as HttpServerChannelHandler will deal with HTTP method restrictions
return|return
literal|true
return|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DefaultContextPathMatcher
name|that
init|=
operator|(
name|DefaultContextPathMatcher
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|matchOnUriPrefix
operator|!=
name|that
operator|.
name|matchOnUriPrefix
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|path
operator|.
name|equals
argument_list|(
name|that
operator|.
name|path
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|path
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|matchOnUriPrefix
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

