begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * A runtime exception thrown if an attempt is made to resolve an unknown  * language definition.  *   * @see org.apache.camel.CamelContext#resolveLanguage(String)  */
end_comment

begin_class
DECL|class|NoSuchLanguageException
specifier|public
class|class
name|NoSuchLanguageException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8721487431101572630L
decl_stmt|;
DECL|field|language
specifier|private
specifier|final
name|String
name|language
decl_stmt|;
DECL|method|NoSuchLanguageException (String language)
specifier|public
name|NoSuchLanguageException
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|super
argument_list|(
literal|"No language could be found for: "
operator|+
name|language
argument_list|)
expr_stmt|;
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
block|}
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
block|}
end_class

end_unit

