begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

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
comment|/**  * Default strategy that handles dots and hyphens.  *<p/>  * This can be used for sending keys containing package names that is common by Java frameworks.  */
end_comment

begin_class
DECL|class|DefaultJmsKeyFormatStrategy
specifier|public
class|class
name|DefaultJmsKeyFormatStrategy
implements|implements
name|JmsKeyFormatStrategy
block|{
DECL|field|DOT
specifier|private
specifier|static
specifier|final
name|String
name|DOT
init|=
literal|"."
decl_stmt|;
DECL|field|DOT_REPLACEMENT
specifier|private
specifier|static
specifier|final
name|String
name|DOT_REPLACEMENT
init|=
literal|"_DOT_"
decl_stmt|;
DECL|field|HYPHEN
specifier|private
specifier|static
specifier|final
name|String
name|HYPHEN
init|=
literal|"-"
decl_stmt|;
DECL|field|HYPHEN_REPLACEMENT
specifier|private
specifier|static
specifier|final
name|String
name|HYPHEN_REPLACEMENT
init|=
literal|"_HYPHEN_"
decl_stmt|;
DECL|method|encodeKey (String key)
specifier|public
name|String
name|encodeKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|String
name|answer
init|=
name|StringHelper
operator|.
name|replaceAll
argument_list|(
name|key
argument_list|,
name|DOT
argument_list|,
name|DOT_REPLACEMENT
argument_list|)
decl_stmt|;
name|answer
operator|=
name|StringHelper
operator|.
name|replaceAll
argument_list|(
name|answer
argument_list|,
name|HYPHEN
argument_list|,
name|HYPHEN_REPLACEMENT
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|decodeKey (String key)
specifier|public
name|String
name|decodeKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|String
name|answer
init|=
name|StringHelper
operator|.
name|replaceAll
argument_list|(
name|key
argument_list|,
name|DOT_REPLACEMENT
argument_list|,
name|DOT
argument_list|)
decl_stmt|;
name|answer
operator|=
name|StringHelper
operator|.
name|replaceAll
argument_list|(
name|answer
argument_list|,
name|HYPHEN_REPLACEMENT
argument_list|,
name|HYPHEN
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

