begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.packaging
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
package|;
end_package

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

begin_class
DECL|class|MvelHelper
specifier|public
specifier|final
class|class
name|MvelHelper
block|{
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|MvelHelper
name|INSTANCE
init|=
operator|new
name|MvelHelper
argument_list|()
decl_stmt|;
DECL|field|DOLLAR_ESCAPE
specifier|private
specifier|static
specifier|final
name|Pattern
name|DOLLAR_ESCAPE
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\$"
argument_list|)
decl_stmt|;
DECL|field|CURLY_BRACKET_ESCAPE
specifier|private
specifier|static
specifier|final
name|Pattern
name|CURLY_BRACKET_ESCAPE
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(\\{[a-zA-Z0-9]+?)\\}"
argument_list|)
decl_stmt|;
DECL|field|URL_ESCAPE
specifier|private
specifier|static
specifier|final
name|Pattern
name|URL_ESCAPE
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(?<!href=\")(http(:?s)?://|(:?s)?ftp(?:s)?)"
argument_list|)
decl_stmt|;
DECL|method|MvelHelper ()
specifier|private
name|MvelHelper
parameter_list|()
block|{
comment|// utility class
block|}
DECL|method|escape (final String raw)
specifier|public
specifier|static
name|String
name|escape
parameter_list|(
specifier|final
name|String
name|raw
parameter_list|)
block|{
if|if
condition|(
name|raw
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|String
name|escapedDollars
init|=
name|DOLLAR_ESCAPE
operator|.
name|matcher
argument_list|(
name|raw
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\\\\\$"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|escapedCurlyBrackets
init|=
name|CURLY_BRACKET_ESCAPE
operator|.
name|matcher
argument_list|(
name|escapedDollars
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\\\$1\\\\}"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|escapedUrls
init|=
name|URL_ESCAPE
operator|.
name|matcher
argument_list|(
name|escapedCurlyBrackets
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\\\$1"
argument_list|)
decl_stmt|;
return|return
name|escapedUrls
return|;
block|}
block|}
end_class

end_unit

