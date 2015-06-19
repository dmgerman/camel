begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collection
import|;
end_import

begin_class
DECL|class|StringHelper
specifier|public
specifier|final
class|class
name|StringHelper
block|{
DECL|method|StringHelper ()
specifier|private
name|StringHelper
parameter_list|()
block|{
comment|// Utils Class
block|}
DECL|method|isEmpty (String s)
specifier|public
specifier|static
name|boolean
name|isEmpty
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|==
literal|null
operator|||
name|s
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|after (String text, String after)
specifier|public
specifier|static
name|String
name|after
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|after
parameter_list|)
block|{
if|if
condition|(
operator|!
name|text
operator|.
name|contains
argument_list|(
name|after
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|text
operator|.
name|substring
argument_list|(
name|text
operator|.
name|indexOf
argument_list|(
name|after
argument_list|)
operator|+
name|after
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
DECL|method|before (String text, String before)
specifier|public
specifier|static
name|String
name|before
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|before
parameter_list|)
block|{
if|if
condition|(
operator|!
name|text
operator|.
name|contains
argument_list|(
name|before
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|text
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|text
operator|.
name|indexOf
argument_list|(
name|before
argument_list|)
argument_list|)
return|;
block|}
DECL|method|between (String text, String after, String before)
specifier|public
specifier|static
name|String
name|between
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|after
parameter_list|,
name|String
name|before
parameter_list|)
block|{
name|text
operator|=
name|after
argument_list|(
name|text
argument_list|,
name|after
argument_list|)
expr_stmt|;
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|before
argument_list|(
name|text
argument_list|,
name|before
argument_list|)
return|;
block|}
DECL|method|indentCollection (String indent, Collection<String> list)
specifier|public
specifier|static
name|String
name|indentCollection
parameter_list|(
name|String
name|indent
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|text
range|:
name|list
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|indent
argument_list|)
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

