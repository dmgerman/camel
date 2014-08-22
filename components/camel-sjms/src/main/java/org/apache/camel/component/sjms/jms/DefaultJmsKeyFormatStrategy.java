begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|jms
package|;
end_package

begin_comment
comment|/**  * Default strategy that handles dots and hyphens.  *<p/>  * This can be used for sending keys contain package names that is common by  * Java frameworks.  */
end_comment

begin_class
DECL|class|DefaultJmsKeyFormatStrategy
specifier|public
class|class
name|DefaultJmsKeyFormatStrategy
implements|implements
name|KeyFormatStrategy
block|{
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
name|key
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"_DOT_"
argument_list|)
decl_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceAll
argument_list|(
literal|"-"
argument_list|,
literal|"_HYPHEN_"
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
name|key
operator|.
name|replaceAll
argument_list|(
literal|"_HYPHEN_"
argument_list|,
literal|"-"
argument_list|)
decl_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replace
argument_list|(
literal|"_DOT_"
argument_list|,
literal|"."
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

