begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.concurrent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|concurrent
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Various helper method for thread naming.  */
end_comment

begin_class
DECL|class|ThreadHelper
specifier|public
specifier|final
class|class
name|ThreadHelper
block|{
DECL|field|DEFAULT_PATTERN
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PATTERN
init|=
literal|"Camel Thread ##counter# - #name#"
decl_stmt|;
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
DECL|field|threadCounter
specifier|private
specifier|static
name|AtomicLong
name|threadCounter
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|method|ThreadHelper ()
specifier|private
name|ThreadHelper
parameter_list|()
block|{     }
DECL|method|nextThreadCounter ()
specifier|private
specifier|static
name|long
name|nextThreadCounter
parameter_list|()
block|{
return|return
name|threadCounter
operator|.
name|getAndIncrement
argument_list|()
return|;
block|}
comment|/**      * Creates a new thread name with the given pattern      *<p/>      * See {@link org.apache.camel.spi.ExecutorServiceManager#setThreadNamePattern(String)} for supported patterns.      *      * @param pattern the pattern      * @param name    the name      * @return the thread name, which is unique      */
DECL|method|resolveThreadName (String pattern, String name)
specifier|public
specifier|static
name|String
name|resolveThreadName
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
name|pattern
operator|=
name|DEFAULT_PATTERN
expr_stmt|;
block|}
comment|// we support #longName# and #name# as name placeholders
name|String
name|longName
init|=
name|name
decl_stmt|;
name|String
name|shortName
init|=
name|name
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|?
name|ObjectHelper
operator|.
name|before
argument_list|(
name|name
argument_list|,
literal|"?"
argument_list|)
else|:
name|name
decl_stmt|;
comment|// must quote the names to have it work as literal replacement
name|shortName
operator|=
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|shortName
argument_list|)
expr_stmt|;
name|longName
operator|=
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|longName
argument_list|)
expr_stmt|;
comment|// replace tokens
name|String
name|answer
init|=
name|pattern
operator|.
name|replaceFirst
argument_list|(
literal|"#counter#"
argument_list|,
literal|""
operator|+
name|nextThreadCounter
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceFirst
argument_list|(
literal|"#longName#"
argument_list|,
name|longName
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
name|shortName
argument_list|)
expr_stmt|;
comment|// are there any #word# combos left, if so they should be considered invalid tokens
if|if
condition|(
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
block|}
end_class

end_unit

