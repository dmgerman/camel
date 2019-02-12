begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|spi
operator|.
name|ManagementNameStrategy
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
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * Default implementation of {@link ManagementNameStrategy}  *<p/>  * This implementation will by default use a name pattern as<tt>#name#</tt> and in case  * of a clash, then the pattern will fallback to be using the counter as<tt>#name#-#counter#</tt>.  */
end_comment

begin_class
DECL|class|DefaultManagementNameStrategy
specifier|public
class|class
name|DefaultManagementNameStrategy
implements|implements
name|ManagementNameStrategy
block|{
DECL|field|INVALID_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|INVALID_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|".*#\\w+#.*"
argument_list|)
decl_stmt|;
DECL|field|NAME_COUNTER
specifier|private
specifier|static
specifier|final
name|AtomicLong
name|NAME_COUNTER
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|defaultPattern
specifier|private
specifier|final
name|String
name|defaultPattern
decl_stmt|;
DECL|field|nextPattern
specifier|private
specifier|final
name|String
name|nextPattern
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|namePattern
specifier|private
name|String
name|namePattern
decl_stmt|;
DECL|method|DefaultManagementNameStrategy (CamelContext camelContext)
specifier|public
name|DefaultManagementNameStrategy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|,
literal|"#name#-#counter#"
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultManagementNameStrategy (CamelContext camelContext, String defaultPattern, String nextPattern)
specifier|public
name|DefaultManagementNameStrategy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|defaultPattern
parameter_list|,
name|String
name|nextPattern
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|defaultPattern
operator|=
name|defaultPattern
expr_stmt|;
name|this
operator|.
name|nextPattern
operator|=
name|nextPattern
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getNamePattern ()
specifier|public
name|String
name|getNamePattern
parameter_list|()
block|{
return|return
name|namePattern
return|;
block|}
annotation|@
name|Override
DECL|method|setNamePattern (String namePattern)
specifier|public
name|void
name|setNamePattern
parameter_list|(
name|String
name|namePattern
parameter_list|)
block|{
name|this
operator|.
name|namePattern
operator|=
name|namePattern
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|String
name|pattern
init|=
name|getNamePattern
argument_list|()
decl_stmt|;
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
comment|// fallback and use the default pattern which is the same name as the CamelContext has been given
name|pattern
operator|=
name|defaultPattern
operator|!=
literal|null
condition|?
name|defaultPattern
else|:
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getManagementNamePattern
argument_list|()
expr_stmt|;
block|}
name|name
operator|=
name|resolveManagementName
argument_list|(
name|pattern
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|getNextName ()
specifier|public
name|String
name|getNextName
parameter_list|()
block|{
if|if
condition|(
name|isFixedName
argument_list|()
condition|)
block|{
comment|// use the fixed name
return|return
name|getName
argument_list|()
return|;
block|}
else|else
block|{
comment|// or resolve a new name
name|String
name|pattern
init|=
name|getNamePattern
argument_list|()
decl_stmt|;
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
comment|// use a pattern that has a counter to ensure unique next name
name|pattern
operator|=
name|nextPattern
expr_stmt|;
block|}
return|return
name|resolveManagementName
argument_list|(
name|pattern
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|isFixedName ()
specifier|public
name|boolean
name|isFixedName
parameter_list|()
block|{
comment|// the name will be fixed unless there is a counter token
name|String
name|pattern
init|=
name|getNamePattern
argument_list|()
decl_stmt|;
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
comment|// we are not fixed by default
return|return
literal|false
return|;
block|}
return|return
operator|!
name|pattern
operator|.
name|contains
argument_list|(
literal|"#counter#"
argument_list|)
return|;
block|}
comment|/**      * Creates a new management name with the given pattern      *      * @param pattern the pattern      * @param name    the name      * @return the management name      * @throws IllegalArgumentException if the pattern or name is invalid or empty      */
DECL|method|resolveManagementName (String pattern, String name, boolean invalidCheck)
specifier|public
name|String
name|resolveManagementName
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|invalidCheck
parameter_list|)
block|{
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|pattern
argument_list|,
literal|"pattern"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
comment|// must quote the names to have it work as literal replacement
name|name
operator|=
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|name
argument_list|)
expr_stmt|;
comment|// replace tokens
name|String
name|answer
init|=
name|pattern
decl_stmt|;
if|if
condition|(
name|pattern
operator|.
name|contains
argument_list|(
literal|"#counter#"
argument_list|)
condition|)
block|{
comment|// only increment the counter on-demand
name|answer
operator|=
name|pattern
operator|.
name|replaceFirst
argument_list|(
literal|"#counter#"
argument_list|,
literal|""
operator|+
name|nextNameCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// camelId and name is the same tokens
name|answer
operator|=
name|answer
operator|.
name|replaceFirst
argument_list|(
literal|"#camelId#"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceFirst
argument_list|(
literal|"#name#"
argument_list|,
name|name
argument_list|)
expr_stmt|;
comment|// allow custom name resolution as well. For example with camel-core-osgi we have a custom
comment|// name strategy that supports OSGI specific tokens such as #bundleId# etc.
name|answer
operator|=
name|customResolveManagementName
argument_list|(
name|pattern
argument_list|,
name|answer
argument_list|)
expr_stmt|;
comment|// are there any #word# combos left, if so they should be considered invalid tokens
if|if
condition|(
name|invalidCheck
operator|&&
name|INVALID_PATTERN
operator|.
name|matcher
argument_list|(
name|answer
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Pattern is invalid: "
operator|+
name|pattern
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Strategy to do any custom resolution of the name      *      * @param pattern  the pattern      * @param answer   the current answer, which may have custom patterns still to be resolved      * @return the resolved name      */
DECL|method|customResolveManagementName (String pattern, String answer)
specifier|protected
name|String
name|customResolveManagementName
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|answer
parameter_list|)
block|{
return|return
name|answer
return|;
block|}
DECL|method|nextNameCounter ()
specifier|private
specifier|static
name|long
name|nextNameCounter
parameter_list|()
block|{
comment|// we want to be 1-based, so increment first
return|return
name|NAME_COUNTER
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
comment|/**      * To reset the counter, should only be used for testing purposes.      *      * @param value the counter value      */
DECL|method|setCounter (int value)
specifier|public
specifier|static
name|void
name|setCounter
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|NAME_COUNTER
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
